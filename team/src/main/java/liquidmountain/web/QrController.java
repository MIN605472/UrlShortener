package liquidmountain.web;

import liquidmountain.domain.qr.*;
import liquidmountain.domain.qr.qrcodemonkey.LogoQrCodeMonkey;
import liquidmountain.domain.qr.qrcodemonkey.QrQrCodeMonkey;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class QrController {
    private static final Logger LOGGER = LoggerFactory.getLogger(QrController.class);

    @GetMapping(value = "/api/qrs")
    public ResponseEntity<byte[]> createQr(@RequestParam String url, @RequestParam String fg, @RequestParam String
            bg, @RequestParam(required = false) String id) {
        Commands.QrCommand qrGenQrCommand = new Commands.QrCommand(new QrQrCodeMonkey(id, url, new Color(fg), new
                Color(bg)));
        Image qr = qrGenQrCommand.execute();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(qr.getMediaType());
        try {
            byte[] image = IOUtils.toByteArray(qr.getInputStream());
            return new ResponseEntity<>(image, headers, HttpStatus.OK);
        } catch (IOException e) {
            LOGGER.error("When returning a response to a request to '/api/qrs'");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/api/logos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LogoId> createLogo(@RequestPart("logoImg") MultipartFile multipartFile) {
        try {
            String[] mediaType = multipartFile.getContentType().split("/");
            Image logoImage = new Image(multipartFile.getInputStream(), new MediaType(mediaType[0], mediaType[1]));
            LogoIdGenerator logoIdGenerator = new LogoQrCodeMonkey(logoImage);
            Commands.LogoCommand qrGenLogoCommand = new Commands.LogoCommand(logoIdGenerator);
            LogoId logoId = qrGenLogoCommand.execute();
            logoImage.close();
            return new ResponseEntity<>(logoId, HttpStatus.OK);
        } catch (IOException e) {
            LOGGER.error("When returning a response to a request to '/api/logos'", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
