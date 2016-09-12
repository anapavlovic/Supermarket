package com.example.cubesschool8.supermarket.data.response;

import com.example.cubesschool8.supermarket.data.DataCategory;

import java.util.ArrayList;

/**
 * Created by cubesschool8 on 9/12/16.
 */
public class ResponseCategory {


    public ResponseCategoryPom data;


    public class ResponseCategoryPom {

        public String status;
        public String error;
        public ArrayList<DataCategory> results;
    }
}
