package com.example.choijh.subwayapp;

import android.os.StrictMode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Bus_lost_property extends AppCompatActivity implements ListViewBtnAdapter_lost.ListBtnClickListener{
    TextView text;

    String key="4559786d636775733832665a4d7853";
    String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_lost_property);
        setTitle("분실물 센터");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Adapter 및 ListView 생성
        ListView listView;
        ListViewBtnAdapter_lost adapter;
        ArrayList<ListViewBtnItem_lost> items = new ArrayList<ListViewBtnItem_lost>();

        //items 로드
        loadItemsFromDB(items);

        // Adapter 생성
        adapter = new ListViewBtnAdapter_lost(Bus_lost_property.this, R.layout.listview_btn_item_lost, items, this) ;

        // 리스트뷰 참조 및 Adapter달기
        listView = findViewById(R.id.listview_lost);
        listView.setAdapter(adapter);

        // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // TODO : item click
            }
        }) ;

        //API
        StrictMode.enableDefaults();

        //text= (TextView)findViewById(R.id.text_result_lost);
        //test();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListBtnClick(int position) {
        Toast.makeText(this, (position + 1) + " Item is selected..", Toast.LENGTH_SHORT).show() ;
    }



    //ArrayList에 데이터를 로드하는 loadItemsFromDB() 함수 추가
    public boolean loadItemsFromDB(ArrayList<ListViewBtnItem_lost> list) {
        ListViewBtnItem_lost item ;

        if (list == null) {
            list = new ArrayList<ListViewBtnItem_lost>() ;
        }

        // 아이템 생성.
        item = new ListViewBtnItem_lost() ;
        item.setText(" 즐겨찾기 초기화") ;
        item.setText2("010-1934-872");
        list.add(item) ;

        item = new ListViewBtnItem_lost() ;
        item.setText(" 문의하기") ;
        item.setText2("010-3240-1234");
        list.add(item) ;

        return true ;
    }
}
