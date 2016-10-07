package com.example.cubesschool8.supermarket.data.response;

import com.example.cubesschool8.supermarket.data.DataAddWishlist;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Ana on 10/3/2016.
 */
public class ResponseAddWishlist {

    public ResponseAddWishlistPom data;


    public class ResponseAddWishlistPom {
        public String status;
        public String message;
        public String error;
        public ArrayList<String> results;
    }
}
