package com.example.choijh.subwayapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class Subway_search extends AppCompatActivity {
    ListView station_list; // 리스트 뷰
    ArrayList<SubwayItem> tmp;
    SearchAdapter searchAdapter = new SearchAdapter(); // 어댑터
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway_search);

        // 임시로 넣어둔 툴바
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.enableDefaults();
        station_list = (ListView) findViewById(R.id.station_list);
        getDB();

        station_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), Subway_detailPage.class);
                intent.putExtra("station", tmp.get(i).getSubway_code());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.subway_search, menu);

        searchView = (SearchView)menu.findItem(R.id.action_subway_search).getActionView();
        // searchview 길이 꽉차게 하기
        searchView.setMaxWidth(Integer.MAX_VALUE);
        // searchview 힌트 메시지
        searchView.setQueryHint("역명으로 검색합니다.");
        searchView.setIconified(false);

        //SearchView의 검색 이벤트
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //검색버튼을 눌렀을 경우
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 0) {
                    station_list.setFilterText(query);
                } else {
                    station_list.clearTextFilter();
                }

                searchView.clearFocus(); // 검색 버튼 클릭 시 키보드 내려가게 하기
                return true;
            }
            //텍스트가 바뀔때마다 호출
            @Override
            public boolean onQueryTextChange(String newText) {
                searchAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }

    private void getDB() {
        DBOpenHelper help = new DBOpenHelper(getApplicationContext());
        help.open();

        tmp = new ArrayList<SubwayItem>();
        Cursor search_cursor = help.Station_sort();
        while(search_cursor.moveToNext()){
            String code = search_cursor.getString(search_cursor.getColumnIndex("station_code"));
            String name = search_cursor.getString(search_cursor.getColumnIndex("station_name"));
            String line = search_cursor.getString(search_cursor.getColumnIndex("station_line"));
            String fr = search_cursor.getString(search_cursor.getColumnIndex("station_fr"));
            String favorite = search_cursor.getString(search_cursor.getColumnIndex("favorite_station"));

            searchAdapter.addItem(code, name, line, fr, favorite); // 어댑터에 데이터 삽입
            tmp.add(new SubwayItem(code, name, line, fr, favorite));
        }

        station_list.setAdapter(searchAdapter); // 리스트뷰에 데이터 올리기
        search_cursor.close();
        help.close();

    }


}





