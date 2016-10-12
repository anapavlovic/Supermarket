package com.example.cubesschool8.supermarket.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.adapter.NavigationAdapter;
import com.example.cubesschool8.supermarket.adapter.RecyclerAdapter;
import com.example.cubesschool8.supermarket.constant.Constant;
import com.example.cubesschool8.supermarket.customComponents.CustomTextViewFont;
import com.example.cubesschool8.supermarket.data.DataCategory;
import com.example.cubesschool8.supermarket.data.DataContainer;
import com.example.cubesschool8.supermarket.data.response.ResponseProducts;
import com.example.cubesschool8.supermarket.data.response.ResponseWishlist;
import com.example.cubesschool8.supermarket.networking.DataLoader;
import com.example.cubesschool8.supermarket.networking.GsonRequest;
import com.example.cubesschool8.supermarket.tool.BusProvider;
import com.example.cubesschool8.supermarket.tool.MessageObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyPurchasesActivity extends ActivityWithMessage {
    private ImageView mDrawerMenu, mSearch, mShoppingCart;
    private final String REQUEST_TAG = "Start_activity";
    private CustomTextViewFont tvEmptyCategory, tvEmptyCategory2;
    private RecyclerAdapter mrecyclerAdapter;
    public RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager, drawerLayoutManager;

    private android.support.v4.widget.DrawerLayout mDrawerLayout;
    private ExpandableListView mDrawerlist;
    public static NavigationAdapter mAdapter;
    private DataCategory data;
    private int categoryPosition;
    private int mChildPosition;
    private RelativeLayout mRelativeProgress;
    private ProgressBar progressBar;
    private String childId;
    private Animation animation;
    private GsonRequest<ResponseProducts> mSubcategoriesRequest;
    private ArrayList<DataCategory> subCategories;
    private HashMap<DataCategory, List<DataCategory>> childList = new HashMap<>();
    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_purchases);
        inicComp();
        addListener();


    }


    public void inicComp() {
        mDrawerMenu = (ImageView) findViewById(R.id.drawerMenu);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSearch = (ImageView) findViewById(R.id.search);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerlist = (ExpandableListView) findViewById(R.id.drawerList);
        mShoppingCart = (ImageView) findViewById(R.id.shopingCart);
        tvEmptyCategory = (CustomTextViewFont) findViewById(R.id.tvEmptyCategory);
        tvEmptyCategory2 = (CustomTextViewFont) findViewById(R.id.tvEmpty);
        mRelativeProgress = (RelativeLayout) findViewById(R.id.relativeProgressHome);
        progressBar = (ProgressBar) findViewById(R.id.progressHome);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#feea00"), PorterDuff.Mode.SRC_IN);

        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        if (DataContainer.myPurchasesList !=null) {
          //  mrecyclerAdapter = new RecyclerAdapter(this, DataContainer.myPurchasesList);
          //  recyclerView.setAdapter(mrecyclerAdapter);
        } else {

            tvEmptyCategory.setVisibility(View.VISIBLE);
            tvEmptyCategory.setText(R.string.empty_category);
            animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_empty_category);
            animation.setFillAfter(true);
            tvEmptyCategory.setAnimation(animation);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    tvEmptyCategory2.setVisibility(View.VISIBLE);
                    tvEmptyCategory2.setText(R.string.empty);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_empty_category);
                    tvEmptyCategory2.setAnimation(animation);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }

        if (DataContainer.categories != null) {
            for (int i = 0; i < DataContainer.categories.size(); i++) {
                subCategories = DataContainer.categories.get(i).subcategories;
                childList.put(DataContainer.categories.get(i), subCategories);

            }
        }

        mAdapter = new NavigationAdapter(this, DataContainer.categories, childList);
        mDrawerlist.setAdapter(mAdapter);

    }

    public void addListener() {
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
}
