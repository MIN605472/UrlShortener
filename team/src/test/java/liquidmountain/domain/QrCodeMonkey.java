package liquidmountain.domain;

import liquidmountain.domain.qr.Image;
import liquidmountain.domain.qr.QrGenerator;
import liquidmountain.repository.fixture.QrFixture;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class QrCodeMonkey {
    @Test
    public void thatItGeneratesAQr() {
        try {
            QrGenerator qr = QrFixture.qrWithLogoAndDiffColors();
            Image qrImg = qr.gen();
            Assert.assertNotNull(qrImg);
        } catch (IOException e) {
            Assert.fail();
        }
    }
}
