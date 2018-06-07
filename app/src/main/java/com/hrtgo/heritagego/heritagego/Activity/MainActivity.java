package com.hrtgo.heritagego.heritagego.Activity;


import android.content.Context;
import android.support.annotation.NonNull;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.hrtgo.heritagego.heritagego.Fragment.navHomefrag;
import com.hrtgo.heritagego.heritagego.R;
import com.hrtgo.heritagego.heritagego.Fragment.navSettingfrag;
import com.hrtgo.heritagego.heritagego.Fragment.navMapsfrag;
import com.hrtgo.heritagego.heritagego.Fragment.navSearchfrag;
import com.hrtgo.heritagego.heritagego.untill.customize;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private android.support.v7.widget.Toolbar actionToolBar;
    private BottomNavigationView bottomNavigationView;
    List<String> fragmentList = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    // create instance View
    private void initView(){
        // initial action bar
        actionToolBar = findViewById(R.id.action_tool_bar_custom);
        initCustomizeActionBar();
        //initial bottom navigation view
        bottomNavigationView = findViewById(R.id.nav_bottom_view);
        customize.disableShiftMode(bottomNavigationView);
        initCustomizeNavigationBottom();

        loadFragment("Home", new navHomefrag());
    }


    // customize Action Bar
    private void initCustomizeActionBar(){
        actionToolBar = findViewById(R.id.action_tool_bar_custom);
        if(actionToolBar != null) {
            setSupportActionBar(actionToolBar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();
            LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customAcionBar = inflater.inflate(R.layout.tool_action_bar_customize, null);
            actionBar.setCustomView(customAcionBar);
            customize.customizeActionBar(actionToolBar, actionBar, customAcionBar);
        }
    }

    private void initCustomizeNavigationBottom(){
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }

    private void initData(){
        fragmentList.add(this.getResources().getString(R.string.navigation_bottom_home));
        fragmentList.add(this.getResources().getString(R.string.navigation_bottom_Maps));
        fragmentList.add(this.getResources().getString(R.string.naviagtion_bottom_setting));
        fragmentList.add(this.getResources().getString(R.string.navigation_bottom_search));
    }


    // bắt sự kiện item ở bottom navigation bar
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = new navHomefrag();
        String title = "Home";

        switch (item.getItemId()) {
            case R.id.nav_action_bottom_home:
                fragment = new navHomefrag();
                title = item.getTitle().toString();
                item.setChecked(true);
                break;
            case R.id.nav_action_bottom_maps:
                fragment = new navMapsfrag();
                title = item.getTitle().toString();
                item.setChecked(true);
                break;
            case R.id.nav_action_bottom_setting:
                fragment = new navSettingfrag();
                title = item.getTitle().toString();
                item.setChecked(true);
                break;
            case R.id.nav_action_bottom_search:
                fragment = new navSearchfrag();
                title = item.getTitle().toString();
                item.setChecked(true);
                break;
        }

        return loadFragment(title, fragment);
    }

    // load fragment into R.id.fragment_main_LL_container (LL = LinearLayout)
    // Show/Hide Fragment at navigation bar

    private boolean loadFragment(String fragmentName, Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if(getSupportFragmentManager().findFragmentByTag(fragmentName) == null){
            switch (fragmentName){
                case "Home":
                    fragmentTransaction.add(R.id.fragment_main_LL_Container, fragment, fragmentName).addToBackStack(fragmentName);
                    break;
                case "Maps":
                    fragmentTransaction.add(R.id.fragment_main_LL_Container, fragment, fragmentName).addToBackStack(fragmentName);
                    break;
                case "Setting":
                    fragmentTransaction.add(R.id.fragment_main_LL_Container, fragment, fragmentName).addToBackStack(fragmentName);
                    break;
                case "Search":
                    fragmentTransaction.add(R.id.fragment_main_LL_Container, fragment, fragmentName).addToBackStack(fragmentName);
                    break;
            }
        } else {
            fragmentTransaction.show(getSupportFragmentManager().findFragmentByTag(fragmentName));
        }

        for (int i = 0; i < fragmentList.size(); i++) {

                if(!fragmentList.get(i).equals(fragmentName)){
                    if(getSupportFragmentManager().findFragmentByTag(fragmentList.get(i)) != null){
                        fragmentTransaction.hide(getSupportFragmentManager().findFragmentByTag(fragmentList.get(i)));
                    }
                }
        }

        fragmentTransaction.commit();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
