package com.example.choijh.subwayapp;

import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class Bus_search extends AppCompatActivity {
    ListView bus_list; // 리스트 뷰
    SearchView searchView;
    BusSearchAdapter busAdapter = new BusSearchAdapter();
    DBOpenHelper help;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.bus_search_toolbar);
        setSupportActionBar(toolbar);

        help = new DBOpenHelper(getApplicationContext());
        help.open();
        StrictMode.enableDefaults();


        bus_list = (ListView) findViewById(R.id.bus_list);
        getDB();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bus_search, menu);

        searchView = (SearchView)menu.findItem(R.id.action_bus_search).getActionView();
        // searchview 길이 꽉차게 하기
        searchView.setMaxWidth(Integer.MAX_VALUE);
        // searchview 힌트 메시지
        searchView.setQueryHint("버스 노선 번호로 검색합니다.");
        searchView.setIconified(false);

        //SearchView의 검색 이벤트
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //검색버튼을 눌렀을 경우
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 0) {
                    bus_list.setFilterText(query);
                } else {
                    bus_list.clearTextFilter();
                }

                searchView.clearFocus(); // 검색 버튼 클릭 시 키보드 내려가게 하기
                return true;
            }
            //텍스트가 바뀔때마다 호출
            @Override
            public boolean onQueryTextChange(String newText) {
                busAdapter.getFilter().filter(newText);
                return true;
            }
        });

        return true;
    }

    private void getDB() {

        Cursor search_cursor = help.Bus_sort();
        while(search_cursor.moveToNext()){
            String id = search_cursor.getString(search_cursor.getColumnIndex("route_id"));
            String num = search_cursor.getString(search_cursor.getColumnIndex("route_num"));
            String type = search_cursor.getString(search_cursor.getColumnIndex("route_type"));
            String start = search_cursor.getString(search_cursor.getColumnIndex("start_nodenm"));
            String end = search_cursor.getString(search_cursor.getColumnIndex("end_nodenm"));
            String favorite = search_cursor.getString(search_cursor.getColumnIndex("favorite_bus"));

            busAdapter.addItem(id, num, type, start, end, favorite); // 어댑터에 데이터 삽입
        }

        bus_list.setAdapter(busAdapter); // 리스트뷰에 데이터 올리기
        search_cursor.close();

    }




}
