package com.hrtgo.heritagego.heritagego.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hrtgo.heritagego.heritagego.API.API;
import com.hrtgo.heritagego.heritagego.Adapter.rcvCommentAdapter;
import com.hrtgo.heritagego.heritagego.Interface.OnLoadMoreListener;
import com.hrtgo.heritagego.heritagego.Interface.getParams;
import com.hrtgo.heritagego.heritagego.Interface.ConnectivityReceiverListener;
import com.hrtgo.heritagego.heritagego.Model.userComment;
import com.hrtgo.heritagego.heritagego.R;
import com.hrtgo.heritagego.heritagego.Worker.VolleySingleton;
import com.hrtgo.heritagego.heritagego.until.customize;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentActivity extends BaseActivity{

    android.support.v7.widget.Toolbar actionToolBar;
    RecyclerView rcvComment;
    TextView txtLocationName, txtLocationAdress, txtAmountOfComment;
    ImageView icApplication, icBackpress;
    String LocationID;
    int listSize;
    // currentPage loaded is 1
    long totalComment = 0;
    int currentPage = 0;
    String locationName, address, infoPlatform, language;
    double latitude, longitude;

    ArrayList<userComment> commentList;
    rcvCommentAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        language = super.mLanguage;
        Log.e("language", language);
        initView();
    }


    private void initView(){
        initCustomizeActionBar();
//        initComnmentBar();

        txtLocationName = findViewById(R.id.txt_location_name_comment_activity);
        txtLocationAdress = findViewById(R.id.txt_location_address_comment_activity);
        txtAmountOfComment = findViewById(R.id.txt_amount_of_comment_activity);
        rcvComment = findViewById(R.id.rcv_comment_activity);

        getIntentData();
        insertComment();

    }

    // customize Action bar
    private void initCustomizeActionBar(){
        actionToolBar = findViewById(R.id.action_tool_bar_custom_comment);
        if(actionToolBar != null) {
            setSupportActionBar(actionToolBar);
            ActionBar actionBar = getSupportActionBar();
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customAcionBar = inflater.inflate(R.layout.tool_action_bar_customize, null);
            actionBar.setCustomView(customAcionBar);
            customize.customizeActionBar(actionToolBar, actionBar, customAcionBar);

            iconBackpress();
        }
    }

//    private void initComnmentBar(){
//        commentBar = findViewById(R.id.tool_bar_comment);
//        if(commentBar != null){
//            LayoutInflater inflater = LayoutInflater.from(this);
//            View layoutCommentBar = inflater.inflate(R.layout.tool_bar_comment, null);
//            commentBar.addView(layoutCommentBar);
//        }
//    }


    private void iconBackpress(){
        icBackpress = findViewById(R.id.ic_img_backpress);
        icBackpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getIntentData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("Data");
        if(bundle != null){
            LocationID = bundle.getString("ID");
            currentPage = bundle.getInt("CurrentPage");

            locationName = bundle.getString("LocationName");
            txtLocationName.setText(locationName);

            address = bundle.getString("Address");
            txtLocationAdress.setText(address);

            latitude = bundle.getDouble("latitude");
            longitude = bundle.getDouble("longitude");
            Log.e("commentActivity", String.valueOf(latitude) + ", " + String.valueOf(longitude));

            infoPlatform = bundle.getString("infoPlatform");

            totalComment = bundle.getLong("Commented");
            txtAmountOfComment.setText(String.valueOf(totalComment));
            commentList = (ArrayList<userComment>) bundle.getSerializable("List");

            //comment list and adapter
            setCommentAdapter();
            if( commentList.size() > 0){
                adapter.userComments = commentList;
                adapter.setType(adapter.view_type_trigger_load);
                adapter.notifyDataSetChanged();

                addTriggerBottomList();
                // delete bottom item if comment list = 0
                if(adapter.countNumberAmountLeft() == 0){
                    removeTriggerBottomList();
                }
            }
            else {
                commentList = new ArrayList<>();
                callComentAPI(getCommentURL(currentPage));
            }
        }
    }

    // initial rcvCommentAdapter
    private void setCommentAdapter(){

        rcvComment.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvComment.setLayoutManager(linearLayoutManager);

        //divider recycler item list
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rcvComment.getContext(), DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.comment_divider);
        dividerItemDecoration.setDrawable(drawable);
        rcvComment.addItemDecoration(dividerItemDecoration);

        adapter = new rcvCommentAdapter(commentList, this, "CommentActivity", totalComment);
        rcvComment.setAdapter(adapter);

        onLoadmoreRCV();
    }

//    get comment URL
    private String getCommentURL(int currentPage){
        String url = API.GET_COMMENTED() + LocationID + "/" + currentPage;
        return url;
    }

//    send request to API
    private void callComentAPI(String URL){
        String url = URL;

        StringRequest commentRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleySingleton.getInStance(this).getRequestQueue().add(commentRequest);
    }

    private void parseJson(String json){
        try {
            JSONObject root = new JSONObject(json);

            JSONArray pdata = root.getJSONArray("pdata");

            // delete null element of comment list before add more data to comment list
            if(commentList.size() != 0){
                removeTriggerBottomList();
            }

            for (int i = 0; i< pdata.length(); i++){
                JSONObject element = pdata.getJSONObject(i);

                commentList.add(new userComment(element.getString("UserName"),
                        element.getString("Contents"),
                        element.getString("PostTime")));
            }

            Log.e("sizeList", String.valueOf(pdata.length()) + " " + commentList.size());

            // notify data changed after download and parse json
            if(commentList.size() > 0) {
                adapter.userComments = commentList;
                adapter.notifyDataSetChanged();
                addTriggerBottomList();
            }

            // delete null element in comment list if there is nothing to download
            if(adapter.countNumberAmountLeft() <= 0){
                removeTriggerBottomList();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ListCommentResponse", "parse failse");
        }
    }

    private void insertComment(){
        final EditText edtComment;
        edtComment = findViewById(R.id.txt_comment);
        edtComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEND){

                    final String comment = edtComment.getText().toString();

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(edtComment.getWindowToken(), 0);

                    callSaveCommentAPI(new getParams() {
                        @Override
                        public void setParams(Map<String, String> params) {
                            params.put("HerID", LocationID);
                            params.put("Latitude", String.valueOf(latitude));
                            params.put("Longitude", String.valueOf(longitude));
                            params.put("InforPlatform", infoPlatform);
                            params.put("UserName", "Ẩn Danh");
                            params.put("Contents", comment);

                            Log.e("Params", params.toString());
                        }
                    });

                    edtComment.setText("");

                    return true;
                }

                return false;
            }
        });
    }

    private void callSaveCommentAPI(final getParams getParams){
        String url = API.SAVE_COMMENT();
        StringRequest insertComment = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                commentList.clear(); Chua load lai list comment sau khi save
//                adapter.userComments.clear();
//                currentPage = 1;
//                callComentAPI(getCommentURL(1));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CommentActivity.this, "Failed to post comment", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                getParams.setParams(params);
                return params;
            }
        };

        insertComment.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInStance(this).getRequestQueue().add(insertComment);
    }

    private void onLoadmoreRCV(){
        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
               changeViewTypeAdapter(adapter.view_type_loadmore);
               currentPage++;
               //Log.e("currentPage", String.valueOf(currentPage));
               new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       callComentAPI(getCommentURL(currentPage));
                   }
               }, 2000);
            }
        });
    }

    private void changeViewTypeAdapter(int viewType){
        adapter.notifyItemRemoved(adapter.userComments.size() - 1);
        adapter.setType(viewType);
        adapter.notifyItemInserted(adapter.userComments.size() - 1);
    }

    private void addTriggerBottomList(){
        commentList.add(null);
        adapter.setType(adapter.view_type_trigger_load);
        adapter.notifyItemInserted(adapter.userComments.size() - 1);
    }

    private void removeTriggerBottomList(){
        commentList.remove(commentList.size() - 1);
        adapter.userComments = commentList;
        adapter.notifyItemRemoved(adapter.userComments.size() - 1);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }
}
