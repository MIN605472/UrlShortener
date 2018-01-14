package liquidmountain.services;

public interface UrlVerifier {
    /**
     * Check if @param url is safe.
     * @param url que se va a comprobar si es segura
     * @return true = segura, false = insegura
     */
    public boolean isSafe(String url);
}
