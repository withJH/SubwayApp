package com.example.choijh.subwayapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class DetailPagerAdapter extends FragmentStatePagerAdapter {
    private int mPageCount;
    private String stationName;
    private String stationCode;

    public DetailPagerAdapter(FragmentManager fm, int mPageCount, String stationName, String stationCode) {
        super(fm);
        this.mPageCount = mPageCount;
        this.stationName = stationName;
        this.stationCode = stationCode;

        /*
        DetailPageOneFragment fragment = new DetailPageOneFragment(); // Fragment 생성
        DetailPageTwoFragment fragment2 = new DetailPageTwoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("station", stationName);
        bundle.putString("code", stationCode);// Key, Value
        fragment.setArguments(bundle);
        fragment2.setArguments(bundle);
        */
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return DetailPageOneFragment.newInstance(0, stationName, stationCode);
            case 1:
                return DetailPageTwoFragment.newInstance(1, stationName, stationCode);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mPageCount;
    }
}
