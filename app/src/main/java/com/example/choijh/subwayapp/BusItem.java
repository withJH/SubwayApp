package com.example.choijh.subwayapp;

public class BusItem {

    private String bus_station;
    private String bus_x;
    private String bus_y;
    private String bus_distance;

    public String getBusStation() {        return bus_station;    }

    public void setBusStation(String a) {
        this.bus_station = a;
    }

    public String getBusx() {
        return bus_x;
    }

    public void setBusx(String b) {
        this.bus_x = b;
    }

    public String getBusy() {
        return bus_y;
    }

    public void setBusy(String c) {
        this.bus_y = c;
    }

    public String getBusDistance() {
        return bus_distance;
    }

    public void setBusDistance(String d) {
        this.bus_distance = d;
    }
}
