package com.example.cubesschool8.supermarket.data.response;

import com.example.cubesschool8.supermarket.data.DataToken;

/**
 * Created by cubesschool8 on 9/12/16.
 */
public class ResponseToken {
    public ResponseTokenPom data;


    public class ResponseTokenPom {

        public String status;
        public String error;
        public DataToken results;
    }
}
