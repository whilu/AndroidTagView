/*
 * Copyright 2015 lujun
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.lujun.androidtagview;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import androidx.customview.widget.ViewDragHelper;

import static co.lujun.androidtagview.Utils.dp2px;

/**
 * Author: lujun(http://blog.lujun.co)
 * Date: 2015-12-31 11:47
 */
public class TagView extends View {

    /** Border width*/
    private float mBorderWidth;

    /** Border radius*/
    private float mBorderRadius;

    /** Text size*/
    private float mTextSize;

    /** Horizontal padding for this view, include left & right padding(left & right padding are equal*/
    private int mHorizontalPadding;

    /** Vertical padding for this view, include top & bottom padding(top & bottom padding are equal)*/
    private int mVerticalPadding;

    /** TagView border color*/
    private int mBorderColor;

    /** TagView background color*/
    private int mBackgroundColor;

    /** TagView background color*/
    private int mSelectedBackgroundColor;

    /** TagView text color*/
    private int mTextColor;

    /** Whether this view clickable*/
    private boolean isViewClickable;

    /** Whether this view selectable*/
    private boolean isViewSelectable;

    /** Whether this view selected*/
    private boolean isViewSelected;

    /** The max length for this tag view*/
    private int mTagMaxLength;

    /** OnTagClickListener for click action*/
    private OnTagClickListener mOnTagClickListener;

    /** Move slop(default 5dp)*/
    private int mMoveSlop = 5;

    /** Scroll slop threshold 4dp*/
    private int mSlopThreshold = 4;

    /** How long trigger long click callback(default 500ms)*/
    private int mLongPressTime = 500;

    /** Text direction(support:TEXT_DIRECTION_RTL & TEXT_DIRECTION_LTR, default TEXT_DIRECTION_LTR)*/
    private int mTextDirection = View.TEXT_DIRECTION_LTR;

    /** The distance between baseline and descent*/
    private float bdDistance;

    /** Whether to support 'letters show with RTL(eg: Android to diordnA)' style(default false)*/
    private boolean mTagSupportLettersRTL = false;

    private Paint mPaint, mRipplePaint;

    private RectF mRectF;

    private String mAbstractText, mOriginText;

    private boolean isUp, isMoved, isExecLongClick;

    private int mLastX, mLastY;

    private float fontH, fontW;

    private float mTouchX, mTouchY;

    /** The ripple effect duration(default 1000ms)*/
    private int mRippleDuration = 1000;

    private float mRippleRadius;

    private int mRippleColor;

    private int mRippleAlpha;

    private Path mPath;

    private Typeface mTypeface;

    private ValueAnimator mRippleValueAnimator;

    private Bitmap mBitmapImage;

    private boolean mEnableCross;

    private float mCrossAreaWidth;

    private float mCrossAreaPadding;

    private int mCrossColor;

    private float mCrossLineWidth;

    private boolean unSupportedClipPath = false;

    private Runnable mLongClickHandle = new Runnable() {
        @Override
        public void run() {
            if (!isMoved && !isUp){
                int state = ((TagContainerLayout)getParent()).getTagViewState();
                if (state == ViewDragHelper.STATE_IDLE){
                    isExecLongClick = true;
                    mOnTagClickListener.onTagLongClick((int) getTag(), getText());
                }
            }
        }
    };

    public TagView(Context context, String text){
        super(context);
        init(context, text);
    }

    public TagView(Context context, String text, int defaultImageID){
        super(context);
        init(context, text);
        mBitmapImage = BitmapFactory.decodeResource(getResources(), defaultImageID);
    }

    private void init(Context context, String text){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRipplePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRipplePaint.setStyle(Paint.Style.FILL);
        mRectF = new RectF();
        mPath = new Path();
        mOriginText = text == null ? "" : text;
        mMoveSlop = (int) dp2px(context, mMoveSlop);
        mSlopThreshold = (int) dp2px(context, mSlopThreshold);
    }

    private void onDealText(){
        if(!TextUtils.isEmpty(mOriginText)) {
            mAbstractText = mOriginText.length() <= mTagMaxLength ? mOriginText
                    : mOriginText.substring(0, mTagMaxLength - 3) + "...";
        }else {
            mAbstractText = "";
        }
        mPaint.setTypeface(mTypeface);
        mPaint.setTextSize(mTextSize);
        final Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        fontH = fontMetrics.descent - fontMetrics.ascent;
        if (mTextDirection == View.TEXT_DIRECTION_RTL){
            fontW = 0;
            for (char c : mAbstractText.toCharArray()) {
                String sc = String.valueOf(c);
                fontW += mPaint.measureText(sc);
            }
        }else {
            fontW = mPaint.measureText(mAbstractText);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = mVerticalPadding * 2 + (int) fontH;
        int width = mHorizontalPadding * 2 + (int) fontW + (isEnableCross() ? height : 0) + (isEnableImage() ? height : 0);
        mCrossAreaWidth = Math.min(Math.max(mCrossAreaWidth, height), width);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF.set(mBorderWidth, mBorderWidth, w - mBorderWidth, h - mBorderWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // draw background
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(getIsViewSelected() ? mSelectedBackgroundColor : mBackgroundColor);
        canvas.drawRoundRect(mRectF, mBorderRadius, mBorderRadius, mPaint);

        // draw border
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setColor(mBorderColor);
        canvas.drawRoundRect(mRectF, mBorderRadius, mBorderRadius, mPaint);

        // draw ripple for TagView
        drawRipple(canvas);

        // draw text
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mTextColor);

        if (mTextDirection == View.TEXT_DIRECTION_RTL) {
            if (mTagSupportLettersRTL){
                float tmpX = (isEnableCross() ? getWidth() + getHeight() : getWidth()) / 2
                        + fontW / 2;
                for (char c : mAbstractText.toCharArray()) {
                    String sc = String.valueOf(c);
                    tmpX -= mPaint.measureText(sc);
                    canvas.drawText(sc, tmpX, getHeight() / 2 + fontH / 2 - bdDistance, mPaint);
                }
            }else {
                canvas.drawText(mAbstractText,
                        (isEnableCross() ? getWidth() + fontW : getWidth()) / 2 - fontW / 2,
                        getHeight() / 2 + fontH / 2 - bdDistance, mPaint);
            }
        } else {
            canvas.drawText(mAbstractText,
                    (isEnableCross() ? getWidth() - getHeight() : getWidth()) / 2 - fontW / 2 + (isEnableImage() ? getHeight() / 2 : 0),
                    getHeight() / 2 + fontH / 2 - bdDistance, mPaint);
        }

        // draw cross
        drawCross(canvas);

        // draw image
        drawImage(canvas);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (isViewClickable){
            int y = (int) event.getY();
            int x = (int) event.getX();
            int action = event.getAction();
            switch (action){
                case MotionEvent.ACTION_DOWN:
                    if (getParent() != null) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    mLastY = y;
                    mLastX = x;
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (!isViewSelected && (Math.abs(mLastY - y) > mSlopThreshold
                            || Math.abs(mLastX - x) > mSlopThreshold)){
                        if (getParent() != null) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                        isMoved = true;
                        return false;
                    }
                    break;
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            mRippleRadius = 0.0f;
            mTouchX = event.getX();
            mTouchY = event.getY();
            splashRipple();
        }
        if (isEnableCross() && isClickCrossArea(event) && mOnTagClickListener != null){
            if (action == MotionEvent.ACTION_UP) {
                mOnTagClickListener.onTagCrossClick((int) getTag());
            }
            return true;
        }else if (isViewClickable && mOnTagClickListener != null){
            int x = (int) event.getX();
            int y = (int) event.getY();
            switch (action){
                case MotionEvent.ACTION_DOWN:
                    mLastY = y;
                    mLastX = x;
                    isMoved = false;
                    isUp = false;
                    isExecLongClick = false;
                    postDelayed(mLongClickHandle, mLongPressTime);
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (isMoved){
                        break;
                    }
                    if (Math.abs(mLastX - x) > mMoveSlop || Math.abs(mLastY - y) > mMoveSlop){
                        isMoved = true;
                        if (isViewSelected){
                            mOnTagClickListener.onSelectedTagDrag((int) getTag(), getText());
                        }
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    isUp = true;
                    if (!isExecLongClick && !isMoved) {
                        mOnTagClickListener.onTagClick((int) getTag(), getText());
                    }
                    break;
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    private boolean isClickCrossArea(MotionEvent event){
        if (mTextDirection == View.TEXT_DIRECTION_RTL){
            return event.getX() <= mCrossAreaWidth;
        }
        return event.getX() >= getWidth() - mCrossAreaWidth;
    }

    private void drawImage(Canvas canvas){
        if (isEnableImage()) {
            Bitmap scaledImageBitmap = Bitmap.createScaledBitmap(mBitmapImage, Math.round(getHeight() - mBorderWidth), Math.round(getHeight() - mBorderWidth), false);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(new BitmapShader(scaledImageBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
            RectF rect = new RectF(mBorderWidth, mBorderWidth, getHeight() - mBorderWidth, getHeight() - mBorderWidth);
            canvas.drawRoundRect(rect, rect.height()/2, rect.height()/2, paint);
        }
    }

    private void drawCross(Canvas canvas){
        if (isEnableCross()){
            mCrossAreaPadding = mCrossAreaPadding > getHeight() / 2 ? getHeight() / 2 :
                    mCrossAreaPadding;
            int ltX, ltY, rbX, rbY, lbX, lbY, rtX, rtY;
            ltX = mTextDirection == View.TEXT_DIRECTION_RTL ? (int)(mCrossAreaPadding) :
                    (int)(getWidth() - getHeight() + mCrossAreaPadding);
            ltY = mTextDirection == View.TEXT_DIRECTION_RTL ? (int)(mCrossAreaPadding) :
                    (int)(mCrossAreaPadding);
            lbX = mTextDirection == View.TEXT_DIRECTION_RTL ? (int)(mCrossAreaPadding) :
                    (int)(getWidth() - getHeight() + mCrossAreaPadding);
            lbY = mTextDirection == View.TEXT_DIRECTION_RTL ?
                    (int)(getHeight() - mCrossAreaPadding) : (int)(getHeight() - mCrossAreaPadding);
            rtX = mTextDirection == View.TEXT_DIRECTION_RTL ?
                    (int)(getHeight() - mCrossAreaPadding) : (int)(getWidth() - mCrossAreaPadding);
            rtY = mTextDirection == View.TEXT_DIRECTION_RTL ? (int)(mCrossAreaPadding) :
                    (int)(mCrossAreaPadding);
            rbX = mTextDirection == View.TEXT_DIRECTION_RTL ?
                    (int)(getHeight() - mCrossAreaPadding) : (int)(getWidth() - mCrossAreaPadding);
            rbY = mTextDirection == View.TEXT_DIRECTION_RTL ?
                    (int)(getHeight() - mCrossAreaPadding) : (int)(getHeight() - mCrossAreaPadding);

            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(mCrossColor);
            mPaint.setStrokeWidth(mCrossLineWidth);
            canvas.drawLine(ltX, ltY, rbX, rbY, mPaint);
            canvas.drawLine(lbX, lbY, rtX, rtY, mPaint);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void drawRipple(Canvas canvas){
        if (isViewClickable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB &&
                canvas != null && !unSupportedClipPath){

            // Disable hardware acceleration for 'Canvas.clipPath()' when running on API from 11 to 17
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2){
                setLayerType(LAYER_TYPE_SOFTWARE, null);
            }
            try {
                canvas.save();
                mPath.reset();

                canvas.clipPath(mPath);
                mPath.addRoundRect(mRectF, mBorderRadius, mBorderRadius, Path.Direction.CCW);

//                bug: https://github.com/whilu/AndroidTagView/issues/88
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    canvas.clipPath(mPath);
                } else {
                    canvas.clipPath(mPath, Region.Op.REPLACE);
                }

                canvas.drawCircle(mTouchX, mTouchY, mRippleRadius, mRipplePaint);
                canvas.restore();
            }catch (UnsupportedOperationException e){
                unSupportedClipPath = true;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void splashRipple(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && mTouchX > 0 && mTouchY > 0){
            mRipplePaint.setColor(mRippleColor);
            mRipplePaint.setAlpha(mRippleAlpha);
            final float maxDis = Math.max(Math.max(Math.max(mTouchX, mTouchY),
                    Math.abs(getMeasuredWidth() - mTouchX)), Math.abs(getMeasuredHeight() - mTouchY));

            mRippleValueAnimator = ValueAnimator.ofFloat(0.0f, maxDis).setDuration(mRippleDuration);
            mRippleValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animValue = (float) animation.getAnimatedValue();
                    mRippleRadius = animValue >= maxDis ? 0 : animValue;
                    postInvalidate();
                }
            });
            mRippleValueAnimator.start();
        }
    }

    public String getText(){
        return mOriginText;
    }

    public boolean getIsViewClickable(){
        return isViewClickable;
    }

    public boolean getIsViewSelected(){
        return isViewSelected;
    }

    public void setTagMaxLength(int maxLength){
        this.mTagMaxLength = maxLength;
        onDealText();
    }

    public void setOnTagClickListener(OnTagClickListener listener){
        this.mOnTagClickListener = listener;
    }

    public int getTagBackgroundColor(){
        return mBackgroundColor;
    }

    public int getTagSelectedBackgroundColor(){
        return mSelectedBackgroundColor;
    }

    public void setTagBackgroundColor(int color){
        this.mBackgroundColor = color;
    }

    public void setTagSelectedBackgroundColor(int color){
        this.mSelectedBackgroundColor = color;
    }

    public void setTagBorderColor(int color){
        this.mBorderColor = color;
    }

    public void setTagTextColor(int color){
        this.mTextColor = color;
    }

    public void setBorderWidth(float width) {
        this.mBorderWidth = width;
    }

    public void setBorderRadius(float radius) {
        this.mBorderRadius = radius;
    }

    public void setTextSize(float size) {
        this.mTextSize = size;
        onDealText();
    }

    public void setHorizontalPadding(int padding) {
        this.mHorizontalPadding = padding;
    }

    public void setVerticalPadding(int padding) {
        this.mVerticalPadding = padding;
    }

    public void setIsViewClickable(boolean clickable) {
        this.isViewClickable = clickable;
    }

    public void setImage(Bitmap newImage) {
        this.mBitmapImage = newImage;
        this.invalidate();
    }

    public void setIsViewSelectable(boolean viewSelectable) {
        isViewSelectable = viewSelectable;
    }

    //TODO change background color
    public void selectView() {
        if (isViewSelectable && !getIsViewSelected()) {
            this.isViewSelected = true;
            postInvalidate();
        }
    }

    public void deselectView() {
        if (isViewSelectable && getIsViewSelected()) {
            this.isViewSelected = false;
            postInvalidate();
        }
    }

    public interface OnTagClickListener{
        void onTagClick(int position, String text);
        void onTagLongClick(int position, String text);
        void onSelectedTagDrag(int position, String text);
        void onTagCrossClick(int position);
    }

    public int getTextDirection() {
        return mTextDirection;
    }

    public void setTextDirection(int textDirection) {
        this.mTextDirection = textDirection;
    }

    public void setTypeface(Typeface typeface) {
        this.mTypeface = typeface;
        onDealText();
    }

    public void setRippleAlpha(int mRippleAlpha) {
        this.mRippleAlpha = mRippleAlpha;
    }

    public void setRippleColor(int mRippleColor) {
        this.mRippleColor = mRippleColor;
    }

    public void setRippleDuration(int mRippleDuration) {
        this.mRippleDuration = mRippleDuration;
    }

    public void setBdDistance(float bdDistance) {
        this.bdDistance = bdDistance;
    }

    public boolean isEnableImage() { return mBitmapImage != null && mTextDirection != View.TEXT_DIRECTION_RTL; }

    public boolean isEnableCross() {
        return mEnableCross;
    }

    public void setEnableCross(boolean mEnableCross) {
        this.mEnableCross = mEnableCross;
    }

    public float getCrossAreaWidth() {
        return mCrossAreaWidth;
    }

    public void setCrossAreaWidth(float mCrossAreaWidth) {
        this.mCrossAreaWidth = mCrossAreaWidth;
    }

    public float getCrossLineWidth() {
        return mCrossLineWidth;
    }

    public void setCrossLineWidth(float mCrossLineWidth) {
        this.mCrossLineWidth = mCrossLineWidth;
    }

    public float getCrossAreaPadding() {
        return mCrossAreaPadding;
    }

    public void setCrossAreaPadding(float mCrossAreaPadding) {
        this.mCrossAreaPadding = mCrossAreaPadding;
    }

    public int getCrossColor() {
        return mCrossColor;
    }

    public void setCrossColor(int mCrossColor) {
        this.mCrossColor = mCrossColor;
    }

    public boolean isTagSupportLettersRTL() {
        return mTagSupportLettersRTL;
    }

    public void setTagSupportLettersRTL(boolean mTagSupportLettersRTL) {
        this.mTagSupportLettersRTL = mTagSupportLettersRTL;
    }
}
