package liquidmountain.services;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class GoogleSafeBrowsingUrlVerifier implements UrlVerifier {

//    @Value("${google.api_key}")
    private String API_KEY = "";


    public GoogleSafeBrowsingUrlVerifier() {}

    @Override
    public boolean isSafe(String url) {

        boolean isSafeUrl = true;

        try {
            Client client = ClientBuilder.newClient();

            Response response = client.target("https://safebrowsing.googleapis.com/v4/threatMatches:find?key="+API_KEY)
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
                            "        {\"url\": \"" + url +"\"}\n" +
                            "      ]\n" +
                            "    }\n" +
                            "  }",MediaType.APPLICATION_JSON));

            System.out.println(response.getStatus());
            if(response.getStatus()!=200){
                isSafeUrl = false;
            }

        } catch (RestClientException restException) {
            System.out.print("Exception occurred during HTTP call to Google Safe Browsing API: " + restException);
        }
        return isSafeUrl;
    }
}

