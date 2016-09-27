package com.example.cubesschool8.supermarket.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.adapter.NavigationDrawerAdapter;
import com.example.cubesschool8.supermarket.adapter.RecyclerAdapter;
import com.example.cubesschool8.supermarket.data.DataContainer;
import com.example.cubesschool8.supermarket.tool.BusProvider;
import com.example.cubesschool8.supermarket.tool.MessageObject;

public class HomeActivity extends ActivityWithMessage {

    private ImageView mDrawerMenu, mSearch, mShoppingCart;

    private RecyclerAdapter mrecyclerAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager, drawerLayoutManager;

    private DrawerLayout mDrawerLayout;
    private RecyclerView mDrawerRecyclerview;
    private NavigationDrawerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        inicComp();
        addListener();

        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        mrecyclerAdapter = new RecyclerAdapter(this, DataContainer.products);
        recyclerView.setAdapter(mrecyclerAdapter);

    }


    public void addListener() {
        mDrawerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(mDrawerRecyclerview);
            }
        });

        mShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BasketActivity.class));
            }
        });


    }

    public void inicComp() {
        mDrawerMenu = (ImageView) findViewById(R.id.drawerMenu);
        mSearch = (ImageView) findViewById(R.id.search);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSearch = (ImageView) findViewById(R.id.search);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerRecyclerview = (RecyclerView) findViewById(R.id.drawerList);
        mShoppingCart = (ImageView) findViewById(R.id.shopingCart);

        drawerLayoutManager = new LinearLayoutManager(getApplicationContext());
        mDrawerRecyclerview.setLayoutManager(drawerLayoutManager);
        mAdapter = new NavigationDrawerAdapter(getApplicationContext(), DataContainer.categories);
        mDrawerRecyclerview.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
