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

import com.hrtgo.heritagego.heritagego.Adapter.rcvHomeAdapterTabNear;
import com.hrtgo.heritagego.heritagego.Model.LocationHome;
import com.hrtgo.heritagego.heritagego.R;

import java.util.ArrayList;

public class homeTabNear extends Fragment{

    ArrayList<LocationHome> locationNear;
    RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_tab_near, container, false);

        initView(view);
        initData();
        setRecyclerView();

        return view;
    }

    // create instance view
    private void initView(View view){
        recyclerView = view.findViewById(R.id.recycler_view_home_tab_near);
    }

    // set adapter for recyclerView at Tab Near
    private void setRecyclerView(){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        final rcvHomeAdapterTabNear adapter = new rcvHomeAdapterTabNear(locationNear, getActivity());
        recyclerView.setAdapter(adapter);
    }

    private void initData(){
        locationNear = new ArrayList<>();
        locationNear.add(new LocationHome(R.drawable.benh_vien_da_khoa_sai_gon, "20", "Bệnh Viện Đa Khoa Sài Gòn"));
        locationNear.add(new LocationHome(R.drawable.cau_mong_sai_gon, "50", "Chợ Bến Thành"));
        locationNear.add(new LocationHome(R.drawable.cho_ben_thanh, "50", "Chợ Bến Thành"));
        locationNear.add(new LocationHome(R.drawable.ben_nha_rong, "1000000", "Bến Nhà Rồng"));
        locationNear.add(new LocationHome(R.drawable.dinh_doc_lap, "1000", "Dinh Độc Lập"));
        locationNear.add(new LocationHome(R.drawable.buu_dien_trung_tam_sai_gon, "10000000", "Bưu Điện Trung Tâm Sài Gòn"));
    }
}
