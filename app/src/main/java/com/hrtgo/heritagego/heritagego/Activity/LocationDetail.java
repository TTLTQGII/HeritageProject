package com.hrtgo.heritagego.heritagego.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.Marker;
import com.hrtgo.heritagego.heritagego.DirectionTask.DirectionTask;
import com.hrtgo.heritagego.heritagego.DirectionTask.DirectionTaskListener;
import com.hrtgo.heritagego.heritagego.DirectionTask.Route;
import com.hrtgo.heritagego.heritagego.Interface.getParams;
import com.hrtgo.heritagego.heritagego.R;
import com.hrtgo.heritagego.heritagego.Worker.VolleySingleton;

import com.hrtgo.heritagego.heritagego.Worker.parseJsonLocationDetail;
import com.hrtgo.heritagego.heritagego.untill.customize;
import com.hrtgo.heritagego.heritagego.Adapter.imgListAdapterLocationDetail;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.blogc.android.views.ExpandableTextView;

public class LocationDetail extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, DirectionTaskListener{


    android.support.v7.widget.Toolbar actionToolBar;
    int locationID = 0;
    TextView txtLocationName, txtLocationDistance, txtLocationAddress, txtAmountOfView;
    TextView txtAmountOfLike, txtAmountOfComment;
    RelativeLayout imgBtnLike, imgBtnComment;
    ImageView imgLike, imgComment;
    ImageView icApplication;
    RelativeLayout DirectionMap;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
    double latitude, longitude;
    public String Destination;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_detail);
        //Create Action bar
        initView();
        getUserLocation();
        initCustomizeActionBar();
        getIntentData();
        iconBackpress();
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

        txtAmountOfLike = findViewById(R.id.txt_amount_of_like);
        txtAmountOfComment = findViewById(R.id.txt_amount_of_comment);
        imgLike = findViewById(R.id.img_like);

        imgBtnLike = findViewById(R.id.btn_like);
        imgBtnComment = findViewById(R.id.btn_comment);
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

    public void getDirection(){
        DirectionMap = findViewById(R.id.derection_container);

            DirectionMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity1();
                }
            });


    }

    // start Maps Activity
    private void startActivity1(){
        Intent DirectionMap = new Intent(this, MapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("destination", Destination);
        bundle.putDouble("latitude", latitude);
        bundle.putDouble("longitude", longitude);
        bundle.putString("locationName", String.valueOf(txtLocationName.getText()));
        bundle.putString("Address", String.valueOf(txtLocationAddress.getText()));
        bundle.putString("Viewed", String.valueOf(txtAmountOfView.getText()));
        DirectionMap.putExtra("Data", bundle);
        startActivity(DirectionMap);
    }

    public void bindData(String locationName, String address, int Viewed){
        txtLocationName.setText(locationName);
        txtLocationAddress.setText(address);
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
        imgListAdapterLocationDetail imgAdapter = new imgListAdapterLocationDetail(imgLocationDetails, this, imgViewPager);
        imgViewPager.setAdapter(imgAdapter);

        //int imgCount = imgAdapter.getCount();
    }

    // create like and comment event
    public void eventLikeComment(final int Liked, int Comment){
        txtAmountOfLike.setText(String.valueOf(Liked));
        txtAmountOfComment.setText(String.valueOf(Comment));

        imgBtnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtAmountOfLike.setText(String.valueOf(Liked + 1));
                imgLike.setImageResource(R.drawable.ic_like_active_32dp);
                imgBtnLike.setClickable(false);

                callLikeAPI(new getParams() {
                    @Override
                    public void setParams(Map<String, String> params) {
                        params.put("HerID", String.valueOf(locationID));
                        params.put("Latitude", String.valueOf(latitude));
                        params.put("Longitude", String.valueOf(longitude));
                        params.put("UserName", "Ẩn Danh");
                        params.put("InforPlatform", getInfoPlatform());
                    }
                });
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

    private String getInfoPlatform(){
        String anroidVersion = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        String modelDevice = Build.MODEL;
        return "android: " + anroidVersion  + " API level: "+ String.valueOf(sdkVersion) + " Model: " + modelDevice;
    }


    private void callLikeAPI(final getParams getParams){
        String url = getString(R.string.request_like);
        StringRequest likeRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(LocationDetail.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("likeError", error.toString());
//                Toast.makeText(LocationDetail.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders(){
                Map<String,String> headers = new HashMap<>();
                return headers;
            }
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                getParams.setParams(params);
                Log.e("put",params.toString());
                return params;
            }
        };

        likeRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInStance(this).getRequestQueue().add(likeRequest);
    }


    // expand and collapse the location content
    // goi qua -> worker asyntask
    public void eventExpandableTextView(final String description, final String content){
        final ExpandableTextView txtDescription = findViewById(R.id.expTxt_description_location_detail);
        txtDescription.setText(Html.fromHtml(description));

        txtDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtDescription.isExpanded()){
                    txtDescription.collapse();
                    txtDescription.setText(Html.fromHtml(description));
                }
                else {
                    txtDescription.expand();
                    txtDescription.setText(Html.fromHtml(content));
                }
            }
        });
    }


    private void parseJson(String json, String currentLocation){
        new parseJsonLocationDetail(this, currentLocation ).execute(json);
    }

    private void callAPI(String currentPage, double latitude, double longitude, final String currentLocation){
        String url = getURL(currentPage, latitude, longitude);
        Log.e("URLDetail", url);
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("LocationDetail response", response);
                parseJson(response, currentLocation);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // control error in here
            }
        });

        VolleySingleton.getInStance(this).getRequestQueue().add(jsonRequest);
    }

    // encode user current location
    private String getEncodedLocation(double latitude, double longitude){
        String encodedLocation = "";

        String tempLatitude = String.valueOf(latitude).trim();
        String tempLongitude = String.valueOf(longitude).trim();

        String Latitude = tempLatitude.replace(".", ",");
        String Longitude = tempLongitude.replace(".", ",");

        try {
            String encodedLatitude = URLEncoder.encode(Latitude, "UTF-8");
            String encodedLongitude = URLEncoder.encode(Longitude, "UTF-8");
            encodedLocation = encodedLatitude + "/" + encodedLongitude;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encodedLocation;
    }

    private String getURL(String currentPage, double latitude, double longitude){
        String url = getString(R.string.request_heritage_location_detail) + currentPage + "/" + getEncodedLocation(latitude, longitude);
        return url;
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

                Log.e("asc","longitude: "+ String.valueOf(longitude)+","  +"latitude: "+String.valueOf(latitude));
            }
            callAPI(String.valueOf(locationID), latitude, longitude, CurrentLocation(latitude,longitude));

        }
    };

    public String CurrentLocation(double CuLagitudeA, double CulongiatudeB)
    {
        Log.e("asb",String.valueOf(latitude)+String.valueOf(longitude));
        return CuLagitudeA + " " + CulongiatudeB;
    }


    public void sendRequest(String origin, String destination) {
        // String origin = CurrentLocation(latitude,longitude);
        Log.e("abc",String.valueOf(latitude) +" "+String.valueOf(longitude));
        //String destination = "10.774604, 106.689337";
        try {
            new DirectionTask( this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("RestrictedApi")
    public void getUserLocation(){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationRequest = new LocationRequest();
       // setInterval...
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
    //Connect to API get json and parse json in Asyntask


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

    @Override
    public void onDirectionFinderStart() {

    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        for (Route route : routes) {
            ((TextView) findViewById(R.id.txt_location_distance)).setText(route.distance.text);
        }
//        localRoute = routes;
    }

}
