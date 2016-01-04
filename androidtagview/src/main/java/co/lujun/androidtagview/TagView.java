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
 * Author: lujun(http://blog.lujun.co)
 * Date: 2015-12-31 11:47
 */
public class TagView extends View {

    /** Border width(default 0.5dp)*/
    private float mBorderWidth = 0.5f;

    /** Border radius(default 15.0dp)*/
    private float mBorderRadius = 15.0f;

    /** Text size(default 14sp)*/
    private float mTextSize = 14;

    /** Horizontal padding for this view, include left & right padding(left & right padding are equal, default 20px)*/
    private int mHorizontalPadding = 20;

    /** Vertical padding for this view, include top & bottom padding(top & bottom padding are equal, default 17px)*/
    private int mVerticalPadding = 17;

    /** TagView border color(default #88F44336)*/
    private int mBorderColor = Color.parseColor("#88F44336");

    /** TagView background color(default #33F44336)*/
    private int mBackgroundColor = Color.parseColor("#33F44336");

    /** TagView text color(default #FF666666)*/
    private int mTextColor = Color.parseColor("#FF666666");

    /** Whether this view clickable(default unclickable)*/
    private boolean isViewClickable;

    /** The max length for this tag view*/
    private int mTagMaxLength;

    /** OnTagClickListener for click action*/
    private OnTagClickListener mOnTagClickListener;

    private Paint mPaint;

    private RectF mRectF;

    private Rect mTextBound;

    private String mAbstractText, mOriginText;

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
        mTextSize = attributes.getDimension(R.styleable.AndroidTagView_tag_text_size,
                Utils.sp2px(context, mTextSize));
        mBorderColor = attributes.getColor(R.styleable.AndroidTagView_tag_border_color,
                mBorderColor);
        mBackgroundColor = attributes.getColor(R.styleable.AndroidTagView_tag_background_color,
                mBackgroundColor);
        mTextColor = attributes.getColor(R.styleable.AndroidTagView_tag_text_color, mTextColor);
        isViewClickable = attributes.getBoolean(R.styleable.AndroidTagView_tag_clickable, false);
        // FIXME can not get all attributes
        attributes.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mTextSize);
        mRectF = new RectF();
        mTextBound = new Rect();
        mOriginText = text;
    }

    private void onDealText(){
        if(!TextUtils.isEmpty(mOriginText)) {
            mAbstractText = mOriginText.length() <= mTagMaxLength ? mOriginText
                    : mOriginText.substring(0, mTagMaxLength - 3) + "...";
        }
        mPaint.getTextBounds(mAbstractText, 0, mAbstractText.length(), mTextBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mHorizontalPadding * 2 + mTextBound.width(),
                mVerticalPadding * 2 + mTextBound.height());
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
        mPaint.setColor(mTextColor);
        canvas.drawText(mAbstractText, getWidth() / 2 - mTextBound.width() / 2,
                getHeight() / 2 + mTextBound.height() / 2, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isViewClickable && event.getAction() == MotionEvent.ACTION_DOWN
                && mOnTagClickListener != null){
            mOnTagClickListener.onTagClick((int) getTag(), getText());
            return true;
        }
        return super.onTouchEvent(event);
    }

    public String getText(){
        return mOriginText;
    }

    public void setTagMaxLength(int maxLength){
        mTagMaxLength = maxLength;
        onDealText();
    }

    public void setOnTagClickListener(OnTagClickListener listener){
        mOnTagClickListener = listener;
    }

    public void setTagBackgroundColor(int color){
        mBackgroundColor = color;
    }

    public void setTagBorderColor(int color){
        mBorderColor = color;
    }

    public void setTagTextColor(int color){
        mTextColor = color;
    }

    public interface OnTagClickListener{
        void onTagClick(int position, String text);
    }
}
