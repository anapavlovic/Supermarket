package com.example.cubesschool8.supermarket.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.customComponents.CustomTextViewFont;
import com.example.cubesschool8.supermarket.data.DataMyPurchases;

import java.util.ArrayList;

/**
 * Created by Ana on 10/13/2016.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.Holder> {
    private Context context;
    private ArrayList<DataMyPurchases.Orders> list;
    private LayoutInflater inflater;

    public OrderAdapter(Context context, ArrayList<DataMyPurchases.Orders> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = inflater.inflate(R.layout.recycler_order_layout, parent, false);
        return new Holder(row);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        holder.name.setText(list.get(position).name);
        holder.price.setText(String.format("%.2f", Double.parseDouble(list.get(position).price)));
        Glide.with(context).load(list.get(position).image).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ImageView image;
        private CustomTextViewFont name, price;

        public Holder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.imgAdapter);
            name = (CustomTextViewFont) itemView.findViewById(R.id.name);
            price = (CustomTextViewFont) itemView.findViewById(R.id.price);
        }
    }
}
