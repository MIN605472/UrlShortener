package liquidmountain.repository;

import liquidmountain.domain.qr.QrGenerator;
import liquidmountain.repository.fixture.QrFixture;
import liquidmountain.repository.fixture.ShortURLFixture;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

public class QrRepositoryTests {
    private EmbeddedDatabase db;
    private ShortURLRepository shortURLRepository;
    private QrRepository qrRepository;
    private JdbcTemplate jdbc;

    @Before
    public void setup() {
        db = new EmbeddedDatabaseBuilder().setType(HSQL).addScript("schema-hsqldb.sql").build();
        jdbc = new JdbcTemplate(db);
        qrRepository = new QrRepositoryImpl(jdbc);
        shortURLRepository = new ShortURLRepositoryImpl(jdbc);
        shortURLRepository.save(ShortURLFixture.url1());
    }

    @Test
    public void thatItSavesAQr() {
        try {
            QrGenerator qr = QrFixture.qrWithoutLogo();
            int id = qrRepository.saveQr(qr);
            Assert.assertEquals(0, id);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void thatItFailsIfTheHashDontExist() {
        try {
            qrRepository.saveQr(QrFixture.qrWithWrongHash());
            Assert.fail();
        } catch (DataAccessException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void thatItRetrievesAnExistingQr() {
        try {
            int id = qrRepository.saveQr(QrFixture.qrWithoutLogo());
            qrRepository.retrieveQr("1", id);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void thatItFailsWhenRetrievingNonExistentQr() {
        try {
            qrRepository.retrieveQr("1", 42);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void thatItUpdateAnExistingQr() {
        try {
            int id = qrRepository.saveQr(QrFixture.qrWithoutLogo());
            qrRepository.update(QrFixture.qrWithoutLogo().getData(), id, QrFixture.qrWithLogoAndDiffColors());
            QrGenerator afterUpdate = qrRepository.retrieveQr(QrFixture.qrWithoutLogo().getData(), id);
            Assert.assertEquals(QrFixture.qrWithLogoAndDiffColors().getBg().getHexColor(), afterUpdate.getBg()
                    .getHexColor());
            Assert.assertEquals(QrFixture.qrWithLogoAndDiffColors().getFg().getHexColor(), afterUpdate.getFg()
                    .getHexColor());
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
