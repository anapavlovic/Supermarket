package com.example.cubesschool8.supermarket.data.response;

import com.example.cubesschool8.supermarket.data.DataAddWishlist;
import com.example.cubesschool8.supermarket.data.DataProducts;
import com.example.cubesschool8.supermarket.data.DataWishlist;

import java.util.ArrayList;

/**
 * Created by cubesschool8 on 10/3/16.
 */
public class ResponseWishlist {

    public ResponseWishlistPom data;


    public class ResponseWishlistPom {

        public String status;
        public String message;
        public String error;
        public ArrayList<DataProducts > results;
    }
}
