package liquidmountain.domain.qr;

public abstract class QrGenerator {
    protected String data;
    protected Color bg;
    protected Color fg;
    protected Image logoImg;

    public QrGenerator(String data, Color bg, Color fg, Image logoImg) {
        this.data = data;
        this.bg = bg;
        this.fg = fg;
        this.logoImg = logoImg;
    }

    public abstract Image gen();

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Color getBg() {
        return bg;
    }

    public void setBg(Color bg) {
        this.bg = bg;
    }

    public Color getFg() {
        return fg;
    }

    public void setFg(Color fg) {
        this.fg = fg;
    }

    public Image getLogoImg() {
        return logoImg;
    }

    public void setLogoImg(Image logoImg) {
        this.logoImg = logoImg;
    }
}
