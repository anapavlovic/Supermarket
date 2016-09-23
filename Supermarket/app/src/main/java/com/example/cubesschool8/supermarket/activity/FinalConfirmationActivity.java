package com.example.cubesschool8.supermarket.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.cubesschool8.supermarket.R;
import com.example.cubesschool8.supermarket.adapter.FinalConfirmationAdapter;
import com.example.cubesschool8.supermarket.customComponents.CustomEditTextFont;
import com.example.cubesschool8.supermarket.customComponents.CustomTextViewFont;
import com.example.cubesschool8.supermarket.data.DataContainer;
import com.example.cubesschool8.supermarket.networking.DataLoader;
import com.example.cubesschool8.supermarket.tool.BusProvider;
import com.example.cubesschool8.supermarket.tool.MessageObject;

public class FinalConfirmationActivity extends ActivityWithMessage {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FinalConfirmationAdapter mAdapter;
    private Button buy;
    private RelativeLayout relativeProgress;
    private ProgressBar progressBar;
    private CustomTextViewFont mTotal;

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
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                DataContainer.basketList.clear();
                                finish();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();

                }
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

    }


}
