package co.lujun.androidtagview.colors;

import android.graphics.Color;

/**
 * Created by egistli on 2016/1/20.
 */
public class PureColorFactory implements ColorFactory {
    private static final String BG_COLOR_ALPHA = "33";
    private static final String BD_COLOR_ALPHA = "88";
    private static final int SHARP727272 = Color.parseColor("#FF727272");

    private String color;
    private int[] colors;

    public PureColorFactory(final ColorTheme theme) {
        this.color = theme.getColorHex();
        cacheColors();
    }

    private void cacheColors() {
        int bgColor = Color.parseColor("#" + BG_COLOR_ALPHA + color);
        int bdColor = Color.parseColor("#" + BD_COLOR_ALPHA + color);
        int tColor = SHARP727272;
        colors = new int[]{bgColor, bdColor, tColor};
    }

    @Override
    public int[] colorForTag(final String tag) {
        return colors;
    }
}
