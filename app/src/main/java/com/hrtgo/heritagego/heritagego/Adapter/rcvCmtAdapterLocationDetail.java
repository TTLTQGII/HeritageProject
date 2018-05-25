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

public class rcvCmtAdapterLocationDetail extends RecyclerView.Adapter<rcvCmtAdapterLocationDetail.viewHoler> {

    ArrayList<userComment> userComments;
    Context context;

    public rcvCmtAdapterLocationDetail(ArrayList<userComment> userComments, Context context) {
        this.userComments = userComments;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.user_comment, parent, false);

        return new viewHoler(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHoler holder, int position) {
        holder.txtUserID.setText(userComments.get(position).getUserName());
        holder.txtContent.setText(userComments.get(position).getContent());
        holder.txtCommentPostTime.setText(userComments.get(position).getPostTime());
    }

    @Override
    public int getItemCount() {
        return userComments.size();
    }

    public class viewHoler extends RecyclerView.ViewHolder{

        TextView txtUserID, txtContent, txtCommentPostTime;

        public viewHoler(View itemView) {
            super(itemView);

            txtUserID = itemView.findViewById(R.id.txt_comment_user_ID);
            txtContent = itemView.findViewById(R.id.txt_comment_contents);
            txtCommentPostTime = itemView.findViewById(R.id.txt_comment_postTime);
        }
    }
}
