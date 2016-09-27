package com.example.cubesschool8.supermarket.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.example.cubesschool8.supermarket.R;

public class TermsOfUseActivity extends AppCompatActivity {

    private WebView mWebTerms;
    private ImageView mBackImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_use);

        mWebTerms = (WebView) findViewById(R.id.webTerms);
        mBackImg = (ImageView) findViewById(R.id.backweb);
        mWebTerms.setBackgroundColor(Color.parseColor("#7f000000"));
        mWebTerms.loadUrl("http://shop.cubes.rs/online-shop/3.html");

        addListener();
    }

    public void addListener() {
        mBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
