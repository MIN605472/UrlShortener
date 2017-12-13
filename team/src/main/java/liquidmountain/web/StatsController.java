package liquidmountain.web;

import liquidmountain.domain.Click;
import liquidmountain.repository.ClickRepository;
import liquidmountain.repository.ShortURLRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.HeaderParam;
import javax.xml.ws.Response;
import java.util.List;

@RestController
public class StatsController {
    private static final Logger LOG = LoggerFactory
            .getLogger(UrlShortenerController.class);

    @Autowired
    protected ShortURLRepository shortURLRepository;

    @Autowired
    protected ClickRepository clickRepository;

    @RequestMapping(value = "/api/stats/{id:(?!link).*}", method = RequestMethod.GET)
    public ResponseEntity<List<Click>> showStats(@PathVariable String id,
                                                 HttpServletRequest request) {
        if(id.contains("url="))
            id = id.replace("url=", "");
        LOG.info("Requested stats with hash " + id);
        HttpHeaders h = new HttpHeaders();
        ResponseEntity<List<Click>> stats = new ResponseEntity<>(clickRepository.findByHash(id), h, HttpStatus.ACCEPTED);
        return stats;
    }
}
