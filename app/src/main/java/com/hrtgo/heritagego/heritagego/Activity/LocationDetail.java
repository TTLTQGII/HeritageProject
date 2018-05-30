package com.hrtgo.heritagego.heritagego.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hrtgo.heritagego.heritagego.R;
import com.hrtgo.heritagego.heritagego.Worker.VolleySingleton;
import com.hrtgo.heritagego.heritagego.Worker.parseJsonFamousTab;
import com.hrtgo.heritagego.heritagego.Worker.parseJsonLocationDetail;
import com.hrtgo.heritagego.heritagego.untill.customize;
import com.hrtgo.heritagego.heritagego.Adapter.imgListAdapterLocationDetail;

import java.util.ArrayList;

import at.blogc.android.views.ExpandableTextView;

public class LocationDetail extends AppCompatActivity {

    android.support.v7.widget.Toolbar actionToolBar;
    int locationID = 0;
    TextView txtLocationName, txtLocationDistance, txtLocationAddress, txtAmountOfView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_detail);
        //Create Action bar
        initCustomizeActionBar();
        getIntentData();
        initView();
        callAPI(String.valueOf(locationID),"5", "7");
    }



    @Override
    protected void onStart() {
        super.onStart();
    }
    // customize Action bar
    private void initCustomizeActionBar(){
        actionToolBar = findViewById(R.id.action_tool_bar_custom_location_detail);
        if(actionToolBar != null) {
            setSupportActionBar(actionToolBar);
            ActionBar actionBar = getSupportActionBar();
            LayoutInflater inflater = (LayoutInflater) LocationDetail.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customAcionBar = inflater.inflate(R.layout.tool_action_bar_customize, null);
            actionBar.setCustomView(customAcionBar);
            customize.customizeActionBar(actionToolBar, actionBar, customAcionBar);
        }
    }

    private void initView(){

        txtLocationName = findViewById(R.id.txt_location_name);
        txtLocationDistance = findViewById(R.id.txt_location_distance);
        txtLocationAddress = findViewById(R.id.txt_location_address);
        txtAmountOfView = findViewById(R.id.txt_amount_of_view);

    }

    public void bindData(String locationName, String address, String distance, int Viewed){
        txtLocationName.setText(locationName);
        txtLocationAddress.setText(address);
        txtLocationDistance.setText(distance);
        txtAmountOfView.setText(String.valueOf(Viewed));
    }

    // get data put from home activity
    private void getIntentData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            locationID = bundle.getInt("ID");
        }
        Log.e("locationID", String.valueOf(locationID));
    }


    // gọi qua worker -> worker asyntask
    // push image into viewpager
    public void eventViewPager(ArrayList<String> imgLocationDetails){
        ViewPager imgViewPager;
        imgViewPager = findViewById(R.id.img_location_container_detail);

        imgListAdapterLocationDetail imgAdapter = new imgListAdapterLocationDetail(imgLocationDetails, this);
        imgViewPager.setAdapter(imgAdapter);

        //int imgCount = imgAdapter.getCount();
    }

    // create like and comment event
    public void eventLikeComment(final int Liked, int Comment){
        final TextView txtAmountOfLike, txtAmountOfComment;
        final RelativeLayout imgBtnLike, imgBtnComment;
        final ImageView imgLike, imgComment;
        txtAmountOfLike = findViewById(R.id.txt_amount_of_like);
        txtAmountOfComment = findViewById(R.id.txt_amount_of_comment);
        imgLike = findViewById(R.id.img_like);
        txtAmountOfLike.setText(String.valueOf(Liked));
        txtAmountOfComment.setText(String.valueOf(Comment));

        imgBtnLike = findViewById(R.id.btn_like);
        imgBtnComment = findViewById(R.id.btn_comment);

        imgBtnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtAmountOfLike.setText(String.valueOf(Liked + 1));
                imgLike.setImageResource(R.drawable.ic_like_active_32dp);
                imgBtnLike.setClickable(false);
            }
        });

        imgBtnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // temp function
                int amountOfComent = Integer.valueOf((String)txtAmountOfComment.getText());
                amountOfComent = amountOfComent + 1;
                txtAmountOfComment.setText(String.valueOf(amountOfComent));
            }
        });
    }


    // expand and collapse the location content
    // goi qua -> worker asyntask
    public void eventExpandableTextView(final String description, final String content){
        final ExpandableTextView txtDescription = findViewById(R.id.expTxt_description_location_detail);
        txtDescription.setText(description);

        txtDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtDescription.isExpanded()){
                    txtDescription.collapse();
                    txtDescription.setText(description);
                }
                else {
                    txtDescription.expand();
                    txtDescription.setText(content);
                }
            }
        });
    }

    //Connect to API get json and parse json in Asyntask
    private void getListData(String url){
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("LocationDetail", response);
                parseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // control error in here
            }
        });

        VolleySingleton.getInStance(this).getRequestQueue().add(jsonRequest);
    }


    private void parseJson(String json){
        new parseJsonLocationDetail(this).execute(json);
    }

    private void callAPI(String currentPage, String longTiTue, String latituge){
        String url = getString(R.string.request_heritage_location_detail) + currentPage + "/" +longTiTue + "/" +latituge;
        Log.e("URLDetail", url);
        getListData(url);
    }
}
