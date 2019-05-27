package com.example.choijh.subwayapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class BusMainFirstFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;

    TextView tv;

    // newInstance constructor for creating fragment with arguments
    public static BusMainFirstFragment newInstance(int page, String title) {
        BusMainFirstFragment fragment = new BusMainFirstFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragment.setArguments(args);
        return fragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bus_main_first, container, false);
        tv = (TextView)view.findViewById(R.id.fratext1);
        if(((Bus_main)Bus_main.mBusmain).bdata.length > 0)
            tv.setText(((Bus_main)Bus_main.mBusmain).bdata[0]);
        return view;
    }
}