package com.hrtgo.heritagego.heritagego.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hrtgo.heritagego.heritagego.Activity.HeritageActivity;
import com.hrtgo.heritagego.heritagego.Adapter.rcvAdapterTabsHome;
import com.hrtgo.heritagego.heritagego.Interface.OnLoadMoreListener;

import com.hrtgo.heritagego.heritagego.Model.heritageInfoHomeModel;
import com.hrtgo.heritagego.heritagego.R;
import com.hrtgo.heritagego.heritagego.Worker.VolleySingleton;
import com.hrtgo.heritagego.heritagego.until.customize;


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
    HeritageActivity heritageActivity1 = (HeritageActivity) getActivity();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        listData = new ArrayList<>();
        super.onCreate(savedInstanceState);

        // khong hieu o day
        heritageActivity1.tabFamousHome = this; // khong hieu o day


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_famous, container, false);
        initView(view);
        callAPI(getURL(String.valueOf(currentPage)));
        return view;
    }


    // Create inStance view
    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_home_tab_famous);
        setHomeRecyclerView();
    }


    @Override
    public void onStart() {
        super.onStart();


    }

    // set adapter for recyclerView at Tab Famous
    private void setHomeRecyclerView() {
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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        callAPI(getURL(String.valueOf(currentPage)));
                    }
                }, 2000);

                Log.e("ListData", String.valueOf(listData.size()));
            }

        });
    }

    // call API get DATA
    private void callAPI(String url) {
        //startOverLay();
        Log.e("currentPage", String.valueOf(currentPage));
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
                if (listData.size() != 0) {
                    if (listData.get(listData.size() - 1) == null) {
                        listData.remove(listData.size() - 1);
                        adapter.locationDatas = listData;
                        adapter.notifyItemRemoved(adapter.locationDatas.size() - 1);
                    }
                }
                heritageActivity1.mNetworkNotification.setVisibility(View.VISIBLE);
            }
        });

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 3, DefaultRetryPolicy.DEFAULT_TIMEOUT_MS));
        VolleySingleton.getInStance(this.getContext()).getRequestQueue().add(jsonRequest);
    }

    public void parseJson(String result) {
        heritageActivity1.mNetworkNotification.setVisibility(View.GONE);
        try {
            JSONObject root = new JSONObject(result);

            JSONArray pdataArray = root.getJSONArray("pdata");

            if (listData.size() != 0) {
                if (listData.get(listData.size() - 1) == null) {
                    listData.remove(listData.size() - 1);
                    adapter.locationDatas = listData;
                    adapter.notifyItemRemoved(adapter.locationDatas.size() - 1);
                }
            } else if (pdataArray.length() == 0) {
                return;
            }

            for (int i = 0; i < pdataArray.length(); i++) {
                JSONObject location = pdataArray.getJSONObject(i);

                listData.add(new heritageInfoHomeModel(location.getInt("ID")
                        , location.getString("Name")
                        , location.getInt("Liked")
                        , location.getInt("Viewed")
                        , location.getString("ImagePath")));
            }

            if (listData.size() > 0) {
                adapter.locationDatas = listData;
                onDataChanged();
                Log.e("listHome", String.valueOf(listData.get(listData.size() - 1).getLocationName()));
                Log.e("listHomeA", String.valueOf(adapter.locationDatas.get(adapter.locationDatas.size() - 1).getLocationName()));
                Log.e("listHomeSize", String.valueOf(listData.size()));
                Log.e("listHomeSizeA", String.valueOf(adapter.locationDatas.size()));
            }

        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
    }

    private void onDataChanged() {
        adapter.notifyDataSetChanged();
        adapter.loaded();
    }

    private String getURL(String currentPage) {
        String url = API.HOME_LIKE() + currentPage.trim();
        return url;
    }

    public void startOverLay() {
        pBar = new ProgressDialog(getActivity());
        customize.startloading(pBar);
    }


    public void stopOverLay() {
        pBar.dismiss();
    }

    public void getConnect(boolean isConnected) {
        if (isConnected) {
            callAPI(getURL(String.valueOf(currentPage)));
        } else {
            heritageActivity1.mNetworkNotification.setVisibility(View.VISIBLE);
        }
    }

}
