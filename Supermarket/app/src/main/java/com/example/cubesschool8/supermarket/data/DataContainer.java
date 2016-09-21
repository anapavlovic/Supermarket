package com.example.cubesschool8.supermarket.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by cubesschool8 on 9/9/16.
 */
public class DataContainer {

    public static String TOKEN;

    public static ArrayList<DataCategory> categories;
    public static ArrayList<DataCity> cities;
    public static ArrayList<DataProducts> products;
    public static ArrayList<DataReservation> reservations;
    public static ArrayList<DataSignUp> signup;
    public static DataLogIn login;
    public static ArrayList<DataForgotPassword> forgotPassword;
    public static DataSingleProduct singleProductList;

    public static ArrayList<DataProducts> basketList = new ArrayList<>();
    public static DataLogIn addressChange = new DataLogIn();


}
