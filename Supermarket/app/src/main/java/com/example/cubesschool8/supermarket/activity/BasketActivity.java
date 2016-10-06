package com.example.cubesschool8.supermarket.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.adapter.BasketAdapter;
import com.example.cubesschool8.supermarket.customComponents.CustomEditTextFont;
import com.example.cubesschool8.supermarket.customComponents.CustomTextViewFont;
import com.example.cubesschool8.supermarket.data.DataContainer;
import com.example.cubesschool8.supermarket.data.DataLogIn;
import com.example.cubesschool8.supermarket.data.DataProducts;
import com.example.cubesschool8.supermarket.data.DataSignUp;
import com.example.cubesschool8.supermarket.tool.BusProvider;
import com.example.cubesschool8.supermarket.tool.MessageObject;

import java.util.ArrayList;

public class BasketActivity extends ActivityWithMessage implements BasketAdapter.OnItemCountChanged {

    private ImageView mbackBasket;
    public BasketAdapter mAdapter;
    private RecyclerView mRecycler;
    private Button mBuyButton;
    private RecyclerView.LayoutManager mLayoutmanager;
    private CustomTextViewFont mTotalSum;
    private DataLogIn person = new DataLogIn();
    private RelativeLayout rootView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        iniComp();
        addListener();

        mLayoutmanager = new LinearLayoutManager(getApplicationContext());
        mRecycler.setLayoutManager(mLayoutmanager);

        mAdapter = new BasketAdapter(this, DataContainer.basketList);
        mAdapter.setOnItemCountChanged(this);
        mRecycler.setAdapter(mAdapter);
        displaytotalPrice();

    }


    public void iniComp() {
        mbackBasket = (ImageView) findViewById(R.id.productBack);
        mRecycler = (RecyclerView) findViewById(R.id.recyclerBasket);
        mBuyButton = (Button) findViewById(R.id.buyButton);
        mTotalSum = (CustomTextViewFont) findViewById(R.id.totalSum);
        rootView = (RelativeLayout) findViewById(R.id.rootView);



    }

    public void addListener() {
        mbackBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DataContainer.login != null) {
                    if (DataContainer.basketList.isEmpty()) {
                        BusProvider.getInstance().post(new MessageObject(R.string.praznakorpa, 3000, MessageObject.MESSAGE_INFO));
                    } else {
                        Intent i = new Intent(getApplicationContext(), AddressChangeActivity.class);
                        i.putExtra("total", mTotalSum.getText().toString());
                        startActivity(i);
                    }
                } else {
                    mBuyButton.setClickable(false);
                    BusProvider.getInstance().post(new MessageObject(R.string.no_profile_buy, 3000, MessageObject.MESSAGE_INFO));

                    rootView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBuyButton.setClickable(true);
                        }
                    },5000);

                }
            }
        });
    }

    public void displaytotalPrice() {
        if (DataContainer.basketList.isEmpty()) {
            mTotalSum.setText("0.00");
        }
        double total = 0;
        for (int i = 0; i < DataContainer.basketList.size(); i++) {
            total = total + (Double.parseDouble(DataContainer.basketList.get(i).first_price) * DataContainer.basketList.get(i).count);
            mTotalSum.setText(String.valueOf(total));
            mAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onItemcount() {
        displaytotalPrice();
    }
}
