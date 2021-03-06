package com.example.cubesschool8.supermarket.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;


import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.cubesschool8.supermarket.activity.HomeActivity;
import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.activity.IntroActivity;
import com.example.cubesschool8.supermarket.activity.LogInActivity;
import com.example.cubesschool8.supermarket.adapter.NavigationAdapter;
import com.example.cubesschool8.supermarket.constant.Constant;
import com.example.cubesschool8.supermarket.customComponents.CustomEditTextFont;
import com.example.cubesschool8.supermarket.customComponents.CustomTextViewFont;
import com.example.cubesschool8.supermarket.data.DataContainer;
import com.example.cubesschool8.supermarket.data.response.ResponseForgotPassword;
import com.example.cubesschool8.supermarket.data.response.ResponseLogIn;
import com.example.cubesschool8.supermarket.networking.DataLoader;
import com.example.cubesschool8.supermarket.networking.GsonRequest;
import com.example.cubesschool8.supermarket.tool.BusProvider;
import com.example.cubesschool8.supermarket.tool.MessageObject;
import com.google.gson.JsonSyntaxException;


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
    private CustomTextViewFont mpasswordForgot, mSkip;
    private CheckBox mCheckSaveUserDAata;
    private Dialog mDialog;
    private final String REQUEST_TAG = "Start_activity";
    private GsonRequest<ResponseLogIn> mRequestLogIn;
    private SharedPreferences prefs;
    private ProgressBar progressBar;
    private RelativeLayout mUser, mRelativeProgress;
    private Handler handler = new Handler();
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
        mSkip = (CustomTextViewFont) getView().findViewById(R.id.tvSkip);
        progressBar = (ProgressBar) getView().findViewById(R.id.progressLogIn);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);

    }

    private void addListener() {

        mSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prefs = getActivity().getSharedPreferences("PrefsIntro", 0);
                boolean b = prefs.getBoolean("firstRun", false);
                if (b == false) {
                    startActivity(new Intent(getActivity(), IntroActivity.class));
                    getActivity().finish();
                } else {
                    Intent i = new Intent(getActivity(), HomeActivity.class);
                    i.putExtra("skip", NavigationAdapter.SKIP);
                    startActivity(i);
                    getActivity().finish();
                }
            }
        });
        mProceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUsername.getText().toString().equalsIgnoreCase("") || mPass.getText().toString().equalsIgnoreCase("")) {
                    mProceedButton.setEnabled(false);
                    BusProvider.getInstance().post(new MessageObject(R.string.empty_fields, 3000, MessageObject.MESSAGE_ERROR));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mProceedButton.setEnabled(true);
                        }
                    }, 5000);


                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    mProceedButton.setText("");
                    mRequestLogIn = new GsonRequest<ResponseLogIn>(Constant.LOGIN_URL + "?token=" + DataContainer.TOKEN + "&email=" + mUsername.getText().toString() + "&password=" + mPass.getText().toString(),
                            Request.Method.GET, ResponseLogIn.class, new Response.Listener<ResponseLogIn>() {
                        @Override
                        public void onResponse(ResponseLogIn response) {
                            Log.i("Response", response.toString());
                            DataContainer.login = response.data.results;
                            DataContainer.LOGIN_TOKEN = response.data.login_token;

                            progressBar.setVisibility(View.GONE);
                            mProceedButton.setText(R.string.Nastavi);
                            if (mCheckSaveUserDAata.isChecked()) {
                                // String md5Username = encryptIt(mUsername.getText().toString());
                                //  String md5Password= encryptIt(mPass.getText().toString());
                                prefs = getActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putBoolean("checked", true);
                                editor.putString("username", mUsername.getText().toString());
                                editor.putString("password", mPass.getText().toString());
                                editor.commit();

                                prefs = getActivity().getSharedPreferences("PrefsIntro", 0);
                                boolean b = prefs.getBoolean("firstRun", false);
                                if (b == false) {
                                    startActivity(new Intent(getActivity(), IntroActivity.class));
                                    getActivity().finish();
                                } else {
                                    startActivity(new Intent(getActivity(), HomeActivity.class));
                                    getActivity().finish();
                                }

                            } else {
                                startActivity(new Intent(getActivity(), IntroActivity.class));
                                getActivity().finish();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {
                                Log.i("error", error.toString());
                                mProceedButton.setEnabled(false);
                                progressBar.setVisibility(View.GONE);
                                mProceedButton.setText(R.string.Nastavi);
                                if (error.toString().equals("com.android.volley.NoConnectionError: java.net.UnknownHostException: Unable to resolve host \"shop.cubes.rs\": No address associated with hostname")) {
                                    BusProvider.getInstance().post(new MessageObject(R.string.net_error, 3000, MessageObject.MESSAGE_ERROR));
                                } else if (error.toString().equals("com.android.volley.VolleyError: com.google.gson.JsonSyntaxException: java.lang.IllegalStateException: Expected BEGIN_OBJECT but was BEGIN_ARRAY at line 1 column 87 path $.data.results")) {
                                    BusProvider.getInstance().post(new MessageObject(R.string.login_incorrect, 3000, MessageObject.MESSAGE_ERROR));
                                }else {
                                    BusProvider.getInstance().post(new MessageObject(R.string.error, 3000, MessageObject.MESSAGE_ERROR));
                                }
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProceedButton.setEnabled(true);

                                    }
                                }, 5000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


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
                                        BusProvider.getInstance().post(new MessageObject(R.string.email_pass_forgot, 3000, MessageObject.MESSAGE_INFO));

                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.i("error", error.toString());
                                        BusProvider.getInstance().post(new MessageObject(R.string.error, 3000, MessageObject.MESSAGE_ERROR));

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
    }

    ;

    @Override
    public void onDestroy() {
        super.onDestroy();
        DataLoader.cancelRequest(getContext(), REQUEST_TAG);
    }
}
