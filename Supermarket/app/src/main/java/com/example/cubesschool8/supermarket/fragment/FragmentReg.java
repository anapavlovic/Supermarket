package com.example.cubesschool8.supermarket.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.cubesschool8.supermarket.MainActivity;
import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.adapter.SpinnerAdapter;
import com.example.cubesschool8.supermarket.customComponents.CustomEditTextFont;

import java.util.ArrayList;

/**
 * Created by cubesschool8 on 9/7/16.
 */
public class FragmentReg extends android.support.v4.app.Fragment {
    private CustomEditTextFont mName, mSurname, mEmail, mPass, mPassRetype, mMobile, mPhone, mFax, mStreet, mNumber, mApparment, mFloor, mEntrance, mPostalCode, mCompanyName, mCompanyPib;
    private Spinner citySpinner, daySpinner, monthSpinner, yearSpinner;
    private CheckBox mCheckBox;
    private RadioButton mFemale, mMale;
    private RadioGroup mRadioGroup;
    private SpinnerAdapter mAdapterCity, mAdapterDay, mAdapterMonth, mAdapterYear;
    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<String> listDay = new ArrayList<>();
    private ArrayList<String> listMonth = new ArrayList<>();
    private ArrayList<String> listYear = new ArrayList<>();
    private Button mRegButton;
    private Switch mSwitch;
    private RelativeLayout mRelativeCompany;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration_page, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addLists();
        inicComp();
        addListener();

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mSwitch.isChecked()) {
                    mRelativeCompany.setVisibility(View.VISIBLE);
                }else{
                    mRelativeCompany.setVisibility(View.GONE);
                }
            }
        });


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
        mApparment = (CustomEditTextFont) getView().findViewById(R.id.etAppartment);
        mFloor = (CustomEditTextFont) getView().findViewById(R.id.etFloor);
        mEntrance = (CustomEditTextFont) getView().findViewById(R.id.etEntrance);
        mPostalCode = (CustomEditTextFont) getView().findViewById(R.id.etPostalcode);
        mRadioGroup = (RadioGroup) getView().findViewById(R.id.radioGroup);

        citySpinner = (Spinner) getView().findViewById(R.id.spinnerCity);
        daySpinner = (Spinner) getView().findViewById(R.id.spinnerDay);
        monthSpinner = (Spinner) getView().findViewById(R.id.spinnerMonth);
        yearSpinner = (Spinner) getView().findViewById(R.id.spinnerYear);

        mCheckBox = (CheckBox) getView().findViewById(R.id.checkboxNewsLetter);

        mFemale = (RadioButton) getView().findViewById(R.id.radioFemale);
        mMale = (RadioButton) getView().findViewById(R.id.radioMale);

        mAdapterCity = new SpinnerAdapter(getActivity(), R.layout.spinner_adapter, list);
        citySpinner.setAdapter(mAdapterCity);

        mAdapterDay = new SpinnerAdapter(getActivity(), R.layout.spinner_adapter, listDay);
        daySpinner.setAdapter(mAdapterDay);

        mAdapterMonth = new SpinnerAdapter(getActivity(), R.layout.spinner_adapter, listMonth);
        monthSpinner.setAdapter(mAdapterMonth);

        mAdapterYear = new SpinnerAdapter(getActivity(), R.layout.spinner_adapter, listYear);
        yearSpinner.setAdapter(mAdapterYear);
        mRegButton = (Button) getView().findViewById(R.id.regButt);
        mSwitch = (Switch) getView().findViewById(R.id.dSwitch);
        mRelativeCompany = (RelativeLayout) getView().findViewById(R.id.relativeCompany);
        mCompanyName = (CustomEditTextFont) getView().findViewById(R.id.etCompanyName);
        mCompanyPib = (CustomEditTextFont) getView().findViewById(R.id.etCompanyPib);
    }

    private void addListener() {

        mRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (daySpinner.getSelectedItem().toString().equalsIgnoreCase("31") && (monthSpinner.getSelectedItem().toString().equalsIgnoreCase("02") || monthSpinner.getSelectedItem().toString().equalsIgnoreCase("04") ||
                        monthSpinner.getSelectedItem().toString().equalsIgnoreCase("06") || monthSpinner.getSelectedItem().toString().equalsIgnoreCase("09") || monthSpinner.getSelectedItem().toString().equalsIgnoreCase("11"))) {
                    Toast.makeText(getActivity(), "Izabrani mesec nema taj broj dana.", Toast.LENGTH_SHORT).show();
                } else if (daySpinner.getSelectedItem().toString().equalsIgnoreCase("30") && monthSpinner.getSelectedItem().toString().equalsIgnoreCase("02")) {
                    Toast.makeText(getActivity(), "Izabrani mesec nema taj broj dana.", Toast.LENGTH_SHORT).show();
                } else if (!mEmail.getText().toString().trim().matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
                    mEmail.setError("Invalid Email Address");
                } else {
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
            }
        });
    }

    private void addLists() {
        list.add("Izaberite grad:");
        list.add("Beograd");
        list.add("Novi Sad");
        list.add("Valjevo");
        listDay.add("01");
        listDay.add("02");
        listDay.add("03");
        listDay.add("04");
        listDay.add("05");
        listDay.add("06");
        listDay.add("07");
        listDay.add("08");
        listDay.add("09");
        listDay.add("10");
        listDay.add("11");
        listDay.add("12");
        listDay.add("13");
        listDay.add("14");
        listDay.add("15");
        listDay.add("16");
        listDay.add("17");
        listDay.add("18");
        listDay.add("19");
        listDay.add("20");
        listDay.add("21");
        listDay.add("22");
        listDay.add("23");
        listDay.add("24");
        listDay.add("25");
        listDay.add("26");
        listDay.add("27");
        listDay.add("28");
        listDay.add("29");
        listDay.add("30");
        listDay.add("31");

        listMonth.add("01");
        listMonth.add("02");
        listMonth.add("03");
        listMonth.add("04");
        listMonth.add("05");
        listMonth.add("06");
        listMonth.add("07");
        listMonth.add("08");
        listMonth.add("09");
        listMonth.add("10");
        listMonth.add("11");
        listMonth.add("12");

        listYear.add("1998");
        listYear.add("1997");
        listYear.add("1996");
        listYear.add("1995");
        listYear.add("1994");
        listYear.add("1993");
        listYear.add("1992");
        listYear.add("1991");
        listYear.add("1990");
        listYear.add("1989");
        listYear.add("1988");
        listYear.add("1987");
        listYear.add("1986");
        listYear.add("1985");
        listYear.add("1984");
        listYear.add("1983");
        listYear.add("1982");
        listYear.add("1981");
        listYear.add("1980");
        listYear.add("1979");
        listYear.add("1978");
        listYear.add("1977");
        listYear.add("1976");
        listYear.add("1975");
        listYear.add("1974");
        listYear.add("1973");
        listYear.add("1972");
        listYear.add("1971");
        listYear.add("1970");
        listYear.add("1969");
        listYear.add("1968");
    }
}
