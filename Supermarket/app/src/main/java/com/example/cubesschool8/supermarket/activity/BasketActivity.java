package com.example.cubesschool8.supermarket.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.cubesschool8.supermarket.R;

public class BasketActivity extends AppCompatActivity {

    private ImageView mbackBasket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
    }



}
