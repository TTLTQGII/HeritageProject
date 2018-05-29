package com.hrtgo.heritagego.heritagego.Worker;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static final String Tag = "VolleySingleton";
    private RequestQueue requestQueue;
    private static VolleySingleton inStance;

    private VolleySingleton(Context context){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
    }

    public static synchronized VolleySingleton getInStance(Context context){
        if (inStance == null)
            inStance = new VolleySingleton(context);
        return inStance;
    }

    public RequestQueue getRequestQueue(){
        return requestQueue;
    }
}
