package urlshortener.common.domain;

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
