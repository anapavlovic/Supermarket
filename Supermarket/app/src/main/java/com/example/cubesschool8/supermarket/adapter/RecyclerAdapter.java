package com.example.cubesschool8.supermarket.adapter;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.cubesschool8.supermarket.R;

import com.example.cubesschool8.supermarket.activity.ProductItemActivity;

import com.example.cubesschool8.supermarket.constant.Constant;
import com.example.cubesschool8.supermarket.customComponents.CustomTextViewFont;
import com.example.cubesschool8.supermarket.data.DataContainer;
import com.example.cubesschool8.supermarket.data.DataLogIn;
import com.example.cubesschool8.supermarket.data.DataProducts;

import com.example.cubesschool8.supermarket.data.response.ResponseAddWishlist;
import com.example.cubesschool8.supermarket.data.response.ResponseWishlist;
import com.example.cubesschool8.supermarket.networking.DataLoader;
import com.example.cubesschool8.supermarket.networking.GsonRequest;
import com.example.cubesschool8.supermarket.tool.BusProvider;
import com.example.cubesschool8.supermarket.tool.MessageObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ana on 9/16/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder> {

    private Context mContex;
    private int resource;
    private LayoutInflater inflater;
    private ArrayList<DataProducts> mList;
    public boolean mComponentEnabled;
    private GsonRequest<ResponseAddWishlist> mRequestAddWishlist;
    private final String REQUEST_TAG = "Start_activity";


    public RecyclerAdapter(Context context, ArrayList<DataProducts> object) {
        this.mContex = context;
        this.mList = object;
        inflater = LayoutInflater.from(mContex);
        mComponentEnabled = true;

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

        holder.yellowBasket.setEnabled(mComponentEnabled);


///check wishList
        if (DataContainer.login != null) {
            if (DataContainer.login.wish_list != null && !DataContainer.login.wish_list.isEmpty()) {
                for (int j = 0; j < DataContainer.login.wish_list.size(); j++) {

                    if (mList.get(position).id.equalsIgnoreCase(DataContainer.login.wish_list.get(j))) {
                        holder.star.setTag(R.drawable.yellowstar);
                        holder.star.setImageResource(R.drawable.yellowstar);
                        break;
                    } else {
                        holder.star.setTag(R.drawable.emptystar);
                        holder.star.setImageResource(R.drawable.emptystar);

                    }

                }
            } else {
                holder.star.setTag(R.drawable.emptystar);
                holder.star.setImageResource(R.drawable.emptystar);
            }
        } else {
            holder.star.setTag(R.drawable.emptystar);
            holder.star.setImageResource(R.drawable.emptystar);
        }


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public DataProducts getItem(int position) {
        return mList.get(position);
    }


    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView productImage, yellowBasket, star;
        private CustomTextViewFont productTitle;
        private CustomTextViewFont productPrice;


        public Holder(View itemView) {
            super(itemView);

            productImage = (ImageView) itemView.findViewById(R.id.adapterImageView);
            productPrice = (CustomTextViewFont) itemView.findViewById(R.id.productPriceTv);
            productTitle = (CustomTextViewFont) itemView.findViewById(R.id.productTitleTv);
            yellowBasket = (ImageView) itemView.findViewById(R.id.yellowBasket);
            star = (ImageView) itemView.findViewById(R.id.star);
            productImage.setOnClickListener(this);
            yellowBasket.setOnClickListener(this);
            star.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {


            if (v == productImage) {
                Intent i = new Intent(v.getContext(), ProductItemActivity.class);
                i.putExtra("position", getAdapterPosition());
                mContex.startActivity(i);

            } else if (v == yellowBasket) {

                try {
                    DataProducts data = new DataProducts();
                    data = (DataProducts) mList.get(getAdapterPosition()).clone();


                    //  int position = getAdapterPosition();


                    if (mList.get(getAdapterPosition()).count > 0) {
                        for (int i = 0; i < DataContainer.basketList.size(); i++) {
                            if (mList.get(getAdapterPosition()).id.equalsIgnoreCase(DataContainer.basketList.get(i).id)) {
                                mList.get(getAdapterPosition()).count++;
                                DataContainer.basketList.get(i).count++;

                                BusProvider.getInstance().post(new MessageObject(R.string.dodato_korpa, 3000, MessageObject.MESSAGE_SUCCESS));


                                mComponentEnabled = false;

                                itemView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mComponentEnabled = true;
                                        notifyDataSetChanged();
                                    }
                                }, 5000);

                            } else {

                            }
                        }

                    } else {
                        mList.get(getAdapterPosition()).count++;
                        data.count++;
                        DataContainer.basketList.add(data);

                        BusProvider.getInstance().post(new MessageObject(R.string.dodato_korpa, 3000, MessageObject.MESSAGE_SUCCESS));


                        mComponentEnabled = false;

                        itemView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mComponentEnabled = true;
                                notifyDataSetChanged();

                            }
                        }, 5000);

                    }

                    notifyDataSetChanged();

                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            } else if (v == star) {

                Object tag = star.getTag();
                if (DataContainer.login != null) {

                    if (tag.equals(R.drawable.emptystar)) {

////ADD
                        mRequestAddWishlist = new GsonRequest<ResponseAddWishlist>(Constant.URL_FAVOURITES_ADD, Request.Method.POST, ResponseAddWishlist.class, new Response.Listener<ResponseAddWishlist>() {
                            @Override
                            public void onResponse(ResponseAddWishlist response) {
                                DataContainer.addWishlist = response.data.results;
                                DataContainer.login.wish_list.add(mList.get(getAdapterPosition()).id);
                                star.setImageResource(R.drawable.yellowstar);


                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("Error", error.toString());
                                BusProvider.getInstance().post(new MessageObject(error.toString(), 3000, MessageObject.MESSAGE_ERROR));
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();

                                params.put("token", DataContainer.TOKEN);
                                params.put("user_id", DataContainer.login.id);
                                params.put("item_id", mList.get(getAdapterPosition()).id);
                                return params;
                            }
                        };
                        DataLoader.addRequest(mContex, mRequestAddWishlist, REQUEST_TAG);


                    } else if (tag.equals(R.drawable.yellowstar)) {
//// REMOVE WISH
                        mRequestAddWishlist = new GsonRequest<ResponseAddWishlist>(Constant.URL_FAVOURITES_ADD, Request.Method.POST, ResponseAddWishlist.class, new Response.Listener<ResponseAddWishlist>() {
                            @Override
                            public void onResponse(ResponseAddWishlist response) {
                                DataContainer.addWishlist = response.data.results;
                                DataContainer.login.wish_list.remove(mList.get(getAdapterPosition()).id);
                                star.setImageResource(R.drawable.emptystar);

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("Error", error.toString());
                                BusProvider.getInstance().post(new MessageObject(error.toString(), 3000, MessageObject.MESSAGE_ERROR));
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();

                                params.put("token", DataContainer.TOKEN);
                                params.put("user_id", DataContainer.login.id);
                                params.put("item_id", mList.get(getAdapterPosition()).id);
                                return params;
                            }
                        };

                        DataLoader.addRequest(mContex, mRequestAddWishlist, REQUEST_TAG);

                    }
                } else {
                    Toast.makeText(mContex, "Ulogujte se", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }


}


