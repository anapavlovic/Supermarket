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
    private int time;


    public interface OnActionTimeListener {
        void onAction();
    }


    public void setOnActionTimeListener(OnActionTimeListener onActionTimeListener) {

        this.mOnActionTimeListener = onActionTimeListener;
    }

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

        Typeface t = Typeface.createFromAsset(context.getAssets(), "AvenirLTStd-Book.otf");
        setTypeface(t);
    }


    public void onActionTime(final int time) {
        this.time = time;

        this.addTextChangedListener(new TextWatcher() {
            CountDownTimer timer = null;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {





            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>=3){
                if (timer != null) {
                    timer.cancel();
                }
                timer = new CountDownTimer(time, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        if (mOnActionTimeListener != null) {
                            mOnActionTimeListener.onAction();
                        }
                    }
                }.start();
            }}
        });

    }
}
