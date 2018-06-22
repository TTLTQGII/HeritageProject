package com.hrtgo.heritagego.heritagego.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hrtgo.heritagego.heritagego.Model.userComment;
import com.hrtgo.heritagego.heritagego.R;

import java.util.ArrayList;

public class rcvCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<userComment> userComments;
    Context context;
    private String contextFlag;

    private final int view_type_item = 0;
    private final int view_type_loadmore = 1;

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
        }
    }

    @Override
    public int getItemViewType(int position) {
        return  view_type_item;
    }

//    position == (userComments.size() - 1) ? view_type_loadmore :

    @Override
    public int getItemCount() {
        if (userComments.size() != 0){
            if (contextFlag == "LocationDetail"){
                return 3;
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
}
