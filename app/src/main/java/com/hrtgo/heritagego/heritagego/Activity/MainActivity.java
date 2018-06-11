package com.hrtgo.heritagego.heritagego.Activity;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

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
        checkLocationPermission();
    }

    // create instance View
    private void initView() {
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
    private void initCustomizeActionBar() {
        actionToolBar = findViewById(R.id.action_tool_bar_custom);
        if (actionToolBar != null) {
            setSupportActionBar(actionToolBar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();
            LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customAcionBar = inflater.inflate(R.layout.tool_action_bar_customize, null);
            actionBar.setCustomView(customAcionBar);
            customize.customizeActionBar(actionToolBar, actionBar, customAcionBar);
        }
    }

    private void initCustomizeNavigationBottom() {
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }

    private void initData() {
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

    private boolean loadFragment(String fragmentName, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (getSupportFragmentManager().findFragmentByTag(fragmentName) == null) {
            switch (fragmentName) {
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

            if (!fragmentList.get(i).equals(fragmentName)) {
                if (getSupportFragmentManager().findFragmentByTag(fragmentList.get(i)) != null) {
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

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user asynchronously -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }

    }
}
