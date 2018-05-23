package com.hrtgo.heritagego.heritagego.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hrtgo.heritagego.heritagego.Model.LocationHome;
import com.hrtgo.heritagego.heritagego.R;

import java.util.ArrayList;

public class rcvHomeAdapterTabNear extends RecyclerView.Adapter<rcvHomeAdapterTabNear.viewHolder>{

    ArrayList<LocationHome> locationHomes;
    Context context;

    public rcvHomeAdapterTabNear(ArrayList<LocationHome> locationHomes, Context context) {
        this.locationHomes = locationHomes;
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
        holder.imgBtnRCVhome.setImageResource(locationHomes.get(position).getLocationImage());
        holder.txtLocationName.setText(locationHomes.get(position).getLocationName());
        holder.txtAmountOfLocationViewed.setText(locationHomes.get(position).getLocationViewed());
    }

    @Override
    public int getItemCount() {
        return locationHomes.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        ImageButton imgBtnRCVhome;
        TextView txtLocationName, txtAmountOfLocationViewed;

        public viewHolder(View itemView) {
            super(itemView);

           imgBtnRCVhome = itemView.findViewById(R.id.imgBtn_recycler_view_home_item);
           txtLocationName = itemView.findViewById(R.id.txt_name_location_ecycler_view_home_item);
           txtAmountOfLocationViewed = itemView.findViewById(R.id.txt_amount_of_view);
        }
   }
}
