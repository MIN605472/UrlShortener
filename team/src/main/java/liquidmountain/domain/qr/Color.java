package liquidmountain.domain.qr;

public class Color {
    private String color;

    public Color() {
    }

    public Color(String hexColor) {
        this.color = hexColor;
    }

    public String getHexColor() {
        return color;
    }

    public void setHexColor(String color) {
        this.color = color;
    }
}
