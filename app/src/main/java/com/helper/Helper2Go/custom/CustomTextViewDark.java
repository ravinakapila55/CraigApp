package com.helper.Helper2Go.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


public class CustomTextViewDark extends TextView {

    public CustomTextViewDark(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextViewDark(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextViewDark(Context context) {
        super(context);
        init();
    }

    public void init() {
        if (!isInEditMode()){
            Typeface normalTypeface = Typeface.createFromAsset(getContext().getAssets(), "Raleway-Black.ttf");
            setTypeface(normalTypeface);
        }
    }
}