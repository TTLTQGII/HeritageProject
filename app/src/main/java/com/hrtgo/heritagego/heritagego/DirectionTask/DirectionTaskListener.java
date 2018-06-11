package com.hrtgo.heritagego.heritagego.DirectionTask;

import java.util.List;

public interface DirectionTaskListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
