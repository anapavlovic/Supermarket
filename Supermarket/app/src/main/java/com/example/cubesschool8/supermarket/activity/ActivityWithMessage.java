package com.example.cubesschool8.supermarket.activity;

import android.app.Activity;
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

    public ViewGroup mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        inflater = LayoutInflater.from(getApplicationContext());

        busEventListener = new Object() {
            //sta ce da se desi kada stigne obavestenje
            @Subscribe
            public void onMesssageShow(MessageObject messageObject) {
                //Toast.makeText(getApplicationContext(), messageObject.stringResource, Toast.LENGTH_SHORT).show();

                if (mMessageView == null) {
                    mMessageView = inflater.inflate(R.layout.layout_error, mRootView, false);
                    mTextView = (CustomTextViewFont) mMessageView.findViewById(R.id.errorPopup);
                }

                mTextView.setText(messageObject.stringResource);  /// chekirati tip poruke i setovati color texta, (u konstruktoru MessageObjekta da setujemo color)
                mRootView.addView(mMessageView);
                errorAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.down_from_top);
                mMessageView.setVisibility(View.VISIBLE);
                mMessageView.setAnimation(errorAnim);

                errorAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mRootView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                errorAnim.setFillAfter(true);
                            }
                        }, new MessageObject().time);

                        errorAnimBack = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.error_up);
                        mMessageView.setAnimation(errorAnimBack);
                        mMessageView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });


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
