package com.hrtgo.heritagego.heritagego.Worker;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.toolbox.StringRequest;
import com.hrtgo.heritagego.heritagego.Fragment.tabFamousHome;
import com.hrtgo.heritagego.heritagego.Model.heritageInfoHomeModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class parseJsonFamousTab extends AsyncTask<String, Void, ArrayList<heritageInfoHomeModel>> {

    tabFamousHome tabFamousHome;
    private static final String TAG = "Location Json";

    public parseJsonFamousTab(com.hrtgo.heritagego.heritagego.Fragment.tabFamousHome tabFamousHome) {
        this.tabFamousHome = tabFamousHome;
    }

    @Override
    protected ArrayList<heritageInfoHomeModel> doInBackground(String... strings) {
        ArrayList<heritageInfoHomeModel> listData = new ArrayList<>();

        String ID = "", Name = "", Like = "", Viewed = "", imagePath = "";

        try {
            JSONObject parentObject = new JSONObject(strings[0]);

            JSONArray pdataArray = parentObject.getJSONArray("pdata");

            for (int i = 0; i < pdataArray.length(); i++){
                JSONObject location = pdataArray.getJSONObject(i);

                ID = location.getString("ID");
                Name = location.getString("Name");
                Like = location.getString("Liked");
                Viewed = location.getString("Viewed");

                listData.add(new heritageInfoHomeModel(ID, Name, Like, Viewed));
            }

        } catch (JSONException e) {
            Log.e(TAG,e.toString());
        }

        return listData;
    }

    @Override
    protected void onPostExecute(ArrayList<heritageInfoHomeModel> heritageInfoHomeModels) {
        super.onPostExecute(heritageInfoHomeModels);

        tabFamousHome.setHomeRecyclerView(heritageInfoHomeModels);
    }
}
