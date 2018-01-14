package liquidmountain.domain;


/**
 * Class to give persistence Url's geoObjet
 */
public class GeoObject {
    public String status;
    public String country;
    public String countryCode;
    public String region;
    public String regionName;
    public String city;
    public String zip;
    public float lat;
    public float lon;
    public String timezone;
    public String isp;
    public String org;
    public String as;
    public String query;

    /**
     * Constructor
     * @param String status parametro requerido por api externa
     * @param String country, pais
     * @param String countryCode, codigo del pais,
     * @param String region, region del pais
     * @param String regionName, nombre de la region
     * @param String city, ciudad
     * @param String zip, parametro requerido por api externa
     * @param Float lat, latitud
     * @param Float lon, longitud
     * @param String timezone, zona horaria
     * @param String isp, parametro requerido por api externa
     * @param String org, parametro requerido por api externa
     * @param String as, parametro requerido por api externa
     * @param String query, parametro requerido por api externa
     */
    public GeoObject(String status, String country, String countryCode, String region, String regionName, String city, String zip, float lat, float lon, String timezone, String isp, String org, String as, String query) {
        this.status = status;
        this.country = country;
        this.countryCode = countryCode;
        this.region = region;
        this.regionName = regionName;
        this.city = city;
        this.zip = zip;
        this.lat = lat;
        this.lon = lon;
        this.timezone = timezone;
        this.isp = isp;
        this.org = org;
        this.as = as;
        this.query = query;
    }
    public GeoObject () {}
}
