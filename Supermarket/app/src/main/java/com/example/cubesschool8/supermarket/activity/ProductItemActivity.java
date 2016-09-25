package com.example.cubesschool8.supermarket.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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
import com.example.cubesschool8.supermarket.data.response.ResponseSingleProduct;
import com.example.cubesschool8.supermarket.networking.DataLoader;
import com.example.cubesschool8.supermarket.networking.GsonRequest;
import com.example.cubesschool8.supermarket.tool.BusProvider;
import com.example.cubesschool8.supermarket.tool.MessageObject;

/**
 * Created by cubesschool8 on 9/19/16.
 */
public class ProductItemActivity extends ActivityWithMessage {
    private final String REQUEST_TAG = "Start_activity";
    private ImageView mBack, mProductImage;
    private CustomTextViewFont mProductName, mProductSize, mProductColor, mProductMaterial, mProductPrice, mSupplier, mGender;

    private Button mAddProduct;
    private DataProducts data = new DataProducts();
    private GsonRequest<ResponseSingleProduct> mRequestSingleProduct;
    private BasketActivity mBasketActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_item);

        inicComp();
        addListener();
        getData();
        singleProductDataRequest();


    }

    public void getData() {
        data = DataContainer.products.get(getIntent().getIntExtra("position", 0));
        mProductName.setText(data.name);
        mProductPrice.setText(data.first_price);
        mProductSize.setText("Veličine: " + data.sizes);
        mProductColor.setText("Boja: " + data.color);
        Glide.with(getApplicationContext()).load(data.thumb330).into(mProductImage);
    }

    public void inicComp() {
        mBack = (ImageView) findViewById(R.id.productBack);
        mProductName = (CustomTextViewFont) findViewById(R.id.etProductname);
        mProductImage = (ImageView) findViewById(R.id.productImage);
        mProductSize = (CustomTextViewFont) findViewById(R.id.etProductSize);
        mProductColor = (CustomTextViewFont) findViewById(R.id.etProductColor);
        mProductMaterial = (CustomTextViewFont) findViewById(R.id.etProductMaterial);
        mProductPrice = (CustomTextViewFont) findViewById(R.id.productPrice);
        mAddProduct = (Button) findViewById(R.id.AddProductButt);
        mSupplier = (CustomTextViewFont) findViewById(R.id.etProductBrand);
        mGender = (CustomTextViewFont) findViewById(R.id.etProductGender);
        mBasketActivity = new BasketActivity();


    }

    public void addListener() {
        mAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataProducts dataClone = new DataProducts();
                try {
                    dataClone = (DataProducts) data.clone();

                    if (data.count > 0) {
                        for (int i = 0; i < DataContainer.basketList.size(); i++) {
                            if (data.id.equalsIgnoreCase(DataContainer.basketList.get(i).id)) {
                                DataContainer.basketList.get(i).count++;
                                data.count++;
                            }
                        }

                    } else {
                        DataContainer.basketList.add(dataClone);
                        data.count++;
                        dataClone.count++;
                    }


                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }

                BusProvider.getInstance().post(new MessageObject(R.string.dodato_korpa, 3000, MessageObject.MESSAGE_SUCCESS));
                mAddProduct.setEnabled(false);

                mAddProduct.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAddProduct.setEnabled(true);
                    }
                }, 5000);


            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void singleProductDataRequest() {
        mRequestSingleProduct = new GsonRequest<ResponseSingleProduct>(Constant.SINGLE_PRODUCT_URL + "?token=" + DataContainer.TOKEN + "&id=" + data.id, Request.Method.GET, ResponseSingleProduct.class,
                new Response.Listener<ResponseSingleProduct>() {
                    @Override
                    public void onResponse(ResponseSingleProduct response) {
                        DataContainer.singleProductList = response.data.results;
                        mProductMaterial.setText("Materijal: " + DataContainer.singleProductList.material);
                        mSupplier.setText("Brand: " + DataContainer.singleProductList.supplier);
                        mGender.setText("Pol: " + DataContainer.singleProductList.gender);
                        if (mProductSize == null) {
                            mProductSize.setText("Veličine:" + DataContainer.singleProductList.sizes);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", error.toString());
            }
        });
        DataLoader.addRequest(getApplicationContext(), mRequestSingleProduct, REQUEST_TAG);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataLoader.cancelRequest(getApplicationContext(), REQUEST_TAG);
    }
}
