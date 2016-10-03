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

public class SettingsActivity extends AppCompatActivity {
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
                startActivity(new Intent(getApplicationContext(), ProfilActivity.class));
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


                categoryPosition = groupPosition;
                if (groupPosition > 1 && groupPosition < DataContainer.categories.size() + 2) {
                    data = (DataCategory) mAdapter.getGroup(groupPosition - 2);
                    if (mAdapter.getChildrenCount(groupPosition) == 0) {
                        if (HomeActivity.checkList(DataContainer.categoriesLists, data.id) == false) {
                            mDrawerLayout.closeDrawers();
                            mRelativeProgress.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mRelativeProgress.setVisibility(View.VISIBLE);
                                    mSubcategoriesRequest = new GsonRequest<ResponseProducts>(Constant.SUBCATEGORIES_URL + data.id, Request.Method.GET, ResponseProducts.class, new Response.Listener<ResponseProducts>() {
                                        @Override
                                        public void onResponse(ResponseProducts response) {
                                            DataContainer.categoriesLists.put(data.id, response.data.results);
                                            Intent i = new Intent(getApplicationContext(), CategoryItemsActivity.class);
                                            i.putExtra("id", data.id);
                                            startActivity(i);
                                            mRelativeProgress.setVisibility(View.GONE);

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            BusProvider.getInstance().post(new MessageObject(R.string.server_error, 3000, MessageObject.MESSAGE_ERROR));
                                        }
                                    });

                                    DataLoader.addRequest(getApplicationContext(), mSubcategoriesRequest, REQUEST_TAG);
                                }
                            }, 200);


                        } else {
                            Intent i = new Intent(getApplicationContext(), CategoryItemsActivity.class);
                            i.putExtra("id", data.id);
                            startActivity(i);
                            mRelativeProgress.setVisibility(View.GONE);
                        }
                    } else {

                    }
                } else if (groupPosition == DataContainer.categories.size() + 3) {
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                } else if (groupPosition == DataContainer.categories.size() + 4) {

                    startActivity(new Intent(getApplicationContext(), ProfilActivity.class));



                } else if (groupPosition == DataContainer.categories.size() + 5) {

                    startActivity(new Intent(getApplicationContext(), LogInActivity.class));

                    SharedPreferences settings = getSharedPreferences("MyPreferences", 0);
                    settings.edit().remove("checked").commit();
                    settings.edit().remove("username").commit();
                    settings.edit().remove("password").commit();
                    finish();

                }
                return false;
            }
        });

        mDrawerlist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                data = (DataCategory) mAdapter.getGroup(groupPosition - 2);
                categoryPosition = groupPosition;
                mChildPosition = childPosition;
                childId = String.valueOf(id);
                if (HomeActivity.checkList(DataContainer.categoriesLists, data.id + "." + childId) == false) {
                    mDrawerLayout.closeDrawers();
                    mRelativeProgress.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mRelativeProgress.setVisibility(View.VISIBLE);
                            mSubcategoriesRequest = new GsonRequest<ResponseProducts>(Constant.SUBCATEGORIES_URL + data.id, Request.Method.GET, ResponseProducts.class, new Response.Listener<ResponseProducts>() {
                                @Override
                                public void onResponse(ResponseProducts response) {
                                    mRelativeProgress.setVisibility(View.VISIBLE);
                                    DataContainer.categoriesLists.put(data.id + "." + childId, response.data.results);
                                    Intent i = new Intent(getApplicationContext(), CategoryItemsActivity.class);
                                    i.putExtra("id", data.id + "." + childId);
                                    startActivity(i);
                                    mRelativeProgress.setVisibility(View.GONE);


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    BusProvider.getInstance().post(new MessageObject(R.string.server_error, 3000, MessageObject.MESSAGE_ERROR));
                                }
                            });

                            DataLoader.addRequest(getApplicationContext(), mSubcategoriesRequest, REQUEST_TAG);
                        }
                    }, 200);

                } else {
                    Intent i = new Intent(getApplicationContext(), CategoryItemsActivity.class);
                    i.putExtra("id", data.id + "." + childId);
                    startActivity(i);
                    mRelativeProgress.setVisibility(View.GONE);

                }


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
}
