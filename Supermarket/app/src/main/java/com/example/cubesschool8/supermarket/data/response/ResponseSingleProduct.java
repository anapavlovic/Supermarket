package com.example.cubesschool8.supermarket.data.response;

import com.example.cubesschool8.supermarket.data.DataSingleProduct;

/**
 * Created by Ana on 9/20/2016.
 */
public class ResponseSingleProduct {
    public ResponseSingleProductPom data;

    public class ResponseSingleProductPom {

        public String status;
        public String error;
        public DataSingleProduct results;

    }
}
