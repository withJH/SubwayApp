package com.example.choijh.subwayapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class RoutePageAdapter extends FragmentStatePagerAdapter {
    private int mPageCount;

    public RoutePageAdapter(FragmentManager fm, int mPageCount) {
        super(fm);
        this.mPageCount = mPageCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return RoutePageOneFragment.newInstance(0);
            case 1:
                return RoutePageTwoFragment.newInstance(1);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mPageCount;
    }
}
