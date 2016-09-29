package com.example.cubesschool8.supermarket.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.activity.CategoryItemsActivity;
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
        return list.size() + 5;
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder = null;

        if (groupPosition == 0) {
            if (row == null) {
                row = inflater.inflate(R.layout.drawer_adapter_layout, parent, false);
                holder = new Holder();
                holder.name = (CustomTextViewFont) row.findViewById(R.id.drawerName);
                holder.email = (CustomTextViewFont) row.findViewById(R.id.emailDrawer);
                holder.image = (ImageView) row.findViewById(R.id.drawerImage);
                row.setTag(holder);

            } else {
                holder = (Holder) row.getTag();
            }

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

        } else if (groupPosition == 1) {
            if (row == null) {
                row = inflater.inflate(R.layout.drawer_adapter_category, parent, false);
                holder = new Holder();
                holder.category = (CustomTextViewFont) row.findViewById(R.id.categoryString);
                holder.iconImage = (ImageView) row.findViewById(R.id.drawerIcon);
                holder.underline = (ImageView) row.findViewById(R.id.underline);
                row.setTag(holder);

            } else {
                holder = (Holder) row.getTag();
            }
            holder.iconImage.setVisibility(View.VISIBLE);

            holder.iconImage.setImageResource(R.drawable.home);
            holder.underline.setVisibility(View.VISIBLE);
            holder.category.setText("Home");
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context.getApplicationContext(), HomeActivity.class));
                }
            });


        } else if (groupPosition == list.size() + 1) {
            if (row == null) {
                row = inflater.inflate(R.layout.drawer_adapter_category, parent, false);
                holder = new Holder();
                holder.category = (CustomTextViewFont) row.findViewById(R.id.categoryString);
                holder.iconImage = (ImageView) row.findViewById(R.id.drawerIcon);
                holder.underline = (ImageView) row.findViewById(R.id.underline);
                row.setTag(holder);

            } else {
                holder = (Holder) row.getTag();
            }

            holder.category.setText(list.get(groupPosition - 2).toString());
            holder.underline.setVisibility(View.VISIBLE);

        } else if (groupPosition == list.size() + 2) {
            if (row == null) {
                row = inflater.inflate(R.layout.drawer_adapter_category, parent, false);
                holder = new Holder();
                holder.category = (CustomTextViewFont) row.findViewById(R.id.categoryString);
                holder.iconImage = (ImageView) row.findViewById(R.id.drawerIcon);
                holder.underline = (ImageView) row.findViewById(R.id.underline);
                row.setTag(holder);

            } else {
                holder = (Holder) row.getTag();
            }

            holder.category.setText("Podesavanja");

        } else if (groupPosition == list.size() + 3) {
            if (row == null) {
                row = inflater.inflate(R.layout.drawer_adapter_category, parent, false);
                holder = new Holder();
                holder.category = (CustomTextViewFont) row.findViewById(R.id.categoryString);
                holder.iconImage = (ImageView) row.findViewById(R.id.drawerIcon);
                holder.underline = (ImageView) row.findViewById(R.id.underline);
                row.setTag(holder);

            } else {
                holder = (Holder) row.getTag();
            }

            holder.category.setText("Profil");

        } else if (groupPosition == list.size() + 4) {
            if (row == null) {
                row = inflater.inflate(R.layout.drawer_adapter_category, parent, false);
                holder = new Holder();
                holder.category = (CustomTextViewFont) row.findViewById(R.id.categoryString);
                holder.iconImage = (ImageView) row.findViewById(R.id.drawerIcon);
                holder.underline = (ImageView) row.findViewById(R.id.underline);
                row.setTag(holder);

            } else {
                holder = (Holder) row.getTag();
            }

            holder.category.setText("Odjavi se");
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //odjavi se
                }
            });


        } else {

            if (row == null) {
                row = inflater.inflate(R.layout.drawer_adapter_category, parent, false);
                holder = new Holder();
                holder.category = (CustomTextViewFont) row.findViewById(R.id.categoryString);
                holder.iconImage = (ImageView) row.findViewById(R.id.drawerIcon);
                row.setTag(holder);

            } else {
                holder = (Holder) row.getTag();
            }
            holder.category.setText(list.get(groupPosition - 2).toString());


        }

        return row;
    }


    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder = null;
        DataCategory dataCategory = null;
        if (row == null) {
            row = inflater.inflate(R.layout.drawer_adapter_category, parent, false);
            holder = new Holder();
            holder.category = (CustomTextViewFont) row.findViewById(R.id.categoryString);
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
