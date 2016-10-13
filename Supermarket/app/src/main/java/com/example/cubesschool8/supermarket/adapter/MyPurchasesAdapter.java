package com.example.cubesschool8.supermarket.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.activity.MyPurchasesActivity;
import com.example.cubesschool8.supermarket.customComponents.CustomEditTextFont;
import com.example.cubesschool8.supermarket.customComponents.CustomTextViewFont;
import com.example.cubesschool8.supermarket.data.DataMyPurchases;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ana on 10/13/2016.
 */
public class MyPurchasesAdapter extends RecyclerView.Adapter<MyPurchasesAdapter.Holder> {

    private Context context;
    private ArrayList<DataMyPurchases> list;
    private LayoutInflater inflater;

    private RecyclerView.LayoutManager layoutManager;
    private OrderAdapter orderAdapter;


    public MyPurchasesAdapter(Context context, ArrayList<DataMyPurchases> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = inflater.inflate(R.layout.purchases_adapter_layout, parent, false);
        return new Holder(row);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.date.setText(list.get(position).created);
        holder.total.setText(String.format("%.2f", Double.parseDouble(list.get(position).total_price)));

        holder.orderList = list.get(position).orders;
        orderAdapter = new OrderAdapter(context, holder.orderList);
        holder.recyclerView.setAdapter(orderAdapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class Holder extends RecyclerView.ViewHolder {

        private CustomTextViewFont date, total;
        private RecyclerView recyclerView;
        private ArrayList<DataMyPurchases.Orders> orderList;


        public Holder(View itemView) {
            super(itemView);
            orderList = new ArrayList<>();
            date = (CustomTextViewFont) itemView.findViewById(R.id.dated);
            total = (CustomTextViewFont) itemView.findViewById(R.id.totalamount);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerPurch);
            layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);

        }
    }
}
