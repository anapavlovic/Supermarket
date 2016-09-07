package com.example.cubesschool8.supermarket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by cubesschool8 on 9/7/16.
 */
public class StartActivity extends Activity {
private ImageView mLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        inicComp();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    Intent i = new Intent(getApplicationContext(), LogInActivity.class);
                    startActivity(i);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void inicComp(){

        mLogo= (ImageView) findViewById(R.id.logo);
    }
}
