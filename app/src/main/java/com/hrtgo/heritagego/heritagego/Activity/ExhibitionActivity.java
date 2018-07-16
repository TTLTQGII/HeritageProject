package com.hrtgo.heritagego.heritagego.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hrtgo.heritagego.heritagego.Adapter.rcvAdapterExhibition;
import com.hrtgo.heritagego.heritagego.Interface.OnLoadMoreListener;
import com.hrtgo.heritagego.heritagego.Model.exhibitionInfoHome;
import com.hrtgo.heritagego.heritagego.R;
import com.hrtgo.heritagego.heritagego.until.customize;

import java.util.ArrayList;

public class ExhibitionActivity extends BaseActivity {

    android.support.v7.widget.Toolbar actionToolBar;
    ImageView icBackpress;
    RecyclerView mRcvExhibition;
    int currentPage = 1;
    rcvAdapterExhibition adapter;
    ArrayList<exhibitionInfoHome> exhibitionDataList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibition);
        initView();
    }

    private void initView(){
        initCustomizeActionBar();

        mRcvExhibition = findViewById(R.id.rcv_exhibition);
    }

    // customize Action bar
    private void initCustomizeActionBar(){
        actionToolBar = findViewById(R.id.action_tool_bar_custom_exhibition);
        if(actionToolBar != null) {
            setSupportActionBar(actionToolBar);
            ActionBar actionBar = getSupportActionBar();
            LayoutInflater inflater = (LayoutInflater) ExhibitionActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customAcionBar = inflater.inflate(R.layout.tool_action_bar_customize, null);
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

    private void setRecyClerView(){
        mRcvExhibition.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getBaseContext(), LinearLayoutManager.VERTICAL, false);
        mRcvExhibition.setLayoutManager(linearLayoutManager);

        adapter = new rcvAdapterExhibition(mRcvExhibition, exhibitionDataList, this);
        mRcvExhibition.setAdapter(adapter);

        mOnLoadMore();
    }

    private void mOnLoadMore(){
        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                currentPage++;
                exhibitionDataList.add(null);
                adapter.exhibitionData = exhibitionDataList;
                adapter.notifyItemInserted(adapter.exhibitionData.size() - 1);
                // call API get Data
            }
        });
    }

    private void onDataChanged(){
        adapter.notifyDataSetChanged();
        adapter.loaded();
    }

}
