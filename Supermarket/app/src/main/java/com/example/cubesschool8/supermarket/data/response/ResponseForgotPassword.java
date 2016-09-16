package com.example.cubesschool8.supermarket.data.response;

import com.example.cubesschool8.supermarket.data.DataForgotPassword;

import java.util.ArrayList;

/**
 * Created by Ana on 9/15/2016.
 */
public class ResponseForgotPassword {

    public  ResponseForgotPasswordPom data;

    public class ResponseForgotPasswordPom{
        public String status;
        public String message;
        public String error;
        public ArrayList<DataForgotPassword> results;

    }
}
