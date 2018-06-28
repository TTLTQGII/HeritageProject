package com.hrtgo.heritagego.heritagego.Worker;

import android.os.AsyncTask;
import android.util.Log;

import com.hrtgo.heritagego.heritagego.Activity.LocationDetail;
import com.hrtgo.heritagego.heritagego.Model.heritageLocationDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class parseJsonLocationDetail extends AsyncTask<String, Void, ArrayList<heritageLocationDetail>> {

    LocationDetail locationDetail;

    public parseJsonLocationDetail(LocationDetail locationDetail) {
        this.locationDetail = locationDetail;
    }


    @Override
    protected ArrayList<heritageLocationDetail> doInBackground(String... strings) {
        ArrayList<heritageLocationDetail> listData = new ArrayList<>();
        ArrayList<String> imgPaths = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(strings[0]);

            JSONArray imageData = root.getJSONArray("imagedata");
            for (int i = 0; i < imageData.length(); i ++) {
                JSONObject ImagePath = imageData.getJSONObject(i);
                imgPaths.add(ImagePath.getString("ImagePath"));
            }

            String destination = root.getString("Latitude") + " " + root.getString("Longitude");
            Log.e("destination", destination);

            listData.add(new heritageLocationDetail(root.getString("Name"),
                    root.getString("TimeofBuild"),
                    root.getString("Address"),
                    root.getString("Contents"),
                    root.getString("Description"),
                    root.getInt("Liked"),
                    root.getInt("Viewed"),
                    root.getInt("Commented"),
                    imgPaths,
                    destination));

            Log.e("ListDetail", listData.get(0).toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listData;
    }

    @Override
    protected void onPostExecute(ArrayList<heritageLocationDetail> heritageLocationDetails) {
        super.onPostExecute(heritageLocationDetails);

        locationDetail.bindData(heritageLocationDetails.get(0).getName(),
                heritageLocationDetails.get(0).getAddress(),
                heritageLocationDetails.get(0).getViewed());

        locationDetail.eventViewPager(heritageLocationDetails.get(0).getImagePath());

        locationDetail.eventLikeComment(heritageLocationDetails.get(0).getLiked(),
                heritageLocationDetails.get(0).getAmountOfComment(),
                heritageLocationDetails.get(0).getName(),
                heritageLocationDetails.get(0).getAddress());

        locationDetail.eventExpandableTextView(heritageLocationDetails.get(0).getContents(),heritageLocationDetails.get(0).getDescription());

        locationDetail.Destination = heritageLocationDetails.get(0).getDestination();
        locationDetail.getUserLocation();
        locationDetail.getDirectionActivity();

        locationDetail.getListCommentAPI();
    }
}

