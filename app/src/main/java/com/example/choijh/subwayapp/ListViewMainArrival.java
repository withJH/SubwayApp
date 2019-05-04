package com.example.choijh.subwayapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ListViewMainArrival{
    private String stationStr;
    private String titleStr ;
    private String descStr ;

    public void setStation(String stat) {
        stationStr = stat ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setDesc(String desc) {
        descStr = desc ;
    }

    public String getStation() {
        return this.stationStr ;
    }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getDesc() {
        return this.descStr ;
    }
}
