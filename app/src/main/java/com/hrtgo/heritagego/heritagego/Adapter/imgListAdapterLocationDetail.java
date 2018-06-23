package com.hrtgo.heritagego.heritagego.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import com.hrtgo.heritagego.heritagego.Activity.photoViewActivity;

import java.io.ByteArrayOutputStream;
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
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        // chuyen img_viewpager_location_detail thanh child view
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.img_viewpager_location_detail, container, false);
        // anh xa
        final ImageView imgLocationDetail = view.findViewById(R.id.img_viewpager_location_detail);
        // do du lieu
        String Url = context.getResources().getString(R.string.request_image) + imgPath.get(position);
        Log.e("lcDetailImg", Url);
        final int width = imgContainer.getWidth();
        final int height = imgContainer.getHeight();
        Picasso.get().load(Url).resize(width, height).placeholder(R.drawable.image_place_holder).into(imgLocationDetail);
        // add view child to Viewgroup
        container.addView(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                byte[] imageByte = getImageByte(imgLocationDetail);
                Log.e("returnBitmap", String.valueOf(imageByte.length));

                Intent photoViewActivity = new Intent(context, photoViewActivity.class);
                Bundle bundle = new Bundle();
//                bundle.putString("url", imgPath.get(position));
//                bundle.putInt("width", width);
//                bundle.putInt("height", height);
                bundle.putByteArray("imageByte", imageByte);
                photoViewActivity.putExtra("image", bundle);
                v.getContext().startActivity(photoViewActivity);
            }
        });

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    private byte[] getImageByte(ImageView imgLocation){
        byte[] imgByte;

        BitmapDrawable bitmapDrawable = (BitmapDrawable) imgLocation.getDrawable();
        Bitmap bitmap;
        if(bitmapDrawable != null){
            bitmap = bitmapDrawable.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            imgByte = stream.toByteArray();
            return imgByte;
        }else {
            Log.e("returnBitmap", "null");
            return null;
        }
    }
}
