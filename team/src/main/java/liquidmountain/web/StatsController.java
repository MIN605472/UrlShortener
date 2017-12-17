package liquidmountain.web;

import liquidmountain.domain.Click;
import liquidmountain.domain.DataStat;
import liquidmountain.domain.URLStats;
import liquidmountain.repository.ClickRepository;
import liquidmountain.repository.ShortURLRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
public class StatsController {
    private static final Logger LOG = LoggerFactory
            .getLogger(UrlShortenerController.class);

    @Autowired
    protected ShortURLRepository shortURLRepository;

    @Autowired
    protected ClickRepository clickRepository;

    @RequestMapping(value = "/api/stats", method = RequestMethod.GET)
    public ResponseEntity<?> redirectStats(HttpServletRequest request) {
        HttpHeaders h = new HttpHeaders();
        String own = request.getRequestURL().toString();
        String normal = own.substring(0, own.indexOf("api/stats"));
        h.setLocation(URI.create(normal + "stats.html"));
        System.out.println(h.getLocation());
        return new ResponseEntity<>(h, HttpStatus.TEMPORARY_REDIRECT);
    }

    @RequestMapping(value = "/api/stats/{id:(?!link).*}", method = RequestMethod.GET)
    public ResponseEntity<URLStats> showStats(@PathVariable String id,
                                              HttpServletRequest request) {
        if(id.contains("url="))
            id = id.replace("url=", "");
        LOG.info("Requested stats with hash " + id);
        HttpHeaders h = new HttpHeaders();
        List<Click> clicks = clickRepository.findByHash(id);
        //List<List<DataStat>> urlStats; // 0 = countries, 1 = browsers, 2 = platforms
        URLStats urlStats = new URLStats();
        int countryCursor= 0;
        int browserCursor= 0;
        int platformCursor= 0;
        List<String> ips = new ArrayList<>();
        if(clicks.size() > 0) {
            // Aggregate the data
            for (Click click : clicks) {
                if(!ips.contains(click.getIp())){
                    ips.add(click.getIp());
                }
                boolean countryExists = false;
                boolean browserExists = false;
                boolean platformExists = false;
                int countryWhere = 0;
                int browserWhere = 0;
                int platformWhere = 0;
                for (int j = 0; j < urlStats.countries.size(); j++) {
                    if ((click.getCountry() == null && urlStats.countries.get(j).getData() == null)
                            || !click.getCountry().equals(urlStats.countries.get(j).getData())) {
                        countryExists = true;
                        countryWhere = j;
                    }
                }
                for (int j = 0; j < urlStats.browsers.size(); j++) {
                    if ((click.getCountry() == null && urlStats.countries.get(j).getData() == null)
                            || !click.getBrowser().equals(urlStats.browsers.get(j).getData())) {
                        browserExists = true;
                        browserWhere = j;
                    }
                }
                for (int j = 0; j < urlStats.platforms.size(); j++) {
                    if ((click.getCountry() == null && urlStats.countries.get(j).getData() == null)
                            || !click.getPlatform().equals(urlStats.platforms.get(j).getData())) {
                        platformExists = true;
                        platformWhere = j;
                    }
                }
                if (countryExists && !ips.contains(click.getIp())) {
                    urlStats.countries.get(countryWhere).setUsers(urlStats.countries.get(countryWhere).getUsers() + 1);
                } else if(!countryExists){
                    urlStats.countries.add(countryCursor, new DataStat(click.getCountry(), 1));
                    countryCursor++;
                }
                if (browserExists  && !ips.contains(click.getIp())) {
                    urlStats.browsers.get(browserWhere).setUsers(urlStats.browsers.get(browserWhere).getUsers() + 1);
                } else if(!browserExists){
                    urlStats.browsers.add(browserCursor, new DataStat(click.getBrowser(), 1));
                    browserCursor++;
                }
                if (platformExists  && !ips.contains(click.getIp())) {
                    urlStats.platforms.get(platformWhere).setUsers(urlStats.platforms.get(platformWhere).getUsers() + 1);
                } else if(!platformExists){
                    urlStats.platforms.add(platformCursor, new DataStat(click.getPlatform(), 1));
                    platformCursor++;
                }
            }
        }
        return new ResponseEntity<>(urlStats, h, HttpStatus.ACCEPTED);
    }
}

