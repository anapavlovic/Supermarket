package com.example.cubesschool8.supermarket.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.cubesschool8.supermarket.fragment.FragmentPrijava;
import com.example.cubesschool8.supermarket.fragment.FragmentReg;

/**
 * Created by cubesschool8 on 9/7/16.
 */
public class PagerAdapter extends FragmentPagerAdapter {
    private int numOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.numOfTabs = NumOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FragmentPrijava fp = new FragmentPrijava();
                return fp;
            case 1:
                FragmentReg fr = new FragmentReg();
                return fr;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
