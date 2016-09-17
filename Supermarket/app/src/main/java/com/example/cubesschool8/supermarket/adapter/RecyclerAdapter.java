package com.example.cubesschool8.supermarket.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.customComponents.CustomEditTextFont;
import com.example.cubesschool8.supermarket.customComponents.CustomTextViewFont;
import com.example.cubesschool8.supermarket.data.DataProducts;

import java.util.ArrayList;

/**
 * Created by Ana on 9/16/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder> {

    private Context mContex;
    private int resource;
    private LayoutInflater inflater;
    private ArrayList<DataProducts> mList;

    public RecyclerAdapter(Context context, ArrayList<DataProducts> object) {
        this.mContex = context;
        this.mList = object;
        inflater = LayoutInflater.from(mContex);

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View row = inflater.inflate(R.layout.recycler_adapter_products, parent, false);
        return new Holder(row);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.productTitle.setText(mList.get(position).name);
        holder.productPrice.setText(mList.get(position).first_price);
        Glide.with(mContex).load(mList.get(position).thumb330).centerCrop().into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public DataProducts getItem(int position) {
        return mList.get(position);
    }


    public class Holder extends RecyclerView.ViewHolder {

        private ImageView productImage, yellowBasket;
        private CustomTextViewFont productTitle;
        private CustomTextViewFont productPrice;


        public Holder(View itemView) {
            super(itemView);

            productImage = (ImageView) itemView.findViewById(R.id.adapterImageView);
            productPrice = (CustomTextViewFont) itemView.findViewById(R.id.productPriceTv);
            productTitle = (CustomTextViewFont) itemView.findViewById(R.id.productTitleTv);
            yellowBasket = (ImageView) itemView.findViewById(R.id.yellowBasket);
        }
    }
}
