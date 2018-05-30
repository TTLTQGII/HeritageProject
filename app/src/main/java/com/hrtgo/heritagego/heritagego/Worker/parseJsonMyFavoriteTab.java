package com.hrtgo.heritagego.heritagego.Worker;

import android.os.AsyncTask;
import android.util.Log;

import com.hrtgo.heritagego.heritagego.Model.heritageInfoHomeModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class parseJsonMyFavoriteTab  extends AsyncTask<String, Void, ArrayList<heritageInfoHomeModel>> {

    com.hrtgo.heritagego.heritagego.Fragment.tabMyFavoriteHome tabMyFavoriteHome;
    private static final String TAG = "Location Json";

    public parseJsonMyFavoriteTab(com.hrtgo.heritagego.heritagego.Fragment.tabMyFavoriteHome tabMyFavoriteHome) {
        this.tabMyFavoriteHome = tabMyFavoriteHome;
    }

    @Override
    protected ArrayList<heritageInfoHomeModel> doInBackground(String... strings) {

        ArrayList<heritageInfoHomeModel> listData = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(strings[0]);

            JSONArray pdataArray = root.getJSONArray("pdata");

            for (int i = 0; i < pdataArray.length(); i++){
                JSONObject location = pdataArray.getJSONObject(i);

                listData.add(new heritageInfoHomeModel(location.getInt("ID")
                        ,location.getString("Name")
                        ,location.getInt("Liked")
                        ,location.getInt("Viewed")
                        ,location.getString("ImagePath")));
            }

        } catch (JSONException e) {
            Log.e(TAG,e.toString());
        }

        return listData;
    }

    @Override
    protected void onPostExecute(ArrayList<heritageInfoHomeModel> heritageInfoHomeModels) {
        tabMyFavoriteHome.setRecyclerView(heritageInfoHomeModels);
    }
}
