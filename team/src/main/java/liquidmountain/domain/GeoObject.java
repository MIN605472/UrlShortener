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
     * @param status: String parametro requerido por api externa
     * @param country: String pais
     * @param countryCode: String codigo del pais,
     * @param region: String region del pais
     * @param regionName: String nombre de la region
     * @param city: String ciudad
     * @param zip: String parametro requerido por api externa
     * @param lat: Float latitud
     * @param lon: Float longitud
     * @param timezone: String zona horaria
     * @param isp: String parametro requerido por api externa
     * @param org: String parametro requerido por api externa
     * @param as: String parametro requerido por api externa
     * @param query: String parametro requerido por api externa
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
