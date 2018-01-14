package liquidmountain.services;

import org.springframework.stereotype.Service;
import liquidmountain.domain.GeoObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Service
public class GeolocationAPI {
    /**
     * Obtain the city of @param ip
     * @param ip
     * @return
     */
    public String getCity(String ip) {
        Client client = ClientBuilder.newClient();
        System.out.println("envio " + "http://ip-api.com/json/" + ip);
        Response response = client.target("http://ip-api.com/json/" + ip)
                .request(MediaType.APPLICATION_JSON)
                .get();
        GeoObject received = response.readEntity(GeoObject.class);
        return received.city;
    }
}

