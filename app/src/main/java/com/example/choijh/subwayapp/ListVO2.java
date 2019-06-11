package com.example.choijh.subwayapp;

public class ListVO2 {
    private String nearStationName;
    private String nearBus;
    private String nearLocation;
    private String nearTime;

    public String getSN() {
        return nearStationName;
    }

    public void setSN(String nearStationName) {
        this.nearStationName = nearStationName;
    }

    public String getB() {
        return nearBus;
    }

    public void setB(String nearBus) {
        this.nearBus = nearBus;
    }

    public String getL() {
        return nearLocation;
    }

    public void setL(String nearLocation) { this.nearLocation = nearLocation; }

    public String getT() {
        return nearTime;
    }

    public void setT(String nearTime) {
        this.nearTime = nearTime;
    }
}
