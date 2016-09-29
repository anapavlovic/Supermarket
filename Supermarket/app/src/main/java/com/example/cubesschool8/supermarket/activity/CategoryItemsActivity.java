package com.example.cubesschool8.supermarket.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.example.cubesschool8.supermarket.data.DataProducts;
import com.example.cubesschool8.supermarket.data.response.ResponseProducts;
import com.example.cubesschool8.supermarket.networking.DataLoader;
import com.example.cubesschool8.supermarket.networking.GsonRequest;
import com.example.cubesschool8.supermarket.tool.BusProvider;
import com.example.cubesschool8.supermarket.tool.MessageObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CategoryItemsActivity extends ActivityWithMessage {
    private final String REQUEST_TAG = "Start_activity";
    private RecyclerAdapter mAdapter;
    private NavigationAdapter mAdapterNavigation;
    private RecyclerView recyclerView;
    private CustomTextViewFont itemsCount;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<DataProducts> mList;
    private ImageView mMenu, mSearch, mShoppingCart;
    private DrawerLayout mDrawerLayout;
    private ExpandableListView mDrawerlist;
    private ArrayList<DataCategory> subCategories;
    private HashMap<DataCategory, List<DataCategory>> childList = new HashMap<>();
    private DataCategory data;
    private int categoryPosition;
    private int mChildPosition;
    private RelativeLayout mRelativeProgress;
    private ProgressBar progressBar;
    private String childId;
    private GsonRequest<ResponseProducts> mSubcategoriesRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_items);
        inicComp();
        addListener();


    }


    private void inicComp() {
        mMenu = (ImageView) findViewById(R.id.CategoryMenu);
        mSearch = (ImageView) findViewById(R.id.searchCategory);
        mShoppingCart = (ImageView) findViewById(R.id.shopingCartCategory);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerSubcategories);
        itemsCount = (CustomTextViewFont) findViewById(R.id.tvItemsCount);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerlist = (ExpandableListView) findViewById(R.id.drawerList);

        mRelativeProgress = (RelativeLayout) findViewById(R.id.relativeProgressHome);
        progressBar = (ProgressBar) findViewById(R.id.progressHome);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#feea00"), PorterDuff.Mode.SRC_IN);
        for (int i = 0; i < DataContainer.categories.size(); i++) {
            subCategories = DataContainer.categories.get(i).subcategories;
            childList.put(DataContainer.categories.get(i), subCategories);

        }
        mAdapterNavigation = new NavigationAdapter(this, DataContainer.categories, childList);
        mDrawerlist.setAdapter(mAdapterNavigation);


        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String id = extras.getString("id");
            mList = getList(DataContainer.categoriesLists, id);


            mAdapter = new RecyclerAdapter(this, mList);
            recyclerView.setAdapter(mAdapter);

        } else {
        }


        if (mList != null) {
            itemsCount.setText("Ukupno proizvoda: " + String.valueOf(mList.size()));
        } else {
            itemsCount.setText("Ukupno proizvoda: 0");
        }

    }


    public void addListener() {

        mMenu.setOnClickListener(new View.OnClickListener() {
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

                data = (DataCategory) mAdapterNavigation.getGroup(groupPosition - 2);
                categoryPosition = groupPosition;
                childId = String.valueOf(id);
                if (mAdapterNavigation.getChildrenCount(groupPosition) == 0) {
                    mDrawerLayout.closeDrawers();
                    if (checkList(DataContainer.categoriesLists, data.id) == false) {
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
                    } else {
                        Intent i = new Intent(getApplicationContext(), CategoryItemsActivity.class);
                        i.putExtra("id", data.id);
                        startActivity(i);
                        mRelativeProgress.setVisibility(View.GONE);
                    }
                }else {}
                return false;
            }
        });
        mDrawerlist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                data = (DataCategory) mAdapterNavigation.getGroup(groupPosition - 2);
                categoryPosition = groupPosition;
                mChildPosition = childPosition;
                childId = String.valueOf(id);
                mDrawerLayout.closeDrawers();
                if (checkList(DataContainer.categoriesLists, data.id + "." + childId) == false) {
                    mRelativeProgress.setVisibility(View.VISIBLE);
                    mSubcategoriesRequest = new GsonRequest<ResponseProducts>(Constant.SUBCATEGORIES_URL + data.id, Request.Method.GET, ResponseProducts.class, new Response.Listener<ResponseProducts>() {
                        @Override
                        public void onResponse(ResponseProducts response) {
                            mRelativeProgress.setVisibility(View.VISIBLE);
                            DataContainer.categoriesLists.put(data.id + "." + childId, response.data.results);
                            Intent i = new Intent(getApplicationContext(), CategoryItemsActivity.class);
                            i.putExtra("id", data.id+ "." + childId);
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
                } else {
                    Intent i = new Intent(getApplicationContext(), CategoryItemsActivity.class);
                    i.putExtra("id", data.id+ "." + childId);
                    startActivity(i);
                    mRelativeProgress.setVisibility(View.GONE);
                }


                return false;
            }
        });
    }

    public ArrayList<DataProducts> getList(HashMap<String, ArrayList<DataProducts>> list, String id) {
        ArrayList mlist = new ArrayList<DataProducts>();
        Iterator myVeryOwnIterator = list.keySet().iterator();
        while (myVeryOwnIterator.hasNext()) {
            String key = (String) myVeryOwnIterator.next();
            if (key.equalsIgnoreCase(id)) {
                mlist = list.get(key);
                break;
            }

        }
        return mlist;
    }

    public boolean checkList(HashMap<String, ArrayList<DataProducts>> list, String id) {
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

}
