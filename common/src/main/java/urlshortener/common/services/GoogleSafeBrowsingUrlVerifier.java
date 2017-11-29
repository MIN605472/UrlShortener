package urlshortener.common.services;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import urlshortener.common.domain.GoogleSafeBrowsingConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

public class GoogleSafeBrowsingUrlVerifier implements UrlVerifier {

    private static final String PARAMETER_URL = "url";
    private GoogleSafeBrowsingConfig config;
    private RestTemplate restTemplate;

    public GoogleSafeBrowsingUrlVerifier(RestTemplate restTemplate, GoogleSafeBrowsingConfig config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    @Override
    public boolean isSafe(String url) {

        System.out.println("HOLITA");
        boolean isSafeUrl = true;
        Map<String, String> formParameters = config.getParameters();
        formParameters.put(PARAMETER_URL, url);

        try {

            //"https://safebrowsing.googleapis.com/v4/threatLists?key=AIzaSyAj_XBSiZbcDjCA4003qdKHkfwov42JCIs"

            Client client = ClientBuilder.newClient();

            Response response = client.target("https://safebrowsing.googleapis.com/v4/threatMatches:find?key=AIzaSyAj_XBSiZbcDjCA4003qdKHkfwov42JCIs")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity("{\n" +
                            "    \"client\": {\n" +
                            "      \"clientId\":      \"mountain-liquid\"\n" +
                            "    },\n" +
                            "    \"threatInfo\": {\n" +
                            "      \"threatTypes\":      [\"MALWARE\", \"SOCIAL_ENGINEERING\", \"POTENTIALLY_HARMFUL_APPLICATION\", \"UNWANTED_SOFTWARE\"],\n" +
                            "      \"platformTypes\":    [\"ALL_PLATFORMS\"],\n" +
                            "      \"threatEntryTypes\": [\"URL\"],\n" +
                            "      \"threatEntries\": [\n" +
                            "        {\"url\": \"http://www.youtube.com/\"}\n" +
                            "      ]\n" +
                            "    }\n" +
                            "  }",MediaType.APPLICATION_JSON));

            System.out.println(response);


             //"https://safebrowsing.googleapis.com/v4/threatLists?key=AIzaSyAj_XBSiZbcDjCA4003qdKHkfwov42JCIs",


            //System.out.println("Google Safe Browsing API returned HTTP Status: "+response.+
            //        " and message: "+ response.getBody());

            //if (response.getStatusCode() == HttpStatus.OK) {
            //    System.out.print("Possible unsafe link: "+  url + " " + response.getBody());
            //    isSafeUrl = false;
            //}

        } catch (RestClientException restException) {
            System.out.print("Exception occurred during HTTP call to Google Safe Browsing API: " + restException);
        }
        return isSafeUrl;
    }
}

