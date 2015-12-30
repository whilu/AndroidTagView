package co.lujun.androidtagview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Author: lujun
 * Date: 2015/12/30 17:14
 */
public class ContainerLayout extends ViewGroup {

    private int mVerticalInterval = 10;

    private int mHorizontalInterval = 10;
    private int mScreenWidth;

    private static final String TAG = "ContainerLayout";

    public ContainerLayout(Context context){
        this(context, null);
    }

    public ContainerLayout(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public ContainerLayout(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);
        final int childCount = getChildCount();
        int lines = childCount == 0 ? 0 : getChildLines(childCount);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        if (childCount == 0){
            setMeasuredDimension(0, 0);
        }else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize,
                    (mVerticalInterval + getChildAt(0).getMeasuredHeight()) * lines);
        }else {
            setMeasuredDimension(widthSpecSize, heightSpecSize);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int curLeft = l, curTop = t;
        for (int i = 0; i < childCount; i++) {
            final View childView = getChildAt(i);
            if (childView.getVisibility() != GONE) {
                final int width = childView.getMeasuredWidth();
                final int height = childView.getMeasuredHeight();
                if (curLeft + width + mHorizontalInterval > mScreenWidth){
                    curLeft = l;
                    curTop += height + mVerticalInterval;
                }
                Log.d(TAG, getTop() + "," + curTop);
                childView.layout(curLeft, curTop, curLeft + width, curTop + height);
                curLeft += width + mHorizontalInterval;
            }
        }
    }

    private int getChildLines(int childCount){
        int lines = 1;
        for (int i = 0, curLineW = 0; i < childCount; i++) {
            curLineW += getChildAt(i).getMeasuredWidth() + mHorizontalInterval;
            if (curLineW > mScreenWidth){
                lines++;
                curLineW = 0;
            }
        }
        return lines;
    }

    private int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    private int dp2px(float dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
