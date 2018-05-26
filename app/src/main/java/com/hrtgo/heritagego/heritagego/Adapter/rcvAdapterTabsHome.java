package com.hrtgo.heritagego.heritagego.Adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hrtgo.heritagego.heritagego.Model.LocationHome;
import com.hrtgo.heritagego.heritagego.R;

import java.util.ArrayList;

public class rcvAdapterTabsHome extends RecyclerView.Adapter<rcvAdapterTabsHome.viewHolder>{

    ArrayList<LocationHome> locationDatas;
    Context context;

    public rcvAdapterTabsHome(ArrayList<LocationHome> locationDatas, Context context) {
        this.locationDatas = locationDatas;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.recycler_view_home_items_layout, parent, false);

        return new viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.imgBtnRCVHome.setImageResource(locationDatas.get(position).getLocationImage());
        holder.txtLocationName.setText(locationDatas.get(position).getLocationName());
        holder.txtAmountOfLocationView.setText(locationDatas.get(position).getLocationViewed());
    }

    @Override
    public int getItemCount() {
        if(locationDatas != null){
            return locationDatas.size();
        }
        else {
            return 0;
        }
    }


    public class viewHolder extends RecyclerView.ViewHolder{

        TextView txtLocationName, txtAmountOfLocationView;
        ImageView imgBtnRCVHome;

        public viewHolder(final View itemView) {
            super(itemView);

            txtLocationName = itemView.findViewById(R.id.txt_name_location_ecycler_view_home_item);
            txtAmountOfLocationView = itemView.findViewById(R.id.txt_amount_of_view);
            imgBtnRCVHome = itemView.findViewById(R.id.imgBtn_recycler_view_home_item);
        }
    }
}