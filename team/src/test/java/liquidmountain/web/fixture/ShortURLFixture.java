package liquidmountain.web.fixture;

import liquidmountain.domain.ShortURL;

public class ShortURLFixture {

	public static ShortURL someUrl() {
		return new ShortURL("someKey", "http://example.com/", null, null, null,
				null, 307, true, null, null);
	}
}
