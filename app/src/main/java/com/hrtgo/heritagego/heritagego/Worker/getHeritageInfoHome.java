package com.hrtgo.heritagego.heritagego.Worker;

import android.os.AsyncTask;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hrtgo.heritagego.heritagego.Fragment.tabFamousHome;
import com.hrtgo.heritagego.heritagego.R;

public class getHeritageInfoHome extends AsyncTask<String, Void, String> {

    tabFamousHome tabFamousHome;

    public getHeritageInfoHome(com.hrtgo.heritagego.heritagego.Fragment.tabFamousHome tabFamousHome) {
        this.tabFamousHome = tabFamousHome;
    }

    @Override
    protected String doInBackground(String... strings) {

        RequestQueue queue = Volley.newRequestQueue(tabFamousHome.getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, strings[0], new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(tabFamousHome.getContext(), response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(tabFamousHome.getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }
        );

        queue.add(stringRequest);
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
