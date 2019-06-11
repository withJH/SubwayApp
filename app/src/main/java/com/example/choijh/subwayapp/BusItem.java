package com.example.choijh.subwayapp;

public class BusItem {

    private String bus_station;
    private String bus_x;
    private String bus_y;
    private String bus_distance;
    private String routeid;
    private String routeno;
    private String routetp;
    private String endnodenm;
    private String startnodenm;
    private String favorite;

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

    public String getStartnodenm() {
        return startnodenm;
    }

    public void setStartnodenm(String startnodenm) {
        this.startnodenm = startnodenm;
    }

    public String getEndnodenm() {
        return endnodenm;
    }

    public void setEndnodenm(String endnodenm) {
        this.endnodenm = endnodenm;
    }

    public String getRoutetp() {
        return routetp;
    }

    public void setRoutetp(String routetp) {
        this.routetp = routetp;
    }

    public String getRouteno() {
        return routeno;
    }

    public void setRouteno(String routeno) {
        this.routeno = routeno;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getRouteid() {
        return routeid;
    }

    public void setRouteid(String routeid) {
        this.routeid = routeid;
    }
}
