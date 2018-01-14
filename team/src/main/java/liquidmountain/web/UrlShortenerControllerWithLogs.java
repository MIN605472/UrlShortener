package liquidmountain.web;

import io.swagger.annotations.Api;
import liquidmountain.domain.ShortURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller with application endpoints.
 */
@RestController
@Api(value="api",description="Operations pertaining to URLS in Liquid Mountain")
public class UrlShortenerControllerWithLogs extends UrlShortenerController {

    private static final Logger logger = LoggerFactory.getLogger(UrlShortenerControllerWithLogs.class);

    /**
     * Endpoint que permite la redireccion a la pagina indicada por @param id
     * @param id
     * @param request
     * @return
     */
    @Override
    public ResponseEntity<?> redirectTo(@PathVariable String id, HttpServletRequest request) {
        logger.info("Requested redirection with hash " + id);
        return super.redirectTo(id, request);
    }

    /**
     * Endopoint que permite acortar la direccion de @param url, s
     * se puede incluir la fecha y el momento que cadeque.
     * @param url
     * @param date
     * @param time
     * @param sponsor
     * @param request
     * @return
     */
    @Override
    public ResponseEntity<ShortURL> shortener(@RequestParam("url") String url,
                                              @RequestParam("date") String date,
                                              @RequestParam("time") String time,
                                              @RequestParam(value = "sponsor", required = false) String sponsor,
                                              HttpServletRequest request) {
        logger.info("Requested new short for uri " + url + " with expiration date " + date + " y hora " + time);
        return super.shortener(url, date, time, sponsor, request);
    }

    /**
     * Endpoint que verifica si la url aportada @param url esta bien formada
     * y es válida.
     * @param url
     * @param request
     * @return
     */
    @Override
    public ResponseEntity<String> verify(@RequestParam("url") String url, HttpServletRequest request) {
        logger.info("Requested verification of url " + url);
        return super.verify(url, request);
    }

    /**
     * Endpoint que valida si la url @param url es segura.
     * @param url
     * @param request
     * @return
     */
    @Override
    public ResponseEntity<String> checkSafe(@RequestParam("url") String url, HttpServletRequest request) {
        logger.info("Requested safety of url " + url);
        return super.checkSafe(url, request);
    }
}
