package com.example.cne_shop.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.cne_shop.R;


/**
 * Created by 博 on 2017/7/22.
 */

public class MyEditText extends AppCompatEditText implements View.OnTouchListener, View.OnFocusChangeListener, TextWatcher {



    private Drawable mClearTextIcon;
    private OnFocusChangeListener mOnFocusChangeListener;
    private OnTouchListener mOnTouchListener;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyEditText(final Context context) {
        super(context);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyEditText(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyEditText(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init(final Context context) {

        final Drawable drawable = ContextCompat.getDrawable(context, R.drawable.icon_delete_32);
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable); //Wrap the drawable so that it can be tinted pre Lollipop
//        final Drawable wrappedDrawable = getResources().getDrawable(R.drawable.icon_delete_32 , null);
        DrawableCompat.setTint(wrappedDrawable, getCurrentHintTextColor());
        mClearTextIcon = wrappedDrawable;

//        mClearTextIcon= context.getResources().getDrawable(R.drawable.icon_delete_32);
        mClearTextIcon.setBounds(0, 0, mClearTextIcon.getIntrinsicHeight(), mClearTextIcon.getIntrinsicHeight());
        setClearIconVisible(false);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(this);

    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        mOnFocusChangeListener = l;
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        mOnTouchListener = l;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
        if (mOnFocusChangeListener != null) {
            mOnFocusChangeListener.onFocusChange(v, hasFocus);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        final int x = (int) motionEvent.getX();
        if (mClearTextIcon.isVisible() && x > getWidth() - getPaddingRight() - mClearTextIcon.getIntrinsicWidth()) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                setError(null);
                setText("");
            }
            return true;
        }
        return mOnTouchListener != null && mOnTouchListener.onTouch(view, motionEvent);
    }

    @Override
    public final void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (isFocused()) {
            setClearIconVisible(text.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void setClearIconVisible(final boolean visible) {
        mClearTextIcon.setVisible(visible, false);
        final Drawable[] compoundDrawables = getCompoundDrawables();
        setCompoundDrawables(
                compoundDrawables[0],
                compoundDrawables[1],
                visible ? mClearTextIcon : null,
                compoundDrawables[3]);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }
}