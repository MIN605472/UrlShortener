package urlshortener.common.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import javax.ws.rs.core.Response;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.client.Entity;

public class GeolocationAPI {

    public GeolocationAPI() {}

    public String getCity(String ip) {
        Client client = ClientBuilder.newClient();
        System.out.println("envio " + "http://ip-api.com/json/" + ip);
        Response response = client.target("http://ip-api.com/json/" + ip)
                .request(MediaType.APPLICATION_JSON)
                .get();
        System.out.println("recibo " + response);
        GeoObject received = response.readEntity(GeoObject.class);
        System.out.println("recibido " + received.city);
        return received.city;
    }
}

class GeoObject {
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
