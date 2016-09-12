package com.example.cubesschool8.supermarket.data.response;

import com.example.cubesschool8.supermarket.data.DataCity;

import java.util.ArrayList;

/**
 * Created by cubesschool8 on 9/12/16.
 */
public class ResponseCity  {
    public ResponseCityPom2 data;

    public class ResponseCityPom1 {
        public ArrayList<DataCity> townships;

    }

    public class ResponseCityPom2{
        public String status;
        public String error;

        public ResponseCityPom1 results;
    }
}
