package com.example.choijh.subwayapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;

import net.daum.android.map.MapView;

public class BusMapFull extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_map_full);
        // 지도 띄워주는 부분
        MapView mapView = new MapView(this);

        ViewGroup mapViewContainer = findViewById(R.id.map_view2);
        mapViewContainer.addView(mapView);
    }


}
