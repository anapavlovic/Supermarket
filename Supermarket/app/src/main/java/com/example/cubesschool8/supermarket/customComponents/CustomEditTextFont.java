package com.example.cubesschool8.supermarket.customComponents;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Ana on 9/7/2016.
 */
public class CustomEditTextFont extends EditText {
    public CustomEditTextFont(Context context) {
        super(context);
        init(context);
    }

    public CustomEditTextFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomEditTextFont(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {

        Typeface t = Typeface.createFromAsset(context.getAssets(), " ");
        setTypeface(t);
    }
}
