package com.helper.Helper2Go.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatCheckBox;

public class CustomCheckboxTextRegular extends AppCompatCheckBox {
    public CustomCheckboxTextRegular(Context context) {
        super(context);
        init();
    }

    public CustomCheckboxTextRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomCheckboxTextRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        if (!isInEditMode()){
            Typeface normalTypeface = Typeface.createFromAsset(getContext().getAssets(), "Raleway-Regular.ttf");
            setTypeface(normalTypeface);
        }
    }
}
