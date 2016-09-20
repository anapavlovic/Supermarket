package com.example.cubesschool8.supermarket.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.customComponents.CustomEditTextFont;
import com.example.cubesschool8.supermarket.data.DataProducts;

import java.util.ArrayList;

/**
 * Created by cubesschool8 on 9/19/16.
 */
public class BasketAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder> {

    private Context context;
    private int mResource;
    private ArrayList<DataProducts> list;
    private LayoutInflater inflater;

    public BasketAdapter(Context context, ArrayList<DataProducts> object) {
        this.context = context;
        this.list = object;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public RecyclerAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {

       /// View v = inflater.inflate(R.layout.activity_product_item, parent, false);/////
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public DataProducts getItem(int position) {
        return list.get(position);
    }

    public class Holder extends RecyclerView.ViewHolder {
        private CustomEditTextFont mproduct;

        public Holder(View itemView) {
            super(itemView);
            mproduct = (CustomEditTextFont) itemView.findViewById(R.id.basketProductitem);
        }
    }
}
