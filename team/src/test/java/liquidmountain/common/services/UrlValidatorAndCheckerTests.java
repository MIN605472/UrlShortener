package liquidmountain.common.services;

import liquidmountain.services.UrlValidatorAndChecker;
import liquidmountain.services.UrlValidatorAndCheckerImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class UrlValidatorAndCheckerTests {

    @Test
    public void thatIsValidWorked(){
        UrlValidatorAndCheckerImpl validator1 = new UrlValidatorAndCheckerImpl("https://twitter.com");
        UrlValidatorAndCheckerImpl validator2 = new UrlValidatorAndCheckerImpl("hps:/twitter.com");
        Boolean valid1 = validator1.isValid(validator1.url);
        Boolean valid2 = validator2.isValid(validator2.url);
        assertTrue(valid1);
        assertFalse(valid2);
    }

    @Test
    public void thatIsAliveWorked(){
        UrlValidatorAndCheckerImpl validator1 = new UrlValidatorAndCheckerImpl("https://twitter.com");
        UrlValidatorAndCheckerImpl validator2 = new UrlValidatorAndCheckerImpl("https://twitter.com/pccmponentes");
        Boolean valid1 = validator1.isAlive(validator1.url);
        Boolean valid2 = validator2.isAlive(validator2.url);
        assertTrue(valid1);
        assertFalse(valid2);
    }

    @Test
    public void thatValidatorWorked(){
        UrlValidatorAndCheckerImpl validator1 = new UrlValidatorAndCheckerImpl("https://twitter.com");
        UrlValidatorAndCheckerImpl validator2 = new UrlValidatorAndCheckerImpl("hps:/twitter.com");
        Boolean valid1 = validator1.execute();
        Boolean valid2 = validator2.execute();
        assertTrue(valid1);
        assertFalse(valid2);
    }
}
