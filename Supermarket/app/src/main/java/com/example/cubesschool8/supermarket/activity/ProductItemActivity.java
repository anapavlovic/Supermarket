package com.example.cubesschool8.supermarket.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.customComponents.CustomEditTextFont;
import com.example.cubesschool8.supermarket.customComponents.CustomTextViewFont;
import com.example.cubesschool8.supermarket.data.DataContainer;
import com.example.cubesschool8.supermarket.data.DataProducts;

/**
 * Created by cubesschool8 on 9/19/16.
 */
public class ProductItemActivity extends Activity {

    private ImageView mBack, mProductImage;
    private CustomTextViewFont mProductName, mProductSize, mProductColor, mProductMaterial, mProductPrice;

    private Button mAddProduct;
    private DataProducts data = new DataProducts();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_item);
        inicComp();

        data = DataContainer.products.get(getIntent().getIntExtra("position", 0));
        mProductName.setText(data.name);
        mProductPrice.setText(data.first_price);
        Glide.with(getApplicationContext()).load(data.thumb330).into(mProductImage);

    }




    public void inicComp(){
        mBack= (ImageView) findViewById(R.id.productBack);
        mProductName=(CustomTextViewFont) findViewById(R.id.etProductname);
        mProductImage = (ImageView) findViewById(R.id.productImage);
        mProductSize= (CustomTextViewFont) findViewById(R.id.etProductSize);
        mProductColor= (CustomTextViewFont) findViewById(R.id.etProductColor);
        mProductMaterial= (CustomTextViewFont) findViewById(R.id.etProductMaterial);
        mProductPrice= (CustomTextViewFont) findViewById(R.id.productPrice);
        mAddProduct= (Button) findViewById(R.id.AddProductButt);



    }
}
