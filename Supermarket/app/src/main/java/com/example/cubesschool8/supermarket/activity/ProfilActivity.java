package com.example.cubesschool8.supermarket.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.adapter.CitySpinnerAdapter;
import com.example.cubesschool8.supermarket.adapter.NavigationAdapter;
import com.example.cubesschool8.supermarket.adapter.SpinnerAdapter;
import com.example.cubesschool8.supermarket.constant.Constant;
import com.example.cubesschool8.supermarket.customComponents.CustomEditTextFont;
import com.example.cubesschool8.supermarket.customComponents.CustomTextViewFont;
import com.example.cubesschool8.supermarket.data.DataCategory;
import com.example.cubesschool8.supermarket.data.DataContainer;
import com.example.cubesschool8.supermarket.data.DataLogIn;
import com.example.cubesschool8.supermarket.data.response.ResponseChangeProfileData;
import com.example.cubesschool8.supermarket.data.response.ResponsePasswordChange;
import com.example.cubesschool8.supermarket.data.response.ResponseProducts;
import com.example.cubesschool8.supermarket.data.response.ResponseSignUp;
import com.example.cubesschool8.supermarket.networking.DataLoader;
import com.example.cubesschool8.supermarket.networking.GsonRequest;
import com.example.cubesschool8.supermarket.tool.BusProvider;
import com.example.cubesschool8.supermarket.tool.MessageObject;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfilActivity extends ActivityWithMessage {
    private final String REQUEST_TAG = "Start_activity";
    private CustomEditTextFont mName, mSurname, mEmail, mPass, mPassRetype, mMobile, mPhone, mFax, mStreet, mNumber, mApartment, mFloor, mEntrance, mPostalCode, mCompanyName, mCompanyPib;
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
    private Button mIzmeniButton;
    private SwitchCompat mSwitch, mSwitchPass;
    private RelativeLayout relativeProgressProfile, mRelativeCompany, mRelativePasswordCHange;
    private AddressChangeActivity addressChangeAcc;
    String day, month, year;
    private ProgressBar mProgressBar;

    private DrawerLayout mDrawerLayout;
    private ExpandableListView mDrawerlist;
    public static NavigationAdapter mAdapter;
    private ImageView mDrawerMenu, mChangeImage, userPhoto;

    private GsonRequest<ResponseChangeProfileData> mRequestChangeData;
    private GsonRequest<ResponsePasswordChange> mRequestPasswordChange;
    private Map<String, String> params;
    private Dialog dialog;

    private DataCategory data;
    private int categoryPosition;
    private int mChildPosition;
    private String childId;
    private Handler handler = new Handler();

    private GsonRequest<ResponseProducts> mSubcategoriesRequest;
    private ArrayList<DataCategory> subCategories;

    private HashMap<DataCategory, List<DataCategory>> childList = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        inicComp();
        getProfileData();
        addListener();


        mSwitchPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mSwitchPass.isChecked()) {
                    mRelativePasswordCHange.setVisibility(View.VISIBLE);
                } else {
                    mRelativePasswordCHange.setVisibility(View.GONE);
                }
            }
        });
    }


    private void addListener() {

        mDrawerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(mDrawerlist);
            }
        });


        mIzmeniButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIzmeniButton.setEnabled(false);
                changeProfileData();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIzmeniButton.setEnabled(true);
                    }
                }, 5000);

            }

        });

        mChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new AlertDialog.Builder(ProfilActivity.this)
                        .setTitle(R.string.dialog_title)
                        .setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, LogInActivity.CAMERA_REQUEST);
                            }
                        }).setNegativeButton(R.string.dialog_negative, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                galleryIntent.setType("image/*");
                                startActivityForResult(galleryIntent, LogInActivity.GALLERY_REQUEST);
                            }
                        }).show();

            }
        });

        mDrawerlist.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                setGroupClickListener(mDrawerlist, data, mAdapter, mDrawerLayout, relativeProgressProfile, mSubcategoriesRequest, parent, v, groupPosition, id, mProgressBar);
                return false;
            }
        });

        mDrawerlist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                setOnChildClicklistener(mDrawerlist, data, mAdapter, mDrawerLayout, relativeProgressProfile, mSubcategoriesRequest, parent, v, groupPosition, mChildPosition, id);
                return false;
            }
        });

    }

    private void inicComp() {
        userPhoto = (ImageView) findViewById(R.id.userPhoto);
        addressChangeAcc = new AddressChangeActivity();
        mName = (CustomEditTextFont) findViewById(R.id.eTname);
        mSurname = (CustomEditTextFont) findViewById(R.id.etSurname);
        mPassRetype = (CustomEditTextFont) findViewById(R.id.etPassRetype);
        mMobile = (CustomEditTextFont) findViewById(R.id.etMobile);
        mPhone = (CustomEditTextFont) findViewById(R.id.etPhone);
        mEmail = (CustomEditTextFont) findViewById(R.id.etEmail);
        mPass = (CustomEditTextFont) findViewById(R.id.etPass);
        mFax = (CustomEditTextFont) findViewById(R.id.etFax);
        mStreet = (CustomEditTextFont) findViewById(R.id.etStreet);
        mNumber = (CustomEditTextFont) findViewById(R.id.etNumber);
        mApartment = (CustomEditTextFont) findViewById(R.id.etApartment);
        mFloor = (CustomEditTextFont) findViewById(R.id.etFloor);
        mEntrance = (CustomEditTextFont) findViewById(R.id.etEntrance);
        mPostalCode = (CustomEditTextFont) findViewById(R.id.etPostalcode);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        relativeProgressProfile = (RelativeLayout) findViewById(R.id.relativeProgress);
        mRelativePasswordCHange = (RelativeLayout) findViewById(R.id.relativPassChange);
        mSwitchPass = (SwitchCompat) findViewById(R.id.passSwitch);


        list = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.cityArray)));
        listDay = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dayArray)));
        listMonth = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.monthArray)));
        listYear = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.yearArray)));
        mProgressBar = (ProgressBar) findViewById(R.id.progressProfile);
        mProgressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#feea00"), PorterDuff.Mode.SRC_IN);

        citySpinner = (Spinner) findViewById(R.id.spinnerCity);
        daySpinner = (Spinner) findViewById(R.id.spinnerDay);
        monthSpinner = (Spinner) findViewById(R.id.spinnerMonth);
        yearSpinner = (Spinner) findViewById(R.id.spinnerYear);


        mCheckBox = (CheckBox) findViewById(R.id.checkboxNewsLetter);

        mFemale = (RadioButton) findViewById(R.id.radioFemale);
        mMale = (RadioButton) findViewById(R.id.radioMale);

        mAdapterCity = new CitySpinnerAdapter(getApplicationContext(), R.layout.spinner_adapter, DataContainer.cities);
        citySpinner.setAdapter(mAdapterCity);

        mAdapterDay = new SpinnerAdapter(getApplicationContext(), R.layout.spinner_adapter, listDay);
        daySpinner.setAdapter(mAdapterDay);

        mAdapterMonth = new SpinnerAdapter(getApplicationContext(), R.layout.spinner_adapter, listMonth);
        monthSpinner.setAdapter(mAdapterMonth);

        mAdapterYear = new SpinnerAdapter(getApplicationContext(), R.layout.spinner_adapter, listYear);
        yearSpinner.setAdapter(mAdapterYear);

        mIzmeniButton = (Button) findViewById(R.id.changeAddressButton);
        mSwitch = (SwitchCompat) findViewById(R.id.dSwitch);
        mRelativeCompany = (RelativeLayout) findViewById(R.id.relativeCompany);
        mCompanyName = (CustomEditTextFont) findViewById(R.id.etCompanyName);
        mCompanyPib = (CustomEditTextFont) findViewById(R.id.etCompanyPib);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerlist = (ExpandableListView) findViewById(R.id.drawerList);
        mDrawerMenu = (ImageView) findViewById(R.id.drawerMenuProfil);
        mChangeImage = (ImageView) findViewById(R.id.photoChange);

        for (int i = 0; i < DataContainer.categories.size(); i++) {
            subCategories = DataContainer.categories.get(i).subcategories;
            childList.put(DataContainer.categories.get(i), subCategories);

        }
        mAdapter = new NavigationAdapter(this, DataContainer.categories, childList);
        mDrawerlist.setAdapter(mAdapter);
    }

    private void changeProfileData() {
        if (mSwitchPass.isChecked()) {
            if (!mPass.getText().toString().equalsIgnoreCase("") && !mPassRetype.getText().toString().equalsIgnoreCase("")) {
                if (!mPass.getText().toString().equalsIgnoreCase(mPassRetype.getText().toString())) {
                    BusProvider.getInstance().post(new MessageObject(R.string.pass_match, 3000, MessageObject.MESSAGE_ERROR));
                } else {
                    relativeProgressProfile.setVisibility(View.VISIBLE);
                    sendChangeDataRequest();
                    sendChangePasswordRequest();
                }
            } else {
                BusProvider.getInstance().post(new MessageObject(R.string.empty_fields, 3000, MessageObject.MESSAGE_ERROR));
            }

        } else {
            if (mName.getText().toString().equalsIgnoreCase("") || mSurname.getText().toString().equalsIgnoreCase("") || mMobile.getText().toString().equalsIgnoreCase("")
                    || mPhone.getText().toString().equalsIgnoreCase("") || mFax.getText().toString().equalsIgnoreCase("") || mStreet.getText().toString().equalsIgnoreCase("") ||
                    mNumber.getText().toString().equalsIgnoreCase("") || mApartment.getText().toString().equalsIgnoreCase("") || mFloor.getText().toString().equalsIgnoreCase("") || mEntrance.getText().toString().equalsIgnoreCase("") ||
                    citySpinner.getSelectedItem().toString().equalsIgnoreCase("Izaberite grad:")) {

                BusProvider.getInstance().post(new MessageObject(R.string.required_fields, 3000, MessageObject.MESSAGE_ERROR));
            } else {

                relativeProgressProfile.setVisibility(View.VISIBLE);
                sendChangeDataRequest();

            }
        }


    }


    public void sendChangePasswordRequest() {


        mRequestPasswordChange = new GsonRequest<ResponsePasswordChange>(Constant.CHANGE_PASSWORD, Request.Method.POST, ResponsePasswordChange.class, new Response.Listener<ResponsePasswordChange>() {
            @Override
            public void onResponse(ResponsePasswordChange response) {

                DataContainer.passwordChangesList = response.data.results;

                if (response.data.error.equalsIgnoreCase("")) {
                    BusProvider.getInstance().post(new MessageObject(R.string.changes_saved, 3000, MessageObject.MESSAGE_SUCCESS));
                    mIzmeniButton.setText(R.string.profile_data_change);
                    relativeProgressProfile.setVisibility(View.GONE);
                } else {
                    BusProvider.getInstance().post(new MessageObject(R.string.empty_fields, 3000, MessageObject.MESSAGE_INFO));
                    relativeProgressProfile.setVisibility(View.GONE);
                    mIzmeniButton.setText(R.string.profile_data_change);


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", error.toString());
                if (error.toString().equals("com.android.volley.NoConnectionError: java.net.UnknownHostException: Unable to resolve host \"shop.cubes.rs\": No address associated with hostname")) {
                    BusProvider.getInstance().post(new MessageObject(R.string.net_error, 3000, MessageObject.MESSAGE_ERROR));
                } else {
                    BusProvider.getInstance().post(new MessageObject(R.string.error, 3000, MessageObject.MESSAGE_ERROR));
                }
                mIzmeniButton.setText(R.string.profile_data_change);
                relativeProgressProfile.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                params = new HashMap<String, String>();
                params.put("id", DataContainer.login.id);
                params.put("token", DataContainer.TOKEN);
                params.put("login_token", DataContainer.LOGIN_TOKEN);
                params.put("password", mPass.getText().toString());
                params.put("password_retype", mPassRetype.getText().toString());

                return params;
            }
        };

        DataLoader.addRequest(getApplication(), mRequestPasswordChange, REQUEST_TAG);
    }

    public void sendChangeDataRequest() {
        mRequestChangeData = new GsonRequest<ResponseChangeProfileData>(Constant.CHANGE_PROFILE_DATA, Request.Method.POST, ResponseChangeProfileData.class, new Response.Listener<ResponseChangeProfileData>() {
            @Override
            public void onResponse(ResponseChangeProfileData response) {
                Log.i("Response", response.toString());
                DataContainer.changeProfileDataList = response.data.results;

                BusProvider.getInstance().post(new MessageObject(R.string.changes_saved, 3000, MessageObject.MESSAGE_SUCCESS));
                mIzmeniButton.setText(R.string.profile_data_change);
                relativeProgressProfile.setVisibility(View.GONE);

                DataContainer.login.first_name = mName.getText().toString();
                DataContainer.login.last_name = mSurname.getText().toString();
                DataContainer.login.email = mEmail.getText().toString();
                DataContainer.login.cell_phone = mMobile.getText().toString();
                DataContainer.login.land_line = mPhone.getText().toString();
                DataContainer.login.fax = mFax.getText().toString();
                DataContainer.login.street_number = mNumber.getText().toString();
                DataContainer.login.address = mStreet.getText().toString();
                DataContainer.login.floor = mFloor.getText().toString();

                DataContainer.login.apartment = mApartment.getText().toString();
                DataContainer.login.entrance = mEntrance.getText().toString();
                DataContainer.login.city = citySpinner.getSelectedItem().toString();
                DataContainer.login.postal_code = mPostalCode.getText().toString();
                DataContainer.login.floor = mFloor.getText().toString();
                if (mCheckBox.isChecked()) {
                    DataContainer.login.newsletter = String.valueOf(1);
                } else {
                    DataContainer.login.newsletter = String.valueOf(0);
                }
                DataContainer.login.date_of_birth = yearSpinner.getSelectedItem().toString() + "-" + monthSpinner.getSelectedItem().toString() + "-" + daySpinner.getSelectedItem().toString();
                if (mMale.isChecked()) {
                    DataContainer.login.gender = "muski";
                } else {
                    DataContainer.login.gender = "zenski";
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", error.toString());
                if (error.toString().equals("com.android.volley.NoConnectionError: java.net.UnknownHostException: Unable to resolve host \"shop.cubes.rs\": No address associated with hostname")) {
                    BusProvider.getInstance().post(new MessageObject(R.string.net_error, 3000, MessageObject.MESSAGE_ERROR));
                } else {
                    BusProvider.getInstance().post(new MessageObject(R.string.error, 3000, MessageObject.MESSAGE_ERROR));
                }
                mIzmeniButton.setText(R.string.profile_data_change);
                relativeProgressProfile.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                params = new HashMap<String, String>();
                params.put("id", DataContainer.login.id);

                if (mRelativeCompany.getVisibility() == View.VISIBLE) {
                    // params.put("user_type", "company");
                    params.put("company_name", mCompanyName.getText().toString());
                    params.put("pib", mCompanyPib.getText().toString());
                } else {
                    // params.put("user_type", "buyer");
                    params.put("company_name", "");
                    params.put("pib", "");
                }


                params.put("first_name", mName.getText().toString());
                params.put("last_name", mSurname.getText().toString());
                params.put("email", mEmail.getText().toString());

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

        DataLoader.addRequest(getApplicationContext(), mRequestChangeData, REQUEST_TAG);
    }

    public void getProfileData() {
        if (DataContainer.login != null) {
            mName.setText(DataContainer.login.first_name);
            mSurname.setText(DataContainer.login.last_name);
            mEmail.setText(DataContainer.login.email);
            mMobile.setText(DataContainer.login.cell_phone);
            mPhone.setText(DataContainer.login.land_line);
            mFax.setText(DataContainer.login.fax);
            mStreet.setText(DataContainer.login.address);
            mNumber.setText(DataContainer.login.street_number);
            mApartment.setText(DataContainer.login.apartment);
            mFloor.setText(DataContainer.login.floor);
            mEntrance.setText(DataContainer.login.entrance);
            citySpinner.setSelection(addressChangeAcc.getSpinnerIndex(citySpinner, DataContainer.login.city));
            mPostalCode.setText(DataContainer.login.postal_code);

            day = DataContainer.login.date_of_birth.trim().substring(8, DataContainer.login.date_of_birth.length());
            daySpinner.setSelection(addressChangeAcc.getSpinnerIndex(daySpinner, day));
            month = DataContainer.login.date_of_birth.trim().substring(5, 7);
            monthSpinner.setSelection(addressChangeAcc.getSpinnerIndex(monthSpinner, month));
            year = DataContainer.login.date_of_birth.trim().substring(0, 4);
            yearSpinner.setSelection(addressChangeAcc.getSpinnerIndex(yearSpinner, year));
            mEntrance.setText(DataContainer.login.entrance);
            mCompanyName = (CustomEditTextFont) findViewById(R.id.etCompanyName);
            mCompanyPib = (CustomEditTextFont) findViewById(R.id.etCompanyPib);

            if (DataContainer.login.newsletter == String.valueOf("1")) {
                mCheckBox.setChecked(true);
            } else {
                mCheckBox.setChecked(false);
            }
            if (DataContainer.login.gender.equalsIgnoreCase("muski")) {
                mMale.setChecked(true);
            } else {
                mFemale.setChecked(true);
            }
            if (DataContainer.login.company_name != null) {
                if (!DataContainer.login.company_name.equalsIgnoreCase("")) {
                    mSwitch.setChecked(true);
                    mRelativeCompany.setVisibility(View.VISIBLE);
                    mCompanyName.setText(DataContainer.login.company_name);
                    mCompanyPib.setText(DataContainer.login.pib);

                } else {

                }
            } else {

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == LogInActivity.CAMERA_REQUEST) {
            final Uri imageUri = data.getData();
            Glide.with(getApplicationContext()).load(imageUri).asBitmap().centerCrop().into(new BitmapImageViewTarget(userPhoto) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    userPhoto.setImageDrawable(circularBitmapDrawable);
                    DataContainer.login.userImage = imageUri.toString();
                }
            });


        } else if (resultCode == RESULT_OK && requestCode == LogInActivity.GALLERY_REQUEST) {
            Uri imageUri = data.getData();

            Glide.with(getApplicationContext()).load(imageUri).asBitmap().centerCrop().into(new BitmapImageViewTarget(userPhoto) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    userPhoto.setImageDrawable(circularBitmapDrawable);
                }
            });


        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DataLoader.cancelRequest(getApplicationContext(), REQUEST_TAG);
    }
}
