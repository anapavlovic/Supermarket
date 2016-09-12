package com.example.cubesschool8.supermarket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.cubesschool8.supermarket.constant.Constant;
import com.example.cubesschool8.supermarket.data.DataCategory;
import com.example.cubesschool8.supermarket.data.DataContainer;
import com.example.cubesschool8.supermarket.data.response.ResponseCategory;
import com.example.cubesschool8.supermarket.data.response.ResponseCity;
import com.example.cubesschool8.supermarket.data.response.ResponseToken;
import com.example.cubesschool8.supermarket.networking.DataLoader;
import com.example.cubesschool8.supermarket.networking.GsonRequest;

/**
 * Created by cubesschool8 on 9/7/16.
 */
public class StartActivity extends Activity {
    private GsonRequest<ResponseToken> mRequestToken;
    private GsonRequest<ResponseCategory> mRequestCategory;
    private GsonRequest<ResponseCity> mrequestCity;
    private final String REQUEST_TAG = "Start_activity";

    private ImageView mLogo;
    private String jsonResponse;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);


        inicComp();

        mRequestToken = new GsonRequest<ResponseToken>(Constant.GET_TOKEN_URL + "/?username=" + Constant.USERNAME + "&password=" + Constant.PASSWORD, Request.Method.GET, ResponseToken.class, new Response.Listener<ResponseToken>() {
            @Override
            public void onResponse(ResponseToken response) {

                DataContainer.TOKEN = response.data.results.token;

                DataLoader.addRequest(getApplicationContext(), mRequestCategory, REQUEST_TAG);
                DataLoader.addRequest(getApplicationContext(), mrequestCity, REQUEST_TAG);

                startActivity(new Intent(getApplicationContext(), LogInActivity.class));
                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });

        mRequestCategory = new GsonRequest<ResponseCategory>(Constant.CATEGORY_URL + "?token=" + DataContainer.TOKEN, Request.Method.GET, ResponseCategory.class, new Response.Listener<ResponseCategory>() {
            @Override
            public void onResponse(ResponseCategory response) {
                DataContainer.categories = response.data.results;
                Toast.makeText(getApplicationContext(), DataContainer.categories.toString(), Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });

        mrequestCity = new GsonRequest<ResponseCity>(Constant.CITY_URL + "?token=" + DataContainer.TOKEN, Request.Method.GET, ResponseCity.class, new Response.Listener<ResponseCity>() {
            @Override
            public void onResponse(ResponseCity response) {
                DataContainer.cities = response.data.results.townships;

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        DataLoader.addRequest(getApplicationContext(), mRequestToken, REQUEST_TAG);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataLoader.cancelRequest(getApplicationContext(), REQUEST_TAG);
    }

    public void inicComp() {

        mLogo = (ImageView) findViewById(R.id.logo);
        relativeLayout = (RelativeLayout) findViewById(R.id.r);
    }

    public void getToken() {

    }

    public void getReservationList() {
    }

    public void getCategory() {
    }

    public void getHomeProductList() {
    }


}
