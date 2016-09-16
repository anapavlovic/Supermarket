package com.example.cubesschool8.supermarket.data.response;

import com.example.cubesschool8.supermarket.data.DataLogIn;

import java.util.ArrayList;

/**
 * Created by Ana on 9/14/2016.
 */
public class ResponseLogIn {

    public ResponseLogInPom data;

    public class ResponseLogInPom {
        public String status;
        public String error;
        public ArrayList<DataLogIn> results;
        public String token;
        public String login_token;
        public String incorrect_logins;

    }
}
