package com.example.cubesschool8.supermarket.data.response;

import com.example.cubesschool8.supermarket.data.DataOrder;

/**
 * Created by cubesschool8 on 9/26/16.
 */
public class ResponseOrder {

    public ResponseOrderPom data;

    public class ResponseOrderPom{
        public String status;
        public String error;
        public String message;
        public String sum;
        public DataOrder results;
    }
}
