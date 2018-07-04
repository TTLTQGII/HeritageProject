package com.hrtgo.heritagego.heritagego.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



import com.hrtgo.heritagego.heritagego.Fragment.tabFamousHome;
import com.hrtgo.heritagego.heritagego.Fragment.tabMostViewedHome;
import com.hrtgo.heritagego.heritagego.Fragment.tabNearHome;
import com.hrtgo.heritagego.heritagego.Fragment.tabMyFavoriteHome;
import com.hrtgo.heritagego.heritagego.R;


public class navBottomAdapterViewpagerHome extends FragmentPagerAdapter{

    Context context;

    public navBottomAdapterViewpagerHome(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    // get position on viewpager
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch(position){
            case 0:
                fragment = new tabFamousHome();
                break;
            case 1:
                fragment = new tabNearHome();
                break;
            case 2:
                fragment = new tabMostViewedHome();
                break;
            case 3:
                fragment = new tabMyFavoriteHome();
                break;
            default:
                fragment = new tabFamousHome();
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
                title = context.getString(R.string.home_tab_famous_en);
                break;
            case 1:
                title = context.getString(R.string.home_tab_near_en);
                break;
            case 2:
                title = context.getString(R.string.home_tab_mostview_en);
                break;
            case 3:
                title = context.getString(R.string.home_tab_myfavorite_en);
                break;
        }
        return title;
    }

}
