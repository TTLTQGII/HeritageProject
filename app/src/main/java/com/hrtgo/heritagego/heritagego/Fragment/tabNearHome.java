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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hrtgo.heritagego.heritagego.Adapter.rcvAdapterTabsHome;
import com.hrtgo.heritagego.heritagego.Interface.Json;
import com.hrtgo.heritagego.heritagego.Interface.OnLoadMoreListener;
import com.hrtgo.heritagego.heritagego.Model.heritageInfoHomeModel;
import com.hrtgo.heritagego.heritagego.R;
import com.hrtgo.heritagego.heritagego.Worker.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class tabNearHome extends Fragment{

    RecyclerView recyclerView;
    ArrayList<heritageInfoHomeModel> listData;
    rcvAdapterTabsHome adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listData = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_tab_near, container, false);

        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callAPI(getURL("1"));

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    // create instance view
    private void initView(View view){
        recyclerView = view.findViewById(R.id.recycler_view_home_tab_near);
        setNearRecyclerView();
    }

    // set adapter for recyclerView at Tab Near
    public void setNearRecyclerView(){
        recyclerView.setHasFixedSize(false);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new rcvAdapterTabsHome(recyclerView, listData, this.getContext());
        recyclerView.setAdapter(adapter);

        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                listData.add(null);
                adapter.locationDatas = listData;
                adapter.notifyItemInserted(adapter.locationDatas.size() - 1);
                callAPI(getURL("2"));
                Log.e("ListData", String.valueOf(listData.size()));
            }
        });
    }

    // call API get DATA
    private void callAPI(String url){
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // control error in here
                Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getInStance(this.getContext()).getRequestQueue().add(jsonRequest);
    }

    public void parseJson(String result){

        if(listData.size() != 0){
            listData.remove(listData.size()-1);
            adapter.locationDatas = listData;
            adapter.notifyItemRemoved(adapter.locationDatas.size() - 1);
        }

        try {
            JSONObject root = new JSONObject(result);

            JSONArray pdataArray = root.getJSONArray("pdata");

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
        }
    }

    private void onDataChanged(){
        adapter.notifyDataSetChanged();
        adapter.loaded();
    }

    private String getURL(String currentPage){
        String url = getActivity().getResources().getString(R.string.request_heritage_info_home_like) + currentPage.trim();
        return url;
    }
}
