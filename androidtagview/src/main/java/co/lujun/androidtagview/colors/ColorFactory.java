package co.lujun.androidtagview.colors;

/**
 * Author: lujun(http://blog.lujun.co)
 * Date: 2016-1-4 23:20
 */
public interface ColorFactory {
    /**
     * ============= -->border color
     * background color<---||-  Text --||-->text color
     * =============
     */

    /**
     * Fix color themes
     */
    public static final int NONE = -1;
    public static final int RANDOM = 0;
    public static final int PURE_CYAN = 1;
    public static final int PURE_TEAL = 2;

    int[] colorForTag(final String tag);
}
