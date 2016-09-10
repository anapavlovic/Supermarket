package com.example.cubesschool8.supermarket.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ana on 9/9/2016.
 */
public class Results {

    @SerializedName("results")
    private List< String> resultsList;


    public List<String> getToken() {

        return resultsList;
    }
}
