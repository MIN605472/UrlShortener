package liquidmountain.services;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class ExtractInfo {

    public ExtractInfo(){};

    public String extractIP(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    public String extractCountry(HttpServletRequest request) {
        GeolocationAPI geoAPI = new GeolocationAPI();

        return geoAPI.getCity(extractIP(request));
    }

    public String extractBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String browser = "undefined";
        if(userAgent != null){
            if(userAgent.contains("Firefox")) {
                browser = "Mozilla Firefox";
            }
            else if(userAgent.contains("Chrome") && userAgent.contains("KHTML, like Gecko") && !userAgent.contains("Edge")) {
                browser = "Google Chrome";
            }
            else if(userAgent.contains("OPR")) {
                browser = "Opera";
            }
            else if(userAgent.contains("Safari") && userAgent.contains("Mobile")) {
                browser = "Safari";
            }
            else if(userAgent.contains(".NET") || userAgent.contains("rv:11.0")) {
                browser = "Internet Explorer";
            }
            else if(userAgent.contains("Edge")) {
                browser = "Microsoft Edge";
            }
        }
        return browser;
    }

    public String extractOS(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String os = "undefined";
        if(userAgent != null) {
            if(userAgent.contains("Windows NT 10.0")) {
                os = "Windows 10";
            }
            else if(userAgent.contains("Windows NT 6.3")) {
                os = "Windows 8.1";
            }
            else if(userAgent.contains("Windows NT 6.2")) {
                os = "Windows 8";
            }
            else if(userAgent.contains("Windows NT 6.1")) {
                os = "Windows 7";
            }
            else if(userAgent.contains("Windows NT 6.0")) {
                os = "Windows Vista";
            }
            else if(userAgent.contains("Windows NT 5")) {
                os = "Windows XP or older";
            }
            else if(userAgent.contains("Macintosh")) {
                os = "Mac OS";
            }
            else if(userAgent.contains("Android")) {
                os = "Android";
            }
            else if(userAgent.contains("Ubuntu")) {
                os = "Ubuntu Linux";
            }
            else if(userAgent.contains("Linux")) {
                os = "Other Linux";
            }
            else if(userAgent.contains("iPhone") || userAgent.contains("iPad")) {
                os = "iOS";
            }
        }
        return os;
    }

    public String extractReferrer(HttpServletRequest request) { return request.getHeader("referer");}

    public String[] extractAll(HttpServletRequest request) {
        String[] result = {extractBrowser(request), extractCountry(request), extractIP(request),
        extractOS(request), extractReferrer(request)};
        return result;
    }
}
