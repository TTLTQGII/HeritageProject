package com.hrtgo.heritagego.heritagego.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hrtgo.heritagego.heritagego.Model.userComment;
import com.hrtgo.heritagego.heritagego.R;

import java.util.ArrayList;

public class rcvCmtAdapterLocationDetail extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<userComment> userComments;
    Context context;

    private final int view_type_item = 0;
    private final int view_type_loadmore = 1;

    public rcvCmtAdapterLocationDetail(ArrayList<userComment> userComments, Context context) {
        this.userComments = userComments;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.user_comment, null);

        return new commentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof commentHolder){
            commentHolder commentHolder = (rcvCmtAdapterLocationDetail.commentHolder) holder;
            commentHolder.txtUserID.setText(userComments.get(position).getUserName());
            commentHolder.txtContent.setText(userComments.get(position).getContent());
            commentHolder.txtCommentPostTime.setText(userComments.get(position).getPostTime());
        }

    }



    @Override
    public int getItemViewType(int position) {
        return position == (userComments.size() - 1) ? view_type_loadmore : view_type_item;
    }

    @Override
    public int getItemCount() {
        return userComments.size();
    }

    public class commentHolder extends RecyclerView.ViewHolder{

        TextView txtUserID, txtContent, txtCommentPostTime;

        public commentHolder(View itemView) {
            super(itemView);

            txtUserID = itemView.findViewById(R.id.txt_comment_user_ID);
            txtContent = itemView.findViewById(R.id.txt_comment_contents);
            txtCommentPostTime = itemView.findViewById(R.id.txt_comment_postTime);
        }
    }
}
