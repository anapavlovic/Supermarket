package com.example.cubesschool8.supermarket.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.activity.IntroActivity;
import com.example.cubesschool8.supermarket.customComponents.CustomTextViewFont;

/**
 * Created by Ana on 9/23/2016.
 */
public class FragmentSlideViewPagerIntro extends Fragment {

    public ImageView imageBg;
    private CustomTextViewFont mtext;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.intro_layout_fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageBg = (ImageView) getView().findViewById(R.id.slideImg);
        mtext = (CustomTextViewFont) getView().findViewById(R.id.textVintro);


    }
}
