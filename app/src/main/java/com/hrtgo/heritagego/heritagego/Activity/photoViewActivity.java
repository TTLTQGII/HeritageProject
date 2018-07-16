package com.hrtgo.heritagego.heritagego.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.hrtgo.heritagego.heritagego.R;
import com.hrtgo.heritagego.heritagego.until.customize;

public class photoViewActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar actionToolBar;
    ImageView icBackpress;
    PhotoView imgView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);
        initView();

    }

    private void initView(){
        initCustomizeActionBar();
        imgView = findViewById(R.id.image_photo_view);
//        imgLocation = findViewById(R.id.image_photo_view);
        getIntentData();
    }

    // customize Action bar
    private void initCustomizeActionBar(){
        actionToolBar = findViewById(R.id.action_tool_bar_custom_photoView);
        if(actionToolBar != null) {
            setSupportActionBar(actionToolBar);
            ActionBar actionBar = getSupportActionBar();
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customAcionBar = inflater.inflate(R.layout.tool_action_bar_photo_view, null);
            actionBar.setCustomView(customAcionBar);
            customize.customizeActionBar(actionToolBar, actionBar, customAcionBar);

            iconBackpress();
        }
    }

    private void iconBackpress(){
        icBackpress = findViewById(R.id.ic_img_backpress);
        icBackpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getIntentData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("image");
        if(bundle != null) {
            byte[] imageByte = bundle.getByteArray("imageByte");
            photoViewEvent(imageByte);
        }
    }

    private void photoViewEvent(byte[] imageByte){
        Bitmap imgBitmap = BitmapFactory.decodeByteArray(imageByte,0, imageByte.length);
        if(imgBitmap != null) {
            imgView.setImageBitmap(imgBitmap);
        }else {
            Log.e("imageByte", "0");
        }
    }
}
