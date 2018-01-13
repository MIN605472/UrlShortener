package liquidmountain.tasks;

import jdk.nashorn.internal.objects.annotations.Constructor;
import liquidmountain.domain.ShortURL;
import liquidmountain.repository.ShortURLRepository;
import liquidmountain.services.GoogleSafeBrowsingUrlVerifier;
import liquidmountain.services.UrlValidatorAndChecker;
import liquidmountain.services.UrlValidatorAndCheckerImpl;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    protected ShortURLRepository shortURLRepository;

    protected GoogleSafeBrowsingUrlVerifier googleSafeBrowsingUrlVerifier;

    protected List<ShortURL> urlList;

    private final int checkRate = 30 * 60 * 1000;  // media hora

    @Scheduled(fixedRate = checkRate - 1000) // Un minuto menos
    public void getUrls() {
        urlList = shortURLRepository.listAll();
    }

    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void CheckSafe() {
        log.info("Checking if links still safe");
        try {
            googleSafeBrowsingUrlVerifier = new GoogleSafeBrowsingUrlVerifier();
            for(ShortURL s : urlList) {
                if(!googleSafeBrowsingUrlVerifier.isSafe(s.getTarget())){
                    log.info("URL {} not safe anymore.", s.getTarget());
                    s.setSafe(false);
                    s.setMode(HttpStatus.GONE.value());
                } else{
                    s.setSafe(true);
                    s.setMode(HttpStatus.TEMPORARY_REDIRECT.value());
                }
            }
        } catch( NullPointerException e) {
            //No se ha inicializado aun
        }
    }

    @Scheduled(fixedRate = checkRate)
    public void CheckValidated() {
        log.info("Checking if links still valid and alive");
        try{
            for(ShortURL s : urlList) {
                UrlValidatorAndCheckerImpl urlValidatorAndChecker = new UrlValidatorAndCheckerImpl(s.getTarget());
                if(!urlValidatorAndChecker.execute()){
                    log.info("URL {} dead or not valid anymore.", s.getTarget());
                    s.setMode(HttpStatus.GONE.value());
                } else{
                    s.setMode(HttpStatus.TEMPORARY_REDIRECT.value());
                }
            }
        } catch( NullPointerException e) {
            //No se ha inicializado aun
        }
    }

    @Scheduled(fixedRate = checkRate)
    public void CheckExpired() {
        log.info("Checking if links expired");
        try {
            for(ShortURL s : urlList) {
                // Si la fecha guardada es antes que este momento
                Date then1 = s.getExpirationDate();
                java.util.Date then2= new java.util.Date(then1.getTime());
                Instant then = then2.toInstant();
                Instant now = ZonedDateTime.now().toInstant();
                if(then.isBefore(now)){
                    log.info("URL {} expired.", s.getUri().toString());
                    s.setMode(HttpStatus.GONE.value());
                }
            }
        } catch( NullPointerException e) {
            //No se ha inicializado aun
        }

    }
}
