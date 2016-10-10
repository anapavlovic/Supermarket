package com.example.cubesschool8.supermarket.data.response;

import com.example.cubesschool8.supermarket.data.DataProducts;
import com.example.cubesschool8.supermarket.data.DataSignUp;

import java.util.ArrayList;

/**
 * Created by cubesschool8 on 10/10/16.
 */
public class ResponseSearch {

    public ResponseSearchPom data;

    public class ResponseSearchPom {

        public String status;
        public String message;
        public String error;
        public ArrayList<DataProducts> results;

    }
}
