package com.hrtgo.heritagego.heritagego.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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

import java.util.ArrayList;
import java.util.HashMap;

public class navMapsfrag extends Fragment implements OnMapReadyCallback {

    GoogleMap mMap;
    MapView mMapView;
    View view;
    private Double Latitude = 0.00;
    private Double Longitude = 0.00;


    ArrayList<HashMap<String, String>> location = new ArrayList<>();
    HashMap<String, String> map;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());
        mMap = googleMap;

        markersMarker();
    }

    private void markersMarker(){
        String url = getURL();
        Log.e("URLDetail", url);
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
                Latitude = Double.parseDouble(location.get(0).get("Latitude").toString());
                Longitude = Double.parseDouble(location.get(0).get("Longitude").toString());
                LatLng coordinate = new LatLng(Latitude, Longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 17));

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
                        intent.putExtra("ID", Integer.valueOf(marker.getSnippet()));
                        Log.e("markers", marker.getSnippet());
                        startActivity(intent);
                    }
                });

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
        String url = getString(R.string.request_markers);
        return url;
    }
}
