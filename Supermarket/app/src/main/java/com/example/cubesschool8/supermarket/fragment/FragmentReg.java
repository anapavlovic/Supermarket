package com.example.cubesschool8.supermarket.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.cubesschool8.supermarket.activity.LogInActivity;
import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.activity.TermsOfUseActivity;
import com.example.cubesschool8.supermarket.adapter.CitySpinnerAdapter;
import com.example.cubesschool8.supermarket.adapter.SpinnerAdapter;
import com.example.cubesschool8.supermarket.constant.Constant;
import com.example.cubesschool8.supermarket.customComponents.CustomEditTextFont;
import com.example.cubesschool8.supermarket.customComponents.CustomTextViewFont;
import com.example.cubesschool8.supermarket.data.DataContainer;
import com.example.cubesschool8.supermarket.data.response.ResponseSignUp;
import com.example.cubesschool8.supermarket.networking.DataLoader;
import com.example.cubesschool8.supermarket.networking.GsonRequest;
import com.example.cubesschool8.supermarket.tool.BusProvider;
import com.example.cubesschool8.supermarket.tool.MessageObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by cubesschool8 on 9/7/16.
 */
public class FragmentReg extends android.support.v4.app.Fragment {
    private final String REQUEST_TAG = "Start_activity";
    private static String TAG = "jsonResponse";

    private CustomEditTextFont mName, mSurname, mEmail, mPass, mPassRetype, mMobile, mPhone, mFax, mStreet, mNumber, mApartment, mFloor, mEntrance, mPostalCode, mCompanyName, mCompanyPib;
    private CustomTextViewFont mTermsOfUse;
    private Spinner citySpinner, daySpinner, monthSpinner, yearSpinner;
    private CheckBox mCheckBox;
    private RadioButton mFemale, mMale;
    private RadioGroup mRadioGroup;
    private SpinnerAdapter mAdapterDay, mAdapterMonth, mAdapterYear;
    private CitySpinnerAdapter mAdapterCity;
    private List<String> list;
    private List<String> listDay;
    private List<String> listMonth;
    private List<String> listYear;
    private Button mRegButton;
    private SwitchCompat mSwitch;
    private RelativeLayout mRelativeCompany, mRelativeRegistrationLaout;
    private LogInActivity logInAcc;
    private String s;
    private ProgressBar mProgressBar;
    private Handler handler = new Handler();

    private SharedPreferences sharedPreferences;

    private GsonRequest<ResponseSignUp> mRequestSignUp;
    private Map<String, String> params;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration_page, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        inicComp();


        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mSwitch.isChecked()) {
                    mRelativeCompany.setVisibility(View.VISIBLE);
                } else {
                    mRelativeCompany.setVisibility(View.GONE);
                }
            }
        });


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addListener();
    }

    private void inicComp() {
        mName = (CustomEditTextFont) getView().findViewById(R.id.eTname);
        mSurname = (CustomEditTextFont) getView().findViewById(R.id.etSurname);
        mEmail = (CustomEditTextFont) getView().findViewById(R.id.etEmail);
        mPass = (CustomEditTextFont) getView().findViewById(R.id.etPass);
        mPassRetype = (CustomEditTextFont) getView().findViewById(R.id.etPassRetype);
        mMobile = (CustomEditTextFont) getView().findViewById(R.id.etMobile);
        mPhone = (CustomEditTextFont) getView().findViewById(R.id.etPhone);
        mFax = (CustomEditTextFont) getView().findViewById(R.id.etFax);
        mStreet = (CustomEditTextFont) getView().findViewById(R.id.etStreet);
        mNumber = (CustomEditTextFont) getView().findViewById(R.id.etNumber);
        mApartment = (CustomEditTextFont) getView().findViewById(R.id.etApartment);
        mFloor = (CustomEditTextFont) getView().findViewById(R.id.etFloor);
        mEntrance = (CustomEditTextFont) getView().findViewById(R.id.etEntrance);
        mPostalCode = (CustomEditTextFont) getView().findViewById(R.id.etPostalcode);
        mRadioGroup = (RadioGroup) getView().findViewById(R.id.radioGroup);
        mTermsOfUse = (CustomTextViewFont) getView().findViewById(R.id.uslovikoriscenja);
        mRelativeRegistrationLaout = (RelativeLayout) getView().findViewById(R.id.relativeRegistrationLaout);

        list = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.cityArray)));
        listDay = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dayArray)));
        listMonth = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.monthArray)));
        listYear = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.yearArray)));
        mProgressBar = (ProgressBar) getView().findViewById(R.id.progressSignUp);
        mProgressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);

        citySpinner = (Spinner) getView().findViewById(R.id.spinnerCity);
        daySpinner = (Spinner) getView().findViewById(R.id.spinnerDay);
        monthSpinner = (Spinner) getView().findViewById(R.id.spinnerMonth);
        yearSpinner = (Spinner) getView().findViewById(R.id.spinnerYear);


        mCheckBox = (CheckBox) getView().findViewById(R.id.checkboxNewsLetter);

        mFemale = (RadioButton) getView().findViewById(R.id.radioFemale);
        mMale = (RadioButton) getView().findViewById(R.id.radioMale);

        mAdapterCity = new CitySpinnerAdapter(getActivity(), R.layout.spinner_adapter, DataContainer.cities);
        citySpinner.setAdapter(mAdapterCity);

        mAdapterDay = new SpinnerAdapter(getActivity(), R.layout.spinner_adapter, listDay);
        daySpinner.setAdapter(mAdapterDay);

        mAdapterMonth = new SpinnerAdapter(getActivity(), R.layout.spinner_adapter, listMonth);
        monthSpinner.setAdapter(mAdapterMonth);

        mAdapterYear = new SpinnerAdapter(getActivity(), R.layout.spinner_adapter, listYear);
        yearSpinner.setAdapter(mAdapterYear);

        mRegButton = (Button) getView().findViewById(R.id.regButt);
        mSwitch = (SwitchCompat) getView().findViewById(R.id.dSwitch);
        mRelativeCompany = (RelativeLayout) getView().findViewById(R.id.relativeCompany);
        mCompanyName = (CustomEditTextFont) getView().findViewById(R.id.etCompanyName);
        mCompanyPib = (CustomEditTextFont) getView().findViewById(R.id.etCompanyPib);

        logInAcc = new LogInActivity();
    }

    private void addListener() {

        mRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRegButton.setEnabled(false);
                if (daySpinner.getSelectedItem().toString().equalsIgnoreCase("31") && (monthSpinner.getSelectedItem().toString().equalsIgnoreCase("02") || monthSpinner.getSelectedItem().toString().equalsIgnoreCase("04") ||
                        monthSpinner.getSelectedItem().toString().equalsIgnoreCase("06") || monthSpinner.getSelectedItem().toString().equalsIgnoreCase("09") || monthSpinner.getSelectedItem().toString().equalsIgnoreCase("11"))) {
                    BusProvider.getInstance().post(new MessageObject(R.string.days_months, 3000, MessageObject.MESSAGE_ERROR));

                } else if (daySpinner.getSelectedItem().toString().equalsIgnoreCase("30") && monthSpinner.getSelectedItem().toString().equalsIgnoreCase("02")) {
                    BusProvider.getInstance().post(new MessageObject(R.string.days_months, 3000, MessageObject.MESSAGE_ERROR));

                } else if (mName.getText().toString().equalsIgnoreCase(" ") || mSurname.getText().toString().equalsIgnoreCase("") || mMobile.getText().toString().equalsIgnoreCase("")
                        || mPhone.getText().toString().equalsIgnoreCase("") || mFax.getText().toString().equalsIgnoreCase("") || mStreet.getText().toString().equalsIgnoreCase("") ||
                        mNumber.getText().toString().equalsIgnoreCase("") || mApartment.getText().toString().equalsIgnoreCase("") || mFloor.getText().toString().equalsIgnoreCase("") || mEntrance.getText().toString().equalsIgnoreCase("") ||
                        citySpinner.getSelectedItem().toString().equalsIgnoreCase("Izaberite grad:") || mPass.getText().toString().equalsIgnoreCase("") || mPassRetype.getText().toString().equalsIgnoreCase("")) {
                    BusProvider.getInstance().post(new MessageObject(R.string.required_fields, 3000, MessageObject.MESSAGE_ERROR));

                } else if (!mEmail.getText().toString().trim().matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
                    mEmail.setError("Invalid Email Address");
                } else if (mPhone.getText().toString().length() < 5 || mMobile.getText().toString().length() < 5) {
                    BusProvider.getInstance().post(new MessageObject(R.string.phone_length, 3000, MessageObject.MESSAGE_ERROR));

                } else if (mRadioGroup.getCheckedRadioButtonId() == -1) {
                    BusProvider.getInstance().post(new MessageObject(R.string.radiogroup_check, 3000, MessageObject.MESSAGE_ERROR));

                } else if (!mPass.getText().toString().equalsIgnoreCase(mPassRetype.getText().toString())) {
                    mPassRetype.setText("");
                    BusProvider.getInstance().post(new MessageObject(R.string.pass_match, 3000, MessageObject.MESSAGE_ERROR));

                } else {
                    signUpRequest();

                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRegButton.setEnabled(true);
                    }
                }, 5000);
            }
        });


        mTermsOfUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TermsOfUseActivity.class));
            }
        });
    }


    private void signUpRequest() {
        mRegButton.setText("");
        mProgressBar.setVisibility(View.VISIBLE);


        mRequestSignUp = new GsonRequest<ResponseSignUp>(Constant.SIGNUP_URL, Request.Method.POST, ResponseSignUp.class, new Response.Listener<ResponseSignUp>() {
            @Override
            public void onResponse(ResponseSignUp response) {
                Log.i("Response", response.toString());
                DataContainer.signup = response.data.results;

                if (response.data.error != "") {
                    Toast.makeText(getContext(), response.data.error, Toast.LENGTH_SHORT).show();
                    mRegButton.setText(R.string.registration);
                    mProgressBar.setVisibility(View.GONE);
                } else {
                   /* sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user_registered", "user_registered").commit();*/
                    mRegButton.setText(R.string.registration);
                    mProgressBar.setVisibility(View.GONE);
                    logInAcc.viewPager.setCurrentItem(0);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", error.toString());
                if (error.toString().equals("com.android.volley.NoConnectionError: java.net.UnknownHostException: Unable to resolve host \"shop.cubes.rs\": No address associated with hostname")) {
                    BusProvider.getInstance().post(new MessageObject(R.string.net_error, 3000, MessageObject.MESSAGE_ERROR));
                }else {
                    BusProvider.getInstance().post(new MessageObject(R.string.error, 3000, MessageObject.MESSAGE_ERROR));
                }
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mRegButton.setEnabled(true);
//
//                    }
//                }, 5000);
                mRegButton.setText(R.string.registration);
                mProgressBar.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                params = new HashMap<String, String>();

                if (mRelativeCompany.getVisibility() == View.VISIBLE) {
                    params.put("user_type", "company");
                    params.put("company_name", mCompanyName.getText().toString());
                    params.put("pib", mCompanyPib.getText().toString());
                } else {
                    params.put("user_type", "buyer");
                    params.put("company_name", "");
                    params.put("pib", "");
                }

                params.put("first_name", mName.getText().toString());
                params.put("last_name", mSurname.getText().toString());
                params.put("email", mEmail.getText().toString());
                params.put("password", mPass.getText().toString());
                params.put("password_retype", mPassRetype.getText().toString());

                params.put("cell_phone", mMobile.getText().toString());
                params.put("phone", mPhone.getText().toString());
                params.put("fax", mFax.getText().toString());

                params.put("street", mStreet.getText().toString());
                params.put("number", mNumber.getText().toString());
                params.put("appartement", mApartment.getText().toString());
                params.put("floor", mFloor.getText().toString());
                params.put("entrance", mEntrance.getText().toString());

                params.put("city", citySpinner.getSelectedItem().toString());
                params.put("postal_code", mPostalCode.getText().toString());

                params.put("newsletter", mCheckBox.isChecked() ? "1" : "0");

                params.put("day", (String) daySpinner.getSelectedItem());
                params.put("month", (String) monthSpinner.getSelectedItem());
                params.put("year", (String) yearSpinner.getSelectedItem());

                params.put("gender", mMale.isChecked() ? "1" : "2");

                params.put("token", DataContainer.TOKEN);
                return params;
            }
        };

        DataLoader.addRequest(getActivity(), mRequestSignUp, REQUEST_TAG);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DataLoader.cancelRequest(getContext(), REQUEST_TAG);
    }

// Constant.SIGNUP_URL+"?first_name="+ mName.getText().toString()+"&last_name="+mSurname.getText().toString()+"&email="+mEmail.getText().toString()+"&password="+mPass.getText().toString()+
    //  "&password_retype="+mPassRetype.getText().toString()+"&call_phone="+mMobile.getText().toString()+"&phone="+mPhone.getText().toString()+"&fax="+mFax.getText().toString()+
    //         "&street="+mStreet.getText().toString()+"&number="+mNumber.getText().toString()+"&appartment="+mApartment.getText().toString()+"&floor="+mFloor.getText().toString()+
    //         "&entrance="+mEntrance.getText().toString()+"&city="+citySpinner.getSelectedItem().toString()+"&postal_code="+mPostalCode.getText().toString()+"&newsletter=0"+"&day="+
    //       daySpinner.getSelectedItem().toString()+"&month="+monthSpinner.getSelectedItem().toString()+"&year="+yearSpinner.getSelectedItem().toString()+"&gender=1"+"&user_type=buyer"+"&company_name="+"&pib="+"&token="+ DataContainer.TOKEN
}


