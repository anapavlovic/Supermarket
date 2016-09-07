package com.example.cubesschool8.supermarket.customComponents;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Ana on 9/7/2016.
 */
public class CustomTextViewFont extends TextView {
    public CustomTextViewFont(Context context) {
        super(context);
        init(context);
    }

    public CustomTextViewFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomTextViewFont(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context)
    {
        try
        {
            Typeface myFont = Typeface.createFromAsset(context.getAssets(), "AvenirLTStd-Book.otf");

            setTypeface(myFont);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
