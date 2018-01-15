package liquidmountain.web.fixture;

import liquidmountain.domain.Click;
import liquidmountain.domain.ShortURL;

public class ClickFixture {

    public static Click click(ShortURL su) {
        return new Click(null, su.getHash(), null, null, null, null, null, null, false);
    }

    public static Click clickPlatform(ShortURL su) {
        return new Click(null, su.getHash(), null, null, null, "Windows 7", null, null, false);
    }
    public static Click clickBrowser(ShortURL su) {
        return new Click(null, su.getHash(), null, null, "Mozilla Firefox", null, null, null, false);
    }
    public static Click clickCountry(ShortURL su) {
        return new Click(null, su.getHash(), null, null, null, null, null, "Spain", false);
    }
}