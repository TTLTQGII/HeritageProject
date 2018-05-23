package com.hrtgo.heritagego.heritagego.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



import com.hrtgo.heritagego.heritagego.Fragment.homeTabFamous;
import com.hrtgo.heritagego.heritagego.Fragment.homeTabMostViewed;
import com.hrtgo.heritagego.heritagego.Fragment.homeTabNear;
import com.hrtgo.heritagego.heritagego.Fragment.homeTabMyFavorite;


public class navHomeAdapter extends FragmentPagerAdapter{

    public navHomeAdapter(FragmentManager fm) {
        super(fm);
    }

    // get position on viewpager
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch(position){
            case 0:
                fragment = new homeTabFamous();
                break;
            case 1:
                fragment = new homeTabNear();
                break;
            case 2:
                fragment = new homeTabMostViewed();
                break;
            case 3:
                fragment = new homeTabMyFavorite();
                break;
            default:
                fragment = new homeTabFamous();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = "Famous";
                break;
            case 1:
                title = "Near";
                break;
            case 2:
                title = "Most Viewed";
                break;
            case 3:
                title = "My Favorite";
                break;
        }
        return title;
    }

}
