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


public class Subway_lost_property extends AppCompatActivity implements ListViewBtnAdapter_lost.ListBtnClickListener {
    TextView text;

    String key="4559786d636775733832665a4d7853";
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway_lost_property);
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
        adapter = new ListViewBtnAdapter_lost(Subway_lost_property.this, R.layout.listview_btn_item_lost, items, this) ;

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
        item.setText(" 강남역") ;
        item.setText2("02-1934-872");
        list.add(item) ;

        item = new ListViewBtnItem_lost() ;
        item.setText(" 교원역") ;
        item.setText2("031-3240-1234");
        list.add(item) ;

        item = new ListViewBtnItem_lost() ;
        item.setText(" 사당역") ;
        item.setText2("02-240-1234");
        list.add(item) ;
        item = new ListViewBtnItem_lost() ;
        item.setText(" 수원역") ;
        item.setText2("031-250-1224");
        list.add(item) ;item = new ListViewBtnItem_lost() ;
        item.setText(" 세마역") ;
        item.setText2("031-253-1454");
        list.add(item) ;item = new ListViewBtnItem_lost() ;
        item.setText(" 하남역") ;
        item.setText2("032-3230-1334");
        list.add(item) ;
        return true ;
    }
/*
    //API 활용 부분
    void test(){
        TextView status1 = (TextView) findViewById(R.id.text_result_lost); //파싱된 결과확인!

        boolean initem = false, insTEL = false, instatnNm = false;
        String TEL = null, STATION_NM = null;

        String tmp = null;
        try {
            URL url = new URL("http://openapi.seoul.go.kr:8088/"+key+"/xml/SearchSTNInfoByFRCodeService/1/5/124/"
            ); //※만든 URL 넣기
            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");


            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if (parser.getName().equals("STATION_NM")) { //mapy 만나면 내용을 받을수 있게 하자
                            instatnNm = true;
                        }
                        if (parser.getName().equals("TEL")) { //mapy 만나면 내용을 받을수 있게 하자
                            insTEL = true;
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if (instatnNm) { //isMapy이 true일 때 태그의 내용을 저장.
                            STATION_NM = parser.getText();
                            instatnNm = false;
                        }
                        if (insTEL) { //isMapy이 true일 때 태그의 내용을 저장.
                            TEL = parser.getText();
                            insTEL = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:

                        if (parser.getName().equals("STATION_NM")) {
                            status1.setText("지하철역명 : " + STATION_NM + "\n");
                        }
                        if (parser.getName().equals("TEL")) {
                            status1.setText("전화 번호 : " + TEL + "\n");
                        }
                        /*if (parser.getName() != null) {
                            status1.setText(status1.getText() + parser.getName());
                        }*/
                      /*  if (parser.getName().equals("item")) {
                            status1.setText("\n지하철역명 : " + STATION_NM + "\n 전화 번호 : " + TEL + "\n");
                            initem = false;
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {
            status1.setText("에러가..났습니다...");
        }

    }
    */
}
