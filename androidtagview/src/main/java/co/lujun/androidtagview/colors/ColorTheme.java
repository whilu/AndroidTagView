package co.lujun.androidtagview.colors;

/**
 * Created by egistli on 2016/1/20.
 */
public enum ColorTheme {
    CUSTOM(""),
    RANDOM("R0NDAM"),
    PURE_TEAL("009688"),
    PURE_CYAN("00BCD4");

    public static final int ATTR_CUSTOM = -1;
    public static final int ATTR_RANDOM = 0;
    public static final int ATTR_PURE_CYAN = 1;
    public static final int ATTR_PURE_TEAL = 2;

    private final String colorHex;

    ColorTheme(final String colorHex) {
        this.colorHex = colorHex;
    }

    public static ColorTheme getThemeFromAttr(int theme) {
        if (theme == ATTR_RANDOM) {
            return RANDOM;
        } else if (theme == ATTR_PURE_TEAL) {
            return PURE_TEAL;
        } else if (theme == ATTR_PURE_CYAN) {
            return PURE_CYAN;
        }
        return CUSTOM;
    }

    public String getColorHex() {
        return colorHex;
    }
}
