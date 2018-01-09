package liquidmountain.services;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class UrlValidatorAndCheckerImpl extends HystrixCommand<Boolean> implements UrlValidatorAndChecker {
    public final String url;
    public boolean valid = true;
    public boolean alive = true;

    public UrlValidatorAndCheckerImpl(String url){
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("Validator"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(2000)));
        this.url = url;
    }

    @Override
    protected Boolean run() throws Exception {
        String msg = "";
        if (isValid(url)){
            valid = true;
            if (isAlive(url)){
                alive = true;
                msg = "Se ha acortado con exito " + url + "!";
            }else {
                alive = false;
                msg = "Parece que " + url + " no esta disponible!";
            }
        }else{
            valid = false;
            msg = "Parece que " + url + " no esta bien formada!";
        }
//        System.out.println(msg);
        return (valid && alive);
    }

    @Override
    protected Boolean getFallback() {
        alive = false;
        System.out.println("Vuelve a intentar acortar " + url + "!");
        return false;
    }

    @Override
    public boolean isValid(String url){
        UrlValidator urlValidator = new UrlValidator(new String[] { "http",
                "https" });
        return urlValidator.isValid(url);
    }

    @Override
    public boolean isAlive(String url){
        int code = getCode(url);
        System.out.println(code);
        return code == 200;
    }

    private int getCode(String url) {
        int code = -1;
        if(!alive){
            return code;
        }
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
