package com.example.cubesschool8.supermarket.networking;

import android.app.DownloadManager;
import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by cubesschool8 on 9/12/16.
 */
public class GsonRequest<T> extends Request<T> {  //t je genericki tip i vraca tip razlicite klase
    private final int TIMEOUT = 10000;

    private Gson mGson = new Gson();
    private Class<T> mClass;
    private Response.Listener<T> mListener;
    private Response.ErrorListener mErrorListener;
    private Map<String, String> mParams;

    public GsonRequest(String url, Response.ErrorListener listener) {
        super(url, listener);
    }


    ////for GET method
    public GsonRequest(String url, int type, Class<T> myClass, Response.Listener<T> listener, Response.ErrorListener errorListener) {

        super(type, url, errorListener);

        this.mClass = myClass;
        this.mListener = listener;
        this.mErrorListener = errorListener;

        setRetryPolicy(new DefaultRetryPolicy(TIMEOUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)); //prvi par -posle koliko vremena da se javi error ako ne dobijemo response,
        // drugi par- koliko puta da ponovimo taj request dok ne dobijem error
    }


    //for POST method
    public GsonRequest(int type, String url, Class<T> myClass, Map<String, String> params, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(type, url, errorListener);
        this.mClass = myClass;
        this.mListener = listener;
        this.mErrorListener = errorListener;
        this.mParams = params;

        setRetryPolicy(new DefaultRetryPolicy(TIMEOUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));  // response je izvor podataka, a drugi parametar govori kojim karakter setom da tretira response
            return Response.success(mGson.fromJson(json, mClass), HttpHeaderParser.parseCacheHeaders(response)); // mClass, klasa u koju nam vraca string

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }

    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);

    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        return super.parseNetworkError(volleyError);
    }
}
