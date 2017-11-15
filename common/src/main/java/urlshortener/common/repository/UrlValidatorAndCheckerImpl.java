package urlshortener.common.repository;

public class UrlValidatorAndCheckerImpl implements UrlValidatorAndChecker {

    @Override
    public boolean isValid(){
        return true;
    }

    @Override
    public  boolean isAlive(){
        return true;
    }
}
