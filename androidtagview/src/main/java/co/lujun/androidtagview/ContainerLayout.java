package co.lujun.androidtagview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Author: lujun
 * Date: 2015/12/30 17:14
 */
public class ContainerLayout extends ViewGroup {

    /** Vertical interval, default 5(dp)*/
    private float mVerticalInterval;

    /** Horizontal interval, default 5(dp)*/
    private float mHorizontalInterval;

    /** ContainerLayout border width(default 0.5dp)*/
    private float mBorderWidth = 0.5f;

    /** ContainerLayout border radius(default 10.0dp)*/
    private float mBorderRadius = 10.0f;

    /** Tag view average height*/
    private int mChildHeight;

    /** ContainerLayout border color(default #22FF0000)*/
    private int mBorderColor = Color.parseColor("#22FF0000");

    /** ContainerLayout background color(default #11FF0000)*/
    private int mBackgroundColor = Color.parseColor("#11FF0000");

    /** Tags*/
    private List<String> mTags;

    /** AttributeSet for child view*/
    private AttributeSet mAttrs;

    private Paint mPaint;

    private RectF mRectF;

    /** OnTagClickListener for child view*/
    private TagView.OnTagClickListener mOnTagClickListener;

    /** Default interval(dp)*/
    private static final float DEFAULT_INTERVAL = 5;

    public ContainerLayout(Context context) {
        this(context, null);
    }

    public ContainerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContainerLayout(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr){
        mAttrs = attrs;
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AndroidTagView,
                defStyleAttr, 0);
        mVerticalInterval = attributes.getDimension(R.styleable.AndroidTagView_vertical_interval,
                Utils.dp2px(context, DEFAULT_INTERVAL));
        mHorizontalInterval = attributes.getDimension(R.styleable.AndroidTagView_horizontal_interval,
                Utils.dp2px(context, DEFAULT_INTERVAL));
        mBorderWidth = attributes.getDimension(R.styleable.AndroidTagView_container_border_width,
                Utils.dp2px(context, mBorderWidth));
        mBorderRadius = attributes.getDimension(R.styleable.AndroidTagView_container_corner_radius,
                Utils.dp2px(context, mBorderRadius));
        mBorderColor = attributes.getColor(R.styleable.AndroidTagView_container_border_color,
                mBorderColor);
        mBackgroundColor = attributes.getColor(R.styleable.AndroidTagView_container_background_color,
                mBackgroundColor);

        attributes.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectF = new RectF();
        setWillNotDraw(false);
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
            setMeasuredDimension(widthSpecSize, (int)((mVerticalInterval + childHeight) * lines
                    - mVerticalInterval + getPaddingTop() + getPaddingBottom()));
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRectF.set(canvas.getClipBounds().left, canvas.getClipBounds().top,
                canvas.getClipBounds().right, canvas.getClipBounds().bottom);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mBackgroundColor);
        canvas.drawRoundRect(mRectF, mBorderRadius, mBorderRadius, mPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setColor(mBorderColor);
        canvas.drawRoundRect(mRectF, mBorderRadius, mBorderRadius, mPaint);
    }

    private int getChildLines(int childCount){
        int lines = 1;
        for (int i = 0, curLineW = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            int dis = childView.getMeasuredWidth() + (int) mHorizontalInterval;
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

    private void onSetTag(){
        if (mTags == null || mTags.size() == 0){
            return;
        }
        for (String text : mTags) {
            TagView tagView = new TagView(getContext(), mAttrs, 0, text);
            tagView.setTag(mTags.indexOf(text));
            tagView.setOnTagClickListener(mOnTagClickListener);
            addView(tagView);
        }
        postInvalidate();
    }

    public void setVerticalInterval(float interval){
        mVerticalInterval = Utils.dp2px(getContext(), interval);
    }

    public void setHorizontalInterval(float interval){
        mHorizontalInterval = Utils.dp2px(getContext(), interval);
    }

    public float getVerticalInterval(){
        return mVerticalInterval;
    }

    public float getHorizontalInterval(){
        return mHorizontalInterval;
    }

    public void setTags(List<String> tags){
        mTags = tags;
        onSetTag();
    }

    public void setOnTagClickListener(TagView.OnTagClickListener listener){
        mOnTagClickListener = listener;
    }
}
