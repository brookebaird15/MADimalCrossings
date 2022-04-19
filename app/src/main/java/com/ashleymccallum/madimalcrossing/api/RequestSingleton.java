package com.ashleymccallum.madimalcrossing.api;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestSingleton {

    public static RequestSingleton instance;
    private RequestQueue requestQueue;
    private static Context context;

    private RequestSingleton(Context context) {
        this.context = context;
    }

    public static RequestSingleton getInstance(Context context) {
        if(instance == null) {
            instance = new RequestSingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }
}
