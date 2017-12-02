package urlshortener.common.services;

public interface UrlValidatorAndChecker {

    public boolean isValid(String url);

    public boolean isAlive(String url);
}
