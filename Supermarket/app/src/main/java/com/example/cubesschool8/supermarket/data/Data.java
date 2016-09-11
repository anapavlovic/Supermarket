package com.example.cubesschool8.supermarket.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ana on 9/11/2016.
 */
public class Data {
    @SerializedName("status")
    private String mStatus;

    @SerializedName("error")
    private String mError;

    @SerializedName("results")
    private Results mResults;

}
