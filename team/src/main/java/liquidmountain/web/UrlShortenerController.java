package liquidmountain.web;

import com.google.common.hash.Hashing;
import liquidmountain.services.*;
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

	@Autowired
	protected ExtractInfo extractInfo;

//	@Autowired
//	protected GeolocationAPI  geoAPI;

	@RequestMapping(value = "/{id:(?!link).*}", method = RequestMethod.GET)
	public ResponseEntity<?> redirectTo(@PathVariable String id,
			HttpServletRequest request) {
		ShortURL l = shortURLRepository.findByKey(id);
		if (l != null) {
			ExtractInfo ex = new ExtractInfo();
			createAndSaveClick(id, ex.extractAll(request));
			return createSuccessfulRedirectToResponse(l);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	/**
	 *
	 * @param hash
	 * @param info:
	 *            0: browser
	 *            1: country
	 *            2: IP
	 *            3: OS
	 *            4: referrer
	 */
	private void createAndSaveClick(String hash, String[] info) {
		Click cl = new Click(null, hash, new Date(System.currentTimeMillis()),
				info[4], info[0], info[3], info[2], info[1]);
		cl=clickRepository.save(cl);
		System.out.println(info[0] + " " + info[3] + " " + info[1]);
		LOG.info(cl!=null?"["+hash+"] saved with id ["+cl.getId()+"]":"["+hash+"] was not saved");
	}


	private ResponseEntity<?> createSuccessfulRedirectToResponse(ShortURL l) {
		HttpHeaders h = new HttpHeaders();
		h.setLocation(URI.create(l.getTarget()));
		return new ResponseEntity<>(h, HttpStatus.valueOf(l.getMode()));
	}

	@RequestMapping(value = "/link", method = RequestMethod.POST)
	public ResponseEntity<ShortURL> shortener(@RequestParam("url") String url,
											  @RequestParam(value = "sponsor", required = false) String sponsor,
											  HttpServletRequest request) {
		ExtractInfo ex = new ExtractInfo();
		ShortURL su = createAndSaveIfValid(url, sponsor, UUID
				.randomUUID().toString(), ex.extractIP(request));
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
