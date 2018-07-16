package com.hrtgo.heritagego.heritagego.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hrtgo.heritagego.heritagego.R;
import com.hrtgo.heritagego.heritagego.until.Language;
import com.hrtgo.heritagego.heritagego.until.customize;

public class MainActivity extends BaseActivity {

    android.support.v7.widget.Toolbar actionToolBar;
    Intent mIntent;
    boolean mDoubleBackPress = false;
    String Language;
    Toast mToast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Language = super.mLanguage;
        Log.e("Language", Language);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        initCustomizeActionBar();
        mActivityEvent();
    }

    // customize Action bar
    private void initCustomizeActionBar(){
        actionToolBar = findViewById(R.id.action_tool_bar_custom_activity_main);
        if(actionToolBar != null) {
            setSupportActionBar(actionToolBar);
            ActionBar actionBar = getSupportActionBar();
            LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customAcionBar = inflater.inflate(R.layout.tool_action_bar_main_activity, null);
            actionBar.setCustomView(customAcionBar);
            customize.customizeActionBar(actionToolBar, actionBar, customAcionBar);

        }
    }


    @Override
    public void onBackPressed() {
        if(this.mDoubleBackPress){
            mToast.cancel();
            super.onBackPressed();
            return;
        }

        this.mDoubleBackPress = true;
        mToast =  Toast.makeText(this, this.getString(R.string.exit_notification),  Toast.LENGTH_SHORT);
        mToast.show();
        new Handler().postDelayed(new Runnable() {@Override
        public void run() {
            mDoubleBackPress = false;
        }},2000);
    }

    private void mActivityEvent(){
        RelativeLayout mHeritage, mExhibition, mSaved, mDocument;

        mHeritage = findViewById(R.id.mHeritage_button);
        mExhibition = findViewById(R.id.mExhibition_button);
        mSaved = findViewById(R.id.mSaved_button);
        mDocument = findViewById(R.id.mDocument_button);

        mHeritage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(v.getContext(), HeritageActivity.class);
                startActivity(mIntent);
            }
        });
        mExhibition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(v.getContext(), ExhibitionActivity.class);
                startActivity(mIntent);
            }
        });
    }
}
