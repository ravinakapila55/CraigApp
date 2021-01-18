package com.helper.Helper2Go.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

public class CustomRadioButtonRegular extends RadioButton {

    public CustomRadioButtonRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomRadioButtonRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomRadioButtonRegular(Context context) {
        super(context);
        init();
    }

    public void init() {
        if (!isInEditMode()){
            Typeface normalTypeface = Typeface.createFromAsset(getContext().getAssets(), "Raleway-Regular.ttf");
            setTypeface(normalTypeface);
        }
    }
}