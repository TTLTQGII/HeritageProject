package com.hrtgo.heritagego.heritagego.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hrtgo.heritagego.heritagego.R;
import com.hrtgo.heritagego.heritagego.untill.customize;
import com.hrtgo.heritagego.heritagego.Adapter.imgListAdapterLocationDetail;

import java.util.ArrayList;

import at.blogc.android.views.ExpandableTextView;

public class LocationDetail extends AppCompatActivity {

    android.support.v7.widget.Toolbar actionToolBar;
    TextView txtAmountOfLike, txtAmountOfComment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_detail);

        //Create Action bar
        initCustomizeActionBar();
        initViewAndEvent();
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


    void initViewAndEvent(){
        final TextView txtLocationName, txtLocationDistance, txtLocationAddress, txtAmountOfView, txtAmountOfLike;
        eventLikeComment();
        ArrayList<Integer> listTest = new ArrayList<>();

        listTest.add(R.drawable.cho_ben_thanh);
        listTest.add(R.drawable.benh_vien_da_khoa_sai_gon);
        listTest.add(R.drawable.buu_dien_trung_tam_sai_gon);

        eventViewPager(listTest);
    }

    // gọi qua worker -> worker asyntask
    // push image into viewpager
    private void eventViewPager(ArrayList<Integer> imgLocationDetails){
        ViewPager imgViewPager;
        imgViewPager = findViewById(R.id.img_location_container_detail);

        imgListAdapterLocationDetail imgAdapter = new imgListAdapterLocationDetail(imgLocationDetails, this);
        imgViewPager.setAdapter(imgAdapter);

        //int imgCount = imgAdapter.getCount();
    }

    // create like and comment event
    public void eventLikeComment(){
        final RelativeLayout imgBtnLike, imgBtnComment;
        final ImageView imgLike, imgComment;
        txtAmountOfLike = findViewById(R.id.txt_amount_of_like);
        txtAmountOfComment = findViewById(R.id.txt_amount_of_comment);
        imgLike = findViewById(R.id.img_like);

        imgBtnLike = findViewById(R.id.btn_like);
        imgBtnComment = findViewById(R.id.btn_comment);

        imgBtnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amoutOfLike = Integer.valueOf((String) txtAmountOfLike.getText());
                amoutOfLike = amoutOfLike + 1;
                txtAmountOfLike.setText(String.valueOf(amoutOfLike));
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


}
