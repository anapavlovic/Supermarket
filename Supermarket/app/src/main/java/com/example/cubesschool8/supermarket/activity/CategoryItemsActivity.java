package com.example.cubesschool8.supermarket.activity;

import android.content.Context;
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
    private CustomTextViewFont itemsCount, tvEmptyCategory, tvEmptyCategory2;
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
    private Animation animation;
    private HomeActivity mHomeactivity;
    private Context context;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_items);
        inicComp();
        addListener();
        context = this.getApplicationContext();

    }


    private void inicComp() {
        mMenu = (ImageView) findViewById(R.id.CategoryMenu);
        mSearch = (ImageView) findViewById(R.id.searchCategory);
        mShoppingCart = (ImageView) findViewById(R.id.shopingCartCategory);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerSubcategories);
        itemsCount = (CustomTextViewFont) findViewById(R.id.tvItemsCount);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerlist = (ExpandableListView) findViewById(R.id.drawerList);
        tvEmptyCategory = (CustomTextViewFont) findViewById(R.id.tvEmptyCategory);
        tvEmptyCategory2 = (CustomTextViewFont) findViewById(R.id.tvEmpty);
        mHomeactivity = new HomeActivity();
        mRelativeProgress = (RelativeLayout) findViewById(R.id.relativeProgressHome);
        progressBar = (ProgressBar) findViewById(R.id.progressCategory);
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


        if (!mList.isEmpty()) {
            itemsCount.setText("Ukupno proizvoda: " + String.valueOf(mList.size()));
        } else {
            itemsCount.setVisibility(View.GONE);
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

    }


    public void addListener() {

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
            }
        });

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

                setGroupClickListener(mDrawerlist, data, mAdapterNavigation, mDrawerLayout, mRelativeProgress, mSubcategoriesRequest, parent, v, groupPosition, id, progressBar);

                return false;

            }
        });

        mDrawerlist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                setOnChildClicklistener(mDrawerlist, data, mAdapterNavigation, mDrawerLayout, mRelativeProgress, mSubcategoriesRequest, parent, v, groupPosition, mChildPosition, id);
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
