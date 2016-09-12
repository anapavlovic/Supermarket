package com.example.cubesschool8.supermarket.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ana on 9/12/2016.
 */
public class DataCategory {

    public String id;
    public String name;
    public ArrayList<DataCategory> subcategories;

    @Override
    public String toString() {
        return name;
    }
}
