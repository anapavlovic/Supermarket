package com.example.cubesschool8.supermarket.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;



import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.cubesschool8.supermarket.activity.HomeActivity;
import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.constant.Constant;
import com.example.cubesschool8.supermarket.customComponents.CustomEditTextFont;
import com.example.cubesschool8.supermarket.customComponents.CustomTextViewFont;
import com.example.cubesschool8.supermarket.data.DataContainer;
import com.example.cubesschool8.supermarket.data.response.ResponseForgotPassword;
import com.example.cubesschool8.supermarket.data.response.ResponseLogIn;
import com.example.cubesschool8.supermarket.networking.DataLoader;
import com.example.cubesschool8.supermarket.networking.GsonRequest;


import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;



/**
 * Created by cubesschool8 on 9/7/16.
 */
public class FragmentPrijava extends android.support.v4.app.Fragment {

    private Button mProceedButton;
    private CustomEditTextFont mUsername, mPass;
    private CustomTextViewFont mpasswordForgot;
    private CheckBox mCheckSaveUserDAata;
    private Dialog mDialog;
    private final String REQUEST_TAG = "Start_activity";
    private GsonRequest<ResponseLogIn> mRequestLogIn;

    private GsonRequest<ResponseForgotPassword> mRequestForgotPassword;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_prijava_page, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicComp();
        addListener();

    }

    private void inicComp() {
        mProceedButton = (Button) getView().findViewById(R.id.proceedButt);
        mUsername = (CustomEditTextFont) getView().findViewById(R.id.tvPrijava);
        mPass = (CustomEditTextFont) getView().findViewById(R.id.tvPass);
        mpasswordForgot = (CustomTextViewFont) getView().findViewById(R.id.tvpassforgot);
        mCheckSaveUserDAata = (CheckBox) getView().findViewById(R.id.checkboxSaveUserData);
    }

    private void addListener() {
        mProceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUsername.getText().toString().equalsIgnoreCase("") || mPass.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), R.string.proceed, Toast.LENGTH_SHORT).show();
                } else {
                    mRequestLogIn = new GsonRequest<ResponseLogIn>(Constant.LOGIN_URL + "?token=" + DataContainer.TOKEN + "&email=" + mUsername.getText().toString() + "&password=" + mPass.getText().toString(),
                            Request.Method.GET, ResponseLogIn.class, new Response.Listener<ResponseLogIn>() {
                        @Override
                        public void onResponse(ResponseLogIn response) {
                            Log.i("Response", response.toString());
                            DataContainer.login = response.data.results;
                            if (response.data.error != "") {
                                Toast.makeText(getContext(), R.string.login_incorrect, Toast.LENGTH_SHORT).show();
                            } else {
                                if (mCheckSaveUserDAata.isChecked()) {
                                   // String md5Username = encryptIt(mUsername.getText().toString());
                                  //  String md5Password= encryptIt(mPass.getText().toString());
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putBoolean("checked", true);
                                    editor.putString("username", mUsername.getText().toString());
                                    editor.putString("password", mPass.getText().toString());
                                    editor.commit();
                                    startActivity(new Intent(getActivity(), HomeActivity.class));


                                } else {
                                    startActivity(new Intent(getActivity(), HomeActivity.class));
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("error", error.toString());

                        }
                    });
                    DataLoader.addRequest(getActivity(), mRequestLogIn, REQUEST_TAG);


                }
            }
        });

        mpasswordForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Unesite email adresu")
                        .setView(R.layout.custom_dialog)
                        .setPositiveButton("Posalji", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CustomEditTextFont emailAddress = (CustomEditTextFont) mDialog.findViewById(R.id.customPassForgotDialog);
//passwodforgot request
                                mRequestForgotPassword = new GsonRequest<ResponseForgotPassword>(Constant.FORGOT_PASSWORD_URL + "?token=" + DataContainer.TOKEN + "&email=" + emailAddress.getText().toString(),
                                        Request.Method.GET, ResponseForgotPassword.class, new Response.Listener<ResponseForgotPassword>() {
                                    @Override
                                    public void onResponse(ResponseForgotPassword response) {
                                        DataContainer.forgotPassword = response.data.results;
                                        Toast.makeText(getContext(), "Error" + response.data.error, Toast.LENGTH_SHORT).show();
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                });

                                DataLoader.addRequest(getActivity(), mRequestForgotPassword, REQUEST_TAG);


                            }
                        }).setNegativeButton("Odustani", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDialog.dismiss();
                            }
                        }).show();
            }
        });
    }


    public static String encryptIt(String value) throws NoSuchAlgorithmException {

        return value;
    };
}
