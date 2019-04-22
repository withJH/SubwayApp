package com.example.choijh.subwayapp;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Subway_search extends AppCompatActivity {
    ListView station_list; // 리스트 뷰
    String key="7a6c6556566a686338384f56675879"; // 지하철역 이름으로 검색 인증키

    SearchAdapter searchAdapter = new SearchAdapter(); // 어댑터
    SearchView searchView;
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.subway_search, menu);

        searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        // searchview 길이 꽉차게 하기
        searchView.setMaxWidth(Integer.MAX_VALUE);
        // searchview 힌트 메시지
        searchView.setQueryHint("역명으로 검색합니다.");
        searchView.setIconified(false);

        final MenuItem menuItem = menu.getItem(0);
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
