package com.example.cubesschool8.supermarket.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.customComponents.CustomTextViewFont;
import com.example.cubesschool8.supermarket.data.DataCategory;
import com.example.cubesschool8.supermarket.data.DataContainer;
import com.example.cubesschool8.supermarket.data.DataProducts;

import java.util.ArrayList;

/**
 * Created by Ana on 9/26/2016.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.Holder> {

    private Context context;
    private ArrayList<DataCategory> list;
    private LayoutInflater inflater;


    public NavigationDrawerAdapter(Context context, ArrayList<DataCategory> object) {
        this.context = context;
        this.list = object;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = null;
        if (viewType == 0) {
            row = inflater.inflate(R.layout.drawer_adapter_layout, parent, false);
        } else {
            row = inflater.inflate(R.layout.drawer_adapter_category, parent, false);
        }
        return new Holder(row);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        int itemType = getItemViewType(position);

        if (itemType == 0) {
            Glide.with(context).load(DataContainer.login.userImage).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.image) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.image.setImageDrawable(circularBitmapDrawable);

                }
            });
            holder.name.setText(DataContainer.login.first_name + " " + DataContainer.login.last_name);
            holder.email.setText(DataContainer.login.email);
        } else if (itemType == 1) {
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(R.drawable.home);
            holder.underline.setVisibility(View.VISIBLE);

            holder.category.setText("Home");
            holder.category.setTextColor(Color.parseColor("#ff3366"));
        } else if (itemType == 2) {
            holder.category.setText(list.get(position - 2).name);
        }

    }

    @Override
    public int getItemCount() {
        return list.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else if (position == 1) {
            return 1;
        } else {
            return 2;
        }
    }

    public class Holder extends RecyclerView.ViewHolder {

        private ImageView image, icon, underline;
        private CustomTextViewFont name, email, category;

        public Holder(View itemView) {
            super(itemView);

            name = (CustomTextViewFont) itemView.findViewById(R.id.drawerName);
            email = (CustomTextViewFont) itemView.findViewById(R.id.emailDrawer);
            image = (ImageView) itemView.findViewById(R.id.drawerImage);
            underline = (ImageView) itemView.findViewById(R.id.underline);

            icon = (ImageView) itemView.findViewById(R.id.drawerIcon);
            category = (CustomTextViewFont) itemView.findViewById(R.id.categoryString);

        }
    }
}
