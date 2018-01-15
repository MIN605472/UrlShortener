package liquidmountain.repository.fixture;

import liquidmountain.domain.qr.Color;
import liquidmountain.domain.qr.Image;
import liquidmountain.domain.qr.QrGenerator;
import liquidmountain.domain.qr.QrQrCodeMonkey;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class QrFixture {
    public static QrGenerator qrWithoutLogo() throws IOException {
        return new QrQrCodeMonkey("1", new Color("#000000"), new Color("#FFFFFF"), null);
    }

    public static QrGenerator qrWithLogoAndDiffColors() throws IOException {
        Path resDir = Paths.get("src","test","resources", "logo.png");
        Image logo = new Image(Files.newInputStream(resDir), MediaType.IMAGE_PNG);
        return new QrQrCodeMonkey("1", new Color("#424242"), new Color("#999999"), logo);
    }

    public static QrGenerator qrWithWrongHash() {
        return new QrQrCodeMonkey("wrong", new Color("#000000"), new Color("#FFFFFF"), null);
    }
}
