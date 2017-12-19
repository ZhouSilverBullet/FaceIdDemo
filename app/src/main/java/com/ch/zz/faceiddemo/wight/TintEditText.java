package com.ch.zz.faceiddemo.wight;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.View;

import com.ch.zz.faceiddemo.R;

/**
 * Created by admin on 2017/11/28.
 */
public class TintEditText extends AppCompatEditText implements View.OnFocusChangeListener {
    private Drawable drawableLeft;
    private Drawable wrappedDrawable;
    private Drawable drawableTop;
    private Drawable drawableRight;
    private Drawable drawableBottom;

    public TintEditText(Context context) {
        super(context);
        init();
    }

    public TintEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TintEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        /*我们都知道，TextView的子类都是可以设置Drawable的，当前咯，不此TextView,不知道自己去查资料*/
        setOnFocusChangeListener(this);
        Drawable[] compoundDrawables = getCompoundDrawables();
        drawableLeft = compoundDrawables[0];//左
        drawableTop = compoundDrawables[1];//上
        drawableRight = compoundDrawables[2];//右
        drawableBottom = compoundDrawables[3];//下
        if (drawableLeft != null) {
            /*这里判断下表示设置的左边图片*/
            wrappedDrawable = DrawableCompat.wrap(drawableLeft);
            drawableLeft = wrappedDrawable;
            /*设置默认着色*/
            DrawableCompat.setTint(wrappedDrawable, getResources().getColor(R.color.fd_hui));
            setCompoundDrawables(drawableLeft, drawableTop, drawableRight, drawableBottom);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            /*如果有焦点，就设置成当前文本的颜色，这里的颜色可以自己去修改，也可以自己自定义属性在布局里设置*/
            DrawableCompat.setTint(wrappedDrawable, getResources().getColor(R.color.colorAccent));
        } else {
            /*如果没有焦点，就设置成当前提示文本颜色*/
            DrawableCompat.setTint(wrappedDrawable, getResources().getColor(R.color.fd_hui));
        }
        setCompoundDrawables(drawableLeft, drawableTop, drawableRight, drawableBottom);
    }
}