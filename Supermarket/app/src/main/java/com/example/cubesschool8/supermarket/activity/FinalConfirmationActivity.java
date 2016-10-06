package com.example.cubesschool8.supermarket.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.adapter.FinalConfirmationAdapter;
import com.example.cubesschool8.supermarket.constant.Constant;
import com.example.cubesschool8.supermarket.customComponents.CustomEditTextFont;
import com.example.cubesschool8.supermarket.customComponents.CustomTextViewFont;
import com.example.cubesschool8.supermarket.data.DataContainer;
import com.example.cubesschool8.supermarket.data.DataLogIn;
import com.example.cubesschool8.supermarket.data.DataProducts;
import com.example.cubesschool8.supermarket.data.response.ResponseLogIn;
import com.example.cubesschool8.supermarket.data.response.ResponseOrder;
import com.example.cubesschool8.supermarket.networking.DataLoader;
import com.example.cubesschool8.supermarket.networking.GsonRequest;
import com.example.cubesschool8.supermarket.tool.BusProvider;
import com.example.cubesschool8.supermarket.tool.MessageObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FinalConfirmationActivity extends ActivityWithMessage {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FinalConfirmationAdapter mAdapter;
    private Button buy;
    private RelativeLayout relativeProgress;
    private ProgressBar progressBar;
    private CustomTextViewFont mTotal;
    private GsonRequest<ResponseOrder> mRequestOrder;
    private Map<String, String> params;
    private String articles;
    private final String REQUEST_TAG = "Start_activity";
    private ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_confirmation);
        inicComp();
        addListener();

        String total = getIntent().getStringExtra("total");

        mTotal.setText("Ukupno " + total);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new FinalConfirmationAdapter(getApplicationContext(), DataContainer.basketList);
        recyclerView.setAdapter(mAdapter);

    }

    public void addListener() {
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DataContainer.basketList.isEmpty()) {
                    BusProvider.getInstance().post(new MessageObject(R.string.praznakorpa, 3000, MessageObject.MESSAGE_INFO));
                } else {
                    relativeProgress.setVisibility(View.VISIBLE);

                    mRequestOrder = new GsonRequest<ResponseOrder>(Constant.SEND_ORDER_URL, Request.Method.POST, ResponseOrder.class, new Response.Listener<ResponseOrder>() {
                        @Override
                        public void onResponse(ResponseOrder response) {
                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                            DataContainer.orderResponse = response.data.results;

                            for (int i = 0; i < DataContainer.basketList.size(); i++) {
                                DataContainer.basketList.get(i).count = 0;
                                for (int j = 0; j < DataContainer.products.size(); j++) {
                                    if (DataContainer.products.get(j).id.equalsIgnoreCase(DataContainer.basketList.get(i).id)) {
                                        DataContainer.products.get(j).count = 0;
                                    } else {
                                    }

                                }
                                DataContainer.basketList.clear();
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                DataContainer.addressChange.address.equalsIgnoreCase("");
                                finish();

                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("error", error.toString());
                            BusProvider.getInstance().post(new MessageObject(R.string.server_error, 3000, MessageObject.MESSAGE_ERROR));
                            progressBar.setVisibility(View.GONE);
                            relativeProgress.setVisibility(View.GONE);

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            params = new HashMap<String, String>();
                            params.put("token", DataContainer.TOKEN);
                            params.put("login_token", DataContainer.LOGIN_TOKEN);
                            params.put("buyer_id", DataContainer.login.id);
                            params.put("payment_type", DataContainer.login.payment_type);
                            params.put("articles", setArticlesString());

                            if (DataContainer.addressChange.address.equalsIgnoreCase(DataContainer.login.address) && DataContainer.addressChange.street_number.equalsIgnoreCase(DataContainer.login.street_number) &&
                                    DataContainer.addressChange.apartment.equalsIgnoreCase(DataContainer.login.apartment) && DataContainer.addressChange.floor.equalsIgnoreCase(DataContainer.login.floor) &&
                                    DataContainer.addressChange.entrance.equalsIgnoreCase(DataContainer.login.entrance) && DataContainer.addressChange.city.equalsIgnoreCase(DataContainer.login.city) &&
                                    DataContainer.addressChange.postal_code.equalsIgnoreCase(DataContainer.login.postal_code)) {
                                params.put("new_address", "0");
                            } else {
                                params.put("new_address", "1");
                                params.put("street", DataContainer.addressChange.address);
                                params.put("number", DataContainer.addressChange.street_number);
                                params.put("appartement", DataContainer.addressChange.apartment);
                                params.put("floor", DataContainer.addressChange.floor);
                                params.put("entrance", DataContainer.addressChange.entrance);
                                params.put("city", DataContainer.addressChange.city);
                                params.put("postal_code", DataContainer.addressChange.postal_code);
                            }


                            return params;
                        }
                    };
                    DataLoader.addRequest(getApplicationContext(), mRequestOrder, REQUEST_TAG);


                }
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    public void inicComp() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewConf);
        buy = (Button) findViewById(R.id.finalConfButton);
        relativeProgress = (RelativeLayout) findViewById(R.id.relativeProgressConf);
        progressBar = (ProgressBar) findViewById(R.id.progressConf);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#feea00"), PorterDuff.Mode.SRC_IN);
        mTotal = (CustomTextViewFont) findViewById(R.id.totalSumConf);
        mBack = (ImageView) findViewById(R.id.confBack);


    }

    private String setArticlesString() {
        for (int i = 0; i < DataContainer.basketList.size(); i++) {
            articles += (DataContainer.basketList.get(i).id + "," + DataContainer.basketList.get(i).count + ", 0 |");

        }

        return articles;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DataLoader.cancelRequest(getApplicationContext(), REQUEST_TAG);
    }
}
