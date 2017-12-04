package urlshortener.common.services;

public interface UrlVerifier {
    public boolean isSafe(String url);
}
