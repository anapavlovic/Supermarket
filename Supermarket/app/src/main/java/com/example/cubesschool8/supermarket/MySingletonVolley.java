package com.example.cubesschool8.supermarket;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Ana on 9/9/2016.
 */
public class MySingletonVolley {

    private static MySingletonVolley instance;
    private Context mContext;
    private RequestQueue mRequestQueue;

    public MySingletonVolley(Context context) {
        this.mContext = context;
        mRequestQueue = getRequestQueue();

    }

    public static synchronized MySingletonVolley getInstance(Context context) {
        if (instance == null) {
            instance = new MySingletonVolley(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {

        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
