package co.lujun.androidtagview.colors;

import android.graphics.Color;

/**
 * Created by egistli on 2016/1/20.
 */
public class RandomColorFactory implements ColorFactory {
    public static final String RED = "F44336";
    public static final String LIGHTBLUE = "03A9F4";
    public static final String AMBER = "FFC107";
    public static final String ORANGE = "FF9800";
    public static final String YELLOW = "FFEB3B";
    public static final String LIME = "CDDC39";
    public static final String BLUE = "2196F3";
    public static final String INDIGO = "3F51B5";
    public static final String LIGHTGREEN = "8BC34A";
    public static final String GREY = "9E9E9E";
    public static final String DEEPPURPLE = "673AB7";
    public static final String TEAL = "009688";
    public static final String CYAN = "00BCD4";

    public static final int SHARP666666 = Color.parseColor("#FF666666");

    private static final String BG_COLOR_ALPHA = "33";
    private static final String BD_COLOR_ALPHA = "88";

    private static final String[] COLORS = new String[]{RED, LIGHTBLUE, AMBER, ORANGE, YELLOW,
            LIME, BLUE, INDIGO, LIGHTGREEN, GREY, DEEPPURPLE, TEAL, CYAN};

    @Override
    public int[] colorForTag(final String tag) {
        // RandomColorFactory ignores tag passed in
        int random = (int)(Math.random() * COLORS.length);
        int bgColor = Color.parseColor("#" + BG_COLOR_ALPHA + COLORS[random]);
        int bdColor = Color.parseColor("#" + BD_COLOR_ALPHA + COLORS[random]);
        int tColor = SHARP666666;
        return new int[]{bgColor, bdColor, tColor};
    }
}
