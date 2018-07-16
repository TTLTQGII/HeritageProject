package com.hrtgo.heritagego.heritagego.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hrtgo.heritagego.heritagego.API.API;
import com.hrtgo.heritagego.heritagego.Activity.HeritageActivity;
import com.hrtgo.heritagego.heritagego.Adapter.rcvAdapterTabsHome;
import com.hrtgo.heritagego.heritagego.Interface.OnLoadMoreListener;
import com.hrtgo.heritagego.heritagego.Model.heritageInfoHomeModel;
import com.hrtgo.heritagego.heritagego.R;
import com.hrtgo.heritagego.heritagego.Worker.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class tabMostViewedHome extends Fragment {
    RecyclerView recyclerView;
    ArrayList<heritageInfoHomeModel> listData;
    rcvAdapterTabsHome adapter;
    int currentPage = 1;
    HeritageActivity heritageActivity3 = (HeritageActivity) getActivity();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listData = new ArrayList<>();

        heritageActivity3.tabMostViewedHome = this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_most_viewed, container, false);

        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        callAPI(getURL("1"));
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_home_tab_most_viewed);
        setMostViewRecyclerView();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    // set adapter for recyclerView at Tab MostViewed
    public void setMostViewRecyclerView() {
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

            }
        });
    }

    // call API get DATA
    private void callAPI(String url) {
        //startOverLay();
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // control error in here
                if (listData.size() != 0) {
                    if (listData.get(listData.size() - 1) == null) {
                        listData.remove(listData.size() - 1);
                        adapter.locationDatas = listData;
                        adapter.notifyItemRemoved(adapter.locationDatas.size() - 1);
                    }
                }
                heritageActivity3.mNetworkNotification.setVisibility(View.VISIBLE);
            }
        });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 3, DefaultRetryPolicy.DEFAULT_TIMEOUT_MS));
        VolleySingleton.getInStance(this.getContext()).getRequestQueue().add(jsonRequest);
    }

    public void parseJson(String result) {
        heritageActivity3.mNetworkNotification.setVisibility(View.GONE);

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
            }

        } catch (JSONException e) {
            //Log.e(TAG,e.toString());
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

    public void getConnect(boolean isConnected) {
        if (isConnected) {
            callAPI(getURL(String.valueOf(currentPage)));
        } else {
            heritageActivity3.mNetworkNotification.setVisibility(View.VISIBLE);
        }
    }
}
