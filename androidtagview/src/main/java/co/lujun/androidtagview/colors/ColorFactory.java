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

    int[] colorForTag(final String tag);
}
