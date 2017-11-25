package urlshortener.common.services;

public class GoogleSafeBrowsingUrlVerifier implements UrlVerifier {
    @Override
    public boolean isSafe() {
        return true;
    }
}
