package liquidmountain.domain.qr.qrcodemonkey;

import com.fasterxml.jackson.databind.ObjectMapper;
import liquidmountain.domain.qr.Image;
import liquidmountain.domain.qr.LogoId;
import liquidmountain.domain.qr.LogoIdGenerator;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class LogoQrCodeMonkey implements LogoIdGenerator {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(LogoQrCodeMonkey.class);
    private static final String LOGO_URI = "https://qr-generator.qrcode.studio/qr/uploadImage";
    private static final String FORM_NAME = "file";

    private Image logoImg;

    public LogoQrCodeMonkey(Image logoImg) {
        this.logoImg = logoImg;
    }

    @Override
    public LogoId genId() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(LOGO_URI, new HttpEntity<>(buildParts(), headers),
                String.class);
        LogoIdDto logoIdDto;
        try {
            logoIdDto = new ObjectMapper().readValue(response.getBody(), LogoIdDto.class);
        } catch (IOException e) {
            LOGGER.error("When parsing a JSON string", e);
            throw new RuntimeException();
        }
        return new LogoId(logoIdDto.getFile());
    }

    private MultiValueMap<String, Object> buildParts() {
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        HttpHeaders imgPartHeaders = new HttpHeaders();
        imgPartHeaders.setContentType(logoImg.getMediaType());
        imgPartHeaders.setContentDispositionFormData(FORM_NAME, genFilename());
        parameters.add(FORM_NAME, new HttpEntity<>(new InputStreamResource(logoImg.getInputStream()), imgPartHeaders));
        return parameters;
    }

    private String genFilename() {
        return "whatever." + logoImg.getMediaType().getSubtype();
    }

    private static class LogoIdDto {
        private String file;

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }
    }
}
