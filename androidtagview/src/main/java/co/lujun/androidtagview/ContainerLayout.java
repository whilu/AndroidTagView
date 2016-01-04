package co.lujun.androidtagview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: lujun
 * Date: 2015-12-30 17:14
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

    /** The sensitive of the ViewDragHelper(default 1.0f, normal)*/
    private float mSensitivity = 1.0f;

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

    /** Can drag view(default false)*/
    private boolean mDragEnable;

    /** OnTagClickListener for child view*/
    private TagView.OnTagClickListener mOnTagClickListener;

    private Paint mPaint;

    private RectF mRectF;

    private ViewDragHelper mViewDragHelper;

    private List<View> mChildViews;

    private int[] mViewPos;

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
        mDragEnable = attributes.getBoolean(R.styleable.AndroidTagView_container_enable_drag, false);
        mSensitivity = attributes.getFloat(R.styleable.AndroidTagView_container_drag_sensitivity,
                mSensitivity);
        attributes.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectF = new RectF();
        mChildViews = new ArrayList<View>();
        mViewDragHelper = ViewDragHelper.create(this, mSensitivity, new DragHelperCallBack());
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
        int availableW = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int childCount = getChildCount();
        int curLeft = getPaddingLeft(), curTop = getPaddingTop();
        mViewPos = new int[childCount * 2];
        for (int i = 0; i < childCount; i++) {
            final View childView = getChildAt(i);
            if (childView.getVisibility() != GONE) {
                int width = childView.getMeasuredWidth();
                if (curLeft + width + mHorizontalInterval - getPaddingLeft() > availableW){
                    curLeft = getPaddingLeft();
                    curTop += mChildHeight + mVerticalInterval;
                }
                mViewPos[i * 2] = curLeft;
                mViewPos[i * 2 + 1] = curTop;
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

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mViewDragHelper.continueSettling(true)){
            requestLayout();
        }
    }

    private int getChildLines(int childCount){
        int availableW = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int lines = 1;
        for (int i = 0, curLineW = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            int dis = childView.getMeasuredWidth() + (int) mHorizontalInterval;
            int height = childView.getMeasuredHeight();
            mChildHeight = i == 0 ? height : Math.min(mChildHeight, height);
            curLineW += dis;
            if (curLineW > availableW){
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
        for (int i = 0; i < mTags.size(); i++) {
            onAddTag(mTags.get(i), mChildViews.size());
        }
        postInvalidate();
    }

    private void onAddTag(String text, int position){
        TagView tagView = new TagView(getContext(), mAttrs, 0, text);
        tagView.setOnTagClickListener(mOnTagClickListener);
        mChildViews.add(position, tagView);
        if (position < mChildViews.size()){
            for (int i = position; i < mChildViews.size(); i++) {
                mChildViews.get(i).setTag(i);
            }
        }else {
            tagView.setTag(position);
        }
        addView(tagView, position);
    }

    private int[] onGetNewPosition(View view){
        int left = view.getLeft();
        int top = view.getTop();
        int bestMatchLeft = mViewPos[(int)view.getTag() * 2];
        int bestMatchTop = mViewPos[(int)view.getTag() * 2 + 1];
        int tmpTopDis = Math.abs(top - bestMatchTop);
        for (int i = 0; i < mViewPos.length / 2; i++) {
            if (Math.abs(top - mViewPos[i * 2 +1]) < tmpTopDis){
                bestMatchTop = mViewPos[i * 2 +1];
                tmpTopDis = Math.abs(top - mViewPos[i * 2 +1]);
            }
        }
        int rowChildCount = 0;
        int tmpLeftDis = 0;
        for (int i = 0; i < mViewPos.length / 2; i++) {
            if (mViewPos[i * 2 + 1] == bestMatchTop){
                if (rowChildCount == 0){
                    bestMatchLeft = mViewPos[i * 2];
                    tmpLeftDis = Math.abs(left - bestMatchLeft);
                }else {
                    if (Math.abs(left - mViewPos[i * 2]) < tmpLeftDis){
                        bestMatchLeft = mViewPos[i * 2];
                        tmpLeftDis = Math.abs(left - bestMatchLeft);
                    }
                }
                rowChildCount++;
            }
        }
        return new int[]{bestMatchLeft, bestMatchTop};
    }

    private int onGetCoordinateReferPos(int left, int top){
        int  pos = 0;
        for (int i = 0; i < mViewPos.length / 2; i++) {
            if (left == mViewPos[i * 2] && top == mViewPos[i * 2 + 1]){
                pos = i;
            }
        }
        return pos;
    }

    private void onChangeView(View view, int newPos, int originPos){
        mChildViews.remove(originPos);
        mChildViews.add(newPos, view);
        for (View child : mChildViews) {
            child.setTag(mChildViews.indexOf(child));
        }

        removeViewAt(originPos);
        addView(view, newPos);
    }

    private class DragHelperCallBack extends ViewDragHelper.Callback{

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return mDragEnable;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            final int leftX = getPaddingLeft();
            final int rightX = getWidth() - child.getWidth() - getPaddingRight();
            return Math.min(Math.max(left, leftX), rightX);
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            final int topY = getPaddingTop();
            final int bottomY = getHeight() - child.getHeight() - getPaddingBottom();
            return Math.min(Math.max(top, topY), bottomY);
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return getMeasuredWidth() - child.getMeasuredWidth();
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return getMeasuredHeight() - child.getMeasuredHeight();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            int[] pos = onGetNewPosition(releasedChild);
            int posRefer = onGetCoordinateReferPos(pos[0], pos[1]);
            onChangeView(releasedChild, posRefer, (int) releasedChild.getTag());
            mViewDragHelper.settleCapturedViewAt(pos[0], pos[1]);
            invalidate();
        }
    }

    /**
     * Set vertical interval
     * @param interval
     */
    public void setVerticalInterval(float interval){
        mVerticalInterval = Utils.dp2px(getContext(), interval);
        postInvalidate();
    }

    /**
     * Set horizontal interval.
     * @param interval
     */
    public void setHorizontalInterval(float interval){
        mHorizontalInterval = Utils.dp2px(getContext(), interval);
        postInvalidate();
    }

    /**
     * Get vertical interval in this view.
     * @return
     */
    public float getVerticalInterval(){
        return mVerticalInterval;
    }

    /**
     * Get horizontal interval in this view.
     * @return
     */
    public float getHorizontalInterval(){
        return mHorizontalInterval;
    }

    /**
     * Set tags
     * @param tags
     */
    public void setTags(List<String> tags){
        mTags = tags;
        onSetTag();
    }

    /**
     * Set OnTagClickListener for TagView.
     * @param listener
     */
    public void setOnTagClickListener(TagView.OnTagClickListener listener){
        mOnTagClickListener = listener;
    }

    /**
     * Set whether the child view can be dragged.
     * @param enable
     */
    public void setDragEnable(boolean enable){
        mDragEnable = enable;
    }

    /**
     * Inserts the specified TagView into this ContainerLayout at the end.
     * @param text
     */
    public void addTag(String text){
        addTag(text, mChildViews.size());
    }

    /**
     * Inserts the specified TagView into this ContainerLayout at the specified location.
     * The TagView is inserted before the current element at the specified location.
     * @param text
     * @param position
     */
    public void addTag(String text, int position){
        onAddTag(text, position);
        postInvalidate();
    }
}
