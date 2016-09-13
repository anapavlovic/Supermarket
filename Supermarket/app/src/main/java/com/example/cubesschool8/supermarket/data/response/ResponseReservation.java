package com.example.cubesschool8.supermarket.data.response;

import com.example.cubesschool8.supermarket.data.DataReservation;

import java.util.ArrayList;

/**
 * Created by Ana on 9/12/2016.
 */
public class ResponseReservation {
    public ResponseReservationPom data;

    public class ResponseReservationPom{
        public  String status;
        public String error;
        public ArrayList<DataReservation> results;
    }
}
