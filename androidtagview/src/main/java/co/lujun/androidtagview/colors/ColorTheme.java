package co.lujun.androidtagview.colors;

/**
 * Created by egistli on 2016/1/20.
 */
public enum ColorTheme {
    NONE(""),
    RANDOM("R0NDAM"),
    PURE_TEAL("009688"),
    PURE_CYAN("00BCD4");

    private final String colorHex;

    ColorTheme(final String colorHex) {
        this.colorHex = colorHex;
    }

    public static ColorTheme getThemeFromAttr(int theme) {
        if (theme == 0) {
            return RANDOM;
        } else if (theme == 1) {
            return PURE_TEAL;
        } else if (theme == 2) {
            return PURE_CYAN;
        }
        return NONE;
    }

    public String getColorHex() {
        return colorHex;
    }
}
