package com.example.cubesschool8.supermarket.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.activity.HomeActivity;
import com.example.cubesschool8.supermarket.customComponents.CustomTextViewFont;
import com.example.cubesschool8.supermarket.data.DataCategory;
import com.example.cubesschool8.supermarket.data.DataContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by cubesschool8 on 9/28/16.
 */
public class NavigationAdapter extends BaseExpandableListAdapter {

    public static final int SKIP = 0;
    private Context context;
    private int resource;
    private ArrayList<DataCategory> list;
    private LayoutInflater inflater;
    private HashMap<DataCategory, List<DataCategory>> listDataChild;


    public NavigationAdapter(Context context, List<DataCategory> listData,
                             HashMap<DataCategory, List<DataCategory>> listChildData) {
        this.context = context;
        this.list = (ArrayList<DataCategory>) listData;
        this.listDataChild = listChildData;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public int getGroupCount() {
        return list.size() + 7;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int childrenCount = 0;

        if (groupPosition == 0 || groupPosition == 1 || groupPosition == list.size() + 1 || groupPosition == list.size() + 2 || groupPosition == list.size() + 3) {
            childrenCount = 0;
        } else if (groupPosition == 2) {
            childrenCount = 3;
        } else {
            childrenCount = 0;
        }

        return childrenCount;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listDataChild.get(list.get(groupPosition - 2))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, final View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder = null;


        if (groupPosition == 0) {
            row = inflater.inflate(R.layout.drawer_adapter_layout, parent, false);
            holder = new Holder();
            holder.name = (CustomTextViewFont) row.findViewById(R.id.drawerName);
            holder.email = (CustomTextViewFont) row.findViewById(R.id.emailDrawer);
            holder.image = (ImageView) row.findViewById(R.id.drawerImage);
            row.setTag(R.layout.drawer_adapter_layout, holder);


            Bundle extras = ((Activity) context).getIntent().getExtras();

            if (DataContainer.login != null) {
                if (extras != null) {
                    if (extras.getInt("skip") == SKIP) {
                    }
                } else {
                    Glide.with(context).load(DataContainer.login.userImage).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.image) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            new Holder().image.setImageDrawable(circularBitmapDrawable);

                        }
                    });


                    holder.name.setText(DataContainer.login.first_name + " " + DataContainer.login.last_name);
                    holder.email.setText(DataContainer.login.email);
                }
            } else {
            }

        } else if (groupPosition == 1) {

            row = inflater.inflate(R.layout.drawer_adapter_category, parent, false);
            holder = new Holder();
            holder.category = (CustomTextViewFont) row.findViewById(R.id.categoryString);
            holder.iconImage = (ImageView) row.findViewById(R.id.drawerIcon);
            holder.underline = (ImageView) row.findViewById(R.id.underline);
            row.setTag(R.layout.drawer_adapter_category, holder);


            if (holder.iconImage != null) {
                holder.iconImage.setVisibility(View.VISIBLE);
                holder.category.setVisibility(View.VISIBLE);
                holder.iconImage.setImageResource(R.drawable.home);
                holder.underline.setVisibility(View.VISIBLE);
                holder.category.setText("Home");
                holder.category.setTextColor(Color.parseColor("#ff3366"));
            }

        }

        //LINIJA
        else if (groupPosition == list.size() + 2) {

            row = inflater.inflate(R.layout.drawer_adapter_category, parent, false);
            holder = new Holder();
            holder.category = (CustomTextViewFont) row.findViewById(R.id.categoryString);
            holder.iconImage = (ImageView) row.findViewById(R.id.drawerIcon);
            holder.underline = (ImageView) row.findViewById(R.id.underline);
            row.setTag(R.layout.drawer_adapter_category, holder);


            holder.category.setText("");
            holder.category.setVisibility(View.GONE);
            holder.iconImage.setVisibility(View.GONE);
            holder.underline.setVisibility(View.VISIBLE);


        } else if (groupPosition == list.size() + 3) {

            row = inflater.inflate(R.layout.drawer_adapter_category, parent, false);
            holder = new Holder();
            holder.category = (CustomTextViewFont) row.findViewById(R.id.categoryString);
            holder.iconImage = (ImageView) row.findViewById(R.id.drawerIcon);
            holder.underline = (ImageView) row.findViewById(R.id.underline);
            row.setTag(R.layout.drawer_adapter_category, holder);


            holder.category.setVisibility(View.VISIBLE);
            holder.iconImage.setVisibility(View.VISIBLE);
            holder.category.setText(R.string.settings);
            holder.iconImage.setImageResource(R.drawable.ic_settings_black_24dp);


            holder.underline.setVisibility(View.GONE);


        } else if (groupPosition == list.size() + 4) {

            row = inflater.inflate(R.layout.drawer_adapter_category, parent, false);
            holder = new Holder();
            holder.category = (CustomTextViewFont) row.findViewById(R.id.categoryString);
            holder.iconImage = (ImageView) row.findViewById(R.id.drawerIcon);
            holder.underline = (ImageView) row.findViewById(R.id.underline);
            row.setTag(R.layout.drawer_adapter_category, holder);


            holder.category.setVisibility(View.VISIBLE);
            holder.category.setText(R.string.profil);
            holder.iconImage.setVisibility(View.VISIBLE);
            holder.underline.setVisibility(View.GONE);
            holder.iconImage.setImageResource(R.drawable.usernamegray);


        } else if (groupPosition == list.size() + 5) {

            row = inflater.inflate(R.layout.drawer_adapter_category, parent, false);
            holder = new Holder();
            holder.category = (CustomTextViewFont) row.findViewById(R.id.categoryString);
            holder.iconImage = (ImageView) row.findViewById(R.id.drawerIcon);
            holder.underline = (ImageView) row.findViewById(R.id.underline);
            row.setTag(R.layout.drawer_adapter_category, holder);

            holder.category.setVisibility(View.VISIBLE);
            holder.category.setText(R.string.statistics);
            holder.iconImage.setVisibility(View.VISIBLE);
            holder.underline.setVisibility(View.GONE);
            holder.iconImage.setImageResource(R.drawable.ststc);


        } else if (groupPosition == list.size() + 6) {

            row = inflater.inflate(R.layout.drawer_adapter_category, parent, false);
            holder = new Holder();
            holder.category = (CustomTextViewFont) row.findViewById(R.id.categoryString);
            holder.iconImage = (ImageView) row.findViewById(R.id.drawerIcon);
            holder.underline = (ImageView) row.findViewById(R.id.underline);
            row.setTag(R.layout.drawer_adapter_category, holder);


            if (holder.category != null) {
                holder.category.setVisibility(View.VISIBLE);
                holder.category.setText(R.string.logout);

                holder.underline.setVisibility(View.GONE);
                holder.iconImage.setVisibility(View.VISIBLE);
                holder.iconImage.setImageResource(R.drawable.profil);
            }

        } else {


            row = inflater.inflate(R.layout.drawer_adapter_category, parent, false);
            holder = new Holder();
            holder.category = (CustomTextViewFont) row.findViewById(R.id.categoryString);
            holder.iconImage = (ImageView) row.findViewById(R.id.drawerIcon);
            holder.underline = (ImageView) row.findViewById(R.id.underline);
            row.setTag(R.layout.drawer_adapter_category, holder);


            holder.category.setText(list.get(groupPosition - 2).toString());

            holder.category.setVisibility(View.VISIBLE);
            holder.iconImage.setVisibility(View.INVISIBLE);
            holder.underline.setVisibility(View.GONE);

            if (list.get(groupPosition - 2).name.equalsIgnoreCase("Clothing")) {
                holder.iconImage.setVisibility(View.VISIBLE);
                holder.iconImage.setImageResource(R.drawable.menuicon);
                if (isExpanded) {
                    holder.category.setTextColor(context.getResources().getColor(R.color.pink_navigation));

                } else {
                    holder.category.setTextColor(Color.BLACK);
                }

            } else {
            }


        }

        return row;
    }


    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder = null;
        DataCategory dataCategory = null;
        if (row == null) {
            row = inflater.inflate(R.layout.navigation_adapter_child_layout, parent, false);
            holder = new Holder();
            holder.category = (CustomTextViewFont) row.findViewById(R.id.childString);
            row.setTag(holder);
        } else {
            holder = (Holder) row.getTag();
        }
        if (getChild(groupPosition, childPosition) != null) {
            dataCategory = (DataCategory) getChild(groupPosition, childPosition);
            holder.category.setText(dataCategory.toString());
        }

        return row;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public class Holder {
        private ImageView iconImage, image, underline;
        private CustomTextViewFont category, name, email;
    }


}
