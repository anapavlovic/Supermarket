package com.example.cubesschool8.supermarket.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.customComponents.CustomTextViewFont;
import com.example.cubesschool8.supermarket.tool.BusProvider;
import com.example.cubesschool8.supermarket.tool.MessageObject;
import com.squareup.otto.Subscribe;

/**
 * Created by cubesschool8 on 9/14/16.
 */
public class ActivityWithMessage extends AppCompatActivity {


    public CustomTextViewFont mErrorPopUp;
    private View mMessageView;
    private CustomTextViewFont mTextView;
    private LayoutInflater inflater;
    private Object busEventListener;
    private Animation errorAnim, errorAnimBack;
    public static final int MESSAGE_ERROR = 0, MESSAGE_SUCCESS = 1, MESSAGE_INFO = 2;
    public ViewGroup mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        inflater = LayoutInflater.from(getApplicationContext());

        busEventListener = new Object() {
            //sta ce da se desi kada stigne obavestenje
            @Subscribe
            public void onMesssageShow(MessageObject messageObject) {

                if (mMessageView == null) {
                    mMessageView = inflater.inflate(R.layout.layout_error, mRootView, false);
                    mTextView = (CustomTextViewFont) mMessageView.findViewById(R.id.errorPopup);
                }

                mTextView.setText(messageObject.stringResource);  /// chekirati tip poruke i setovati color texta


                switch (messageObject.type) {
                    case MESSAGE_ERROR:
                        mTextView.setTextColor(getResources().getColor(R.color.message_error));
                        break;
                    case MESSAGE_INFO:
                        mTextView.setTextColor(getResources().getColor(R.color.colorAccent));
                        break;
                    case MESSAGE_SUCCESS:
                        mTextView.setTextColor(getResources().getColor(R.color.message_success));
                        break;
                    default:
                        break;

                }


                mRootView.addView(mMessageView);
                errorAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.down_from_top);
                mMessageView.setAnimation(errorAnim);
                errorAnim.setFillAfter(true);

                mRootView.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        errorAnimBack = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.error_up);
                        mMessageView.setAnimation(errorAnimBack);
                        mMessageView.setVisibility(View.GONE);
                        mRootView.removeView(mMessageView);
                    }
                }, messageObject.time);


            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        mRootView = (ViewGroup) findViewById(R.id.rootView);
        BusProvider.getInstance().register(busEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(busEventListener);
    }


}
