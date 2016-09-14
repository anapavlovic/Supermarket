package com.example.cubesschool8.supermarket.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.cubesschool8.supermarket.activity.MainActivity;
import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.customComponents.CustomEditTextFont;
import com.example.cubesschool8.supermarket.customComponents.CustomTextViewFont;

/**
 * Created by cubesschool8 on 9/7/16.
 */
public class FragmentPrijava extends android.support.v4.app.Fragment {

    private Button mProceedButton;
    private CustomEditTextFont mUsername, mPass;
    private CustomTextViewFont mpasswordForgot;
    private Dialog mDialog;


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

    }

    private void addListener() {
        mProceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUsername.getText().toString().equalsIgnoreCase("") || mPass.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), R.string.proceed, Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(getActivity(), MainActivity.class));
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
                                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", emailAddress.getText().toString(), null));
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Resetujte lozinku");
                                emailIntent.putExtra(Intent.EXTRA_TEXT, "Text body");
                               // emailIntent.putExtra(Intent.EXTRA_STREAM, url);
                                startActivity(Intent.createChooser(emailIntent, "send email..."));


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
}
