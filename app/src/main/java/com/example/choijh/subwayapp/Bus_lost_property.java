package com.example.choijh.subwayapp;

import android.os.StrictMode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

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

        try {
            InputStream is = getBaseContext().getResources().getAssets().open("bus_tel.xls");
            Workbook wb = Workbook.getWorkbook(is);

            if(wb != null) {
                Sheet sheet = wb.getSheet(0);   // 시트 불러오기
                if(sheet != null) {
                    int colTotal = sheet.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet.getColumn(colTotal-1).length;

                    StringBuilder sb;
                    for(int row=rowIndexStart;row<rowTotal;row++) {
                        sb = new StringBuilder();
                        item = new ListViewBtnItem_lost() ;
                        for(int col=0;col<colTotal;col++) {
                            String contents = sheet.getCell(col, row).getContents();

                            if(col==0){
                                item.setText(contents) ;
                            }else if(col==1){
                                item.setText2(contents);
                            }
                            sb.append("col"+col+" : "+contents+" , ");
                        }
                        Log.i("test", sb.toString());
                        list.add(item) ;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        return true ;
    }
}
