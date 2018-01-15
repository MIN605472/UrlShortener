package liquidmountain.repository;

import liquidmountain.domain.qr.Color;
import liquidmountain.domain.qr.Image;
import liquidmountain.domain.qr.QrGenerator;
import liquidmountain.domain.qr.QrQrCodeMonkey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Repository
public class QrRepositoryImpl implements QrRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(QrRepositoryImpl.class);

    @Autowired
    protected JdbcTemplate jdbc;

    public QrRepositoryImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public int saveQr(QrGenerator qr) {
        try {
            final String query = "INSERT INTO QR(SHORTURL, FG, BG, LOGO_IMG, LOGO_MEDIATYPE) VALUES (?,?,?,?,?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbc.update(con -> {
                PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, qr.getData());
                ps.setString(2, qr.getFg().getHexColor());
                ps.setString(3, qr.getBg().getHexColor());
                if (qr.getLogoImg() != null) {
                    ps.setBlob(4, qr.getLogoImg().getInputStream());
                    ps.setString(5, qr.getLogoImg().getMediaType().toString());
                } else {
                    ps.setNull(4, Types.BLOB);
                    ps.setNull(5, Types.VARCHAR);
                }
                return ps;
            }, keyHolder);
            return keyHolder.getKey().intValue();
        } catch (DataAccessException e) {
            LOGGER.error("When saving a new QR", e);
            throw e;
        }
    }

    @Override
    public void update(String shorUrl, int qrId, QrGenerator qrGenerator) {
        if (qrGenerator.getLogoImg() == null && qrGenerator.getBg() == null && qrGenerator.getFg() == null) {
            return;
        }

        StringBuilder query = new StringBuilder("UPDATE QR SET ");
        List<Pair> cols = new ArrayList<>();
        if (qrGenerator.getFg() != null) {
            cols.add(new Pair("fg", qrGenerator.getFg().getHexColor(), Types.VARCHAR));
        }
        if (qrGenerator.getBg() != null) {
            cols.add(new Pair("bg", qrGenerator.getBg().getHexColor(), Types.VARCHAR));
        }
        if (qrGenerator.getLogoImg() != null) {
            cols.add(new Pair("logo_img", qrGenerator.getLogoImg().getInputStream(), Types.BLOB));
            cols.add(new Pair("logo_mediatype", qrGenerator.getLogoImg().getMediaType().toString(), Types.VARCHAR));
        }
        for (int i = 0; i < cols.size() - 1; ++i) {
            query.append(cols.get(i).colName);
            query.append("=?, ");
        }
        if (cols.size() > 0) {
            query.append(cols.get(cols.size() - 1).colName);
            query.append("=? ");
        }
        query.append("WHERE shorturl='");
        query.append(shorUrl);
        query.append("' AND id=");
        query.append(qrId);
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(query.toString());
            for (int i = 0; i < cols.size(); ++i) {
                switch (cols.get(i).sqlType) {
                    case Types.VARCHAR:
                        ps.setString(i + 1, (String) cols.get(i).value);
                        break;
                    case Types.BLOB:
                        ps.setBinaryStream(i + 1, (InputStream) cols.get(i).value);
                        break;
                }
            }
            return ps;
        });
    }

    @Override
    public QrGenerator retrieveQr(String shortUrl, int qrId) {
        final String query = "SELECT SHORTURL, FG, BG, LOGO_IMG, LOGO_MEDIATYPE FROM QR WHERE shorturl = ? AND id = ?";
        try {
            return jdbc.queryForObject(query, (rs, rowNum) -> {
                InputStream logoStream = rs.getBinaryStream(4);
                Image logoImg = null;
                if (logoStream != null) {
                    logoImg = new Image(logoStream, MediaType.parseMediaType(rs.getString(5)));
                }
                return new QrQrCodeMonkey(rs.getString(1), new Color(rs.getString(2)), new Color(rs.getString(3)),
                        logoImg);
            }, shortUrl, qrId);
        } catch (DataAccessException e) {
            LOGGER.error("When retrieving the QR", e);
            throw e;
        }
    }

    private static class Pair {
        String colName;
        Object value;
        int sqlType;

        Pair(String colName, Object value, int sqlType) {
            this.colName = colName;
            this.value = value;
            this.sqlType = sqlType;
        }
    }
}
