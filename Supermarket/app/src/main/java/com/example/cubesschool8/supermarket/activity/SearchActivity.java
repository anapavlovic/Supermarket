package com.example.cubesschool8.supermarket.activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.adapter.NavigationAdapter;
import com.example.cubesschool8.supermarket.adapter.RecyclerAdapter;
import com.example.cubesschool8.supermarket.constant.Constant;
import com.example.cubesschool8.supermarket.customComponents.CustomEditTextFont;
import com.example.cubesschool8.supermarket.data.DataCategory;
import com.example.cubesschool8.supermarket.data.DataContainer;
import com.example.cubesschool8.supermarket.data.DataProducts;
import com.example.cubesschool8.supermarket.data.response.ResponseProducts;
import com.example.cubesschool8.supermarket.data.response.ResponseSearch;
import com.example.cubesschool8.supermarket.data.response.ResponseWishlist;
import com.example.cubesschool8.supermarket.networking.DataLoader;
import com.example.cubesschool8.supermarket.networking.GsonRequest;
import com.example.cubesschool8.supermarket.tool.BusProvider;
import com.example.cubesschool8.supermarket.tool.MessageObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends ActivityWithMessage implements CustomEditTextFont.OnActionTimeListener {


    private final String REQUEST_TAG = "Start_activity";
    private ImageView mDrawerMenu, mSearch, mShoppingCart, iks;

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
    private ProgressBar progressBar, progressIks;
    private String childId;

    private ArrayList<DataCategory> subCategories;

    private HashMap<DataCategory, List<DataCategory>> childList = new HashMap<>();
    private CustomEditTextFont editTextSearch;

    private GsonRequest<ResponseSearch> mRequestSearch;
    private GsonRequest<ResponseProducts> mSubcategoriesRequest;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        inicComp();
        addListener();
        search();

    }


    public void inicComp() {
        editTextSearch = (CustomEditTextFont) findViewById(R.id.editTextSearch);
        iks = (ImageView) findViewById(R.id.iks);
        mDrawerMenu = (ImageView) findViewById(R.id.drawerMenu);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSearch = (ImageView) findViewById(R.id.search);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerlist = (ExpandableListView) findViewById(R.id.drawerList);
        mShoppingCart = (ImageView) findViewById(R.id.shopingCart);
        mRelativeProgress = (RelativeLayout) findViewById(R.id.relativeProgressHome);
        progressIks = (ProgressBar) findViewById(R.id.progressIks);
        progressBar = (ProgressBar) findViewById(R.id.progressHome);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#feea00"), PorterDuff.Mode.SRC_IN);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(mLayoutManager);
//        if (DataContainer.mySearchList != null) {
//            mrecyclerAdapter = new RecyclerAdapter(getApplicationContext(), DataContainer.mySearchList);
//            recyclerView.setAdapter(mrecyclerAdapter);
//        }
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
        iks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextSearch.setText("");
                DataContainer.mySearchList.clear();
                mrecyclerAdapter.notifyDataSetChanged();
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


    public void search() {

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 3) {
                    iks.setVisibility(View.GONE);
                    progressIks.setVisibility(View.VISIBLE);
                    mRequestSearch = new GsonRequest<ResponseSearch>(Constant.PRODUCTS_URL, Request.Method.POST, ResponseSearch.class, new Response.Listener<ResponseSearch>() {
                        @Override
                        public void onResponse(ResponseSearch response) {
                            if (DataContainer.mySearchList != null) {
                                DataContainer.mySearchList.clear();
                                mrecyclerAdapter.notifyDataSetChanged();
                            }
                            DataContainer.mySearchList = response.data.results;
                            mrecyclerAdapter = new RecyclerAdapter(getApplicationContext(), DataContainer.mySearchList);
                            recyclerView.setAdapter(mrecyclerAdapter);
                            mrecyclerAdapter.notifyDataSetChanged();

                            iks.setVisibility(View.VISIBLE);
                            progressIks.setVisibility(View.GONE);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("mRequestMyPurchases", "onErrorResponse:" + error.toString());
                            mRelativeProgress.setVisibility(View.VISIBLE);
                            mRelativeProgress.setClickable(true);
                            progressBar.setVisibility(View.INVISIBLE);
                            BusProvider.getInstance().post(new MessageObject(R.string.server_error, 3000, MessageObject.MESSAGE_ERROR));
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    mRelativeProgress.setVisibility(View.GONE);
                                    progressBar.setVisibility(View.VISIBLE);
                                }
                            }, 4000);
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("search", "1");
                            params.put("token", DataContainer.TOKEN);
                            params.put("mlimit", "500");
                            params.put("start", "0");
                            params.put("term", editTextSearch.getText().toString());
                            return params;
                        }
                    };

                    DataLoader.addRequest(getApplicationContext(), mRequestSearch, REQUEST_TAG);

                } else {
                    //editTextSearch.onActionTime(3000);
                }


            }
        });

    }

    @Override
    public void onAction() {

        Toast.makeText(getApplicationContext(), "niste kucali 3 sec.", Toast.LENGTH_SHORT).show();


    }
}
