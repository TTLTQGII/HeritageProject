package com.hrtgo.heritagego.heritagego.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hrtgo.heritagego.heritagego.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class imgListAdapterLocationDetail extends PagerAdapter {

    ArrayList<String> imgPath;
    Context context;
    ViewPager imgContainer;

    public imgListAdapterLocationDetail(ArrayList<String> imgPath, Context context, ViewPager container) {
        this.imgPath = imgPath;
        this.context = context;
        this.imgContainer = container;
    }

    @Override
    public int getCount() {
        return imgPath.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // chuyen img_viewpager_location_detail thanh child view
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.img_viewpager_location_detail, container, false);
        // anh xa
        ImageView imgLocationDetail = view.findViewById(R.id.img_viewpager_location_detail);
        // do du lieu
        String Url = context.getResources().getString(R.string.request_image) + imgPath.get(position);
        Log.e("lcDetailImg", Url);
        int width = imgContainer.getWidth();
        int height = imgContainer.getHeight();
        Picasso.get().load(Url).resize(width, height).into(imgLocationDetail);
        // add view child to Viewgroup
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
