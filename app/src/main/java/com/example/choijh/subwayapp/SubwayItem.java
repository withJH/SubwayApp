package com.example.choijh.subwayapp;

public class SubwayItem {

    private String subway_code;
    private String subway_name;
    private String line;
    private String fr_code;
    private String favorite;

    public SubwayItem(){

    }

    public SubwayItem(String code, String name, String line, String fr, String favorite) {
        subway_code = code;
        subway_name = name;
        this.line = line;
        fr_code = fr;
        this.favorite = favorite;
    }

    public String getSubway_code() {
        return subway_code;
    }

    public void setSubway_code(String code) {
        this.subway_code = code;
    }

    public String getSubway_name() {
        return subway_name;
    }

    public void setSubway_name(String name) {
        this.subway_name = name;
    }

    public String getFr_code() {
        return fr_code;
    }

    public void setFr_code(String code) {
        this.fr_code = code;
    }


    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }
}