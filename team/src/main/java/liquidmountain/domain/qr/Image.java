package liquidmountain.domain.qr;

import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.InputStream;

public class Image {
    private InputStream inputStream;
    private MediaType mediaType;

    public Image(InputStream inputStream, MediaType mediaType) {
        this.inputStream = inputStream;
        this.mediaType = mediaType;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public void close() throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
    }
}
