package com.example.choijh.subwayapp;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class BusNearbySecondFragment extends Fragment {

    private ListView listview ;
    private BusNearbyTwoListViewAdapter adapter;
    String busnamelist; //id에 따른 버스 이름들
    String routeidlist;
    String staorderlist;
    public BusNearbySecondFragment() {
    }

    public static BusNearbySecondFragment newInstance(int page){
        Bundle args = new Bundle();

        BusNearbySecondFragment fragment = new BusNearbySecondFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bus_nearby_two, container, false);
        //변수 초기화
        adapter = new BusNearbyTwoListViewAdapter();
        listview = (ListView)view.findViewById(R.id.listview_near2);

        //어뎁터 할당
        listview.setAdapter(adapter);

        //내주변 버스 정보
        StrictMode.enableDefaults();

        String a =((BusNearbyFirstFragment)BusNearbyFirstFragment.mBusnear).data; //버스정류장이름+거리
        String b =((BusNearbyFirstFragment)BusNearbyFirstFragment.mBusnear).stationID; //정류장ID

        //버스정류장이름+거리 리스트 데이터 자르기
        String[] bdata = a.split("#");
        //정류장ID 데이터 자르기
        String[] id = b.split("/");
        String stdlist[];
        String buslist[];
        String slist[];
        String rlist[];
        String tmp;
        String info[]=null;

            for (int i = 0; i < bdata.length; i++) {
                System.out.println("bdata[" + i + "] : " + bdata[i]);
                stdlist = bdata[i].split("/");
                //주변 버스 리스트 출력(test)
                busNear(id[i]);
                if(busnamelist==null)continue;
                else {
                    System.out.println("버스들@" + busnamelist);
                    buslist = busnamelist.split("/");
                    slist = staorderlist.split("/");
                    rlist = routeidlist.split("/");
                    for (int j = 0; j < buslist.length; j++) {
                        tmp = null;
                        info = null;
                        tmp = busInfo(id[i], rlist[j], slist[j]);
                        //info= tmp.split("/");
                        info = "안녕/하쇼/".split("/");
                        //adapter를 통한 값 전달
                        adapter.addVO(stdlist[0], buslist[j], info[0], info[1]);
                    }
                }
            }

        return view;
    }
    //주변 버스 조회 및 저장 함수
    public void busNear(String id){
        StringBuffer buffer = new StringBuffer();
        String buslist = null, rlist = null, slist=null;
        int check = 0;
        String queryUrl = "http://openapi.gbis.go.kr/ws/rest/busstationservice/route?serviceKey=xai6s9wk7CVjmtsSCDrv1%2BNEj11WClzz%2FfEUE7rSXDoYo%2Bj%2BmergaU9GzMabdOFNDDgeFZIsVPw4LscETN2aDg%3D%3D&stationId=" + id;
        //String queryUrl = "http://openapi.gbis.go.kr/ws/rest/busarrivalservice/station?serviceKey=xai6s9wk7CVjmtsSCDrv1%2BNEj11WClzz%2FfEUE7rSXDoYo%2Bj%2BmergaU9GzMabdOFNDDgeFZIsVPw4LscETN2aDg%3D%3D&stationId=200000194";
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
                        else if(tag.equals("routeId")) {
                            parser.next();
                            rlist=parser.getText();
                        }
                        else if(tag.equals("staOrder")) {
                            parser.next();
                            slist=parser.getText();
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
                                busnamelist = buslist + "/";
                                routeidlist = rlist + "/";
                                staorderlist = slist + "/";
                                check++;
                            }
                            else {
                                busnamelist += buslist + "/";
                                routeidlist += rlist + "/";
                                staorderlist += slist + "/";
                            }
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
    public String busInfo(String id, String rid, String s){
        StringBuffer buffer = new StringBuffer();
        String infolist = null,infolist2 = null, returnlist=null;
        int check = 0;
        String queryUrl = "http://openapi.gbis.go.kr/ws/rest/busarrivalservice?serviceKey=xai6s9wk7CVjmtsSCDrv1%2BNEj11WClzz%2FfEUE7rSXDoYo%2Bj%2BmergaU9GzMabdOFNDDgeFZIsVPw4LscETN2aDg%3D%3D&stationId=" + id+"&routeId="+rid+"&staOrder="+s;
        //String queryUrl = "http://openapi.gbis.go.kr/ws/rest/busarrivalservice?serviceKey=xai6s9wk7CVjmtsSCDrv1%2BNEj11WClzz%2FfEUE7rSXDoYo%2Bj%2BmergaU9GzMabdOFNDDgeFZIsVPw4LscETN2aDg%3D%3D&stationId=200000194&routeId=200000010&staOrder=132";
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
                        if(tag.equals("MsgBody")) ;// 첫번째 검색결과
                        else if(tag.equals("locationNo1")) {
                            parser.next();
                            infolist=parser.getText();
                        }
                        else if(tag.equals("predictTime1")) {
                            parser.next();
                            infolist2=parser.getText();
                        }
                        else
                            parser.next();
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= parser.getName(); //태그 이름 얻어오기

                        if(tag.equals("MsgBody")) { // 첫번째 검색결과종료..줄바꿈
                            if(check==0) {
                                returnlist = infolist + "정거장 전/";
                                returnlist = infolist2 + "분 후/";
                                check++;
                            }
                            else {
                                returnlist += infolist + "정거장 전/";
                                returnlist += infolist2 + "분 후/";
                            }
                        }
                        break;
                }
                eventType= parser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnlist;
    }
}
