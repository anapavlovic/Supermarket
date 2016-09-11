package com.example.cubesschool8.supermarket.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ana on 9/9/2016.
 */
public class Results {

    @SerializedName("token")
    private String resultsToken;

    private List<Cities> resultCity;
    private List<Townships> resultTownShip;


    public String getResultsToken() {
        return resultsToken;
    }

    public List<Cities> getResultCity() {
        return resultCity;
    }

    public List<Townships> getResultTownShip() {
        return resultTownShip;
    }




    public class Cities {
        @SerializedName("city")
        private String city;

        @SerializedName("postal_code")
        private String postalCode;

    }


    public class Townships {
        @SerializedName("name")
        private String name;

        @SerializedName("estates")
        private ArrayList<String> estates;

    }
}
