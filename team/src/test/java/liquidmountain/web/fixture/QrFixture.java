package liquidmountain.web.fixture;

import liquidmountain.domain.qr.Color;
import liquidmountain.domain.qr.QrGenerator;
import liquidmountain.domain.qr.QrQrCodeMonkey;

public class QrFixture {
    public static QrQrCodeMonkey qrWithoutLogo() {
        return new QrQrCodeMonkey("1", new Color("#000000"), new Color("#FFFFFF"), null);
    }

    public static QrGenerator qrWithRandomColors() {
        return new QrQrCodeMonkey("1", new Color("#424242"), new Color("#999999"), null);
    }

    public static QrGenerator qrWithWrongHash() {
        return new QrQrCodeMonkey("wrong", new Color("#000000"), new Color("#FFFFFF"), null);
    }
}
