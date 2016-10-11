package com.example.cubesschool8.supermarket.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import android.widget.ExpandableListView;
import android.widget.ImageView;

import android.widget.ProgressBar;
import android.widget.RelativeLayout;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.adapter.NavigationAdapter;

import com.example.cubesschool8.supermarket.adapter.RecyclerAdapter;
import com.example.cubesschool8.supermarket.constant.Constant;
import com.example.cubesschool8.supermarket.data.DataCategory;
import com.example.cubesschool8.supermarket.data.DataContainer;
import com.example.cubesschool8.supermarket.data.DataProducts;
import com.example.cubesschool8.supermarket.data.response.ResponseProducts;
import com.example.cubesschool8.supermarket.data.response.ResponseWishlist;
import com.example.cubesschool8.supermarket.networking.DataLoader;
import com.example.cubesschool8.supermarket.networking.GsonRequest;
import com.example.cubesschool8.supermarket.tool.BusProvider;
import com.example.cubesschool8.supermarket.tool.MessageObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class HomeActivity extends ActivityWithMessage {

    public static final int WISHLIST = 0, MY_PURCHASE_LIST = 1;

    private final String REQUEST_TAG = "Start_activity";
    private ImageView mDrawerMenu, mSearch, mShoppingCart;

    private RecyclerAdapter mrecyclerAdapter;
    public RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager, drawerLayoutManager;

    private DrawerLayout mDrawerLayout;
    private ExpandableListView mDrawerlist;
    public static NavigationAdapter mAdapter;
    private DataCategory data;
    private int categoryPosition;
    private int mChildPosition;
    private RelativeLayout mRelativeProgress;
    private ProgressBar progressBar;
    private String childId;

    private GsonRequest<ResponseProducts> mSubcategoriesRequest;
    private GsonRequest<ResponseWishlist> mRequestWishList;
    private ArrayList<DataCategory> subCategories;
    private GsonRequest<ResponseProducts> mRequestProducts;

    private HashMap<DataCategory, List<DataCategory>> childList = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        inicComp();
        addListener();

        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            if (extras.getInt("wishlist") == WISHLIST) {
//                mrecyclerAdapter = new RecyclerAdapter(this, DataContainer.wishList);
//                recyclerView.setAdapter(mrecyclerAdapter);
//            } else if (extras.getInt("wishlist") == MY_PURCHASE_LIST) {
//                mrecyclerAdapter = new RecyclerAdapter(this, DataContainer.myPurchasesList);
//                recyclerView.setAdapter(mrecyclerAdapter);
//            } else {
//                mrecyclerAdapter = new RecyclerAdapter(this, DataContainer.products);
//                recyclerView.setAdapter(mrecyclerAdapter);
//            }
//        } else {
        mrecyclerAdapter = new RecyclerAdapter(this, DataContainer.products);
        recyclerView.setAdapter(mrecyclerAdapter);
        //  }


    }


    public void addListener() {

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
            }
        });
        mDrawerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(mDrawerlist);
            }
        });

        mShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BasketActivity.class));
            }
        });

        mDrawerlist.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {


                setGroupClickListener(mDrawerlist, data, mAdapter, mDrawerLayout, mRelativeProgress, mSubcategoriesRequest, parent, v, groupPosition, id, progressBar);
                return false;
            }
        });

        mDrawerlist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                setOnChildClicklistener(mDrawerlist, data, mAdapter, mDrawerLayout, mRelativeProgress, mSubcategoriesRequest, parent, v, groupPosition, mChildPosition, id);

                return false;
            }
        });


    }


    public void inicComp() {
        mDrawerMenu = (ImageView) findViewById(R.id.drawerMenu);
        mSearch = (ImageView) findViewById(R.id.search);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSearch = (ImageView) findViewById(R.id.search);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerlist = (ExpandableListView) findViewById(R.id.drawerList);
        mShoppingCart = (ImageView) findViewById(R.id.shopingCart);
        mRelativeProgress = (RelativeLayout) findViewById(R.id.relativeProgressHome);
        progressBar = (ProgressBar) findViewById(R.id.progressHome);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#feea00"), PorterDuff.Mode.SRC_IN);

        if (DataContainer.categories != null) {
            for (int i = 0; i < DataContainer.categories.size(); i++) {
                subCategories = DataContainer.categories.get(i).subcategories;
                childList.put(DataContainer.categories.get(i), subCategories);

            }
        }
        if (DataContainer.categories != null) {
            mAdapter = new NavigationAdapter(this, DataContainer.categories, childList);
            mDrawerlist.setAdapter(mAdapter);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public static boolean checkList(HashMap<String, ArrayList<DataProducts>> list, String id) {
        boolean isIntheList = false;
        Iterator myVeryOwnIterator = list.keySet().iterator();
        while (myVeryOwnIterator.hasNext()) {
            String key = (String) myVeryOwnIterator.next();
            if (key.equalsIgnoreCase(id)) {
                isIntheList = true;
            } else
                isIntheList = false;

        }
        return isIntheList;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        DataLoader.cancelRequest(getApplicationContext(), REQUEST_TAG);
    }


}
