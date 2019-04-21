package com.example.choijh.subwayapp;

import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Subway_search extends AppCompatActivity {
    ListView station_list; // 리스트 뷰
    String key="7a6c6556566a686338384f56675879"; // 지하철역 이름으로 검색 인증키

    SearchAdapter searchAdapter = new SearchAdapter(); // 어댑터

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway_search);

        // 임시로 넣어둔 툴바(현희가 고쳐줘야할 부분)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // 이 부분까지

        StrictMode.enableDefaults(); // 이걸 안 쓰면 리스트가 안 뜸
        station_list = (ListView) findViewById(R.id.station_list);
        getXmlData(); // API 읽어오기

        EditText editSearch = (EditText)findViewById(R.id.station_search) ; // 검색창

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                String text = edit.toString(); // 검색 입력한 단어 저장
                if (text.length() > 0) {
                    station_list.setFilterText(text);
                } else {
                    station_list.clearTextFilter();
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchAdapter.getFilter().filter(s.toString()); // 검색어를 입력해도 결과가 안 나와서 강제로 메소드를 실행시켜줌
            }
        }) ;
    }

     private void getXmlData(){ // 꼭 네트워크 연결 후 사용(안 하면 안 나옴)

        boolean inCD = false, inNAME = false, inNUM = false, inFR = false;
        String station_code = null, station_nm= null, line_num = null, fr_code = null;

        try{
            URL url = new URL("http://openapi.seoul.go.kr:8088/"+key
                    +"/xml/SearchInfoBySubwayNameService/1/716"); //검색 URL

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("지하철역 이름을 통한 검색 API 파싱 중...");

            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG: //parser가 시작 태그를 만나면 실행 ex) <ul>
                        if (parser.getName().equals("STATION_CD")) {
                            inCD = true;
                            break;
                        } else if (parser.getName().equals("STATION_NM")) {
                            inNAME = true;
                            break;
                        } else if (parser.getName().equals("LINE_NUM")) {
                            inNUM = true;
                            break;
                        } else if (parser.getName().equals("FR_CODE")) {
                            inFR = true;
                            break;
                        } else{
                            break;
                        }
                     case XmlPullParser.TEXT: //parser가 내용에 접근했을때
                        if(inCD){
                            station_code = parser.getText();
                            inCD = false;
                            break;
                        }
                        else if(inNAME){
                            station_nm = parser.getText();
                            inNAME = false;
                            break;
                        }
                        else if(inNUM){
                            line_num = parser.getText();
                            inNUM = false;
                            break;
                        }
                        else if(inFR){
                            fr_code = parser.getText();
                            inFR = false;
                            break;
                        }
                        else{
                            break;
                        }
                    case XmlPullParser.END_TAG: //parser가 종료태그 만났을 때 ex) </ul>
                        if (parser.getName().equals("row")) {
                            searchAdapter.addItem(station_code, station_nm, line_num, fr_code); // 어댑터에 데이터 삽입
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        station_list.setAdapter(searchAdapter); // 리스트뷰에 데이터 올리기
    }
}
