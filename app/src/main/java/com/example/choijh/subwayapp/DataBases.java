package com.example.choijh.subwayapp;

import android.provider.BaseColumns;

public final class DataBases {

    public static final class StationCreateDB implements BaseColumns {

        public static final String CODE = "station_code";
        public static final String NAME = "station_name";
        public static final String LINE = "station_line";
        public static final String FR = "station_fr";
        public static final String _TABLENAME = "Station_info";

        public static final String _CREATE = "create table if not exists "+_TABLENAME+"("
                +CODE+" text not null primary key , "
                +NAME+" text not null , "
                +LINE+" text not null , "
                +FR+" text not null );";
    }
}
