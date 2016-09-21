package com.example.cubesschool8.supermarket.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.constant.Constant;
import com.example.cubesschool8.supermarket.customComponents.CustomEditTextFont;
import com.example.cubesschool8.supermarket.customComponents.CustomTextViewFont;
import com.example.cubesschool8.supermarket.data.DataContainer;
import com.example.cubesschool8.supermarket.data.DataProducts;
import com.example.cubesschool8.supermarket.data.response.ResponseForgotPassword;
import com.example.cubesschool8.supermarket.networking.DataLoader;
import com.example.cubesschool8.supermarket.networking.GsonRequest;

import java.util.ArrayList;

/**
 * Created by cubesschool8 on 9/19/16.
 */
public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.Holder> {


    public interface OnItemCountChanged {
        public void onItemcount();
    }

    private Context context;
    private ArrayList<DataProducts> list;
    private LayoutInflater inflater;
    private OnItemCountChanged onItemCountChanged;


    public BasketAdapter(Context context, ArrayList<DataProducts> object) {
        this.context = context;
        this.list = object;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View row = inflater.inflate(R.layout.basket_adapter_layout, parent, false);
        return new Holder(row);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.mProductName.setText(list.get(position).name);
        holder.mProductCount.setText(String.valueOf(list.get(position).count));
        holder.mProductPrice.setText(list.get(position).first_price);
        Glide.with(context).load(list.get(position).thumb330).into(holder.mProductImage);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public DataProducts getItem(int position) {
        return list.get(position);
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CustomTextViewFont mProductName, mProductPrice, mProductCount;
        private ImageView mProductImage, mMinus, mPlus;
        private Dialog mDialog;

        public Holder(View itemView) {
            super(itemView);
            mProductName = (CustomTextViewFont) itemView.findViewById(R.id.basketProductName);
            mProductPrice = (CustomTextViewFont) itemView.findViewById(R.id.basketProductPrice);
            mProductCount = (CustomTextViewFont) itemView.findViewById(R.id.basketProductCount);
            mProductImage = (ImageView) itemView.findViewById(R.id.basketProductImage);
            mMinus = (ImageView) itemView.findViewById(R.id.basketMinus);
            mPlus = (ImageView) itemView.findViewById(R.id.basketPlus);
            mMinus.setOnClickListener(this);
            mPlus.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == mMinus) {
                if (list.get(getAdapterPosition()).count > 1) {
                    list.get(getAdapterPosition()).count--;
                    notifyDataSetChanged();
                    onItemCountChanged.onItemcount();
                } else if (list.get(getAdapterPosition()).count == 1) {

                    mDialog = new AlertDialog.Builder(context)
                            .setTitle("Da li zelite da uklonite proizvod iz korpe?")
                            .setPositiveButton("DA", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    list.get(getAdapterPosition()).count = 0;
                                    DataContainer.basketList.remove(list.get(getAdapterPosition()));
                                    notifyDataSetChanged();
                                    onItemCountChanged.onItemcount();


                                }
                            }).setNegativeButton("Odustani", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mDialog.dismiss();
                                }
                            }).show();


                }
            } else if (v == mPlus) {
                list.get(getAdapterPosition()).count++;
                notifyDataSetChanged();
                float f = Float.parseFloat(list.get(getAdapterPosition()).first_price);
                f *= list.get(getAdapterPosition()).count;
                onItemCountChanged.onItemcount();
            }
        }
    }


    public void setOnItemCountChanged(OnItemCountChanged monItemCountChanged) {
        onItemCountChanged = monItemCountChanged;
    }
}
