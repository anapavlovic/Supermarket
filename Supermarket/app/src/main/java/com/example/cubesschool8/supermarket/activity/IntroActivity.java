package com.example.cubesschool8.supermarket.activity;


import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.adapter.IntroPagerAdapter;

public class IntroActivity extends FragmentActivity {

    public ViewPager viewPager;
    private PagerAdapter mAdapter;
    private ImageView firstIndicator, secondIndicator, thirdIndicator;
    private Button mCloseButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        inicComp();
        addListener();


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        firstIndicator.setImageResource(R.drawable.circleindicator);
                        secondIndicator.setImageResource(R.drawable.circleindicatortransparent);
                        thirdIndicator.setImageResource(R.drawable.circleindicatortransparent);
                        break;
                    case 1:
                        secondIndicator.setImageResource(R.drawable.circleindicator);
                        firstIndicator.setImageResource(R.drawable.circleindicatortransparent);
                        thirdIndicator.setImageResource(R.drawable.circleindicatortransparent);
                        break;
                    case 2:
                        thirdIndicator.setImageResource(R.drawable.circleindicator);
                        firstIndicator.setImageResource(R.drawable.circleindicatortransparent);
                        secondIndicator.setImageResource(R.drawable.circleindicatortransparent);
                        break;

                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    public void inicComp() {
        mCloseButton = (Button) findViewById(R.id.introButton);
        firstIndicator = (ImageView) findViewById(R.id.first);
        secondIndicator = (ImageView) findViewById(R.id.second);
        thirdIndicator = (ImageView) findViewById(R.id.third);
        viewPager = (ViewPager) findViewById(R.id.introViewPager);
        mAdapter = new IntroPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(0);
        firstIndicator.setImageResource(R.drawable.circleindicator);
        secondIndicator.setImageResource(R.drawable.circleindicatortransparent);
        thirdIndicator.setImageResource(R.drawable.circleindicatortransparent);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.setPageMargin(
                getResources().getDimensionPixelOffset(R.dimen.viewpager_margin));


    }


    public void addListener() {
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LogInActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }
}
