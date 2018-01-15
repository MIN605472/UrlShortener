package liquidmountain.web.fixture;

import liquidmountain.domain.ShortURL;

import java.sql.Date;

public class ShortURLFixture {

	public static ShortURL someUrl() {
		return new ShortURL("someKey", "http://example.com/", null, null, null,
				null, 307, true, null, null);
	}

	public static ShortURL someExpiredUrl() {
		return new ShortURL("someKey", "http://example.com/", null, null, null,
				null, 307, true, null, null, new Date(System.currentTimeMillis() - 10000), null);
	}

	public static ShortURL anotherUrlWithValidHash() {
		return new ShortURL("1", "http://example.com/", null, null, null,
				null, 307, true, null, null);
	}
}
