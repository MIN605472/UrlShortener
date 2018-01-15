package liquidmountain.services;

public interface UrlValidatorAndChecker {

    /**
     * Check if @param url is well formed.
     * @param url: String que se comprueba si esta bien formada
     * @return boolean, false = no es valida, true = es valida
     */
    public boolean isValid(String url);

    /**
     * Check if @param url is Alive.
     * @param url: String que se comprueba si esta viva
     * @return boolean, false = no esta viva, true = esta viva
     */
    public boolean isAlive(String url);
}
