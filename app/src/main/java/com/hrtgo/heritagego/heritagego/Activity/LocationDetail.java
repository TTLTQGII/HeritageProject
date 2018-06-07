package com.hrtgo.heritagego.heritagego.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Marker;
import com.hrtgo.heritagego.heritagego.R;
import com.hrtgo.heritagego.heritagego.Worker.VolleySingleton;

import com.hrtgo.heritagego.heritagego.Worker.parseJsonLocationDetail;
import com.hrtgo.heritagego.heritagego.untill.customize;
import com.hrtgo.heritagego.heritagego.Adapter.imgListAdapterLocationDetail;

import java.util.ArrayList;
import java.util.List;

import at.blogc.android.views.ExpandableTextView;

public class LocationDetail extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    android.support.v7.widget.Toolbar actionToolBar;
    int locationID = 0;
    TextView txtLocationName, txtLocationDistance, txtLocationAddress, txtAmountOfView;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
    double latitude, longitude;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_detail);
        //Create Action bar
        getUserLocation();
        //new getCurrentLocation().execute();
        initCustomizeActionBar();
        getIntentData();
        initView();

    }

//    private class getCurrentLocation extends AsyncTask<Void, Void, Void>{
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            getUserLocation();
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//
//            callAPI(String.valueOf(locationID),String.valueOf(getLongitude()), String.valueOf(getLatitude()));
//        }
//    }

    @SuppressLint("RestrictedApi")
    public void getUserLocation(){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(60000); // 1 minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());

            } else {
                //Request Location Permission
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());

        }
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




    public LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.e("asd", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                longitude = location.getLongitude();
                latitude = location.getLatitude();

                Log.e("asc",String.valueOf(longitude)+","+String.valueOf(latitude));


            }
            callAPI(String.valueOf(locationID));

        }
    };

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

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());

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

    private void callAPI(String currentPage){
        String url = getString(R.string.request_heritage_location_detail) + currentPage + "/" + String.valueOf(longitude) + "/" + String.valueOf(latitude);
        Log.e("URLDetail", url);
        getListData(url);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
