package liquidmountain.web;

import io.swagger.annotations.Api;
import liquidmountain.domain.ShortURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(value="api",description="Operations pertaining to URLS in Liquid Mountain")
public class UrlShortenerControllerWithLogs extends UrlShortenerController {

    private static final Logger logger = LoggerFactory.getLogger(UrlShortenerControllerWithLogs.class);

    @Override
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

    @Override
    public ResponseEntity<String> verify(@RequestParam("url") String url, HttpServletRequest request) {
        logger.info("Requested verification of url " + url);
        return super.verify(url, request);
    }

    @Override
    public ResponseEntity<String> checkSafe(@RequestParam("url") String url, HttpServletRequest request) {
        logger.info("Requested safety of url " + url);
        return super.checkSafe(url, request);
    }
}
