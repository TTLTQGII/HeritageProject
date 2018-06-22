package com.hrtgo.heritagego.heritagego.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hrtgo.heritagego.heritagego.Adapter.rcvCommentAdapter;
import com.hrtgo.heritagego.heritagego.Model.userComment;
import com.hrtgo.heritagego.heritagego.R;
import com.hrtgo.heritagego.heritagego.Worker.VolleySingleton;
import com.hrtgo.heritagego.heritagego.untill.customize;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.io.Serializable;

public class CommentActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar actionToolBar, commentBar;
    RecyclerView rcvComment;
    TextView txtLocationName, txtLocationAdress, txtAmountOfComment;
    EditText edtComment;
    ImageView icApplication;

    String LocationID;
    int listSize;
    // currentPage loaded is 1
    int AmountOfComment = 0, currentPage = 0;
    String locationName, address;

    ArrayList<userComment> commentList;
    rcvCommentAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        initView();
    }

    private void initView(){
        initCustomizeActionBar();
        initComnmentBar();

        txtLocationName = findViewById(R.id.txt_location_name_comment_activity);
        txtLocationAdress = findViewById(R.id.txt_location_address_comment_activity);
        txtAmountOfComment = findViewById(R.id.txt_amount_of_comment_activity);
        rcvComment = findViewById(R.id.rcv_comment_activity);
        edtComment = findViewById(R.id.txt_input_comment);

        setCommentAdapter();

        getIntentData();
    }

    // customize Action bar
    private void initCustomizeActionBar(){
        actionToolBar = findViewById(R.id.action_tool_bar_custom_comment);
        if(actionToolBar != null) {
            setSupportActionBar(actionToolBar);
            ActionBar actionBar = getSupportActionBar();
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customAcionBar = inflater.inflate(R.layout.tool_action_bar_customize, null);
            actionBar.setCustomView(customAcionBar);
            customize.customizeActionBar(actionToolBar, actionBar, customAcionBar);

            iconBackpress();
        }
    }

    private void initComnmentBar(){
        commentBar = findViewById(R.id.tool_bar_comment);
        if(commentBar != null){
            LayoutInflater inflater = LayoutInflater.from(this);
            View layoutCommentBar = inflater.inflate(R.layout.tool_bar_comment, null);
            commentBar.addView(layoutCommentBar);
        }
    }

    private void iconBackpress(){
        icApplication = findViewById(R.id.logo_application);
        icApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getIntentData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("Data");
        if(bundle != null){
            LocationID = bundle.getString("ID");
            currentPage = bundle.getInt("CurrentPage");

            locationName = bundle.getString("LocationName");
            txtLocationName.setText(locationName);

            address = bundle.getString("Address");
            txtLocationAdress.setText(address);

            AmountOfComment = bundle.getInt("Commented");
            txtAmountOfComment.setText(String.valueOf(AmountOfComment));
            if( bundle.getSerializable("List") != null){
                commentList = (ArrayList<userComment>) bundle.getSerializable("List");
                adapter.userComments = commentList;
                adapter.notifyDataSetChanged();
            }
            else {
                commentList = new ArrayList<>();
                callAPI(getURL(currentPage));
            }
        }
    }

    private void setCommentAdapter(){

        rcvComment.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvComment.setLayoutManager(linearLayoutManager);

        //divider recycler item list
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rcvComment.getContext(), DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.comment_divider);
        dividerItemDecoration.setDrawable(drawable);
        rcvComment.addItemDecoration(dividerItemDecoration);

        adapter = new rcvCommentAdapter(commentList, this, "CommentActivity");
        rcvComment.setAdapter(adapter);

    }

    private String getURL(int currentPage){
        String url =  getString(R.string.request_get_commented) + LocationID + "/" + currentPage;
        return url;
    }

    private void callAPI(String URL){
        String url = URL;

        StringRequest commentRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleySingleton.getInStance(this).getRequestQueue().add(commentRequest);
    }

    private void parseJson(String json){
        try {
            JSONObject root = new JSONObject(json);

            JSONArray pdata = root.getJSONArray("pdata");
            for (int i = 0; i< pdata.length(); i++){
                JSONObject element = pdata.getJSONObject(i);

                commentList.add(new userComment(element.getString("UserName"),
                        element.getString("Contents"),
                        element.getString("PostTime")));
            }

            if(commentList.size() > 0) {
                adapter.userComments = commentList;
                Log.e("size", String.valueOf(adapter.getItemCount()) + " " + String.valueOf(adapter.getItemViewType(1)));
                adapter.notifyItemInserted(0);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ListCommentResponse", "parse failse");
        }
    }
}
