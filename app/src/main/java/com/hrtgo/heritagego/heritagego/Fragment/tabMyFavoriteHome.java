package com.hrtgo.heritagego.heritagego.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hrtgo.heritagego.heritagego.Adapter.rcvAdapterTabsHome;
import com.hrtgo.heritagego.heritagego.Model.LocationHome;
import com.hrtgo.heritagego.heritagego.R;

import java.util.ArrayList;

public class tabMyFavoriteHome extends Fragment {

    ArrayList<LocationHome> locationMyfavorite;
    RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_tab_my_favorite, container, false);

        initView(view);
        //initData();
        setRecyclerView();

        return view;
    }

    // create instance view
    private void initView(View view){
        recyclerView = view.findViewById(R.id.recycler_view_home_tab_my_favorite);
    }

    // set adapter for recyclerView at Tab MyFavorite
    private void setRecyclerView(){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        final rcvAdapterTabsHome adapter = new rcvAdapterTabsHome(locationMyfavorite, this.getContext());
        recyclerView.setAdapter(adapter);
    }

    private void initData(){
        locationMyfavorite = new ArrayList<>();
        locationMyfavorite.add(new LocationHome(R.drawable.benh_vien_da_khoa_sai_gon, "20", "Bệnh Viện Đa Khoa Sài Gòn"));
        locationMyfavorite.add(new LocationHome(R.drawable.cho_ben_thanh, "50", "Chợ Bến Thành"));

    }
}
