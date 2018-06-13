package com.hrtgo.heritagego.heritagego.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hrtgo.heritagego.heritagego.R;

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

        // Location 1

        map = new HashMap<String, String>();
        map.put("LocationID", "1");
        map.put("Latitude", "13.860633");
        map.put("Longitude", "100.612155");
        map.put("LocationName", "Ladplakao 76");
        location.add(map);

// Location 2
        map = new HashMap<String, String>();
        map.put("LocationID", "2");
        map.put("Latitude", "13.858747");
        map.put("Longitude", "100.610996");
        map.put("LocationName", "Ladplakao 70");
        location.add(map);

// Location 3
        map = new HashMap<String, String>();
        map.put("LocationID", "3");
        map.put("Latitude", "13.863903");
        map.put("Longitude", "100.614343");
        map.put("LocationName", "Ladplakao 80");
        location.add(map);


// * Focus & Zoom
        Latitude = Double.parseDouble(location.get(0).get("Latitude").toString());
        Longitude = Double.parseDouble(location.get(0).get("Longitude").toString());
        LatLng coordinate = new LatLng(Latitude, Longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 14));

// * Marker (Loop)

        for (int i = 0; i < location.size(); i++) {
            Latitude = Double.parseDouble(location.get(i).get("Latitude").toString());
            Longitude = Double.parseDouble(location.get(i).get("Longitude").toString());
            String name = location.get(i).get("LocationName").toString();
            MarkerOptions marker = new MarkerOptions().position(new LatLng(Latitude, Longitude)).title(name);
            // marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
            googleMap.addMarker(marker);
        }
    }
}
