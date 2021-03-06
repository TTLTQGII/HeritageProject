package com.hrtgo.heritagego.heritagego.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hrtgo.heritagego.heritagego.DirectionTask.DirectionTask;
import com.hrtgo.heritagego.heritagego.DirectionTask.DirectionTaskListener;
import com.hrtgo.heritagego.heritagego.DirectionTask.Route;
import com.hrtgo.heritagego.heritagego.R;
import com.hrtgo.heritagego.heritagego.until.customize;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends BaseActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,DirectionTaskListener {

    GoogleMap mMap;

    String Destination = "";
    double latitude, longitude;
    android.support.v7.widget.Toolbar actionToolBar;
    ImageView icApplication, icBackpress;
    TextView txtLocationName, txtLocationAddress, txtMovementTime;

    List<Marker> originMarkers = new ArrayList<>();
    List<Marker> destinationMarkers = new ArrayList<>();
    List<Polyline> polylinePaths = new ArrayList<>();
    List<Route> mLocalRoute;
    boolean mDirectionflag = false; // check if route existed
    String mDuration;
    ProgressDialog progressDialog;
    LatLng coordinate;

    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initView();
        initMap();

    }

    private void initView(){
        txtLocationName = findViewById(R.id.txt_location_name);
        txtLocationAddress = findViewById(R.id.txt_location_address);
        txtMovementTime = findViewById(R.id.txt_location_duration);

        initCustomizeActionBar();
        getIntentData();

    }

    @SuppressLint("RestrictedApi")
    private void initMap(){

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
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocation, Looper.myLooper());

            } else {
                //Request Location Permission
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocation, Looper.myLooper());
        }
    }

    // Get data from LocationDetail
    private void getIntentData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("Data");
        if(bundle != null) {
            Destination = bundle.getString("destination");
            CurrentLocation(bundle.getDouble("latitude"), bundle.getDouble("longitude"));
            txtLocationName.setText(bundle.getString("locationName"));
            txtLocationAddress.setText(bundle.getString("Address"));
        }
        Log.e("Destination", String.valueOf(Destination));
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

    // customize Action bar
    private void initCustomizeActionBar(){
        actionToolBar = findViewById(R.id.action_tool_bar_custom_activity_map);
        if(actionToolBar != null) {
            setSupportActionBar(actionToolBar);
            ActionBar actionBar = getSupportActionBar();
            LayoutInflater inflater = (LayoutInflater) MapsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customAcionBar = inflater.inflate(R.layout.tool_action_bar_customize, null);
            actionBar.setCustomView(customAcionBar);
            customize.customizeActionBar(actionToolBar, actionBar, customAcionBar);

            iconBackpress();
        }
    }



    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("RestrictedApi")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(60000); // 1 minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocation, Looper.myLooper());
                googleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocation, Looper.myLooper());
            googleMap.setMyLocationEnabled(true);
        }


        coordinate = new LatLng(16.460293, 107.590843);
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 5));

    }

    //Lấy vị trí hiện tại của thiết bị
    LocationCallback mLocation = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                longitude = location.getLongitude();
                latitude = location.getLatitude();

                //Place current location marker
                //LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                coordinate= new LatLng(location.getLatitude(), location.getLongitude());

                //move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 11));
            }
            if(!mDirectionflag) {
                sendRequest(CurrentLocation(latitude, longitude), Destination);
            }
        }
    };


    public String CurrentLocation(double CuLagitudeA, double CulongiatudeB)
    {
        Log.e("asb",String.valueOf(latitude)+String.valueOf(longitude));
        return CuLagitudeA + " " + CulongiatudeB;
    }



    // gửi request về GetDirectionTask
    private void sendRequest(String origin, String destination) {
        // String origin = CurrentLocation(latitude,longitude);
        Log.e("abc",String.valueOf(latitude) +" "+String.valueOf(longitude));
        //String destination = "10.774604, 106.689337";
        try {
            new DirectionTask( this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
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

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocation, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
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
    public void onDirectionFinderStart() {

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess (List<Route> routes) {
        drawPolylinePath(routes);
    }

    private void drawPolylinePath(List<Route> routes){
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.txt_location_distance)).setText(route.distance.text);
            txtMovementTime.setText(route.duration.text);

            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    @Override
    public void onLocationChanged(Location location) {


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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
}