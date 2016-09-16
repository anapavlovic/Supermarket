package com.example.cubesschool8.supermarket.data.response;

import com.example.cubesschool8.supermarket.data.DataSignUp;

import java.util.ArrayList;

/**
 * Created by Ana on 9/14/2016.
 */
public class ResponseSignUp {

    public ResponseSignUpPom data;

    public class ResponseSignUpPom {

        public String status;
        public String message;
        public String error;
        public ArrayList<DataSignUp> results;
    }
}
