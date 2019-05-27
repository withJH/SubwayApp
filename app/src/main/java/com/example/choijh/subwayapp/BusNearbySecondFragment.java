package com.example.choijh.subwayapp;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class BusNearbySecondFragment extends Fragment {

    public BusNearbySecondFragment() {
    }

    public static BusNearbySecondFragment newInstance(int page){
        Bundle args = new Bundle();

        BusNearbySecondFragment fragment = new BusNearbySecondFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bus_nearby_two, container, false);
        StrictMode.enableDefaults();

        return view;
    }
}
