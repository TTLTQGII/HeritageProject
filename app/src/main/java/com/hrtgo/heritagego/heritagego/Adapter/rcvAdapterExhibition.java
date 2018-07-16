package com.hrtgo.heritagego.heritagego.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import com.hrtgo.heritagego.heritagego.API.API;
import com.hrtgo.heritagego.heritagego.Interface.OnLoadMoreListener;
import com.hrtgo.heritagego.heritagego.Model.exhibitionInfoHome;
import com.hrtgo.heritagego.heritagego.R;
import com.squareup.picasso.Picasso;

public class rcvAdapterExhibition extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<exhibitionInfoHome> exhibitionData;
    private Context context;

    private final int view_type_item = 0;
    private final int view_type_loading = 1;
    private OnLoadMoreListener onLoadMoreListener;
    public boolean isLoading = false;
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;

    public rcvAdapterExhibition(RecyclerView mRecyclerView, final ArrayList<exhibitionInfoHome> exhibitionData, Context context) {
        this.exhibitionData = exhibitionData;
        this.context = context;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                //????????
                if(!isLoading & totalItemCount <= (lastVisibleItem + visibleThreshold) & totalItemCount >= 10){
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

    @Override
    public int getItemCount() {
        if (exhibitionData.size() != 0) {
            return exhibitionData.size();
        }else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return exhibitionData.get(position) == null ? view_type_loading : view_type_item;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == view_type_item){
            LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
            View itemView = mInflater.inflate(R.layout.items_layout_rcv_exhibition, parent, false);
            return new itemHolder(itemView);
        }else if(viewType == view_type_loading){
            LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
            View loadingView = mInflater.inflate(R.layout.item_loading_layout, parent, false);
            return new itemLoadingHolder(loadingView);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof itemHolder){
            itemHolder mItem = (rcvAdapterExhibition.itemHolder) holder;
            mItem.txtExhibitionName.setText(exhibitionData.get(position).getExhibitionName());

            Picasso.get()
                    .load(API.IMAGE_LINK() + exhibitionData.get(position).getImgPath())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.image_place_holder)
                    .into(mItem.imgExhibition);
        }

        if(holder instanceof itemLoadingHolder){
            itemLoadingHolder mItem = (rcvAdapterExhibition.itemLoadingHolder) holder;
            mItem.progressBar.setIndeterminate(true);
        }
    }

    public class itemHolder extends RecyclerView.ViewHolder{

        TextView txtExhibitionName;
        ImageView imgExhibition;

        public itemHolder(View itemView) {
            super(itemView);

            txtExhibitionName = itemView.findViewById(R.id.txt_name_exhibition_item);
            imgExhibition = itemView.findViewById(R.id.img_recycler_view_exhibition_item);
        }
    }

    public class itemLoadingHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;

        public itemLoadingHolder(View itemView) {
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
