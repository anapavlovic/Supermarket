package com.example.cubesschool8.supermarket.customComponents;

import android.content.Context;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.Timer;

/**
 * Created by Ana on 9/7/2016.
 */
public class CustomEditTextFont extends EditText {

    private OnActionTimeListener mOnActionTimeListener;

    private CountDownTimer timer;

    public interface OnActionTimeListener {
        void onAction();
    }


    public void setOnActionTimeListener(OnActionTimeListener onActionTimeListener) {

        this.mOnActionTimeListener = onActionTimeListener;
    }

    public CustomEditTextFont(Context context) {
        super(context);
        init(context);

        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                onActionTime(3000);

            }
        });
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

        Typeface t = Typeface.createFromAsset(context.getAssets(), "AvenirLTStd-Book.otf");
        setTypeface(t);
    }







    public void onActionTime(int time) {

        timer = new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if (mOnActionTimeListener != null) {
                    mOnActionTimeListener.onAction();
                }
            }
        }.start();


    }
}
