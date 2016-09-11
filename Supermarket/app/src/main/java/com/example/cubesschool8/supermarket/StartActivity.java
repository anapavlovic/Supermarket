package com.example.cubesschool8.supermarket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cubesschool8.supermarket.constant.Constant;
import com.example.cubesschool8.supermarket.data.DataContainer;
import com.example.cubesschool8.supermarket.data.Results;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cubesschool8 on 9/7/16.
 */
public class StartActivity extends Activity {
    private ImageView mLogo;
    private String jsonResponse;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);


        inicComp();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(0);

                    getToken(Constant.GET_TOKEN_URL + "/?password=" + Constant.PASSWORD + "&username=" + Constant.USERNAME);


                    Intent i = new Intent(getApplicationContext(), LogInActivity.class);
                    startActivity(i);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }


    public void inicComp() {

        mLogo = (ImageView) findViewById(R.id.logo);
        relativeLayout = (RelativeLayout) findViewById(R.id.r);
    }

    public void getToken(String tokenUrl) {

        RequestQueue queue = MySingletonVolley.getInstance(this.getApplicationContext()).
                getRequestQueue();

        String url = tokenUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Gson gson = new Gson();
                            Results r = gson.fromJson(response, Results.class);
                            DataContainer.TOKEN = r.getResultsToken();

                            getCityList(Constant.CITY_URL + "/?password=" + Constant.PASSWORD + "&username=" + Constant.USERNAME + "&token=" + DataContainer.TOKEN);

                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                            // Toast.makeText(getApplicationContext(),  R.string.server_error , Toast.LENGTH_SHORT).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        MySingletonVolley.getInstance(this).addToRequestQueue(stringRequest);


    }


    public void getCityList(String cityUrl) {
        RequestQueue queue = MySingletonVolley.getInstance(this.getApplicationContext()).
                getRequestQueue();

        String url = cityUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Gson gson = new Gson();
                            Results r = gson.fromJson(response, Results.class);
                            for (Results.Cities city : r.getResultCity()) {
                                DataContainer.mainCitysList.add(city);
                            }
                            for (Results.Townships t : r.getResultTownShip()) {
                                DataContainer.mainTownshipList.add(t);
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),  R.string.server_error , Toast.LENGTH_SHORT).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        });

        MySingletonVolley.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void getReservationList() {
    }

    public void getCategory() {
    }

    public void getHomeProductList() {
    }

}
