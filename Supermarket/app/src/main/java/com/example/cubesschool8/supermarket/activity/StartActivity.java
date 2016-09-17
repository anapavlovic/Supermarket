package com.example.cubesschool8.supermarket.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.constant.Constant;
import com.example.cubesschool8.supermarket.data.DataContainer;
import com.example.cubesschool8.supermarket.data.response.ResponseCategory;
import com.example.cubesschool8.supermarket.data.response.ResponseCity;
import com.example.cubesschool8.supermarket.data.response.ResponseLogIn;
import com.example.cubesschool8.supermarket.data.response.ResponseProducts;
import com.example.cubesschool8.supermarket.data.response.ResponseReservation;
import com.example.cubesschool8.supermarket.data.response.ResponseToken;
import com.example.cubesschool8.supermarket.networking.DataLoader;
import com.example.cubesschool8.supermarket.networking.GsonRequest;
import com.example.cubesschool8.supermarket.tool.BusProvider;
import com.example.cubesschool8.supermarket.tool.MessageObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Created by cubesschool8 on 9/7/16.
 */
public class StartActivity extends ActivityWithMessage {
    private GsonRequest<ResponseToken> mRequestToken;
    private GsonRequest<ResponseCategory> mRequestCategory;
    private GsonRequest<ResponseCity> mrequestCity;
    private GsonRequest<ResponseProducts> mRequestProducts;
    private GsonRequest<ResponseReservation> mRequestReservation;


    private final String REQUEST_TAG = "Start_activity";

    private ImageView mLogo;
    private String jsonResponse;
    private RelativeLayout relativeLayout;
    private int counter;
    private SharedPreferences sharedPreferences;

    private GsonRequest<ResponseLogIn> mRequestLogIn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);


        inicComp();



        mRequestToken = new GsonRequest<ResponseToken>(Constant.GET_TOKEN_URL + "/?username=" + Constant.USERNAME + "&password=" + Constant.PASSWORD, Request.Method.GET, ResponseToken.class, new Response.Listener<ResponseToken>() {
            @Override
            public void onResponse(ResponseToken response) {
                checkVolleyFinished();
                DataContainer.TOKEN = response.data.results.token;

                DataLoader.addRequest(getApplicationContext(), mRequestCategory, REQUEST_TAG);
                DataLoader.addRequest(getApplicationContext(), mrequestCity, REQUEST_TAG);
                DataLoader.addRequest(getApplicationContext(), mRequestProducts, REQUEST_TAG);
                DataLoader.addRequest(getApplicationContext(), mRequestReservation, REQUEST_TAG);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                checkVolleyFinished();
                BusProvider.getInstance().post(new MessageObject());

            }
        });

        mRequestCategory = new GsonRequest<ResponseCategory>(Constant.CATEGORY_URL + "?token=" + DataContainer.TOKEN, Request.Method.GET, ResponseCategory.class, new Response.Listener<ResponseCategory>() {
            @Override
            public void onResponse(ResponseCategory response) {
                checkVolleyFinished();
                DataContainer.categories = response.data.results;

                // Toast.makeText(getApplicationContext(), DataContainer.categories.toString(), Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                checkVolleyFinished();

            }
        });

        mrequestCity = new GsonRequest<ResponseCity>(Constant.CITY_URL + "?token=" + DataContainer.TOKEN, Request.Method.GET, ResponseCity.class, new Response.Listener<ResponseCity>() {
            @Override
            public void onResponse(ResponseCity response) {
                checkVolleyFinished();
                DataContainer.cities = response.data.results.townships;


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                checkVolleyFinished();
            }
        });

        mRequestProducts = new GsonRequest<ResponseProducts>(Constant.PRODUCTS_URL + "?token=" + DataContainer.TOKEN, Request.Method.GET, ResponseProducts.class, new Response.Listener<ResponseProducts>() {
            @Override
            public void onResponse(ResponseProducts response) {

                DataContainer.products = response.data.results;
                checkVolleyFinished();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                checkVolleyFinished();
            }
        });

        mRequestReservation = new GsonRequest<ResponseReservation>(Constant.RESERVATION_URL + "?token=" + DataContainer.TOKEN, Request.Method.GET, ResponseReservation.class, new Response.Listener<ResponseReservation>() {
            @Override
            public void onResponse(ResponseReservation response) {

                DataContainer.reservations = response.data.results;

                checkVolleyFinished();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                checkVolleyFinished();

            }

        });

        DataLoader.addRequest(getApplicationContext(), mRequestToken, REQUEST_TAG);


    }


    public  synchronized void checkVolleyFinished() {
        if (counter == 4) {
            checkifUserDataSaved();
            finish();
        }else  {
            counter++;
        }


    }

    public void checkifUserDataSaved() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean s = sharedPreferences.getBoolean("checked", false);
        String encryptmUsername = sharedPreferences.getString("username", "");
        String encryptmPass = sharedPreferences.getString("password", "");

        String username = decryptIt(encryptmUsername);
        String password = decryptIt(encryptmPass);

        if (s == true) {
            mRequestLogIn = new GsonRequest<ResponseLogIn>(Constant.LOGIN_URL + "?token=" + DataContainer.TOKEN + "&email=" + encryptmUsername + "&password=" + encryptmPass,
                    Request.Method.GET, ResponseLogIn.class, new Response.Listener<ResponseLogIn>() {
                @Override
                public void onResponse(ResponseLogIn response) {
                    Log.i("Response", response.toString());
                    DataContainer.login = response.data.results;
                    if (response.data.error != "") {
                        Toast.makeText(getApplicationContext(), R.string.login_incorrect, Toast.LENGTH_SHORT).show();
                    }  else {
                        }
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("error", error.toString());
                    startActivity(new Intent(getApplicationContext(), LogInActivity.class));
                }
            });

            DataLoader.addRequest(getApplicationContext(), mRequestLogIn, REQUEST_TAG);

        }else{
            startActivity(new Intent(getApplicationContext(), LogInActivity.class));
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataLoader.cancelRequest(getApplicationContext(), REQUEST_TAG);
    }

    public void inicComp() {

        mLogo = (ImageView) findViewById(R.id.logo);
        relativeLayout = (RelativeLayout) findViewById(R.id.rootView);
    }




    public static String decryptIt(String value) {

        return value;
    }




}
