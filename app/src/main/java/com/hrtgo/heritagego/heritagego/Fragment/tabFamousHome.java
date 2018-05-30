package com.hrtgo.heritagego.heritagego.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hrtgo.heritagego.heritagego.Adapter.rcvAdapterTabsHome;
import com.hrtgo.heritagego.heritagego.Model.heritageInfoHomeModel;
import com.hrtgo.heritagego.heritagego.R;
import com.hrtgo.heritagego.heritagego.Worker.VolleySingleton;
import com.hrtgo.heritagego.heritagego.Worker.parseJsonFamousTab;

import java.util.ArrayList;

public class tabFamousHome extends Fragment {

    RecyclerView recyclerView;
    String result = "";

    private static final String TAG = "Location Json";

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_tab_famous, container, false);
        initView(view);
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callAPI("1");
    }

    // Create inStance view
    private void initView(View view){
        recyclerView = view.findViewById(R.id.recycler_view_home_tab_famous);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    // set adapter for recyclerView at Tab Famous
    public void setHomeRecyclerView(ArrayList<heritageInfoHomeModel> locationFamous){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        final rcvAdapterTabsHome adapter = new rcvAdapterTabsHome(locationFamous, this.getContext());
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    // Connect to API get json and parse json in Asyntask
    private void getListData(String url){
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                parseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // control error in here
            }
        });

        VolleySingleton.getInStance(this.getContext()).getRequestQueue().add(jsonRequest);
    }

    private void parseJson(String json){
        new parseJsonFamousTab(this).execute(json);
    }

    private void callAPI(String currentPage){
        String url = getActivity().getResources().getString(R.string.request_heritage_info_home_view) + currentPage;
        getListData(url);
    }
}
