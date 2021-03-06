package com.hrtgo.heritagego.heritagego.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hrtgo.heritagego.heritagego.API.API;
import com.hrtgo.heritagego.heritagego.Adapter.rcvAdapterTabsHome;
import com.hrtgo.heritagego.heritagego.Interface.OnLoadMoreListener;
import com.hrtgo.heritagego.heritagego.Model.heritageInfoHomeModel;
import com.hrtgo.heritagego.heritagego.R;
import com.hrtgo.heritagego.heritagego.Worker.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class navSearchfrag extends Fragment {

    EditText edtSearch;
    RecyclerView recyclerView;
    ArrayList<heritageInfoHomeModel> listData;
    rcvAdapterTabsHome adapter;

    int currentPage = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listData = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nav_bottom_search_fragment, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        recyclerView = view.findViewById(R.id.rcv_search);
        setSearchRecyclerView();
        editextSearchEvent(view);
    }

    private void editextSearchEvent(View view){
        edtSearch = view.findViewById(R.id.txt_search);
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    listData.clear();
                    adapter.locationDatas = listData;
                    onDataChanged();
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);
                    callAPI(getURL("1", edtSearch.getText().toString()));
                    return true;
                }

                return false;
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }

//    private void setFocusEditSearch(){
//        edtSearch.setFocusableInTouchMode(true);
//        edtSearch.requestFocus();
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//        ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(edtSearch, 0);
//    }

    private void setSearchRecyclerView(){
        recyclerView.setHasFixedSize(false);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new rcvAdapterTabsHome(recyclerView, listData, this.getContext());
        recyclerView.setAdapter(adapter);

        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                currentPage++;
                //if(currentPage < 5) {
                listData.add(null);
                adapter.locationDatas = listData;
                adapter.notifyItemInserted(adapter.locationDatas.size() - 1);
                //}
                callAPI(getURL(String.valueOf(currentPage), edtSearch.getText().toString()));
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
                parseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // control error in here
                //stopOverLay();
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
        }
    }

    private void onDataChanged(){
        adapter.notifyDataSetChanged();
        adapter.loaded();
    }

    private String getURL(String currentPage, String txtSearch){
        String search = txtSearch.trim();

        try {
            search = URLEncoder.encode(txtSearch.trim(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = API.SEARCH() + search + "/" + currentPage ;
        Log.e("urlSearch", url);
        return url;
    }
}
