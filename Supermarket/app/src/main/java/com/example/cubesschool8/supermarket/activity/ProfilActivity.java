package com.example.cubesschool8.supermarket.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
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
import com.example.cubesschool8.supermarket.data.response.ResponseProducts;
import com.example.cubesschool8.supermarket.data.response.ResponseSignUp;
import com.example.cubesschool8.supermarket.networking.DataLoader;
import com.example.cubesschool8.supermarket.networking.GsonRequest;
import com.example.cubesschool8.supermarket.tool.BusProvider;
import com.example.cubesschool8.supermarket.tool.MessageObject;

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

    private GsonRequest<ResponseSignUp> mRequestChangeData;
    private Map<String, String> params;
    private Dialog dialog;

    private DataCategory data;
    private int categoryPosition;
    private int mChildPosition;
    private String childId;

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
               if(mSwitchPass.isChecked()){
                   mRelativePasswordCHange.setVisibility(View.VISIBLE);
               }else{
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
                changeProfileData();

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


                categoryPosition = groupPosition;
                if (groupPosition > 1 && groupPosition < DataContainer.categories.size() + 2) {
                    data = (DataCategory) mAdapter.getGroup(groupPosition - 2);
                    if (mAdapter.getChildrenCount(groupPosition) == 0) {
                        if (HomeActivity.checkList(DataContainer.categoriesLists, data.id) == false) {
                            mDrawerLayout.closeDrawers();
                            relativeProgressProfile.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    relativeProgressProfile.setVisibility(View.VISIBLE);
                                    mSubcategoriesRequest = new GsonRequest<ResponseProducts>(Constant.SUBCATEGORIES_URL + data.id, Request.Method.GET, ResponseProducts.class, new Response.Listener<ResponseProducts>() {
                                        @Override
                                        public void onResponse(ResponseProducts response) {
                                            DataContainer.categoriesLists.put(data.id, response.data.results);
                                            Intent i = new Intent(getApplicationContext(), CategoryItemsActivity.class);
                                            i.putExtra("id", data.id);
                                            startActivity(i);
                                            relativeProgressProfile.setVisibility(View.GONE);

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            BusProvider.getInstance().post(new MessageObject(R.string.server_error, 3000, MessageObject.MESSAGE_ERROR));
                                        }
                                    });

                                    DataLoader.addRequest(getApplicationContext(), mSubcategoriesRequest, REQUEST_TAG);
                                }
                            }, 200);


                        } else {
                            Intent i = new Intent(getApplicationContext(), CategoryItemsActivity.class);
                            i.putExtra("id", data.id);
                            startActivity(i);
                            relativeProgressProfile.setVisibility(View.GONE);
                        }
                    } else {

                    }
                } else if (groupPosition == DataContainer.categories.size() + 3) {
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                } else if (groupPosition == DataContainer.categories.size() + 4) {

                    mDrawerLayout.closeDrawers();
                    mRootView.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            startActivity(new Intent(getApplicationContext(), ProfilActivity.class));

                        }
                    }, 200);


                } else if (groupPosition == DataContainer.categories.size() + 5) {

                    startActivity(new Intent(getApplicationContext(), LogInActivity.class));

                    SharedPreferences settings = getSharedPreferences("MyPreferences", 0);
                    settings.edit().remove("checked").commit();
                    settings.edit().remove("username").commit();
                    settings.edit().remove("password").commit();
                    finish();

                }
                return false;
            }
        });

        mDrawerlist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                data = (DataCategory) mAdapter.getGroup(groupPosition - 2);
                categoryPosition = groupPosition;
                mChildPosition = childPosition;
                childId = String.valueOf(id);
                if (HomeActivity.checkList(DataContainer.categoriesLists, data.id + "." + childId) == false) {
                    mDrawerLayout.closeDrawers();
                    relativeProgressProfile.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            relativeProgressProfile.setVisibility(View.VISIBLE);
                            mSubcategoriesRequest = new GsonRequest<ResponseProducts>(Constant.SUBCATEGORIES_URL + data.id, Request.Method.GET, ResponseProducts.class, new Response.Listener<ResponseProducts>() {
                                @Override
                                public void onResponse(ResponseProducts response) {
                                    relativeProgressProfile.setVisibility(View.VISIBLE);
                                    DataContainer.categoriesLists.put(data.id + "." + childId, response.data.results);
                                    Intent i = new Intent(getApplicationContext(), CategoryItemsActivity.class);
                                    i.putExtra("id", data.id + "." + childId);
                                    startActivity(i);
                                    relativeProgressProfile.setVisibility(View.GONE);


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    BusProvider.getInstance().post(new MessageObject(R.string.server_error, 3000, MessageObject.MESSAGE_ERROR));
                                }
                            });

                            DataLoader.addRequest(getApplicationContext(), mSubcategoriesRequest, REQUEST_TAG);
                        }
                    }, 200);

                } else {
                    Intent i = new Intent(getApplicationContext(), CategoryItemsActivity.class);
                    i.putExtra("id", data.id + "." + childId);
                    startActivity(i);
                    relativeProgressProfile.setVisibility(View.GONE);

                }


                return false;
            }
        });

    }

    private void inicComp() {
        userPhoto = (ImageView) findViewById(R.id.userPhoto);
        addressChangeAcc = new AddressChangeActivity();
        mName = (CustomEditTextFont) findViewById(R.id.eTname);
        mSurname = (CustomEditTextFont) findViewById(R.id.etSurname);
        mEmail = (CustomEditTextFont) findViewById(R.id.etEmail);
        mPass = (CustomEditTextFont) findViewById(R.id.etPass);
        mPassRetype = (CustomEditTextFont) findViewById(R.id.etPassRetype);
        mMobile = (CustomEditTextFont) findViewById(R.id.etMobile);
        mPhone = (CustomEditTextFont) findViewById(R.id.etPhone);
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
        if (!mPass.getText().toString().equalsIgnoreCase(mPassRetype.getText().toString())) {
            BusProvider.getInstance().post(new MessageObject(R.string.pass_match, 3000, MessageObject.MESSAGE_ERROR));

        }
        relativeProgressProfile.setVisibility(View.VISIBLE);


        mRequestChangeData = new GsonRequest<ResponseSignUp>(Constant.SIGNUP_URL, Request.Method.POST, ResponseSignUp.class, new Response.Listener<ResponseSignUp>() {
            @Override
            public void onResponse(ResponseSignUp response) {
                Log.i("Response", response.toString());
                DataContainer.signup = response.data.results;

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

                if (mPass.getText().length() > 0 && mPassRetype.getText().length() > 0) {
                    params.put("password", mPass.getText().toString());
                    params.put("password_retype", mPassRetype.getText().toString());
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

               // params.put("token", DataContainer.TOKEN);
                return params;
            }
        };

        DataLoader.addRequest(getApplicationContext(), mRequestChangeData, REQUEST_TAG);


    }

    public void getProfileData() {
        mName.setText(DataContainer.login.first_name);
        mSurname.setText(DataContainer.login.last_name);
        mEmail.setText(DataContainer.login.email);
        mMobile.setText(DataContainer.login.cell_phone);
        mPhone.setText(DataContainer.login.photo);
        mFax.setText(DataContainer.login.fax);
        mStreet.setText(DataContainer.login.address);
        mNumber.setText(DataContainer.login.street_number);
        mApartment.setText(DataContainer.login.apartment);
        mFloor.setText(DataContainer.login.floor);
        mEntrance.setText(DataContainer.login.entrance);
        citySpinner.setSelection(addressChangeAcc.getSpinnerIndex(citySpinner, DataContainer.login.city));
        mPostalCode.setText(DataContainer.login.postal_code);

        day = DataContainer.login.date_of_birth.trim().substring(0, 2);
        daySpinner.setSelection(addressChangeAcc.getSpinnerIndex(daySpinner, day));
        month = DataContainer.login.date_of_birth.trim().substring(3, 5);
        monthSpinner.setSelection(addressChangeAcc.getSpinnerIndex(monthSpinner, month));
        year = DataContainer.login.date_of_birth.trim().substring(7, DataContainer.login.date_of_birth.length());
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
            mSwitch.setChecked(true);
            mRelativeCompany.setVisibility(View.VISIBLE);
            mCompanyName.setText(DataContainer.login.company_name);
            mCompanyPib.setText(DataContainer.login.pib);

        } else {

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
}
