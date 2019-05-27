package com.example.choijh.subwayapp;

import android.graphics.drawable.Drawable;

public class ListVO1 {
    private String nearStationName;
    private String nearDistance;
    private String nearBus;

    public String getSN() {
        return nearStationName;
    }

    public void setSN(String nearStationName) {
        this.nearStationName = nearStationName;
    }

    public String getD() {
        return nearDistance;
    }

    public void setD(String nearDistance) {
        this.nearDistance = nearDistance;
    }

    public String getB() {
        return nearBus;
    }

    public void setB(String nearBus) {
        this.nearBus = nearBus;
    }
}
