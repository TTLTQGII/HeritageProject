package com.hrtgo.heritagego.heritagego.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hrtgo.heritagego.heritagego.Adapter.rcvAdapterTabsHome;
import com.hrtgo.heritagego.heritagego.Interface.OnLoadMoreListener;
import com.hrtgo.heritagego.heritagego.Model.heritageInfoHomeModel;
import com.hrtgo.heritagego.heritagego.R;
import com.hrtgo.heritagego.heritagego.Worker.VolleySingleton;
import com.hrtgo.heritagego.heritagego.untill.customize;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import com.hrtgo.heritagego.heritagego.API.API;

public class tabFamousHome extends Fragment {

    RecyclerView recyclerView;
    ProgressDialog pBar;
    ArrayList<heritageInfoHomeModel> listData;
    int currentPage = 1;
    rcvAdapterTabsHome adapter;


    private static final String TAG = "Location Home";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        listData = new ArrayList<>();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_tab_famous, container, false);
        initView(view);
        callAPI(getURL(String.valueOf(currentPage)));
        return  view;
    }


    // Create inStance view
    private void initView(View view){
        recyclerView = view.findViewById(R.id.recycler_view_home_tab_famous);
        setHomeRecyclerView();
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    // set adapter for recyclerView at Tab Famous
    private void setHomeRecyclerView(){
        recyclerView.setHasFixedSize(false);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new rcvAdapterTabsHome(recyclerView, listData, this.getContext());
        recyclerView.setAdapter(adapter);

        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                currentPage++;
                listData.add(null);
                adapter.locationDatas = listData;
                adapter.notifyItemInserted(adapter.locationDatas.size() - 1);
                callAPI(getURL(String.valueOf(currentPage)));

                Log.e("ListData", String.valueOf(listData.size()));
            }

        });
    }

    // call API get DATA
    private void callAPI(String url){
        //startOverLay();
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("StringResponse", response);
                parseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // control error in here
                //stopOverLay();
                if(listData.size() != 0){
                    listData.remove(listData.size()-1);
                    adapter.locationDatas = listData;
                    adapter.notifyItemRemoved(adapter.locationDatas.size() - 1);
                }
                Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getInStance(this.getContext()).getRequestQueue().add(jsonRequest);
    }

    public void parseJson(String result){
        try {
            JSONObject root = new JSONObject(result);

            JSONArray pdataArray = root.getJSONArray("pdata");

            if(listData.size() != 0){
                listData.remove(listData.size()-1);
                adapter.locationDatas = listData;
                adapter.notifyItemRemoved(adapter.locationDatas.size() - 1);
            }
            else if(pdataArray.length() == 0){
                return;
            }

            for (int i = 0; i < pdataArray.length(); i++){
                JSONObject location = pdataArray.getJSONObject(i);

                listData.add(new heritageInfoHomeModel(location.getInt("ID")
                        ,location.getString("Name")
                        ,location.getInt("Liked")
                        ,location.getInt("Viewed")
                        ,location.getString("ImagePath")));
            }

            if(listData.size() > 0){
                adapter.locationDatas = listData;
                onDataChanged();
            }

        } catch (JSONException e) {
            Log.e(TAG,e.toString());
        }
    }

    private void onDataChanged(){
        adapter.notifyDataSetChanged();
        adapter.loaded();
    }

    private String getURL(String currentPage){
        String url = API.HOME_LIKE() + currentPage.trim();
        return url;
    }

    public void startOverLay(){
        pBar = new ProgressDialog(getActivity());
        customize.startloading(pBar);
    }


    public void stopOverLay(){
        pBar.dismiss();
    }
}
