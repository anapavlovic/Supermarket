package com.example.cubesschool8.supermarket.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.constant.Constant;
import com.example.cubesschool8.supermarket.customComponents.CustomTextViewFont;
import com.example.cubesschool8.supermarket.data.DataContainer;
import com.example.cubesschool8.supermarket.data.DataProducts;
import com.example.cubesschool8.supermarket.data.response.ResponseWishlist;
import com.example.cubesschool8.supermarket.networking.DataLoader;
import com.example.cubesschool8.supermarket.networking.GsonRequest;
import com.example.cubesschool8.supermarket.tool.BusProvider;
import com.example.cubesschool8.supermarket.tool.MessageObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatisticsActivity extends AppCompatActivity {
    private final String REQUEST_TAG = "Start_activity";
    private ImageView mBack;
    private CustomTextViewFont totalAmount, purchase, wishListCount, wishlist;

    private GsonRequest<ResponseWishlist> mRequestWishList;

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
        }
    }

    public void setWishListCount() {
        super.onResume();
        if (DataContainer.wishList != null) {
            wishListCount.setText(String.valueOf(DataContainer.wishList.size()));
        } else {
            wishListCount.setText(String.valueOf(0));
        }
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
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                i.putExtra("wishlist", HomeActivity.WISHLIST);
                startActivity(i);
            }
        });
    }

    private void inicComp() {
        mBack = (ImageView) findViewById(R.id.statisticsback);
        totalAmount = (CustomTextViewFont) findViewById(R.id.tvUkupanIznos);
        purchase = (CustomTextViewFont) findViewById(R.id.tvKupovina);
        wishListCount = (CustomTextViewFont) findViewById(R.id.tvWishList);
        wishlist = (CustomTextViewFont) findViewById(R.id.tvwl);
    }

    public void getWishlist() {


        mRequestWishList = new GsonRequest<ResponseWishlist>(Constant.URL_FAVOURITES_LIST, Request.Method.POST, ResponseWishlist.class, new Response.Listener<ResponseWishlist>() {
            @Override
            public void onResponse(ResponseWishlist response) {

                DataContainer.wishList = response.data.results;


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error", error.toString());
                BusProvider.getInstance().post(new MessageObject(R.string.server_error, 3000, MessageObject.MESSAGE_ERROR));
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        DataLoader.cancelRequest(getApplicationContext(), REQUEST_TAG);
    }
}



