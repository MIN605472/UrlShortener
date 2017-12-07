package liquidmountain.common.repository.fixture;

import liquidmountain.domain.Click;
import liquidmountain.domain.ShortURL;

public class ClickFixture {

	public static Click click(ShortURL su) {
		return new Click(null, su.getHash(), null, null, null, null, null, null);
	}
}
