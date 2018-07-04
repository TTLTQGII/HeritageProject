package com.hrtgo.heritagego.heritagego.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hrtgo.heritagego.heritagego.Interface.OnLoadMoreListener;
import com.hrtgo.heritagego.heritagego.Model.userComment;
import com.hrtgo.heritagego.heritagego.R;

import java.util.ArrayList;
import java.util.EmptyStackException;

public class rcvCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<userComment> userComments;
    Context context;
    private String contextFlag;
    public long totalComment;
    private OnLoadMoreListener onLoadMoreListener;

    public int getCurrent_page() {
        return (userComments.size()-1)/10;
    }

    private final int view_type_item = 0;
    public final int view_type_loadmore = 1;
    public final int view_type_trigger_load = 2;

    private long AmountOfComementLeft;

    public long getAmountOfComementLeft() {
        return totalComment - (userComments.size()-1);
    }

    public void setAmountOfComementLeft(long amountOfComementLeft) {
        AmountOfComementLeft = amountOfComementLeft;
    }

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public rcvCommentAdapter(ArrayList<userComment> userComments, Context context, String activity, long totalComment) {
        this.userComments = userComments;
        this.context = context;
        this.contextFlag = activity;
        this.totalComment = totalComment;
    }

    public rcvCommentAdapter(ArrayList<userComment> userComments, Context context, String activity) {
        this.userComments = userComments;
        this.context = context;
        this.contextFlag = activity;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if (viewType == view_type_item) {
           LayoutInflater inflater = LayoutInflater.from(parent.getContext());
           View itemView = inflater.inflate(R.layout.user_comment, parent, false);
           return new commentHolder(itemView);
       }else if (viewType == view_type_loadmore){
           LayoutInflater inflater = LayoutInflater.from(parent.getContext());
           View itemView = inflater.inflate(R.layout.item_loading_layout, parent, false);
           return new loadingHolder(itemView);
       }else if (viewType == view_type_trigger_load){
           LayoutInflater inflater = LayoutInflater.from(parent.getContext());
           View itemView = inflater.inflate(R.layout.m_item_trigger_load, parent, false);
           return new triggerLoadHolder(itemView);
       }
       return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof commentHolder){
            commentHolder commentHolder = (rcvCommentAdapter.commentHolder) holder;

            commentHolder.txtUserName.setText(userComments.get(position).getUserName());
            commentHolder.txtContent.setText(userComments.get(position).getContent());
            commentHolder.txtCommentPostTime.setText(userComments.get(position).getPostTime());

            Log.e("commentListSizeAdatper", String.valueOf(userComments.size()));
            Log.e("commentListSizeTotal", String.valueOf(totalComment));
        }
        if (holder instanceof loadingHolder){
            rcvCommentAdapter.loadingHolder loadingViewHolder = (rcvCommentAdapter.loadingHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
        if (holder instanceof triggerLoadHolder){
            rcvCommentAdapter.triggerLoadHolder triggerLoadHolder = (rcvCommentAdapter.triggerLoadHolder) holder;
            triggerLoadHolder.txtAmountOfCommentLeft.setText(String.valueOf(countNumberAmountLeft()));
        }
    }

    public long countNumberAmountLeft(){
        long result = 0;

        if(getAmountOfComementLeft() > 10){
            result = 10;
        }
        else{
            result = getAmountOfComementLeft();
        }

        return result;
    }



    @Override
    public int getItemViewType(int position) {
        if(contextFlag == "LocationDetail"){
            return view_type_item;
        }else if(contextFlag == "CommentActivity"){
            if(userComments.get(position) == null) {
                if (getType() == view_type_item) {
                    return view_type_item;
                } else if (getType() == view_type_loadmore) {
                    return view_type_loadmore;
                } else if (getType() == view_type_trigger_load) {
                    return view_type_trigger_load;
                } else {
                    return view_type_item;
                }
            }
            else {
                return view_type_item;
            }
        }else {
            throw new EmptyStackException();
        }
    }


//    position == (userComments.size() - 1) ? view_type_loadmore :

    @Override
    public int getItemCount() {
        if (userComments.size() != 0){
            if (contextFlag == "LocationDetail"){
                if(userComments.size() == 1){
                    return 1;
                }else if(userComments.size() == 2){
                    return 2;
                }else{
                    return 3;
                }
            }
            else if(contextFlag == "CommentActivity"){
                return userComments.size();
            }
            else{
                return 0;
            }
        }
        else {
            return 0;
        }
    }

    public class commentHolder extends RecyclerView.ViewHolder{

        TextView txtUserName, txtContent, txtCommentPostTime;

        public commentHolder(View itemView) {
            super(itemView);

            txtUserName = itemView.findViewById(R.id.txt_comment_user_ID);
            txtContent = itemView.findViewById(R.id.txt_comment_contents);
            txtCommentPostTime = itemView.findViewById(R.id.txt_comment_postTime);
        }
    }

    public class loadingHolder extends RecyclerView.ViewHolder{

        ProgressBar progressBar;

        public loadingHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBarLoading);
        }
    }

    public class triggerLoadHolder extends RecyclerView.ViewHolder{
        TextView txtAmountOfCommentLeft;

        public triggerLoadHolder(View itemView) {
            super(itemView);
            txtAmountOfCommentLeft = itemView.findViewById(R.id.txt_amount_of_comment_left);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                    }
                });
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener){
        this.onLoadMoreListener = onLoadMoreListener;
    }
}
