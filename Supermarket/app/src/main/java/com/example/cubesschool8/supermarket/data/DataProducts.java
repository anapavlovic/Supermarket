package com.example.cubesschool8.supermarket.data;

import java.io.Serializable;

/**
 * Created by Ana on 9/12/2016.
 */
public class DataProducts implements Cloneable {

    public String name;
    public String description;

    public String store_id;
    public String seo_name;
    public String image;
    public String first_price;
    public String price;
    public String sizes;
    public String spotlight;
    public String new_article;
    public String stock;
    public String brand;
    public String store;
    public String thumb80;
    public String thumb126;
    public String thumb330;



    @Override
    public String toString() {
        return name + " " + description;
    }
/*
    @Override
    public DataProducts clone() throws CloneNotSupportedException {

        DataProducts pom = new DataProducts();

        pom.name=this.name;

        return pom;
    }
 */

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
