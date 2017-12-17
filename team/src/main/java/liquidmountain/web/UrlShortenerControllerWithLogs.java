package liquidmountain.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import liquidmountain.domain.ShortURL;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UrlShortenerControllerWithLogs extends UrlShortenerController {

	private static final Logger logger = LoggerFactory.getLogger(UrlShortenerControllerWithLogs.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<?> index(HttpServletRequest request) {
		logger.info("Requested index");
		return super.index(request);
	}


	@Override
	@RequestMapping(value = "/{id:(?!link|index|stats).*}", method = RequestMethod.GET)
	public ResponseEntity<?> redirectTo(@PathVariable String id, HttpServletRequest request) {
		logger.info("Requested redirection with hash " + id);
		return super.redirectTo(id, request);
	}

	@Override
	public ResponseEntity<ShortURL> shortener(@RequestParam("url") String url,
											  @RequestParam("date") String date,
											  @RequestParam("time") String time,
											  @RequestParam(value = "sponsor", required = false) String sponsor,
											  HttpServletRequest request) {
		logger.info("Requested new short for uri " + url + " with expiration date " + date + " y hora " + time);
		return super.shortener(url, date, time, sponsor, request);
	}

//	@Override
//	@RequestMapping(value = "/stats/{id}", method = RequestMethod.GET)
//	public ResponseEntity<List<Click>> showStats(@PathVariable String id, HttpServletRequest request) {
//		logger.info("Requested stats with hash " + id);
//		return super.showStats(id, request);
//	}
}
