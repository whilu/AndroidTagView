package co.lujun.androidtagview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Author: lujun
 * Date: 2015/12/31 11:47
 */
public class TagView extends TextView {

    public TagView(Context context){
        this(context, null, 0);
    }

    public TagView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public TagView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr){
        setSingleLine();
    }
}
