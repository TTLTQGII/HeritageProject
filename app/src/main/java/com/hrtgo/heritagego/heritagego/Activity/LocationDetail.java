package com.hrtgo.heritagego.heritagego.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hrtgo.heritagego.heritagego.R;
import com.hrtgo.heritagego.heritagego.untill.customize;

public class LocationDetail extends AppCompatActivity {

    private android.support.v7.widget.Toolbar actionToolBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_detail);

        initViewAndEvent();
    }

    @Override
    protected void onStart() {
        super.onStart();

        actionToolBar = findViewById(R.id.action_tool_bar_custom_location_detail);
        initCustomizeActionBar();
    }

    void initViewAndEvent(){

        final TextView txtLocationName, txtLocationDistance, txtLocationAddress, txtAmountOfView, txtAmountOfLike, txtAmountOfComment;
        ViewPager imgViewPager;
        ImageView imgLike, imgComment;

        imgLike = findViewById(R.id.img_btn_like);
        txtAmountOfLike = findViewById(R.id.txt_amount_of_like);

        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtAmountOfLike.setText("0");
            }
        });
    }

    private void initCustomizeActionBar(){
        if(actionToolBar != null) {
            setSupportActionBar(actionToolBar);
            ActionBar actionBar = getSupportActionBar();
            LayoutInflater inflater = (LayoutInflater) LocationDetail.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customAcionBar = inflater.inflate(R.layout.tool_action_bar_customize, null);
            actionBar.setCustomView(customAcionBar);
            customize.customizeActionBar(actionToolBar, actionBar, customAcionBar);
        }
    }
}
