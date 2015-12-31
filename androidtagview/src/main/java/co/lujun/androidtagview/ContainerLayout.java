package co.lujun.androidtagview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Author: lujun
 * Date: 2015/12/30 17:14
 */
public class ContainerLayout extends ViewGroup {

    /** Vertical interval, default 5(dp)*/
    private int mVerticalInterval;

    /** Horizontal interval, default 5(dp)*/
    private int mHorizontalInterval;

    /** Tag view average height*/
    private int mChildHeight;

    /** Default interval(dp)*/
    private static final float DEFAULT_INTERVAL = 5;

    public ContainerLayout(Context context){
        this(context, null);
    }

    public ContainerLayout(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public ContainerLayout(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr){
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AndroidTagView, defStyleAttr, 0);
        mVerticalInterval = (int) attributes.getDimension(R.styleable.AndroidTagView_vertical_interval, dp2px(DEFAULT_INTERVAL));
        mHorizontalInterval = (int) attributes.getDimension(R.styleable.AndroidTagView_horizontal_interval, dp2px(DEFAULT_INTERVAL));
        attributes.recycle();
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
            int childHeight = getChildAt(0).getMeasuredHeight();
            setMeasuredDimension(widthSpecSize, (mVerticalInterval + childHeight) * lines
                    - mVerticalInterval + getPaddingTop() + getPaddingBottom());
        }else {
            setMeasuredDimension(widthSpecSize, heightSpecSize);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int curLeft = getPaddingLeft(), curTop = getPaddingTop();
        for (int i = 0; i < childCount; i++) {
            final View childView = getChildAt(i);
            if (childView.getVisibility() != GONE) {
                int width = childView.getMeasuredWidth();
                if (curLeft + width + mHorizontalInterval
                        > getMeasuredWidth() - getPaddingLeft() - getPaddingRight()){
                    curLeft = getPaddingLeft();
                    curTop += mChildHeight + mVerticalInterval;
                }
                childView.layout(curLeft, curTop, curLeft + width, curTop + mChildHeight);
                curLeft += width + mHorizontalInterval;
            }
        }
    }

    private int getChildLines(int childCount){
        int lines = 1;
        for (int i = 0, curLineW = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            int dis = childView.getMeasuredWidth() + mHorizontalInterval;
            int height = childView.getMeasuredHeight();
            mChildHeight = i == 0 ? height : Math.min(mChildHeight, height);
            curLineW += dis;
            if (curLineW > getMeasuredWidth() - getPaddingLeft() - getPaddingRight()){
                lines++;
                curLineW = dis;
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

    public void setVerticalInterval(float interval){
        mVerticalInterval = dp2px(interval);
    }

    public void setHorizontalInterval(float interval){
        mHorizontalInterval = dp2px(interval);
    }

    public int getVerticalInterval(){
        return mVerticalInterval;
    }

    public int getHorizontalInterval(){
        return mHorizontalInterval;
    }
}
