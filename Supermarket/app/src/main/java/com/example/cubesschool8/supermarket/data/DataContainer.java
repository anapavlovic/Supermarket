package com.example.cubesschool8.supermarket.data;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cubesschool8 on 9/9/16.
 */
public class DataContainer {

    public static String TOKEN;
    public static String LOGIN_TOKEN;

    public static ArrayList<DataCategory> categories;
    public static ArrayList<DataCity> cities;
    public static ArrayList<DataProducts> products;
    public static ArrayList<DataReservation> reservations;
    public static ArrayList<DataSignUp> signup;
    public static DataLogIn login;
    public static ArrayList<DataForgotPassword> forgotPassword;
    public static DataSingleProduct singleProductList;

    public static DataOrder orderResponse;

    public static ArrayList<DataProducts> basketList = new ArrayList<>();
    public static DataLogIn addressChange = new DataLogIn();
    public static ArrayList<DataProducts> wishList;
    public static ArrayList<String> addWishlist;
    public static ArrayList<DataProducts> myPurchasesList;
    public static ArrayList<DataProducts> mySearchList;

    public static String changeProfileDataList;

    public static ArrayList<DataPasswordChange> passwordChangesList;


    public static HashMap<String, ArrayList<DataProducts>> categoriesLists = new HashMap<>();


}
