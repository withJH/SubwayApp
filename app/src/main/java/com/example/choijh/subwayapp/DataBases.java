package com.example.choijh.subwayapp;

import android.provider.BaseColumns;

public final class DataBases {

    public static final class CreateDB implements BaseColumns {
        // 지하철 column
        public static final String STATIONCODE = "station_code";
        public static final String STATIONNAME = "station_name";
        public static final String STATIONLINE = "station_line";
        public static final String STATIONFR = "station_fr";
        public static final String STATIONFAVORITE = "favorite_station";

        // 지하철 테이블 생성
        public static final String STATION_TABLENAME = "Station_info";
        public static final String STATION_CREATE = "create table if not exists "+STATION_TABLENAME+"("
                +STATIONCODE+" text not null primary key , "
                +STATIONNAME+" text not null , "
                +STATIONLINE+" text not null , "
                +STATIONFR+" text not null ,"
                +STATIONFAVORITE+" int null);";

        // 도시코드 column
        public static final String CITYCODE ="city_code";
        public static final String CITYNAME ="city_name";

        // 도시코드 테이블 생성
        public static final String CITY_TABLENAME = "City";
        public static final String CITY_CREATE = "create table if not exists "+CITY_TABLENAME+"("
                +CITYCODE+" text not null primary key , "
                +CITYNAME+" text not null );";

        // 버스 노선 column
        public static final String BUSROUTEID = "route_id";
        public static final String BUSROUTENUMBER = "route_num";
        public static final String BUSROUTETYPE = "route_type";
        public static final String BUSSTARTNO = "start_nodenm";
        public static final String BUSENDNO = "end_nodenm";
        public static final String BUSFAVORITE = "favorite_bus";

        // 버스 테이블 생성
        public static final String BUS_TABLENAME = "Bus_info";
        public static final String BUS_CREATE = "create table if not exists "+BUS_TABLENAME+"("
                +BUSROUTEID+" text primary key , "
                +CITYCODE+" text , "
                +BUSROUTENUMBER+" text , "
                +BUSROUTETYPE+" text , "
                +BUSSTARTNO+" text ,"
                +BUSENDNO+" text ,"
                +BUSFAVORITE+" int );";
    }
}
