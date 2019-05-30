package com.example.choijh.subwayapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper {

    private static final String DATABASE_NAME = "Subway_App.db";
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
            db.execSQL(DataBases.StationCreateDB._CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("ALTER TABLE "+ DataBases.StationCreateDB._TABLENAME+" ADD COLUMN "+DataBases.StationCreateDB.FAVORITE);
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

    public long insert(String station_code, String station_name, String station_line , String station_fr, int favorite){ // 데이터 삽입
        ContentValues values = new ContentValues();
        values.put(DataBases.StationCreateDB.CODE, station_code);
        values.put(DataBases.StationCreateDB.NAME, station_name);
        values.put(DataBases.StationCreateDB.LINE, station_line);
        values.put(DataBases.StationCreateDB.FR, station_fr);
        values.put(DataBases.StationCreateDB.FAVORITE, favorite);
        return mDB.insert(DataBases.StationCreateDB._TABLENAME, null, values);
    }

    public boolean update(String station_code, int update_F){
        ContentValues values = new ContentValues();
        values.put(DataBases.StationCreateDB.FAVORITE, update_F);
        return mDB.update(DataBases.StationCreateDB._TABLENAME, values, "station_code = ?", new String[] {station_code}) > 0;
    }

    public Cursor selectColumns(){ // 데이터 검색 (자세한 사용법은 Subway_search.java 파일에서 사용)
        return mDB.query(DataBases.StationCreateDB._TABLENAME, null, null, null, null, null, null);
    }

//    Favorite 인 항목들만 Select 한다. ( 조건 1일 경우 )
    public Cursor selectFavorite(){
        return mDB.rawQuery("SELECT * FROM " + DataBases.StationCreateDB._TABLENAME + " WHERE " + DataBases.StationCreateDB.FAVORITE + " = 1", null);
    }

    public boolean confirmTable(){ // 테이블 유무 확인
        Cursor cursor = mDB.rawQuery("SELECT * FROM 'Station_info'", null);
        cursor.moveToFirst();

        if(cursor.getCount()>0){
            return true;
        }else{
            return false;
        }
    }


}