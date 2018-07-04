package com.hrtgo.heritagego.heritagego.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.Marker;
import com.hrtgo.heritagego.heritagego.Adapter.rcvCommentAdapter;
import com.hrtgo.heritagego.heritagego.DirectionTask.DirectionTask;
import com.hrtgo.heritagego.heritagego.DirectionTask.DirectionTaskListener;
import com.hrtgo.heritagego.heritagego.DirectionTask.Route;
import com.hrtgo.heritagego.heritagego.Interface.getParams;
import com.hrtgo.heritagego.heritagego.Model.userComment;
import com.hrtgo.heritagego.heritagego.R;
import com.hrtgo.heritagego.heritagego.Worker.VolleySingleton;

import com.hrtgo.heritagego.heritagego.Worker.parseJsonLocationDetail;
import com.hrtgo.heritagego.heritagego.untill.customize;
import com.hrtgo.heritagego.heritagego.Adapter.imgListAdapterLocationDetail;
import com.hrtgo.heritagego.heritagego.API.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    String locationID = "", infoPlatform = "";
    TextView txtLocationName, txtLocationDistance, txtLocationAddress, txtAmountOfView;
    TextView txtAmountOfLike, txtAmountOfComment;
    RelativeLayout imgBtnLike, imgBtnComment;
    ImageView imgLike, imgComment, icApplication, icBackpress;
    boolean viewFlag = false;


    final int commentPage = 1;
    ArrayList<userComment> commentList;
    rcvCommentAdapter commentAdapter;
    RecyclerView recyclerViewComment;

    RelativeLayout DirectionMap;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
    double latitude = 0.00, longitude = 0.00;
    public String Destination = "";
//    List<Route> localRoute;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);
        //Create Action bar
        initView();
        callAPI(locationID);
        infoPlatform = getInfoPlatform();
        //viewFlag = false;
//        Log.e("viewFlag", String.valueOf(viewFlag));
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
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

            iconBackpress();
        }
    }

    private void initView(){
        getIntentData();
        txtLocationName = findViewById(R.id.txt_location_name);
        txtLocationDistance = findViewById(R.id.txt_location_distance);
        txtLocationAddress = findViewById(R.id.txt_location_address);
        txtAmountOfView = findViewById(R.id.txt_amount_of_view);

        txtAmountOfLike = findViewById(R.id.txt_amount_of_like);
        txtAmountOfComment = findViewById(R.id.txt_amount_of_comment);
        imgLike = findViewById(R.id.img_like);

        imgBtnLike = findViewById(R.id.btn_like);
        imgBtnComment = findViewById(R.id.btn_comment);

        initCustomizeActionBar();

        recyclerViewComment = findViewById(R.id.rcv_comment_list_lc);
        commentList = new ArrayList<>();

        setCommentAdapter();
    }

    private void iconBackpress(){
        icBackpress = findViewById(R.id.ic_img_backpress);
        icBackpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlag = false;
                onBackPressed();
            }
        });
    }

    public void getDirectionActivity(){
        DirectionMap = findViewById(R.id.derection_container);
            DirectionMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startDirectionActivity();
                }
            });
    }

    // start Maps Activity
    private void startDirectionActivity(){
        if(latitude != 0.00 & longitude != 0.00 & !Destination.equals("0.0 0.0")) {
            Intent DirectionMap = new Intent(this, MapsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("destination", Destination);
            bundle.putDouble("latitude", latitude);
            bundle.putDouble("longitude", longitude);
            bundle.putString("locationName", String.valueOf(txtLocationName.getText()));
            bundle.putString("Address", String.valueOf(txtLocationAddress.getText()));
            bundle.putString("Viewed", String.valueOf(txtAmountOfView.getText()));
//            bundle.putSerializable("ListRoute", (Serializable) localRoute);
            DirectionMap.putExtra("Data", bundle);
            startActivity(DirectionMap);
        }
        else if(latitude == 0.00 | longitude == 0.00){
            Toast.makeText(this, "Can't access your location", Toast.LENGTH_SHORT).show();
        }
        else if(Destination.equals("0.0 0.0")){
            Toast.makeText(this, "Destination isn't found", Toast.LENGTH_SHORT).show();
        }
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
            locationID = bundle.getString("ID");
        }
        Log.e("locationID", locationID);
    }

    public void callViewAPI(){
        sendViewRequest(new getParams() {
            @Override
            public void setParams(Map<String, String> params) {
                params.put("HerID", locationID);
                params.put("Latitude", String.valueOf(latitude));
                params.put("Longitude", String.valueOf(longitude));
                params.put("UserName", "Ẩn Danh");
                params.put("InforPlatform", infoPlatform);
            }
        });
    }

    private void sendViewRequest(final getParams getParams){
        String url = getViewURL();
        Log.e("LocationDetailViewURL", url);
        StringRequest viewRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("responseView", response);
                if(response.equals("1")){
                    viewFlag = true;
                }else {
                    viewFlag = false;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                viewFlag = false;
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
                Log.e("putView",params.toString());
                return params;
            }
        };

        viewRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInStance(this).getRequestQueue().add(viewRequest);
    }

    private String getViewURL(){
        String url = API.VIEWED();
        return url;
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

//  expandable text
    public void eventExpandableTextView(final String content, final String description){
        final ExpandableTextView txtDescription = findViewById(R.id.expTxt_description_location_detail);
        final ImageView imgExpandArrow = findViewById(R.id.expand_button);
        txtDescription.setText(Html.fromHtml(content));

        txtDescription.setExpandInterpolator(new OvershootInterpolator());

        txtDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtDescription.isExpanded()){
                    txtDescription.collapse();
                    txtDescription.setText(Html.fromHtml(content));
                    imgExpandArrow.setImageResource(R.drawable.ic_arrow_down_20);
                }
                else {
                    txtDescription.expand();
                    txtDescription.setText(Html.fromHtml(description));
                    imgExpandArrow.setImageResource(R.drawable.ic_arrow_up_20);
                }
            }
        });

        imgExpandArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtDescription.isExpanded()){
                    txtDescription.collapse();
                    txtDescription.setText(Html.fromHtml(content));
                    imgExpandArrow.setImageResource(R.drawable.ic_arrow_down_20);
                }
                else {
                    txtDescription.expand();
                    txtDescription.setText(Html.fromHtml(description));
                    imgExpandArrow.setImageResource(R.drawable.ic_arrow_up_20);
                }
            }
        });
    }

//    like and comment event
    public void eventLikeComment(final long Liked, final long Comment, final String LocationName, final String Address){
        txtAmountOfLike.setText(String.valueOf(Liked));
        txtAmountOfComment.setText(String.valueOf(Comment));

        imgBtnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtAmountOfLike.setText(String.valueOf(Liked + 1));
                imgLike.setImageResource(R.drawable.ic_heart_active);
                imgBtnLike.setClickable(false);

                if(latitude != 0.00 & longitude != 0.00 & !infoPlatform.equals("")) {
                    callLikeAPI(new getParams() {
                        @Override
                        public void setParams(Map<String, String> params) {
                            params.put("HerID", locationID);
                            params.put("Latitude", String.valueOf(latitude));
                            params.put("Longitude", String.valueOf(longitude));
                            params.put("UserName", "Ẩn Danh");
                            params.put("InforPlatform", infoPlatform);
                        }
                    });
                }else if(latitude == 0.00 | longitude == 0.00){
                    callLikeAPI(new getParams() {
                        @Override
                        public void setParams(Map<String, String> params) {
                            params.put("HerID", locationID);
                            params.put("Latitude", "Null");
                            params.put("Longitude", "Null");
                            params.put("UserName", "Ẩn Danh");
                            params.put("InforPlatform", infoPlatform);
                        }
                    });
                }else if(infoPlatform.equals("")){

                    callLikeAPI(new getParams() {
                        @Override
                        public void setParams(Map<String, String> params) {
                            params.put("HerID", locationID);
                            params.put("Latitude", String.valueOf(latitude));
                            params.put("Longitude", String.valueOf(longitude));
                            params.put("UserName", "Ẩn Danh");
                            params.put("InforPlatform", "Không lấy được thông tin thiết bị");
                        }
                    });
                }
            }
        });

        imgBtnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getCommentActivity(Comment, LocationName, Address);
            }
        });
    }

    //    go to comment activity
    private void getCommentActivity(long comment, String LocationName, String  Address){
            Intent commentActivity = new Intent(this, CommentActivity.class);
            Bundle commentBundle = new Bundle();
            commentBundle.putString("ID", locationID);
            commentBundle.putString("LocationName", LocationName);
            commentBundle.putString("Address", Address);
            commentBundle.putLong("Commented", comment);
            commentBundle.putInt("CurrentPage", commentPage);
            commentBundle.putSerializable("List", commentList);
            commentBundle.putDouble("latitude", latitude);
            commentBundle.putDouble("longitude", longitude);
            commentBundle.putString("infoPlatform", getInfoPlatform());
            commentActivity.putExtra("Data", commentBundle);
            startActivity(commentActivity);
    }

    //    request like API
    private void callLikeAPI(final getParams getParams){
        String url = API.LIKED();
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


//  Get info of device
    private String getInfoPlatform(){
        String anroidVersion = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        String modelDevice = Build.MODEL;
        return "android: " + anroidVersion  + " API level: "+ String.valueOf(sdkVersion) + " Model: " + modelDevice;
    }

//    Get comment data
    public void getListCommentAPI(){
        String url = getCommentURL();
        //Log.e("CommentURL", url);
        StringRequest commentRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseComment(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        VolleySingleton.getInStance(this).getRequestQueue().add(commentRequest);
    }

    private void parseComment(String json){
        Log.e("jsonComent", json);
        try {
            JSONObject root = new JSONObject(json);
            Log.e("CommentLocationDetail", json);

            JSONArray pdata = root.getJSONArray("pdata");
            for (int i = 0; i< pdata.length(); i++){
                JSONObject element = pdata.getJSONObject(i);

                commentList.add(new userComment(element.getString("UserName"),
                        element.getString("Contents"),
                        element.getString("PostTime")));
            }

            if(commentList.size() > 0) {
                commentAdapter.userComments = commentList;
                Log.e("size", String.valueOf(commentAdapter.getItemCount()) + " " + String.valueOf(commentAdapter.getItemViewType(1)));
                commentAdapter.notifyDataSetChanged();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ListCommentResponse", "parse failse");
        }
    }

    // init comment adapter
    private void setCommentAdapter(){

        recyclerViewComment.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewComment.setLayoutManager(linearLayoutManager);

        //divider recycler item list
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewComment.getContext(), DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.comment_divider);
        dividerItemDecoration.setDrawable(drawable);
        recyclerViewComment.addItemDecoration(dividerItemDecoration);

        commentAdapter = new rcvCommentAdapter(commentList, this, "LocationDetail");
        recyclerViewComment.setAdapter(commentAdapter);

    }

    // expand and collapse the location content
    // goi qua -> worker asyntask

    private void parseJson(String json){
        new parseJsonLocationDetail(this).execute(json);
    }

//    request location detail data
    private void callAPI(String LocationID){
        String url = getURL(LocationID);
        //Log.e("URLDetail", url);
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("LocationDetail response", response);
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


//  URL for location detail data
    private String getURL(String currentPage){
        String url = API.LOCATION_DETAIL() + currentPage;
        return url;
    }
//  URL comment
    private String getCommentURL(){
        String url = API.GET_COMMENTED() + locationID + "/" + commentPage;
        return url;
    }

    // encode user current location


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

//                Log.e("asc","longitude: "+ String.valueOf(longitude)+","  +"latitude: "+String.valueOf(latitude));
                callViewAPI();
                sendRequest(CurrentLocation(latitude,longitude), Destination);
            }

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
            txtLocationDistance.setText(route.distance.text);
        }
//        localRoute = new ArrayList<>();
//        localRoute = routes;
    }

}
