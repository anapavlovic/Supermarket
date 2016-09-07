package com.example.cubesschool8.supermarket.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.cubesschool8.supermarket.MainActivity;
import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.customComponents.CustomEditTextFont;
import com.example.cubesschool8.supermarket.customComponents.CustomTextViewFont;

/**
 * Created by cubesschool8 on 9/7/16.
 */
public class FragmentPrijava extends android.support.v4.app.Fragment {

    private Button mProceedButton;
    private CustomEditTextFont mUsername, mPass;


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
        mUsername = (CustomEditTextFont)getView().findViewById(R.id.tvPrijava);
        mPass = (CustomEditTextFont)getView().findViewById(R.id.tvPass);

    }

    private void addListener() {
        mProceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUsername.getText().toString().equalsIgnoreCase("") || mPass.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(),R.string.proceed, Toast.LENGTH_SHORT).show();}
                else{
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
            }
        });
    }
}
