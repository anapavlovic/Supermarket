package com.example.cubesschool8.supermarket.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.adapter.CitySpinnerAdapter;
import com.example.cubesschool8.supermarket.adapter.SpinnerAdapter;
import com.example.cubesschool8.supermarket.customComponents.CustomEditTextFont;
import com.example.cubesschool8.supermarket.data.DataContainer;
import com.example.cubesschool8.supermarket.data.DataLogIn;
import com.example.cubesschool8.supermarket.tool.BusProvider;
import com.example.cubesschool8.supermarket.tool.MessageObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddressChangeActivity extends ActivityWithMessage {
    private CustomEditTextFont mStreet, mNumber, mApartment, mFloor, mEntrance, mPostalCode;
    private ImageView mBack;
    private Button mChangeAddressConfirm;
    private Spinner mPaymentSpinner, mCity;
    private List<String> paymentList;
    private List<String> cityList;
    private SpinnerAdapter mAdapter;
    private CitySpinnerAdapter cityAdapter;
    private String total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_change);
        inicComp();
        addListener();
        setAddressData();

        total = getIntent().getStringExtra("total");


    }


    private void setAddressData() {
        if (DataContainer.addressChange.address == null) {
            mStreet.setText(DataContainer.login.address);
            mNumber.setText(DataContainer.login.street_number);
            mFloor.setText(DataContainer.login.floor);
            mPostalCode.setText(DataContainer.login.postal_code);
            mApartment.setText(DataContainer.login.apartment);
            mEntrance.setText(DataContainer.login.entrance);
            mCity.setSelection(getSpinnerIndex(mCity, DataContainer.login.city));
        } else {
            mStreet.setText(DataContainer.addressChange.address);
            mNumber.setText(DataContainer.addressChange.street_number);
            mFloor.setText(DataContainer.addressChange.floor);
            mPostalCode.setText(DataContainer.addressChange.postal_code);
            mApartment.setText(DataContainer.addressChange.apartment);
            mEntrance.setText(DataContainer.addressChange.entrance);
            mCity.setSelection(getSpinnerIndex(mCity, DataContainer.addressChange.city));
        }

    }

    private int getSpinnerIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
            }
        }
        return index;
    }


    private void addListener() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mChangeAddressConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStreet.getText().toString().equalsIgnoreCase("") || mNumber.getText().toString().equalsIgnoreCase("") || mFloor.getText().toString().equalsIgnoreCase("") ||
                        mPostalCode.getText().toString().equalsIgnoreCase("") || mApartment.getText().toString().equalsIgnoreCase("") || mEntrance.getText().toString().equalsIgnoreCase("")
                        || mCity.getSelectedItem().toString().equalsIgnoreCase("")) {
                    BusProvider.getInstance().post(new MessageObject(R.string.proceed, 3000, MessageObject.MESSAGE_INFO));
                    ;
                } else {

                    if (mStreet.getText().toString() != DataContainer.login.address || mNumber.getText().toString() != DataContainer.login.street_number ||
                            mFloor.getText().toString() != DataContainer.login.floor || mPostalCode.getText().toString() != DataContainer.login.postal_code || mApartment.getText().toString() != DataContainer.login.apartment
                            || mEntrance.getText().toString() != DataContainer.login.entrance || mCity.getSelectedItem().toString() != DataContainer.login.city) {
                        DataContainer.addressChange.street_number = mNumber.getText().toString();
                        DataContainer.addressChange.address = mStreet.getText().toString();
                        DataContainer.addressChange.floor = mFloor.getText().toString();
                        DataContainer.addressChange.postal_code = mPostalCode.getText().toString();
                        DataContainer.addressChange.apartment = mApartment.getText().toString();
                        DataContainer.addressChange.entrance = mEntrance.getText().toString();
                        DataContainer.addressChange.city = mCity.getSelectedItem().toString();

                        Intent i = new Intent(getApplicationContext(), FinalConfirmationActivity.class);
                        i.putExtra("total", total);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(getApplicationContext(), FinalConfirmationActivity.class);
                        i.putExtra("total", total);
                        startActivity(i);
                        finish();
                    }
                }
            }
        });
    }

    private void inicComp() {
        mStreet = (CustomEditTextFont) findViewById(R.id.etStreetAddress);
        mNumber = (CustomEditTextFont) findViewById(R.id.etNumberAddress);
        mApartment = (CustomEditTextFont) findViewById(R.id.etApartmentAddress);
        mFloor = (CustomEditTextFont) findViewById(R.id.etFloorAddress);
        mEntrance = (CustomEditTextFont) findViewById(R.id.etEntranceAddress);
        mPostalCode = (CustomEditTextFont) findViewById(R.id.etPostalcodeAddress);
        mBack = (ImageView) findViewById(R.id.addressBack);
        mChangeAddressConfirm = (Button) findViewById(R.id.changeAddressButton);
        mCity = (Spinner) findViewById(R.id.etCityAddress);

        cityList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.cityArray)));
        cityAdapter = new CitySpinnerAdapter(getApplicationContext(), R.layout.spinner_adapter, DataContainer.cities);
        mCity.setAdapter(cityAdapter);

        mPaymentSpinner = (Spinner) findViewById(R.id.paymentSpinner);
        paymentList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.paymentOptions)));
        mAdapter = new SpinnerAdapter(getApplicationContext(), R.layout.spinner_adapter, paymentList) {
            @Override
            public boolean isEnabled(int position) {
                if (DataContainer.login.company_name == null) {
                    if (position == 3) {
                        return false;
                    } else {
                        return true;
                    }
                }
                return true;
            }

        };
        mPaymentSpinner.setAdapter(mAdapter);


    }


}
