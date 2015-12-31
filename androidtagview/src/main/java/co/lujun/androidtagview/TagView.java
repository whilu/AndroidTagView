package co.lujun.androidtagview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Author: lujun
 * Date: 2015/12/31 11:47
 */
public class TagView extends TextView {

    private float mBorderWidth = 0.5f;// default 0.5dp

    private float mBorderRadius = 15.0f;// default 15dp

    private float mTextSize = 10;// default 10sp

    private int mHorizontalPadding = 20;// default 20px

    private int mVerticalPadding = 20;// default 10px

    private int mBorderColor = Color.parseColor("#AAD32F2F");// default

    private int mBackgroundColor = Color.parseColor("#33F44336");// default

    private int mTextColor = Color.parseColor("#FF666666");// default

    private boolean isViewClickable;// default false

    private Paint mPaint;

    private RectF mRect, mRectFFill;

    public TagView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr){
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AndroidTagView, defStyleAttr, 0);
        mBorderWidth = attributes.getDimension(R.styleable.AndroidTagView_tag_border_width, Utils.dp2px(context, mBorderWidth));
        mBorderRadius = attributes.getDimension(R.styleable.AndroidTagView_tag_corner_radius, Utils.dp2px(context, mBorderRadius));
        mHorizontalPadding = (int) attributes.getDimension(R.styleable.AndroidTagView_tag_horizontal_padding, mHorizontalPadding);
        mVerticalPadding = (int) attributes.getDimension(R.styleable.AndroidTagView_tag_vertical_padding, mVerticalPadding);
        mTextSize = attributes.getDimension(R.styleable.AndroidTagView_tag_text_size, Utils.sp2px(context, mTextSize));
        mBorderColor = attributes.getColor(R.styleable.AndroidTagView_tag_border_color, mBorderColor);
        mBackgroundColor = attributes.getColor(R.styleable.AndroidTagView_tag_background_color, mBackgroundColor);
        mTextColor = attributes.getColor(R.styleable.AndroidTagView_tag_text_color, mTextColor);
        isViewClickable = attributes.getBoolean(R.styleable.AndroidTagView_tag_clickable, false);
        attributes.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRect = new RectF();
        mRectFFill = new RectF();

        // Set padding
        setPadding(mHorizontalPadding, mVerticalPadding, mHorizontalPadding, mVerticalPadding);
        // default single line
        setSingleLine();
        // default gravity(center)
        setGravity(Gravity.CENTER);
        // Set text size
//        setTextSize(mTextSize);
        // Set text color
        setTextColor(mTextColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (TextUtils.isEmpty(getText()) || getText().toString().length() == 0){
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), 0);
            return;
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRect.set(canvas.getClipBounds().left + mBorderWidth,
                canvas.getClipBounds().top + mBorderWidth,
                canvas.getClipBounds().right - mBorderWidth,
                canvas.getClipBounds().bottom - mBorderWidth);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mBackgroundColor);
        mRectFFill.set(mRect.left + mBorderWidth, mRect.top + mBorderWidth,
                mRect.right - mBorderWidth, mRect.bottom - mBorderWidth);
        canvas.drawRoundRect(mRect, mBorderRadius, mBorderRadius, mPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setColor(mBorderColor);
        canvas.drawRoundRect(mRect, mBorderRadius, mBorderRadius, mPaint);
    }
}
