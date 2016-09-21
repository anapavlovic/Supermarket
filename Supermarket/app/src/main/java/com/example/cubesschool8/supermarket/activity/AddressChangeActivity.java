package com.example.cubesschool8.supermarket.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cubesschool8.supermarket.R;
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
    private CustomEditTextFont mStreet, mNumber, mApartment, mFloor, mEntrance, mPostalCode, mCity;
    private ImageView mBack;
    private Button mChangeAddressConfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_change);
        inicComp();
        addListener();
        setAddressData();

    }


    private void setAddressData() {
        if (DataContainer.addressChange.address == null) {
            mStreet.setText(DataContainer.login.address);
            mNumber.setText(DataContainer.login.street_number);
            mFloor.setText(DataContainer.login.floor);
            mPostalCode.setText(DataContainer.login.postal_code);
            mApartment.setText(DataContainer.login.apartment);
            mEntrance.setText(DataContainer.login.entrance);
            mCity.setText(DataContainer.login.city);
        } else {
            mStreet.setText(DataContainer.addressChange.address);
            mNumber.setText(DataContainer.addressChange.street_number);
            mFloor.setText(DataContainer.addressChange.floor);
            mPostalCode.setText(DataContainer.addressChange.postal_code);
            mApartment.setText(DataContainer.addressChange.apartment);
            mEntrance.setText(DataContainer.addressChange.entrance);
            mCity.setText(DataContainer.addressChange.city);
        }

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
                        || mCity.getText().toString().equalsIgnoreCase("")) {
                    BusProvider.getInstance().post(new MessageObject(R.string.proceed, 3000, MessageObject.MESSAGE_INFO));
                    ;
                } else {

                    if (mStreet.getText().toString() != DataContainer.login.address || mNumber.getText().toString() != DataContainer.login.street_number ||
                            mFloor.getText().toString() != DataContainer.login.floor || mPostalCode.getText().toString() != DataContainer.login.postal_code || mApartment.getText().toString() != DataContainer.login.apartment
                            || mEntrance.getText().toString() != DataContainer.login.entrance || mCity.getText().toString() != DataContainer.login.city) {
                        DataContainer.addressChange.street_number = mNumber.getText().toString();
                        DataContainer.addressChange.address = mStreet.getText().toString();
                        DataContainer.addressChange.floor = mFloor.getText().toString();
                        DataContainer.addressChange.postal_code = mPostalCode.getText().toString();
                        DataContainer.addressChange.apartment = mApartment.getText().toString();
                        DataContainer.addressChange.entrance = mEntrance.getText().toString();
                        DataContainer.addressChange.city = mCity.getText().toString();

                        startActivity(new Intent(getApplicationContext(), FinalConfirmationActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(getApplicationContext(), FinalConfirmationActivity.class));
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
        mCity = (CustomEditTextFont) findViewById(R.id.etCityAddress);


    }
}
