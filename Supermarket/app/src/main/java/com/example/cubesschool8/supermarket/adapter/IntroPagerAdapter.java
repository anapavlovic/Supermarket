package com.example.cubesschool8.supermarket.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.example.cubesschool8.supermarket.fragment.FragmentSlideViewPagerIntro;

/**
 * Created by Ana on 9/23/2016.
 */
public class IntroPagerAdapter extends FragmentPagerAdapter {


    public IntroPagerAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {
        return new FragmentSlideViewPagerIntro();
    }

    @Override
    public int getCount() {
        return 3;
    }
}
