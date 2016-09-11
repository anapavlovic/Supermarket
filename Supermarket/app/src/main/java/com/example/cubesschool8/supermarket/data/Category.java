package com.example.cubesschool8.supermarket.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ana on 9/12/2016.
 */
public class Category {
    @SerializedName("id")
    private String id;

    @SerializedName("category_id")
    private String category_id;

    @SerializedName("connect_id")
    private String connect_id;

    @SerializedName("name")
    private String name;

    @SerializedName("seo_name")
    private String seo_name;

    @SerializedName("parent_id")
    private String parent_id;

    @SerializedName("maxi_parent_id")
    private String maxi_parent_id;

    @SerializedName("parent_path")
    private String parent_path;

    @SerializedName("deep")
    private String deep;

    @SerializedName("banner")
    private String banner;


}
