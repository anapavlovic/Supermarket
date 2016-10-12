package com.example.cubesschool8.supermarket.data.response;

import com.example.cubesschool8.supermarket.data.DataMyPurchases;
import com.example.cubesschool8.supermarket.data.DataProducts;

import java.util.ArrayList;

/**
 * Created by cubesschool8 on 10/7/16.
 */
public class ResponseMyPurchases {

    public ResponseMyPurchasesPom data;


    public class ResponseMyPurchasesPom {
        public String status;
        public String message;
        public String error;
        public ArrayList<DataMyPurchases> results;
    }
}

