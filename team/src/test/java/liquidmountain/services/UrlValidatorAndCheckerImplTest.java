package liquidmountain.services;

import org.junit.Test;

import static org.junit.Assert.*;

public class UrlValidatorAndCheckerImplTest {
    /*@Test
    public void run() throws Exception {
        UrlValidatorAndCheckerImpl validator1 = new UrlValidatorAndCheckerImpl("http://example.com");
        UrlValidatorAndCheckerImpl validator2 = new UrlValidatorAndCheckerImpl("hps:/example.com");
        Boolean valid1 = validator1.execute();
        Boolean valid2 = validator2.execute();
        assertTrue(valid1);
        assertFalse(valid2);
    }*/

    @Test
    public void isValid() throws Exception {
        UrlValidatorAndCheckerImpl validator1 = new UrlValidatorAndCheckerImpl("https://twitter.com");
        UrlValidatorAndCheckerImpl validator2 = new UrlValidatorAndCheckerImpl("hps:/twitter.com");
        Boolean valid1 = validator1.isValid(validator1.url);
        Boolean valid2 = validator2.isValid(validator2.url);
        assertTrue(valid1);
        assertFalse(valid2);
    }

    @Test
    public void isAlive() throws Exception {
        UrlValidatorAndCheckerImpl validator1 = new UrlValidatorAndCheckerImpl("https://twitter.com");
        UrlValidatorAndCheckerImpl validator2 = new UrlValidatorAndCheckerImpl("https://twitter.com/pccmponentes");
        Boolean valid1 = validator1.isAlive(validator1.url);
        Boolean valid2 = validator2.isAlive(validator2.url);
        assertTrue(valid1);
        assertFalse(valid2);
    }

}