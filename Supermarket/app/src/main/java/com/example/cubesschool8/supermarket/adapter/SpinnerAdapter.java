package com.example.cubesschool8.supermarket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cubesschool8.supermarket.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ana on 9/8/2016.
 */
public class SpinnerAdapter extends ArrayAdapter<String> {

    private int resource;
    private Context context;
    private ArrayList<String> list;
    private LayoutInflater inflater;

    public SpinnerAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.context = context;
        this.list = (ArrayList<String>) objects;
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
        holder.text.setText(getItem(position));
        return row;
    }

    private class Holder {
        private TextView text;
    }
}
