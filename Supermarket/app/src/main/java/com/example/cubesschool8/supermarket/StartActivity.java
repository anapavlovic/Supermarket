package com.example.cubesschool8.supermarket;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
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
import com.example.cubesschool8.supermarket.data.response.ResponseProducts;
import com.example.cubesschool8.supermarket.data.response.ResponseReservation;
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
    private GsonRequest<ResponseProducts> mRequestProducts;
    private GsonRequest<ResponseReservation> mRequestReservation;


    private final String REQUEST_TAG = "Start_activity";

    private ImageView mLogo;
    private String jsonResponse;
    private RelativeLayout relativeLayout;

    private int successCount;
    private int errorCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);


        inicComp();

        mRequestToken = new GsonRequest<ResponseToken>(Constant.GET_TOKEN_URL + "/?username=" + Constant.USERNAME + "&password=" + Constant.PASSWORD, Request.Method.GET, ResponseToken.class, new Response.Listener<ResponseToken>() {
            @Override
            public void onResponse(ResponseToken response) {
                successCount++;
                DataContainer.TOKEN = response.data.results.token;

                DataLoader.addRequest(getApplicationContext(), mRequestCategory, REQUEST_TAG);
                DataLoader.addRequest(getApplicationContext(), mrequestCity, REQUEST_TAG);
                DataLoader.addRequest(getApplicationContext(), mRequestProducts, REQUEST_TAG);
                DataLoader.addRequest(getApplicationContext(), mRequestReservation, REQUEST_TAG);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorCount++;
                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();

            }
        });

        mRequestCategory = new GsonRequest<ResponseCategory>(Constant.CATEGORY_URL + "?token=" + DataContainer.TOKEN, Request.Method.GET, ResponseCategory.class, new Response.Listener<ResponseCategory>() {
            @Override
            public void onResponse(ResponseCategory response) {
                successCount++;
                DataContainer.categories = response.data.results;
                checkVolleyFinished();
                // Toast.makeText(getApplicationContext(), DataContainer.categories.toString(), Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorCount++;
                checkVolleyFinished();
                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });

        mrequestCity = new GsonRequest<ResponseCity>(Constant.CITY_URL + "?token=" + DataContainer.TOKEN, Request.Method.GET, ResponseCity.class, new Response.Listener<ResponseCity>() {
            @Override
            public void onResponse(ResponseCity response) {
                successCount++;
                DataContainer.cities = response.data.results.townships;
                checkVolleyFinished();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorCount++;
                checkVolleyFinished();
            }
        });

        mRequestProducts = new GsonRequest<ResponseProducts>(Constant.PRODUCTS_URL + "?token=" + DataContainer.TOKEN, Request.Method.GET, ResponseProducts.class, new Response.Listener<ResponseProducts>() {
            @Override
            public void onResponse(ResponseProducts response) {
                successCount++;
                DataContainer.products = response.data.results;
                checkVolleyFinished();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorCount++;
                checkVolleyFinished();
            }
        });

        mRequestReservation = new GsonRequest<ResponseReservation>(Constant.RESERVATION_URL + "?token=" + DataContainer.TOKEN, Request.Method.GET, ResponseReservation.class, new Response.Listener<ResponseReservation>() {
            @Override
            public void onResponse(ResponseReservation response) {
                successCount++;
                DataContainer.reservations = response.data.results;
                checkVolleyFinished();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorCount++;
                checkVolleyFinished();

            }

        });

        DataLoader.addRequest(getApplicationContext(), mRequestToken, REQUEST_TAG);


    }

    public void checkVolleyFinished() {
        if (errorCount + successCount == 5) {
            startActivity(new Intent(getApplicationContext(), LogInActivity.class));
            finish();
        }else if (errorCount >0) {
            Toast.makeText(getApplicationContext(), R.string.volley_error, Toast.LENGTH_SHORT).show();
        }



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


    public void getReservationList() {
    }


    public void getHomeProductList() {
    }


}
