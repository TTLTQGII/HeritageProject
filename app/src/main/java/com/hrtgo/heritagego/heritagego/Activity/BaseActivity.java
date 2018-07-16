package com.hrtgo.heritagego.heritagego.Activity;

import android.app.Activity;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.hrtgo.heritagego.heritagego.Interface.ConnectivityReceiverListener;


import com.hrtgo.heritagego.heritagego.until.mNetworkController;

public class BaseActivity extends AppCompatActivity implements ConnectivityReceiverListener{

    protected String mLanguage = "ENG";
    public static mNetworkController mNetworkController;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //changeLanguage();
        mLanguage = getLanguage();
    }

    public void changeLanguage(){
        //Language.Change("vi", this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private String getLanguage(){
        SharedPreferences mPrefs = getSharedPreferences("LanguagePrefs", Activity.MODE_PRIVATE);
        String languagePrefs = mPrefs.getString("Language", "en");
        return languagePrefs;
    }

    @Override
    protected void onStart() {
        super.onStart();
        initBroadCastReceiver();
    }

    protected void initBroadCastReceiver(){
        mNetworkController = new mNetworkController();
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        mNetworkController.connectivityReceiverListener=this; // khong hieu o day
        registerReceiver(mNetworkController, mIntentFilter);

    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mNetworkController);
    }
}
