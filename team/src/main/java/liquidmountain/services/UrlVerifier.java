package liquidmountain.services;

public interface UrlVerifier {
    /**
     * Check if @param url is safe.
     * @param url
     * @return
     */
    public boolean isSafe(String url);
}
