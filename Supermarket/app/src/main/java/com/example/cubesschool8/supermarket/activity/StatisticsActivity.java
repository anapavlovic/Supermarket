package com.example.cubesschool8.supermarket.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.constant.Constant;
import com.example.cubesschool8.supermarket.customComponents.CustomTextViewFont;
import com.example.cubesschool8.supermarket.data.DataContainer;
import com.example.cubesschool8.supermarket.data.DataProducts;
import com.example.cubesschool8.supermarket.data.response.ResponseMyPurchases;
import com.example.cubesschool8.supermarket.data.response.ResponseWishlist;
import com.example.cubesschool8.supermarket.networking.DataLoader;
import com.example.cubesschool8.supermarket.networking.GsonRequest;
import com.example.cubesschool8.supermarket.tool.BusProvider;
import com.example.cubesschool8.supermarket.tool.MessageObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class StatisticsActivity extends ActivityWithMessage {
    private final String REQUEST_TAG = "Start_activity";
    private ImageView mBack;
    private CustomTextViewFont totalAmount, purchaseCount, wishListCount, wishlist, purchase;
    private RelativeLayout mRelativeProgress;
    private ProgressBar progressBar;
    private Handler handler = new Handler();

    private GsonRequest<ResponseWishlist> mRequestWishList;
    private GsonRequest<ResponseMyPurchases> mRequestMyPurchases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        inicComp();
        addListener();

        new LoadRequest().execute();

    }


    class LoadRequest extends AsyncTask<GsonRequest, Integer, ArrayList<DataProducts>> {

        @Override
        protected ArrayList<DataProducts> doInBackground(GsonRequest... params) {
            getWishlist();


            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<DataProducts> dataProductses) {
            super.onPostExecute(dataProductses);
            setWishListCount();
            setMyPurchasescount();
            setTotalPurchasesAmount();

        }
    }

    public void setWishListCount() {
        if (DataContainer.login.wish_list != null) {
            wishListCount.setText(String.valueOf(DataContainer.login.wish_list.size()));
        } else {
            wishListCount.setText(String.valueOf(0));
        }
    }


    public void setMyPurchasescount() {
        if (DataContainer.myPurchasesList != null) {
            purchaseCount.setText(String.valueOf(DataContainer.myPurchasesList.size()));

        } else {
            purchaseCount.setText(String.valueOf(0));
        }

    }

    public void setTotalPurchasesAmount() {
        totalAmount.setText(String.valueOf(countTotalPurchasesAmount()));
    }

    private void addListener() {

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), WishListActivity.class);
                //i.putExtra("wishlist", HomeActivity.WISHLIST);
                startActivity(i);
                finish();
            }
        });


        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MyPurchasesActivity.class);
               // i.putExtra("wishlist", HomeActivity.MY_PURCHASE_LIST);
                startActivity(i);
                finish();
            }
        });
    }

    private void inicComp() {
        mBack = (ImageView) findViewById(R.id.statisticsback);
        totalAmount = (CustomTextViewFont) findViewById(R.id.tvUkupanIznos);
        purchaseCount = (CustomTextViewFont) findViewById(R.id.tvKupovina);
        purchase = (CustomTextViewFont) findViewById(R.id.purchase);
        wishListCount = (CustomTextViewFont) findViewById(R.id.tvWishList);
        wishlist = (CustomTextViewFont) findViewById(R.id.tvwl);
        mRelativeProgress = (RelativeLayout) findViewById(R.id.relativeProgressHome);
        progressBar = (ProgressBar) findViewById(R.id.progressHome);
    }

    public void getWishlist() {


        mRequestWishList = new GsonRequest<ResponseWishlist>(Constant.URL_FAVOURITES_LIST, Request.Method.POST, ResponseWishlist.class, new Response.Listener<ResponseWishlist>() {
            @Override
            public void onResponse(ResponseWishlist response) {

                DataContainer.wishList = response.data.results;
                getPurchases();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error", error.toString());
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

                params.put("token", DataContainer.TOKEN);
                params.put("login_token", DataContainer.LOGIN_TOKEN);
                params.put("user_id", DataContainer.login.id);
                return params;
            }
        };
        DataLoader.addRequest(getApplicationContext(), mRequestWishList, REQUEST_TAG);
    }


    public void getPurchases() {
        mRequestMyPurchases = new GsonRequest<ResponseMyPurchases>(Constant.MY_PURCHASES, Request.Method.POST, ResponseMyPurchases.class, new Response.Listener<ResponseMyPurchases>() {
            @Override
            public void onResponse(ResponseMyPurchases response) {

                DataContainer.myPurchasesList = response.data.results;


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

                params.put("token", DataContainer.TOKEN);
                params.put("login_token", DataContainer.LOGIN_TOKEN);
                params.put("id", DataContainer.login.id);
                return params;
            }
        };

        DataLoader.addRequest(getApplicationContext(), mRequestMyPurchases, REQUEST_TAG);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DataLoader.cancelRequest(getApplicationContext(), REQUEST_TAG);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public float countTotalPurchasesAmount() {
        float total = 0;
        if (DataContainer.myPurchasesList != null) {
            for (int i = 0; i < DataContainer.myPurchasesList.size(); i++) {
                total += Float.parseFloat(DataContainer.myPurchasesList.get(i).first_price);

            }
        } else {
        }
        return total;
    }
}



