package liquidmountain.domain.qr;

public abstract class QrGenerator {
    protected String data;
    protected Color bg;
    protected Color fg;
    protected Image logoImg;

    public QrGenerator(String data, Color fg, Color bg, Image logoImg) {
        this.data = data;
        this.bg = bg;
        this.fg = fg;
        this.logoImg = logoImg;
    }

    public abstract Image gen();

    public String getData() {
        return data;
    }

    public Color getBg() {
        return bg;
    }

    public Color getFg() {
        return fg;
    }

    public Image getLogoImg() {
        return logoImg;
    }
}
