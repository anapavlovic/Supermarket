package com.example.cubesschool8.supermarket;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cubesschool8.supermarket.adapter.PagerAdapter;
import com.example.cubesschool8.supermarket.customComponents.CustomEditTextFont;
import com.example.cubesschool8.supermarket.customComponents.CustomTextViewFont;

/**
 * Created by cubesschool8 on 9/7/16.
 */
public class LogInActivity extends AppCompatActivity {
    private ImageView mOverlayImg, mLogo;
    private CustomEditTextFont mUsername, mPassword;
    private LinearLayout mLinearAnim;
    private Button mProceedButton;
    private CustomTextViewFont mPassforgot;

    private Animation animation, animationViewPager, animfadeIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        inicComp();
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.up_from_bottom);
        animation.setFillAfter(true);
        mLogo.setAnimation(animation);

        animfadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        mOverlayImg.setVisibility(View.VISIBLE);
        animfadeIn.setFillAfter(true);
        mOverlayImg.setAnimation(animfadeIn);


        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animationViewPager = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.up_viewpager);
                mLinearAnim.setVisibility(View.VISIBLE);
                animationViewPager.setFillAfter(true);
                mLinearAnim.setAnimation(animationViewPager);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Prijava"));
        tabLayout.addTab(tabLayout.newTab().setText("Registracija"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    private void inicComp() {
        mOverlayImg = (ImageView) findViewById(R.id.overlay);
        mLogo = (ImageView) findViewById(R.id.logoLogin);
        mUsername = (CustomEditTextFont) findViewById(R.id.tvPrijava);
        mPassword = (CustomEditTextFont) findViewById(R.id.tvPass);
        mLinearAnim = (LinearLayout) findViewById(R.id.linearAnim);
        mProceedButton = (Button) findViewById(R.id.proceedButt);
        mPassforgot = (CustomTextViewFont) findViewById(R.id.tvpassforgot);

    }

}
