package liquidmountain.common.services;

import liquidmountain.services.UrlValidatorAndChecker;
import liquidmountain.services.UrlValidatorAndCheckerImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class UrlValidatorAndCheckerTests {

    private UrlValidatorAndChecker validator;

    @Before
    public void setup() {
        validator = new UrlValidatorAndCheckerImpl();
    }

    @Test
    public void thatIsValidWorked(){
        String url1 = "https://twitter.com";
        String url2 = "hps:/twitter.com";
        Boolean valid1 = validator.isValid(url1);
        Boolean valid2 = validator.isValid(url2);
        assertTrue(valid1);
        assertFalse(valid2);
    }

    @Test
    public void thatIsAliveWorked(){
        String url1 = "https://twitter.com";
        String url2 = "https://twitter.com/pccmponentes";
        Boolean valid1 = validator.isAlive(url1);
        Boolean valid2 = validator.isAlive(url2);
        assertTrue(valid1);
        assertFalse(valid2);
    }
}
