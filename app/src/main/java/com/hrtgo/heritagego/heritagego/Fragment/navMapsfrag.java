package com.hrtgo.heritagego.heritagego.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hrtgo.heritagego.heritagego.Activity.LocationDetail;
import com.hrtgo.heritagego.heritagego.R;
import com.hrtgo.heritagego.heritagego.Worker.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class navMapsfrag extends Fragment implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    GoogleMap mMap;
    MapView mMapView;
    View view;
    private Double Latitude = 0.00;
    private Double Longitude = 0.00;


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;

    double currlatitude, currlongitude;


    //ArrayList<HashMap<String, String>> location = new ArrayList<>();
    HashMap<String, String> map;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUserLocation();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.nav_bottom_maps_fragment, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = view.findViewById(R.id.Fragment_map);
        if (mMapView != null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mMap = googleMap;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this.getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationMapFrag, Looper.myLooper());
                googleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationMapFrag, Looper.myLooper());
            googleMap.setMyLocationEnabled(true);
        }
    }

    private void callAPUMarker(){
        String url = getURL();
        //Log.e("navMapsFrag", url);
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("mapfrag response", response);
                ArrayList<HashMap<String, String>> location = null;
                try {

                    JSONArray data = new JSONArray(response);

                    location = new ArrayList<>();
                    HashMap<String, String> map;

                    for(int i = 0; i < data.length(); i++){
                        JSONObject c = data.getJSONObject(i);

                        map = new HashMap<>();
                        map.put("ID", c.getString("ID"));
                        map.put("Latitude", c.getString("Latitude"));
                        map.put("Longitude", c.getString("Longitude"));
                        map.put("LocationName", c.getString("Name"));
                        location.add(map);

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // *** Focus & Zoom
                if(location.size() != 0) {
                    Latitude = currlatitude;
                    Longitude = currlongitude;
                    LatLng coordinate = new LatLng(Latitude, Longitude);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 15));

                    // *** Marker (Loop) Drawable marker
                    for (int i = 0; i < location.size(); i++) {
                        Latitude = Double.parseDouble(location.get(i).get("Latitude").toString());
                        Longitude = Double.parseDouble(location.get(i).get("Longitude").toString());
                        String name = location.get(i).get("LocationName").toString();
                        String ID = location.get(i).get("ID").toString();
                        MarkerOptions marker = new MarkerOptions().position(new LatLng(Latitude, Longitude)).title(name).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_heritage));
                        marker.snippet(ID);

                        mMap.addMarker(marker);
                    }

                    // go to Location Detail
                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            Intent intent = new Intent(getContext(), LocationDetail.class);
                            intent.putExtra("ID", marker.getSnippet());
                            Log.e("markers", marker.getSnippet());
                            startActivity(intent);
                        }
                    });
                }
                else {
                    Latitude = currlatitude;
                    Longitude = currlongitude;
                    LatLng coordinate = new LatLng(Latitude, Longitude);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 15));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // control error in here
            }
        });

        VolleySingleton.getInStance(getActivity()).getRequestQueue().add(jsonRequest);
    }

    private String getURL(){
        String url = getString(R.string.request_markers) + getEncodedLocation(currlatitude, currlongitude);
        return url;
    }

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

    @SuppressLint("RestrictedApi")
    public void getUserLocation(){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getContext());

        mLocationRequest = new LocationRequest();
        // setInterval...
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this.getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationMapFrag, Looper.myLooper());

            } else {
                //Request Location Permission
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationMapFrag, Looper.myLooper());

        }
    }

    //goi vi tri hien tai
    public LocationCallback mLocationMapFrag = new LocationCallback() {
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

                currlongitude = location.getLongitude();
                currlatitude = location.getLatitude();

                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                // bug o day ne..., ko lay kip vi tri hien tai: solution: goi vi tri hien tai o main activity
               // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

                Log.e("asc","longitude: "+ String.valueOf(currlongitude)+","  +"latitude: "+String.valueOf(currlatitude));
            }

            callAPUMarker();

        }
    };

//    public String CurrentLocation(double CuLagitudeA, double CulongiatudeB)
//    {
//        Log.e("asb",String.valueOf(latitude)+String.valueOf(longitude));
//        return CuLagitudeA + " " + CulongiatudeB;
//    }

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
                    if (ContextCompat.checkSelfPermission(this.getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationMapFrag, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this.getContext(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
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
