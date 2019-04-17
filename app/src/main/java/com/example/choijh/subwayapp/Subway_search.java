package com.example.choijh.subwayapp;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

public class Subway_search extends AppCompatActivity {
    ListView station_list;
    String key="7a6c6556566a686338384f56675879";

    ArrayList<ArrayList> subway_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.enableDefaults();

        station_list = (ListView) findViewById(R.id.station_list);
        subway_info = getXmlData();
    }

     ArrayList<ArrayList> getXmlData(){
        SearchAdapter searchAdapter = new SearchAdapter();
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<ArrayList> big_list = new ArrayList<ArrayList>();

        boolean inCD = false, inNAME = false, inNUM = false, inFR = false;
        String station_code = null, station_nm= null, line_num = null, fr_code = null;

        try{
            URL url = new URL("http://openapi.seoul.go.kr:8088/"+key
                    +"/xml/SearchInfoBySubwayNameService/1/716"); //검색 URL부분

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");

            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG: //parser가 시작 태그를 만나면 실행
                        if (parser.getName().equals("STATION_CD")) {
                            inCD = true;
                        } else if (parser.getName().equals("STATION_NM")) {
                            inNAME = true;
                        } else if (parser.getName().equals("LINE_NUM")) {
                            inNUM = true;
                        } else if (parser.getName().equals("FR_CODE")) {
                            inFR = true;
                        }
                        break;
                     case XmlPullParser.TEXT: //parser가 내용에 접근했을때
                        if(inCD){
                            station_code = parser.getText();
                            inCD = false;
                        }
                        if(inNAME){
                            station_nm = parser.getText();
                            inNAME = false;
                        }
                        if(inNUM){
                            line_num = parser.getText();
                            inNUM = false;
                        }
                        if(inFR){
                            fr_code = parser.getText();
                            inFR = false;
                        }
                        break;
                    case XmlPullParser.END_TAG: //parser가 종료태그 만났을 때
                        if (parser.getName().equals("row")) {
                            searchAdapter.addItem(station_code, station_nm, line_num, fr_code);
                            list.add(station_code); list.add(station_nm); list.add(line_num); list.add(fr_code);
                            big_list.add(list);
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        station_list.setAdapter(searchAdapter);
        return big_list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.subway_search, menu);

        SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setIconified(false); // 자동 포커스

        searchView.setQueryHint("역 이름으로 검색");

        // SearchView 검색어 입력/검색 이벤트 처리
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { // query가 입력 단어

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });

        return true;
    }


}
