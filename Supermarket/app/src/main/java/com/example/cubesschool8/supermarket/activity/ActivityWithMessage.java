package com.example.cubesschool8.supermarket.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.squareup.otto.Subscribe;

import java.util.HashMap;

/**
 * Created by cubesschool8 on 9/14/16.
 */
public class ActivityWithMessage extends AppCompatActivity {


    public CustomTextViewFont mErrorPopUp;
    private View mMessageView;
    private CustomTextViewFont mTextView;
    private LayoutInflater inflater;
    private Object busEventListener;
    private Animation errorAnim, errorAnimBack;
    public static final int MESSAGE_ERROR = 0, MESSAGE_SUCCESS = 1, MESSAGE_INFO = 2;
    public ViewGroup mRootView;

    private final String REQUEST_TAG = "Start_activity";
    private DrawerLayout DrawerLayout;
    private ExpandableListView Drawerlist;
    public static NavigationAdapter Adapter;
    private DataCategory mdata;
    private int mcategoryPosition;
    private int mChildPosition;
    private RelativeLayout RelativeProgress;
    private ProgressBar progressBar;
    private String childId;

    private Handler handler = new Handler();

    private GsonRequest<ResponseProducts> SubcategoriesRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        inflater = LayoutInflater.from(getApplicationContext());

        busEventListener = new Object() {
            //sta ce da se desi kada stigne obavestenje
            @Subscribe
            public void onMesssageShow(MessageObject messageObject) {

                if (mMessageView == null) {
                    mMessageView = inflater.inflate(R.layout.layout_error, mRootView, false);
                    mTextView = (CustomTextViewFont) mMessageView.findViewById(R.id.errorPopup);
                }

                mTextView.setText(messageObject.stringResource);  /// chekirati tip poruke i setovati color texta


                switch (messageObject.type) {
                    case MESSAGE_ERROR:
                        mTextView.setTextColor(getResources().getColor(R.color.message_error));
                        break;
                    case MESSAGE_INFO:
                        mTextView.setTextColor(getResources().getColor(R.color.colorAccent));
                        break;
                    case MESSAGE_SUCCESS:
                        mTextView.setTextColor(getResources().getColor(R.color.message_success));
                        break;
                    default:
                        break;

                }


                mRootView.addView(mMessageView);
                errorAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.down_from_top);
                mMessageView.setAnimation(errorAnim);
                errorAnim.setFillAfter(true);

                mRootView.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        errorAnimBack = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.error_up);
                        mMessageView.setAnimation(errorAnimBack);
                        mMessageView.setVisibility(View.GONE);
                        mRootView.removeView(mMessageView);
                    }
                }, messageObject.time);


            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        mRootView = (ViewGroup) findViewById(R.id.rootView);
        BusProvider.getInstance().register(busEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(busEventListener);
    }


    public void setGroupClickListener(ExpandableListView mDrawerlist, DataCategory data, NavigationAdapter mAdapter, DrawerLayout mDrawerLayout, final RelativeLayout mRelativeProgress,
                                      GsonRequest<ResponseProducts> mSubcategoriesRequest, ExpandableListView parent, View v, int groupPosition, long id, ProgressBar mprogressbar) {
        DrawerLayout = mDrawerLayout;
        mdata = data;
        Adapter = mAdapter;
        DrawerLayout = mDrawerLayout;
        RelativeProgress = mRelativeProgress;
        SubcategoriesRequest = mSubcategoriesRequest;
        Drawerlist = mDrawerlist;
        mcategoryPosition = groupPosition;
        progressBar = mprogressbar;

        if (groupPosition == 1) {
            DrawerLayout.closeDrawers();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));


                }
            }, 400);

        } else if (groupPosition > 1 && groupPosition < DataContainer.categories.size() + 2) {
            mdata = (DataCategory) Adapter.getGroup(groupPosition - 2);
            if (Adapter.getChildrenCount(groupPosition) == 0) {
                if (HomeActivity.checkList(DataContainer.categoriesLists, mdata.id) == false) {
                    DrawerLayout.closeDrawers();
                    RelativeProgress.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            RelativeProgress.setVisibility(View.VISIBLE);
                            SubcategoriesRequest = new GsonRequest<ResponseProducts>(Constant.SUBCATEGORIES_URL + mdata.id, Request.Method.GET, ResponseProducts.class, new Response.Listener<ResponseProducts>() {
                                @Override
                                public void onResponse(ResponseProducts response) {
                                    DataContainer.categoriesLists.put(mdata.id, response.data.results);
                                    Intent i = new Intent(getApplicationContext(), CategoryItemsActivity.class);
                                    i.putExtra("id", mdata.id);
                                    startActivity(i);
                                    RelativeProgress.setVisibility(View.GONE);

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    BusProvider.getInstance().post(new MessageObject(R.string.server_error, 3000, MessageObject.MESSAGE_ERROR));
                                }
                            });

                            DataLoader.addRequest(getApplicationContext(), SubcategoriesRequest, REQUEST_TAG);
                        }
                    }, 300);


                } else {
                    DrawerLayout.closeDrawers();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            RelativeProgress.setVisibility(View.GONE);
                            Intent i = new Intent(getApplicationContext(), CategoryItemsActivity.class);
                            i.putExtra("id", mdata.id);
                            startActivity(i);
                        }
                    },300);


                }
            } else {

            }
        } else if (groupPosition == DataContainer.categories.size() + 3) {
            DrawerLayout.closeDrawers();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));


                }
            }, 400);
        } else if (groupPosition == DataContainer.categories.size() + 4) {
            DrawerLayout.closeDrawers();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (DataContainer.login != null) {
                        startActivity(new Intent(getApplicationContext(), ProfilActivity.class));

                    } else {

                        mRelativeProgress.setVisibility(View.VISIBLE);
                        mRelativeProgress.setClickable(true);
                        progressBar.setVisibility(View.INVISIBLE);
                        BusProvider.getInstance().post(new MessageObject(R.string.no_profile, 3000, MessageObject.MESSAGE_INFO));
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRelativeProgress.setVisibility(View.GONE);
                                progressBar.setVisibility(View.VISIBLE);
                            }
                        }, 5000);

                    }

                }
            }, 400);

        } else if (groupPosition == DataContainer.categories.size() + 5) {
            DrawerLayout.closeDrawers();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (DataContainer.login != null) {
                        startActivity(new Intent(getApplicationContext(), StatisticsActivity.class));

                    } else {
                        mRelativeProgress.setVisibility(View.VISIBLE);
                        mRelativeProgress.setClickable(true);
                        progressBar.setVisibility(View.INVISIBLE);
                        BusProvider.getInstance().post(new MessageObject(R.string.no_profile, 3000, MessageObject.MESSAGE_INFO));
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRelativeProgress.setVisibility(View.GONE);
                                progressBar.setVisibility(View.VISIBLE);
                            }
                        }, 5000);
                    }

                }
            }, 400);
        } else if (groupPosition == DataContainer.categories.size() + 6) {
            startActivity(new Intent(getApplicationContext(), LogInActivity.class));

            SharedPreferences settings = getSharedPreferences("MyPreferences", 0);
            settings.edit().remove("checked").commit();
            settings.edit().remove("username").commit();
            settings.edit().remove("password").commit();
            finish();

        }


    }


    public void setOnChildClicklistener(ExpandableListView mDrawerlist, DataCategory data, NavigationAdapter mAdapter, DrawerLayout mDrawerLayout, RelativeLayout mRelativeProgress,
                                        GsonRequest<ResponseProducts> mSubcategoriesRequest, ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        DrawerLayout = mDrawerLayout;
        mdata = data;
        Adapter = mAdapter;
        DrawerLayout = mDrawerLayout;
        RelativeProgress = mRelativeProgress;
        SubcategoriesRequest = mSubcategoriesRequest;
        Drawerlist = mDrawerlist;
        mdata = (DataCategory) Adapter.getGroup(groupPosition - 2);
        mcategoryPosition = groupPosition;
        mChildPosition = childPosition;
        childId = String.valueOf(id);

        if (HomeActivity.checkList(DataContainer.categoriesLists, mdata.id + "." + childId) == false) {
            DrawerLayout.closeDrawers();
            RelativeProgress.postDelayed(new Runnable() {
                @Override
                public void run() {
                    RelativeProgress.setVisibility(View.VISIBLE);
                    SubcategoriesRequest = new GsonRequest<ResponseProducts>(Constant.SUBCATEGORIES_URL + mdata.id, Request.Method.GET, ResponseProducts.class, new Response.Listener<ResponseProducts>() {
                        @Override
                        public void onResponse(ResponseProducts response) {
                            RelativeProgress.setVisibility(View.VISIBLE);
                            DataContainer.categoriesLists.put(mdata.id + "." + childId, response.data.results);
                            Intent i = new Intent(getApplicationContext(), CategoryItemsActivity.class);
                            i.putExtra("id", mdata.id + "." + childId);
                            startActivity(i);
                            RelativeProgress.setVisibility(View.GONE);


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            BusProvider.getInstance().post(new MessageObject(R.string.server_error, 3000, MessageObject.MESSAGE_ERROR));
                        }
                    });

                    DataLoader.addRequest(getApplicationContext(), SubcategoriesRequest, REQUEST_TAG);
                }
            }, 300);

        } else {
            DrawerLayout.closeDrawers();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(getApplicationContext(), CategoryItemsActivity.class);
                    i.putExtra("id", mdata.id + "." + childId);
                    startActivity(i);
                    RelativeProgress.setVisibility(View.GONE);
                }
            },300);


        }


    }


}
