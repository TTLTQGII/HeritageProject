package com.hrtgo.heritagego.heritagego.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hrtgo.heritagego.heritagego.R;
import com.hrtgo.heritagego.heritagego.untill.customize;

public class CommentActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar actionToolBar, commentBar;
    RecyclerView rcvComment;
    TextView txtLocationName, txtLocationAdress, txtAmountOfComment;
    EditText edtComment;
    ImageView icApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        initView();
    }

    private void initView(){
        initCustomizeActionBar();
        initComnmentBar();

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
}
