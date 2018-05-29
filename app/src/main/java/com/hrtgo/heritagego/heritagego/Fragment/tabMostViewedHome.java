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

public class tabMostViewedHome extends Fragment{
    //ArrayList<LocationHome> locationMostViewed; changed model
    RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_tab_most_viewed, container, false);

        initView(view);
        //initData();
        //setRecyclerView();

        return view;
    }

    private void initView(View view){
        recyclerView = view.findViewById(R.id.recycler_view_home_tab_most_viewed);
    }


    // set adapter for recyclerView at Tab MostViewed
//    private void setRecyclerView(){
//        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        final rcvAdapterTabsHome adapter = new rcvAdapterTabsHome(locationMostViewed, this.getContext());
//        recyclerView.setAdapter(adapter);
//    }

//    private void initData(){
//        locationMostViewed = new ArrayList<>();
//        locationMostViewed.add(new LocationHome(R.drawable.cho_ben_thanh, "5000000", "Chợ Bến Thành"));
//        locationMostViewed.add(new LocationHome(R.drawable.ben_nha_rong, "1000000", "Bến Nhà Rồng"));
//        locationMostViewed.add(new LocationHome(R.drawable.dinh_doc_lap, "2000", "Dinh Độc Lập"));
//        locationMostViewed.add(new LocationHome(R.drawable.buu_dien_trung_tam_sai_gon, "100", "Bưu Điện Trung Tâm Sài Gòn"));
//    }
}
