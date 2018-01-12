package liquidmountain.domain.qr.qrcodemonkey;

import liquidmountain.domain.qr.Color;
import liquidmountain.domain.qr.Image;
import liquidmountain.domain.qr.QrGenerator;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;

public class QrQrCodeMonkey implements QrGenerator {
    private static final String QR_URI = "https://qr-generator.qrcode.studio/qr/custom";

    private String logoId;
    private String shortUrl;
    private Color foregroundColor;
    private Color backgroundColor;

    public QrQrCodeMonkey(String logoId, String shortUrl, Color foregroundColor, Color backgroundColor) {
        this.logoId = logoId;
        this.shortUrl = shortUrl;
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
    }

    @Override
    public Image gen() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        byte[] qrImg = restTemplate.postForObject(QR_URI, new HttpEntity<>(toQrInfoDto(), headers), byte[].class);
        return new Image(new ByteArrayInputStream(qrImg), MediaType.IMAGE_PNG);
    }

    private QrInfoDto toQrInfoDto() {
        QrConfigDto qrConfigDto = new QrConfigDto();
        qrConfigDto.setBodyColor(foregroundColor.getHexColor());
        qrConfigDto.setBgColor(backgroundColor.getHexColor());
        qrConfigDto.setEye1Color(foregroundColor.getHexColor());
        qrConfigDto.setEye2Color(foregroundColor.getHexColor());
        qrConfigDto.setEye3Color(foregroundColor.getHexColor());
        qrConfigDto.setEyeBall1Color(foregroundColor.getHexColor());
        qrConfigDto.setEyeBall2Color(foregroundColor.getHexColor());
        qrConfigDto.setEyeBall3Color(foregroundColor.getHexColor());
        qrConfigDto.setLogo(logoId);
        QrInfoDto qrInfoDto = new QrInfoDto();
        qrInfoDto.setData(shortUrl);
        qrInfoDto.setSize(300);
        qrInfoDto.setFile("png");
        qrInfoDto.setConfig(qrConfigDto);
        return qrInfoDto;
    }

    private static class QrInfoDto {
        private String data;
        private int size;
        private String file;
        private QrConfigDto config;

        public QrConfigDto getConfig() {
            return config;
        }

        public void setConfig(QrConfigDto config) {
            this.config = config;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }

    private static class QrConfigDto {
        private String bodyColor;
        private String bgColor;
        private String eye1Color;
        private String eye2Color;
        private String eye3Color;
        private String eyeBall1Color;
        private String eyeBall2Color;
        private String eyeBall3Color;
        private String logo;

        public String getBodyColor() {
            return bodyColor;
        }

        public void setBodyColor(String bodyColor) {
            this.bodyColor = bodyColor;
        }

        public String getBgColor() {
            return bgColor;
        }

        public void setBgColor(String bgColor) {
            this.bgColor = bgColor;
        }

        public String getEye1Color() {
            return eye1Color;
        }

        public void setEye1Color(String eye1Color) {
            this.eye1Color = eye1Color;
        }

        public String getEye2Color() {
            return eye2Color;
        }

        public void setEye2Color(String eye2Color) {
            this.eye2Color = eye2Color;
        }

        public String getEye3Color() {
            return eye3Color;
        }

        public void setEye3Color(String eye3Color) {
            this.eye3Color = eye3Color;
        }

        public String getEyeBall1Color() {
            return eyeBall1Color;
        }

        public void setEyeBall1Color(String eyeBall1Color) {
            this.eyeBall1Color = eyeBall1Color;
        }

        public String getEyeBall2Color() {
            return eyeBall2Color;
        }

        public void setEyeBall2Color(String eyeBall2Color) {
            this.eyeBall2Color = eyeBall2Color;
        }

        public String getEyeBall3Color() {
            return eyeBall3Color;
        }

        public void setEyeBall3Color(String eyeBall3Color) {
            this.eyeBall3Color = eyeBall3Color;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }
    }
}
