package liquidmountain.config;

import liquidmountain.repository.ClickRepository;
import liquidmountain.repository.ClickRepositoryImpl;
import liquidmountain.repository.ShortURLRepository;
import liquidmountain.repository.ShortURLRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

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
}
