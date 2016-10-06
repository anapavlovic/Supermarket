package com.example.cubesschool8.supermarket.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.adapter.NavigationAdapter;
import com.example.cubesschool8.supermarket.constant.Constant;
import com.example.cubesschool8.supermarket.customComponents.CustomTextViewFont;
import com.example.cubesschool8.supermarket.data.DataCategory;
import com.example.cubesschool8.supermarket.data.DataContainer;
import com.example.cubesschool8.supermarket.data.response.ResponseProducts;
import com.example.cubesschool8.supermarket.networking.DataLoader;
import com.example.cubesschool8.supermarket.networking.GsonRequest;
import com.example.cubesschool8.supermarket.tool.BusProvider;
import com.example.cubesschool8.supermarket.tool.MessageObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SettingsActivity extends ActivityWithMessage {
    private final String REQUEST_TAG = "Start_activity";
    public RelativeLayout termsOfuse, profile, logout, support;
    public ImageView userImage, mDrawerMenu;
    private SwitchCompat mSwitchNotification;

    private DrawerLayout mDrawerLayout;
    private ExpandableListView mDrawerlist;
    public static NavigationAdapter mAdapter;


    private DataCategory data;
    private int categoryPosition;
    private int mChildPosition;
    private String childId;
    private RelativeLayout mRelativeProgress;
    private ProgressBar progressBar;
    private GsonRequest<ResponseProducts> mSubcategoriesRequest;
    private ArrayList<DataCategory> subCategories;
    public ViewGroup mRootView;

    private HashMap<DataCategory, List<DataCategory>> childList = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        inicComp();
        addListener();
    }


    private void addListener() {


        termsOfuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TermsOfUseActivity.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DataContainer.login != null) {
                    startActivity(new Intent(getApplicationContext(), ProfilActivity.class));
                } else {
                    BusProvider.getInstance().post(new MessageObject(R.string.no_profile, 3000, MessageObject.MESSAGE_INFO));
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LogInActivity.class));

                SharedPreferences settings = getSharedPreferences("MyPreferences", 0);
                settings.edit().remove("checked").commit();
                settings.edit().remove("username").commit();
                settings.edit().remove("password").commit();
                finish();
            }
        });

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Support", Toast.LENGTH_SHORT).show();
            }
        });
        mDrawerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(mDrawerlist);
            }
        });


        mDrawerlist.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {


                setGroupClickListener(mDrawerlist, data, mAdapter, mDrawerLayout, mRelativeProgress, mSubcategoriesRequest, parent, v, groupPosition, id,progressBar);
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

    private void inicComp() {
        mSwitchNotification = (SwitchCompat) findViewById(R.id.dSwitchSettings);
        termsOfuse = (RelativeLayout) findViewById(R.id.relativeTermsSetting);
        profile = (RelativeLayout) findViewById(R.id.relativeProfileSettings);
        logout = (RelativeLayout) findViewById(R.id.relativeLOgoutSettings);
        support = (RelativeLayout) findViewById(R.id.relativeSupportSettings);
        mDrawerMenu = (ImageView) findViewById(R.id.drawerMenuProfil);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerlist = (ExpandableListView) findViewById(R.id.drawerList);

        if (DataContainer.categories != null) {
            for (int i = 0; i < DataContainer.categories.size(); i++) {
                subCategories = DataContainer.categories.get(i).subcategories;
                childList.put(DataContainer.categories.get(i), subCategories);

            }
        }
        mAdapter = new NavigationAdapter(this, DataContainer.categories, childList);
        mDrawerlist.setAdapter(mAdapter);

        mRelativeProgress = (RelativeLayout) findViewById(R.id.relativeProgressS);
        progressBar = (ProgressBar) findViewById(R.id.progressSetting);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#feea00"), PorterDuff.Mode.SRC_IN);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DataLoader.cancelRequest(getApplicationContext(), REQUEST_TAG);
    }

}
