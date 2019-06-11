package com.example.choijh.subwayapp;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class BusNearbyFirstFragment extends Fragment {

    private ListView listview ;
    private BusNearbyOneListViewAdapter adapter;
    private String[] st = {"버스","정류장","이름"};
    private String[] d = {"100m","50m","70m"};
    private String[] b = {"버스","8번","9번"};
    String data; //버스 정류장 이름 + 거리m
    String[] bdata;
    String stationID; //정류장ID
    public  String id[]; //정류장 ID 토큰 자른것
    public String xx;
    public String yy;
    String busnamelist; //id에 따른 버스 이름들
    String[] busname; //버스 이름들
    public static BusNearbyFirstFragment mBusnear; //다른데서 여기 함수 쓰기 위해서 만듬
    public BusNearbyFirstFragment() {
    }

    public static BusNearbyFirstFragment newInstance(int page){
        Bundle args = new Bundle();

        BusNearbyFirstFragment fragment = new BusNearbyFirstFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bus_nearby_one, container, false);

        //변수 초기화
        adapter = new BusNearbyOneListViewAdapter();
        listview = (ListView)view.findViewById(R.id.listview_near);

        //어뎁터 할당
        listview.setAdapter(adapter);

        //내주변 버스 정보
        StrictMode.enableDefaults();
        xx = ((Bus_main)Bus_main.mBusmain).x;
        yy = ((Bus_main)Bus_main.mBusmain).y;
        busStationNear(xx, yy);

        try {
            //버스정류장이름+거리 리스트 데이터 자르기
            bdata = data.split("#");
            //정류장ID 데이터 자르기
            id = stationID.split("/");
            String stdlist[];
            for (int i = 0; i < bdata.length; i++) {
                System.out.println("bdata[" + i + "] : " + bdata[i]);
                stdlist = bdata[i].split("/");
                //주변 버스 리스트 출력(test)
                busNear(id[i]);
                System.out.println("IDIDIDID : " + id[i]);
                System.out.println("버스들!" + busnamelist);
                //adapter를 통한 값 전달
                adapter.addVO(stdlist[0], stdlist[1], busnamelist);
                System.out.println("버스정류장이름+거리 : "+data);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        mBusnear = this;

        /*//버스정류장이름+거리 리스트 데이터 자르기
        bdata = data.split("#");

        for(int i=0 ; i<bdata.length ; i++)
        {
            System.out.println("bdata["+i+"] : "+bdata[i]);
        }

        //정류장ID 데이터 자르기
        String id[] = stationID.split("/");

        for(int i=0 ; i<id.length ; i++)
        {
            System.out.println("stid["+i+"] : "+id[i]);
        }

        //주변 버스 리스트 출력(test)
        busNear("123");
        System.out.println("버스들!"+busnamelist);

        //adapter를 통한 값 전달
        for(int i=0; i<st.length;i++){
            adapter.addVO(st[i],d[i],b[i]);
        }*/

        return view;
    }

    //주변 버스 정보 api 조회 및 저장 메소드(정류장 이름, 거리)
    public void busStationNear(String x, String y){
        StringBuffer buffer = new StringBuffer();
        String busst = null, busdt = null, stid = null;
        int check = 0;
        String queryUrl = "http://openapi.gbis.go.kr/ws/rest/busstationservice/searcharound?serviceKey=xai6s9wk7CVjmtsSCDrv1%2BNEj11WClzz%2FfEUE7rSXDoYo%2Bj%2BmergaU9GzMabdOFNDDgeFZIsVPw4LscETN2aDg%3D%3D&x="+x+"&y="+y;
        //String queryUrl = "http://openapi.gbis.go.kr/ws/rest/busstationservice/searcharound?serviceKey=xai6s9wk7CVjmtsSCDrv1%2BNEj11WClzz%2FfEUE7rSXDoYo%2Bj%2BmergaU9GzMabdOFNDDgeFZIsVPw4LscETN2aDg%3D%3D&x=127.10989&y=37.03808";
        try {

            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream(); //url위치로 입력스트림 연결


            BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(rd); //inputstream 으로부터 xml 입력받기

            String tag;

            parser.next();
            int eventType = parser.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= parser.getName();//태그 이름 얻어오기
                        if(tag.equals("busStationAroundList")) ;// 첫번째 검색결과
                        else if(tag.equals("stationName")) {
                            parser.next();
                            busst=parser.getText();
                        }
                        else if(tag.equals("distance")) {
                            parser.next();
                            busdt=parser.getText();
                        }
                        else if(tag.equals("stationId")) {
                            parser.next();
                            stid=parser.getText();
                        }
                        else
                            parser.next();
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= parser.getName(); //태그 이름 얻어오기

                        if(tag.equals("busStationAroundList")) { // 첫번째 검색결과종료..줄바꿈
                            if(check==0) {
                                data = busst + "/" + busdt + "m/#";
                                stationID = stid + "/";
                                check++;
                            }
                            else
                                data += busst + "/" + busdt + "m/#";
                                stationID += stid + "/";
                        }
                        break;
                }
                eventType= parser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //주변 버스 조회 및 저장 함수
    public void busNear(String id){
        StringBuffer buffer = new StringBuffer();
        String buslist = null;
        int check = 0;
        String queryUrl = "http://openapi.gbis.go.kr/ws/rest/busstationservice/route?serviceKey=xai6s9wk7CVjmtsSCDrv1%2BNEj11WClzz%2FfEUE7rSXDoYo%2Bj%2BmergaU9GzMabdOFNDDgeFZIsVPw4LscETN2aDg%3D%3D&stationId=" + id;
        //String queryUrl = "http://openapi.gbis.go.kr/ws/rest/busstationservice/route?serviceKey=xai6s9wk7CVjmtsSCDrv1%2BNEj11WClzz%2FfEUE7rSXDoYo%2Bj%2BmergaU9GzMabdOFNDDgeFZIsVPw4LscETN2aDg%3D%3D&stationId=200000078";
        try {

            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream(); //url위치로 입력스트림 연결

            BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(rd); //inputstream 으로부터 xml 입력받기

            String tag;

            parser.next();
            int eventType = parser.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= parser.getName();//태그 이름 얻어오기
                        if(tag.equals("busRouteList")) ;// 첫번째 검색결과
                        else if(tag.equals("routeName")) {
                            parser.next();
                            buslist=parser.getText();
                        }
                        else
                            parser.next();
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= parser.getName(); //태그 이름 얻어오기

                        if(tag.equals("busRouteList")) { // 첫번째 검색결과종료..줄바꿈
                            if(check==0) {
                                busnamelist = buslist + ", ";
                                check++;
                            }
                            else
                                busnamelist += buslist + ", ";
                        }
                        break;
                }
                eventType= parser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
