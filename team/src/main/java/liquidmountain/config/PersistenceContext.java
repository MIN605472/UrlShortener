package liquidmountain.config;

import liquidmountain.repository.ClickRepository;
import liquidmountain.repository.ClickRepositoryImpl;
import liquidmountain.repository.ShortURLRepository;
import liquidmountain.repository.ShortURLRepositoryImpl;
import liquidmountain.services.ExtractInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Configuration
public class PersistenceContext {

    @Autowired
    protected JdbcTemplate jdbc;

    @Bean
    ShortURLRepository shortURLRepository() {
        return new ShortURLRepositoryImpl(jdbc);
    }

    @Bean
    ClickRepository clickRepository() {
        return new ClickRepositoryImpl(jdbc);
    }

    @Bean
    ExtractInfo extractInfo() {return new ExtractInfo();}

}
