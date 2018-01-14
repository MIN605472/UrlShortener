package liquidmountain.services;

public interface UrlValidatorAndChecker {

    /**
     * Check if @param url is well formed.
     * @param url
     * @return
     */
    public boolean isValid(String url);

    /**
     * Check if @param url is Alive.
     * @param url
     * @return
     */
    public boolean isAlive(String url);
}
