package com.example.cubesschool8.supermarket.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.adapter.BasketAdapter;
import com.example.cubesschool8.supermarket.customComponents.CustomEditTextFont;
import com.example.cubesschool8.supermarket.customComponents.CustomTextViewFont;
import com.example.cubesschool8.supermarket.data.DataContainer;
import com.example.cubesschool8.supermarket.data.DataLogIn;
import com.example.cubesschool8.supermarket.data.DataSignUp;

public class BasketActivity extends AppCompatActivity {

    private ImageView mbackBasket;
    private BasketAdapter mAdapter;
    private RecyclerView mRecycler;
    private Button mBuyButton;
    private RecyclerView.LayoutManager mLayoutmanager;
    private CustomEditTextFont mAddressEt;
    private CustomTextViewFont mTotalSum;
    private DataLogIn person = new DataLogIn();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        iniComp();
        addListener();

        mLayoutmanager = new LinearLayoutManager(getApplicationContext());
        mRecycler.setLayoutManager(mLayoutmanager);

        mAdapter = new BasketAdapter(this, DataContainer.basketList);
        mRecycler.setAdapter(mAdapter);



        mAddressEt.setText(DataContainer.login.address);

        float total = 0;
        for (int i = 0; i < DataContainer.basketList.size(); i++) {
            total = total + Float.parseFloat(DataContainer.basketList.get(i).first_price);
            mTotalSum.setText(String.valueOf(total));

        }


    }


    public void iniComp() {
        mbackBasket = (ImageView) findViewById(R.id.productBack);
        mRecycler = (RecyclerView) findViewById(R.id.recyclerBasket);
        mBuyButton = (Button) findViewById(R.id.buyButton);
        mAddressEt = (CustomEditTextFont) findViewById(R.id.etAddressBasket);
        mTotalSum = (CustomTextViewFont) findViewById(R.id.totalSum);


    }

    public void addListener() {
        mbackBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
