package liquidmountain.qr;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/urls/{urlId}")
public class QrController {
    @PostMapping(value = "/qrs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void createQr(@PathVariable(value = "urlId") String urlId, @RequestPart("qrLogo") MultipartFile logo,
                         @RequestPart("qrColor") String color) {
        // TODO: contact external API in order to create the QR
    }

    @GetMapping(value = "/qrs")
    public void listQrs(@PathVariable(value = "urlId") String urlId) {
        // TODO: contact database to list all QRs
    }
}
