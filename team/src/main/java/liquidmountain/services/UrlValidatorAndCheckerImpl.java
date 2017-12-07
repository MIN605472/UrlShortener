package liquidmountain.services;

import org.apache.commons.validator.routines.UrlValidator;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class UrlValidatorAndCheckerImpl implements UrlValidatorAndChecker {

    @Override
    public boolean isValid(String url){
        UrlValidator urlValidator = new UrlValidator(new String[] { "http",
                "https" });
        return urlValidator.isValid(url);
    }

    @Override
    public  boolean isAlive(String url){
        int code = getCode(url);
        System.out.println(code);
        return code == 200;
    }

    private int getCode(String url) {
        int code = -1;
        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            code = connection.getResponseCode();
            if (code == 301) {
                System.out.println("recibido 301, redireccionando");
                code = getCode(connection.getHeaderField("location"));
            }
            if (code == 429) {
                System.out.println("recibido 429, reintentando");
                code = getCode(url);
            }
        } catch (ProtocolException | MalformedURLException e) {
            System.out.println("link malo");
            e.printStackTrace();
        } catch (IOException e) {
            return code;
        }
        return code;
    }
}
