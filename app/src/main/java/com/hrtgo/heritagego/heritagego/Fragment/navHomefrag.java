package com.hrtgo.heritagego.heritagego.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hrtgo.heritagego.heritagego.R;
import com.hrtgo.heritagego.heritagego.Adapter.navBottomAdapterViewpagerHome;
import com.squareup.picasso.Picasso;

public class navHomefrag extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nav_bottom_home_fragment, container, false);

        initeViewAndEvent(view);

        return  view;
    }

    // Use viewpager with tablayout use FragmentStatePagerAdapter
    private void initeViewAndEvent(View view){

        //initial View
        final TabLayout tabLayout;
        ViewPager viewPager;

        tabLayout = view.findViewById(R.id.home_fragment_tab_Layout);
        viewPager = view.findViewById(R.id.home_fragment_tab_viewpagger);

        //initial Event
        navBottomAdapterViewpagerHome adapter = new navBottomAdapterViewpagerHome(getActivity().getSupportFragmentManager(), this.getContext());
        adapter.getItem(0);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //   appBarLayout.setExpanded(true,true);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //appBarLayout.setExpanded(true,true);
            }
        });
    }

}
