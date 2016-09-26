package com.example.cubesschool8.supermarket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.data.DataCity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ana on 9/25/2016.
 */
public class CitySpinnerAdapter extends ArrayAdapter<DataCity> {
    private int resource;
    private Context context;
    private ArrayList<DataCity> list;
    private LayoutInflater inflater;

    public CitySpinnerAdapter(Context context, int resource, ArrayList<DataCity> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.context = context;
        this.list =  objects;
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }


    public View getCustomView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder = null;
        if (row == null) {
            row = inflater.inflate(resource, parent, false);

            holder = new Holder();
            holder.text = (TextView) row.findViewById(R.id.spinnerAdapter);
            row.setTag(holder);

        } else {
            holder = (Holder) row.getTag();
        }

        DataCity data= getItem(position);
        holder.text.setText(data.city);
        return row;
    }

    private class Holder {
        private TextView text;
    }
}
