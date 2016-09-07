package com.example.cubesschool8.supermarket;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.cubesschool8.supermarket.adapter.PagerAdapter;
import com.example.cubesschool8.supermarket.customComponents.CustomEditTextFont;
import com.example.cubesschool8.supermarket.customComponents.CustomTextViewFont;

/**
 * Created by cubesschool8 on 9/7/16.
 */
public class LogInActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 7;
    private static final int GALLERY_REQUEST = 9;

    private ImageView mOverlayImg, mLogo, mBack, mAddPhoto, mUserPhoto;
    private CustomEditTextFont mUsername, mPassword;
    private RelativeLayout mRelative;
    private LinearLayout mLinearAnim;
    private Button mProceedButton;
    private CustomTextViewFont mPassforgot;
    private Dialog dialog;

    private Animation animation, animationViewPager, animfadeIn, fadeOutAnim, fadeInLogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        inicComp();
        addListener();

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.up_from_bottom);
        animation.setFillAfter(true);
        mLogo.setAnimation(animation);

        animfadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        mOverlayImg.setVisibility(View.VISIBLE);
        animfadeIn.setFillAfter(true);
        mOverlayImg.setAnimation(animfadeIn);


        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animationViewPager = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.up_viewpager);
                mLinearAnim.setVisibility(View.VISIBLE);
                animationViewPager.setFillAfter(true);
                mLinearAnim.setAnimation(animationViewPager);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Prijava"));
        tabLayout.addTab(tabLayout.newTab().setText("Registracija"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if (tab.getText().toString().equalsIgnoreCase("Registracija")) {
                    fadeOutAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                    fadeOutAnim.setFillAfter(true);
                    mLogo.setAnimation(fadeOutAnim);
                    mRelative.setVisibility(View.VISIBLE);

                } else {
                    mRelative.setVisibility(View.GONE);
                    fadeInLogo = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_logo);
                    fadeInLogo.setFillAfter(true);
                    mLogo.setAnimation(fadeInLogo);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void addListener() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new AlertDialog.Builder(LogInActivity.this)
                        .setTitle(R.string.dialog_title)
                        .setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                            }
                        }).setNegativeButton(R.string.dialog_negative, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                galleryIntent.setType("image/*");
                                startActivityForResult(galleryIntent, GALLERY_REQUEST);
                            }
                        }).show();
            }
        });


    }

    private void inicComp() {
        mOverlayImg = (ImageView) findViewById(R.id.overlay);
        mLogo = (ImageView) findViewById(R.id.logoLogin);
        mUsername = (CustomEditTextFont) findViewById(R.id.tvPrijava);
        mPassword = (CustomEditTextFont) findViewById(R.id.tvPass);
        mLinearAnim = (LinearLayout) findViewById(R.id.linearAnim);
        mProceedButton = (Button) findViewById(R.id.proceedButt);
        mPassforgot = (CustomTextViewFont) findViewById(R.id.tvpassforgot);
        mRelative = (RelativeLayout) findViewById(R.id.reglayout);
        mBack = (ImageView) findViewById(R.id.back);
        mAddPhoto = (ImageView) findViewById(R.id.addPhoto);
        mUserPhoto = (ImageView) findViewById(R.id.userPhoto);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            Uri imageUri = data.getData();
            Glide.with(getApplicationContext()).load(imageUri).asBitmap().centerCrop().into(new BitmapImageViewTarget(mUserPhoto) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    mUserPhoto.setImageDrawable(circularBitmapDrawable);
                }
            });
            // Bitmap photo = (Bitmap) data.getExtras().get("data");
            //  mUserPhoto.setImageBitmap(photo);

        } else if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST) {
            Uri imageUri = data.getData();

            Glide.with(getApplicationContext()).load(imageUri).asBitmap().centerCrop().into(new BitmapImageViewTarget(mUserPhoto) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    mUserPhoto.setImageDrawable(circularBitmapDrawable);
                }
            });


        }
    }
}
