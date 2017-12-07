package liquidmountain.web;

import com.google.common.hash.Hashing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import liquidmountain.domain.Click;
import liquidmountain.domain.ShortURL;
import liquidmountain.repository.ClickRepository;
import liquidmountain.repository.ShortURLRepository;
import liquidmountain.services.GeolocationAPI;
import liquidmountain.services.GoogleSafeBrowsingUrlVerifier;
import liquidmountain.services.UrlValidatorAndChecker;
import liquidmountain.services.UrlValidatorAndCheckerImpl;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class UrlShortenerController {
	private static final Logger LOG = LoggerFactory
			.getLogger(UrlShortenerController.class);
	@Autowired
	protected ShortURLRepository shortURLRepository;

	@Autowired
	protected ClickRepository clickRepository;

//	@Autowired
//	protected GeolocationAPI  geoAPI;

	@RequestMapping(value = "/{id:(?!link).*}", method = RequestMethod.GET)
	public ResponseEntity<?> redirectTo(@PathVariable String id,
			HttpServletRequest request) {
		ShortURL l = shortURLRepository.findByKey(id);
		if (l != null) {
			createAndSaveClick(id, extractIP(request), extractBrowser(request), extractOS(request), extractCountry(request), extractReferrer(request));
			return createSuccessfulRedirectToResponse(l);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	private void createAndSaveClick(String hash, String ip, String browser, String os, String country, String referrer) {
		Click cl = new Click(null, hash, new Date(System.currentTimeMillis()),
				referrer, browser, os, ip, country);
		cl=clickRepository.save(cl);
		System.out.println(browser + " " + os + " " + country);
		LOG.info(cl!=null?"["+hash+"] saved with id ["+cl.getId()+"]":"["+hash+"] was not saved");
	}

	private String extractIP(HttpServletRequest request) {
		return request.getRemoteAddr();
	}

	private String extractCountry(HttpServletRequest request) {
		GeolocationAPI geoAPI = new GeolocationAPI();
		System.out.println("ip: " + geoAPI.getCity("google.com"));

		return geoAPI.getCity(extractIP(request));
	}

	private String extractBrowser(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		String browser = "undefined";
		if(userAgent != null){
			if(userAgent.contains("Firefox")) {
				browser = "Mozilla Firefox";
			}
			else if(userAgent.contains("Chrome") && userAgent.contains("KHTML, like Gecko") && !userAgent.contains("Edge")) {
				browser = "Google Chrome";
			}
			else if(userAgent.contains("OPR")) {
				browser = "Opera";
			}
			else if(userAgent.contains("Safari") && userAgent.contains("Mobile")) {
				browser = "Safari";
			}
			else if(userAgent.contains(".NET") || userAgent.contains("rv:11.0")) {
				browser = "Internet Explorer";
			}
			else if(userAgent.contains("Edge")) {
				browser = "Microsoft Edge";
			}
		}
		return browser;
	}

	private String extractOS(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		String os = "undefined";
		if(userAgent != null) {
			if(userAgent.contains("Windows NT 10.0")) {
				os = "Windows 10";
			}
			else if(userAgent.contains("Windows NT 6.3")) {
				os = "Windows 8.1";
			}
			else if(userAgent.contains("Windows NT 6.2")) {
				os = "Windows 8";
			}
			else if(userAgent.contains("Windows NT 6.1")) {
				os = "Windows 7";
			}
			else if(userAgent.contains("Windows NT 6.0")) {
				os = "Windows Vista";
			}
			else if(userAgent.contains("Windows NT 5")) {
				os = "Windows XP or older";
			}
			else if(userAgent.contains("Macintosh")) {
				os = "Mac OS";
			}
			else if(userAgent.contains("Android")) {
				os = "Android";
			}
			else if(userAgent.contains("Ubuntu")) {
				os = "Ubuntu Linux";
			}
			else if(userAgent.contains("Linux")) {
				os = "Other Linux";
			}
			else if(userAgent.contains("iPhone") || userAgent.contains("iPad")) {
				os = "iOS";
			}
		}
		return os;
	}

	private String extractReferrer(HttpServletRequest request) { return request.getHeader("referer");}

	private ResponseEntity<?> createSuccessfulRedirectToResponse(ShortURL l) {
		HttpHeaders h = new HttpHeaders();
		h.setLocation(URI.create(l.getTarget()));
		return new ResponseEntity<>(h, HttpStatus.valueOf(l.getMode()));
	}

	@RequestMapping(value = "/stats/{id:(?!link).*}", method = RequestMethod.GET)
	public ResponseEntity<List<Click>> showStats(@PathVariable String id,
												 HttpServletRequest request) {
		HttpHeaders h = new HttpHeaders();
		return new ResponseEntity<>(clickRepository.findByHash(id), h, HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/link", method = RequestMethod.POST)
	public ResponseEntity<ShortURL> shortener(@RequestParam("url") String url,
											  @RequestParam(value = "sponsor", required = false) String sponsor,
											  HttpServletRequest request) {
		ShortURL su = createAndSaveIfValid(url, sponsor, UUID
				.randomUUID().toString(), extractIP(request));
		if (su != null) {
			HttpHeaders h = new HttpHeaders();
			h.setLocation(su.getUri());
			return new ResponseEntity<>(su, h, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	private ShortURL createAndSaveIfValid(String url, String sponsor,
										  String owner, String ip) {


		GoogleSafeBrowsingUrlVerifier googleSafe = new GoogleSafeBrowsingUrlVerifier();

		boolean isSafe = googleSafe.isSafe(url);

		UrlValidatorAndChecker urlValidatorAndChecker = new UrlValidatorAndCheckerImpl();
		if (urlValidatorAndChecker.isValid(url)) {
			if (urlValidatorAndChecker.isAlive(url)) {
				String id = Hashing.murmur3_32()
						.hashString(url, StandardCharsets.UTF_8).toString();
				ShortURL su = new ShortURL(id, url,
						linkTo(
								methodOn(UrlShortenerController.class).redirectTo(
										id, null)).toUri(), sponsor, new Date(
						System.currentTimeMillis()), owner,
						HttpStatus.TEMPORARY_REDIRECT.value(), true, ip, null);
				return shortURLRepository.save(su);
			} else{
				return null;
			}
		} else {
			return null;
		}
	}
}
