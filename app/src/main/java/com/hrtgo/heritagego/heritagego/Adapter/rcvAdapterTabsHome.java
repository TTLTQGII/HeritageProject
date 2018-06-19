package com.hrtgo.heritagego.heritagego.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hrtgo.heritagego.heritagego.Activity.LocationDetail;
import com.hrtgo.heritagego.heritagego.Interface.OnLoadMoreListener;
import com.hrtgo.heritagego.heritagego.Model.heritageInfoHomeModel;
import com.hrtgo.heritagego.heritagego.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class rcvAdapterTabsHome extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public ArrayList<heritageInfoHomeModel> locationDatas;
    Context context;

    private final int view_type_item = 0;
    private final int view_type_loading = 1;
    private OnLoadMoreListener onLoadMoreListener;
    public boolean isLoading = false;
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;

//    public rcvAdapterTabsHome(ArrayList<heritageInfoHomeModel> locationDatas, Context context) {
//        this.locationDatas = locationDatas;
//        this.context = context;
//    }

    public rcvAdapterTabsHome(RecyclerView recyclerView, ArrayList<heritageInfoHomeModel> locationDatas, Context context) {
        this.locationDatas = locationDatas;
        this.context = context;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                //????????
                if(!isLoading & totalItemCount <= (lastVisibleItem + visibleThreshold) & totalItemCount >= 7){
                    if(onLoadMoreListener != null){
                        isLoading = true;
                        onLoadMoreListener.onLoadMore();
                    }
                }

            }


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == view_type_item){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.recycler_view_home_items_layout, parent, false);
            return new itemHolder(itemView);
        }else if(viewType == view_type_loading){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View loadingView = inflater.inflate(R.layout.recycler_view_home_item_loading_layout, parent, false);
            return new LoadingViewHolder(loadingView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof itemHolder){
            itemHolder item = (rcvAdapterTabsHome.itemHolder) holder;
            item.txtLocationName.setText(locationDatas.get(position).getLocationName());
//            item.txtAmountOfLocationView.setText(String.valueOf(locationDatas.get(position).getLocationViewed()));
            item.txtAmountOfLocationView.setText(String.valueOf(locationDatas.size()));
            Picasso.get()
                    .load(context.getResources().getString(R.string.request_image) + locationDatas.get(position).getLocationImagePath())
                    .fit()
                    .into(item.imgLocation);
        }
        if(holder instanceof LoadingViewHolder){
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return locationDatas.get(position) == null ? view_type_loading : view_type_item;
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

    public class itemHolder extends RecyclerView.ViewHolder{

        TextView txtLocationName, txtAmountOfLocationView;
        ImageView imgLocation;

        public itemHolder(final View itemView) {
            super(itemView);

            txtLocationName = itemView.findViewById(R.id.txt_name_location_ecycler_view_home_item);
            txtAmountOfLocationView = itemView.findViewById(R.id.txt_amount_of_view);
            imgLocation = itemView.findViewById(R.id.img_recycler_view_home_item);

//             move to location detail
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), LocationDetail.class);

                    intent.putExtra("ID", locationDatas.get(getAdapterPosition()).getID());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBarLoading);
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener){
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void loaded(){
        isLoading = false;
    }
}