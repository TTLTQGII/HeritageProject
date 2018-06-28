package com.hrtgo.heritagego.heritagego.DirectionTask;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.List;

public class Route implements Serializable{
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;
}
