package com.example.cubesschool8.supermarket.data.response;

import com.example.cubesschool8.supermarket.data.DataCategory;
import com.example.cubesschool8.supermarket.data.DataProducts;

import java.util.ArrayList;

/**
 * Created by Ana on 9/12/2016.
 */
public class ResponseProducts {

    public ResponseProductsPom data;

    public class ResponseProductsPom {
        public String status;
        public String error;
        public ArrayList<DataProducts> results;

    }
}
