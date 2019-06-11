package com.example.choijh.subwayapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper {

    private static final String DATABASE_NAME = "OZ_App.db";
    private static final int DATABASE_VERSION = 2;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mContext;

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(DataBases.CreateDB.STATION_CREATE);
            db.execSQL(DataBases.CreateDB.CITY_CREATE);
            db.execSQL(DataBases.CreateDB.BUS_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("ALTER TABLE "+ DataBases.CreateDB.STATION_TABLENAME+" ADD COLUMN "+DataBases.CreateDB.STATIONFAVORITE);
            onCreate(db);
        }
    }

    public DBOpenHelper(Context context){
        this.mContext = context;
    } // 생성자

    public DBOpenHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void create(){
        mDBHelper.onCreate(mDB);
    }

    public void close(){
        mDB.close();
    }

    public long StationDB_insert(String station_code, String station_name, String station_line , String station_fr, int favorite){ // 데이터 삽입
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.STATIONCODE, station_code);
        values.put(DataBases.CreateDB.STATIONNAME, station_name);
        values.put(DataBases.CreateDB.STATIONLINE, station_line);
        values.put(DataBases.CreateDB.STATIONFR, station_fr);
        values.put(DataBases.CreateDB.STATIONFAVORITE, favorite);
        return mDB.insert(DataBases.CreateDB.STATION_TABLENAME, null, values);
    }

    public long CityDB_insert(String city_code, String city_name){
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.CITYCODE, city_code);
        values.put(DataBases.CreateDB.CITYNAME, city_name);
        return mDB.insert(DataBases.CreateDB.CITY_TABLENAME, null, values);
    }

    public long BusDB_insert(String route_id, String city_code, String route_num, String route_type, String start, String end, int favorite){ // 데이터 삽입
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.BUSROUTEID, route_id);
        values.put(DataBases.CreateDB.CITYCODE, city_code);
        values.put(DataBases.CreateDB.BUSROUTENUMBER, route_num);
        values.put(DataBases.CreateDB.BUSROUTETYPE, route_type);
        values.put(DataBases.CreateDB.BUSSTARTNO, start);
        values.put(DataBases.CreateDB.BUSENDNO, end);
        values.put(DataBases.CreateDB.BUSFAVORITE, favorite);
        return mDB.insert(DataBases.CreateDB.BUS_TABLENAME, null, values);
    }

    public boolean StationDB_update(String station_code, int update_F){
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.STATIONFAVORITE, update_F);
        return mDB.update(DataBases.CreateDB.STATION_TABLENAME, values, "station_code = ?", new String[] {station_code}) > 0;
    }

    public boolean BusDB_update(String routeid, int update_F){
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.BUSFAVORITE, update_F);
        return mDB.update(DataBases.CreateDB.BUS_TABLENAME, values, "route_id = ?", new String[] {routeid}) > 0;
    }

    public Cursor City_Select(){ // 데이터 검색 (자세한 사용법은 Subway_search.java 파일에서 사용)
        Cursor c = mDB.query(DataBases.CreateDB.CITY_TABLENAME, null, null, null, null, null, null);
        return c;
    }

//    Favorite 인 항목들만 Select 한다. ( 조건 1일 경우 )
    public Cursor Station_SelectFavorite(){
        Cursor c = mDB.rawQuery("SELECT * FROM " + DataBases.CreateDB.STATION_TABLENAME + " WHERE " + DataBases.CreateDB.STATIONFAVORITE + " = 1", null);
        return c;
    }

    public Cursor Bus_SelectFavorite(){
        Cursor c = mDB.rawQuery("SELECT * FROM " + DataBases.CreateDB.BUS_TABLENAME + " WHERE " + DataBases.CreateDB.BUSFAVORITE + " = 1", null);
        return c;
    }


    public boolean Station_ConfirmTable(){ // 테이블 유무 확인
        Cursor cursor = mDB.rawQuery("SELECT * FROM " + DataBases.CreateDB.STATION_TABLENAME + ";", null);
        cursor.moveToFirst();

        if(cursor.getCount()>0){
            cursor.close();
            return true;
        }else{
            cursor.close();
            return false;
        }
    }


    public boolean City_ConfirmTable(){ // 테이블 유무 확인
        Cursor cursor = mDB.rawQuery("SELECT * FROM " + DataBases.CreateDB.CITY_TABLENAME + ";", null);
        cursor.moveToFirst();

        if(cursor.getCount()>0){
            cursor.close();
            return true;
        }else{
            cursor.close();
            return false;
        }
    }

    public boolean Bus_ConfirmTable(){ // 테이블 유무 확인
        Cursor cursor = mDB.rawQuery("SELECT * FROM " + DataBases.CreateDB.BUS_TABLENAME + ";", null);
        cursor.moveToFirst();

        if(cursor.getCount()>0){
            cursor.close();
            return true;
        }else{
            cursor.close();
            return false;
        }
    }

    public Cursor Station_sort(){
        Cursor c = mDB.rawQuery( "SELECT * FROM " + DataBases.CreateDB.STATION_TABLENAME + " ORDER BY " + DataBases.CreateDB.STATIONLINE +", "+DataBases.CreateDB.STATIONNAME + ";", null);

        return c;
    }

    public Cursor Bus_sort(){
        Cursor c = mDB.rawQuery( "SELECT * FROM " + DataBases.CreateDB.BUS_TABLENAME + " ORDER BY " + DataBases.CreateDB.BUSROUTENUMBER +", "+DataBases.CreateDB.BUSROUTETYPE + ";", null);

        return c;
    }

}