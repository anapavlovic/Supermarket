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
import com.example.cubesschool8.supermarket.data.DataContainer;
import com.example.cubesschool8.supermarket.data.DataProducts;

import java.util.ArrayList;

/**
 * Created by Ana on 9/21/2016.
 */
public class FinalConfirmationAdapter extends RecyclerView.Adapter<FinalConfirmationAdapter.Holder> {
    private Context context;
    private ArrayList<DataProducts> list;
    private LayoutInflater inflater;


    public FinalConfirmationAdapter(Context context, ArrayList<DataProducts> object) {
        this.context = context;
        this.list = object;
        inflater = LayoutInflater.from(context);


    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = null;
        if (viewType == 0) {
            row = inflater.inflate(R.layout.confirmation_address_adapter, parent, false);
        } else if (viewType == 1) {
            row = inflater.inflate(R.layout.confirmation_product_adapter, parent, false);

        }
        return new Holder(row);

    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        int itemType = getItemViewType(position);
        if (itemType == 0) {
            holder.address.setText(DataContainer.addressChange.address + " " + DataContainer.addressChange.street_number + "\nulaz: " + DataContainer.addressChange.entrance + "  sprat: " + DataContainer.addressChange.floor +
                    "  stan: " + DataContainer.addressChange.appartement + "\n" + DataContainer.addressChange.city + " " + DataContainer.addressChange.postal_code);
        } else if (itemType == 1) {
            holder.title.setText(list.get(position).name);
            holder.price.setText(list.get(position).price);
            holder.count.setText(String.valueOf(list.get(position).count));
            Glide.with(context).load(list.get(position).thumb330).into(holder.image);
        }

    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size()) {
            return 0;

        } else {
            return 1;
        }
    }

    public DataProducts getItem(int position) {
        return list.get(position);
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ImageView image;
        private CustomTextViewFont title, price, count, address;

        public Holder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.confProductImage);
            title = (CustomTextViewFont) itemView.findViewById(R.id.confProductName);
            price = (CustomTextViewFont) itemView.findViewById(R.id.confProductPrice);
            count = (CustomTextViewFont) itemView.findViewById(R.id.confProductCount);
            address = (CustomTextViewFont) itemView.findViewById(R.id.etAddressconf);

        }
    }
}
