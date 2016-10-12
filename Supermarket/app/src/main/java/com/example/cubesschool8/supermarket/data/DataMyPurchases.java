package com.example.cubesschool8.supermarket.data;

import java.util.ArrayList;

/**
 * Created by cubesschool8 on 10/7/16.
 */
public class DataMyPurchases {

    public String id;
    public String user_id;
    public String delivery_id;
    public String country_id;
    public String payment_id;
    public String total_items;
    public String total_price;

    public String total_shipping_price;
    public String notice;
    public String status_id;
    public String created;
    public String bon;
    public String bon_amount;
    public String points;
    public String points_amount;
    public String rate;
    public String designation;
    public String status;
    public String sum;
    public User user;
    public ArrayList<Orders> orders;


    public class User {
        public String company_name;
        public String company_id;
        public String first_name;
        public String last_name;
        public String email;
        public String address;
        public String apartment;
        public String floor;
        public String entrance;
        public String city;
        public String city_part;
        public String postal_code;
        public String cell_phone;


    }

    public class Orders {
        public String id;
        public String shipment_id;
        public String item_id;
        public String store_id;
        public String location_id;
        public String quantity;
        public String first_quantity;
        public String price;
        public String regular_price;
        public String action_price;
        public String action;
        public String size;
        public String color;
        public String shipping_price;
        public String categories_for_analytics;
        public String status_id;
        public String replacement;
        public String replaced_width;
        public String special_sort;
        public String created;
        public String name;
        public String unique_id;
        public String image;
    }


}
