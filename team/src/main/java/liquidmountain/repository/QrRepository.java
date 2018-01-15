package liquidmountain.repository;

import liquidmountain.domain.qr.QrGenerator;

import java.util.List;

public interface QrRepository {
    int saveQr(QrGenerator qr);

    void update(String shorUrl, int qrId, QrGenerator qrGenerator);

    QrGenerator retrieveQr(String shortUrl, int qrId);

}
