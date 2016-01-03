package co.lujun.androidtagview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Author: lujun
 * Date: 2015/12/31 11:47
 */
public class TagView extends View {

    /** Border width(default 0.5dp)*/
    private float mBorderWidth = 0.5f;

    /** Border radius(default 15.0dp)*/
    private float mBorderRadius = 15.0f;

    /** Text size(default 14sp)*/
    private float mAbstractTextSize = 14;

    /** Horizontal padding for this view, include left & right padding(left & right padding are equal, default 20px)*/
    private int mHorizontalPadding = 20;

    /** Vertical padding for this view, include top & bottom padding(top & bottom padding are equal, default 17px)*/
    private int mVerticalPadding = 17;

    /** TagView border color(default #88F44336)*/
    private int mBorderColor = Color.parseColor("#88F44336");

    /** TagView background color(default #33F44336)*/
    private int mBackgroundColor = Color.parseColor("#33F44336");

    /** TagView text color(default #FF666666)*/
    private int mAbstractTextColor = Color.parseColor("#FF666666");

    /** Whether this view clickable(default unclickable)*/
    private boolean isViewClickable;

    private Paint mPaint;

    private RectF mRectF;

    private Rect mAbstractTextBound;

    private String mAbstractText = "", mOriginText = "";

    /** The max length for this tag view*/
    private int mTagMaxLength = 23;

    /** OnTagClickListener for click action*/
    private OnTagClickListener mOnTagClickListener;

    public TagView(Context context, AttributeSet attrs, int defStyleAttr, String text){
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, text);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, String text){
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AndroidTagView,
                defStyleAttr, 0);
        mBorderWidth = attributes.getDimension(R.styleable.AndroidTagView_tag_border_width,
                Utils.dp2px(context, mBorderWidth));
        mBorderRadius = attributes.getDimension(
                R.styleable.AndroidTagView_tag_corner_radius, Utils.dp2px(context, mBorderRadius));
        mHorizontalPadding = (int) attributes.getDimension(
                R.styleable.AndroidTagView_tag_horizontal_padding, mHorizontalPadding);
        mVerticalPadding = (int) attributes.getDimension(
                R.styleable.AndroidTagView_tag_vertical_padding, mVerticalPadding);
        mAbstractTextSize = attributes.getDimension(R.styleable.AndroidTagView_tag_text_size,
                Utils.sp2px(context, mAbstractTextSize));
        mBorderColor = attributes.getColor(R.styleable.AndroidTagView_tag_border_color,
                mBorderColor);
        mBackgroundColor = attributes.getColor(R.styleable.AndroidTagView_tag_background_color,
                mBackgroundColor);
        mAbstractTextColor = attributes.getColor(R.styleable.AndroidTagView_tag_text_color, mAbstractTextColor);
        isViewClickable = attributes.getBoolean(R.styleable.AndroidTagView_tag_clickable, true);
        mTagMaxLength = attributes.getInt(R.styleable.AndroidTagView_tag_max_length, mTagMaxLength);
        attributes.recycle();

        onDealText(text);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mAbstractTextSize);
        mRectF = new RectF();
        mAbstractTextBound = new Rect();

        // get text bound
        mPaint.getTextBounds(mAbstractText, 0, mAbstractText.length(), mAbstractTextBound);
        mPaint.measureText(mAbstractText);
    }

    private void onDealText(String text){
        if(!TextUtils.isEmpty(text)) {
            mOriginText = text;
            mAbstractText = text.length() <= mTagMaxLength ? text : text.substring(0, mTagMaxLength - 3) + "...";
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mHorizontalPadding * 2 + mAbstractTextBound.width(),
                mVerticalPadding * 2 + mAbstractTextBound.height());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRectF.set(canvas.getClipBounds().left + mBorderWidth,
                canvas.getClipBounds().top + mBorderWidth,
                canvas.getClipBounds().right - mBorderWidth,
                canvas.getClipBounds().bottom - mBorderWidth);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mBackgroundColor);
        canvas.drawRoundRect(mRectF, mBorderRadius, mBorderRadius, mPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setColor(mBorderColor);
        canvas.drawRoundRect(mRectF, mBorderRadius, mBorderRadius, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mAbstractTextColor);
        canvas.drawText(mAbstractText, getWidth() / 2 - mAbstractTextBound.width() / 2,
                getHeight() / 2 + mAbstractTextBound.height() / 2, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isViewClickable && event.getAction() == MotionEvent.ACTION_DOWN
                && mOnTagClickListener != null){
            mOnTagClickListener.onTagClick((int)getTag(), getText());
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void setText(String text){
        onDealText(text);
        postInvalidate();
    }

    public String getText(){
        return mOriginText;
    }

    public void setTagMaxLength(int maxLength){
        mTagMaxLength = maxLength;
    }

    public int getTagMaxLength(){
        return mTagMaxLength;
    }

    public void setOnTagClickListener(OnTagClickListener listener){
        mOnTagClickListener = listener;
    }

    public interface OnTagClickListener{
        void onTagClick(int position, String text);
    }
}
