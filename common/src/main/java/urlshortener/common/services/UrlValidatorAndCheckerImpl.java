package urlshortener.common.services;

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
        if(urlValidator.isValid(url)) {
            return true;
        }else{
            return false;
        }
    }

    @Override
    public  boolean isAlive(String url){
        int code = -1;
        try {
        URL urlConnection = new URL(url);
        HttpURLConnection connection = (HttpURLConnection)urlConnection.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        code = connection.getResponseCode();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            if (code == 200){
                return true;
            }else{
                return false;
            }
        }
            if (code == 200){
            return true;
        }else{
            return false;
        }
    }
}
