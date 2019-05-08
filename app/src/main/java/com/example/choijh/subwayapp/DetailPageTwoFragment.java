package com.example.choijh.subwayapp;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;


public class DetailPageTwoFragment  extends Fragment {

    String STATION_CD = "1716";
    String STATION_NAME = "병점";

    String KEY="44714e494967757334386561554557";
    String TYPE ="xml";
    String SERVICE = "SearchSTNInfoByIDService";
    int START_INDEX = 1;
    int END_INDEX = 5;

    InputStream is = null;
    String stationData = getXmlData();
    String data1 = getXmlDataSchedule(1);
    String data2 = getXmlDataSchedule(2);
    String data3 = getXmlDataSchedule(3);

    RadioButton btn1;
    RadioButton btn2;
    RadioButton btn3;
    RadioGroup rg;

    ImageButton ibtn1;
    ImageButton ibtn2;
    ImageButton ibtn3;


    TextView text;
    TextView stationInfo;
    TextView test;

    MapView mapView;
    ArrayList<MapPOIItem> marker = new ArrayList<MapPOIItem>();

    String ArriveData = "99";
    ArrayList<String> arriveList = new ArrayList<String>();
    ImageView subway_iconView1;
    ImageView subway_iconView2;
    ImageView subway_iconView3;
    ImageView subway_iconView4;

    Animation animation1;
    Animation animation2;
    Animation animation3;
    Animation animation4;


    public DetailPageTwoFragment() {
        // Required empty public constructor
    }

    public static DetailPageTwoFragment newInstance(int page){
        Bundle args = new Bundle();

        DetailPageTwoFragment fragment = new DetailPageTwoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail_page_two, container, false);

        StrictMode.enableDefaults();

        text = (TextView) v.findViewById(R.id.schedule);
        text.setText(data1);

        stationInfo = (TextView) v.findViewById(R.id.stationInfo);
        stationInfo.setText(stationData);

        rg = (RadioGroup)v.findViewById(R.id.radioGroup);

        btn1 = (RadioButton) v.findViewById(R.id.rg_btn1);
        btn2 = (RadioButton) v.findViewById(R.id.rg_btn2);
        btn3 = (RadioButton) v.findViewById(R.id.rg_btn3);

        btn1.setOnClickListener(optionOnClickListener);
        btn2.setOnClickListener(optionOnClickListener);
        btn3.setOnClickListener(optionOnClickListener);
        btn1.setChecked(true);

        ibtn1 = (ImageButton) v.findViewById(R.id.restaurant);
        ibtn2 = (ImageButton) v.findViewById(R.id.hospital);
        ibtn3 = (ImageButton) v.findViewById(R.id.hotel);

        ArriveData = arriveData();


        animation1 = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.translate_1);
        animation2 = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.translate_2);
        animation3 = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.translate_3);
        animation4 = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.translate_4);

        subway_iconView1 = (ImageView) v.findViewById(R.id.subway_icon1);
        subway_iconView2 = (ImageView) v.findViewById(R.id.subway_icon2);
        subway_iconView3 = (ImageView) v.findViewById(R.id.subway_icon3);
        subway_iconView4 = (ImageView) v.findViewById(R.id.subway_icon4);



        for(int i=0; i<arriveList.size();i++){

            ArriveData = arriveList.get(i);
            switch (ArriveData){
                case "99" :
                    subway_iconView1.setVisibility(View.INVISIBLE);
                    subway_iconView2.setVisibility(View.INVISIBLE);
                    subway_iconView3.setVisibility(View.INVISIBLE);
                    subway_iconView4.setVisibility(View.INVISIBLE);
                    break;
                case "4" :
                    subway_iconView1.setVisibility(View.VISIBLE);
                    subway_iconView2.setVisibility(View.INVISIBLE);
                    subway_iconView3.setVisibility(View.INVISIBLE);
                    subway_iconView4.setVisibility(View.INVISIBLE);
                    subway_iconView2.clearAnimation();
                    subway_iconView3.clearAnimation();
                    subway_iconView4.clearAnimation();
                    subway_iconView1.startAnimation(animation1);
                    break;
                case "5" :
                    subway_iconView1.setVisibility(View.VISIBLE);
                    subway_iconView2.setVisibility(View.INVISIBLE);
                    subway_iconView3.setVisibility(View.INVISIBLE);
                    subway_iconView4.setVisibility(View.INVISIBLE);
                    subway_iconView2.clearAnimation();
                    subway_iconView3.clearAnimation();
                    subway_iconView4.clearAnimation();
                    subway_iconView1.startAnimation(animation1);
                    break;
                case "3":
                    subway_iconView2.setVisibility(View.VISIBLE);
                    subway_iconView1.setVisibility(View.INVISIBLE);
                    subway_iconView3.setVisibility(View.INVISIBLE);
                    subway_iconView4.setVisibility(View.INVISIBLE);
                    subway_iconView1.clearAnimation();
                    subway_iconView3.clearAnimation();
                    subway_iconView4.clearAnimation();
                    subway_iconView2.startAnimation(animation2);
                    break;
                case "0":
                    subway_iconView2.setVisibility(View.VISIBLE);
                    subway_iconView1.setVisibility(View.INVISIBLE);
                    subway_iconView3.setVisibility(View.INVISIBLE);
                    subway_iconView4.setVisibility(View.INVISIBLE);
                    subway_iconView1.clearAnimation();
                    subway_iconView3.clearAnimation();
                    subway_iconView4.clearAnimation();
                    subway_iconView2.startAnimation(animation2);
                    break;
                case "1":
                    subway_iconView3.setVisibility(View.VISIBLE);
                    subway_iconView2.setVisibility(View.INVISIBLE);
                    subway_iconView1.setVisibility(View.INVISIBLE);
                    subway_iconView4.setVisibility(View.INVISIBLE);
                    subway_iconView2.clearAnimation();
                    subway_iconView1.clearAnimation();
                    subway_iconView4.clearAnimation();
                    subway_iconView3.startAnimation(animation3);
                    break;
                case "2":
                    subway_iconView4.setVisibility(View.VISIBLE);
                    subway_iconView2.setVisibility(View.INVISIBLE);
                    subway_iconView3.setVisibility(View.INVISIBLE);
                    subway_iconView1.setVisibility(View.INVISIBLE);
                    subway_iconView2.clearAnimation();
                    subway_iconView3.clearAnimation();
                    subway_iconView1.clearAnimation();
                    subway_iconView4.startAnimation(animation4);
                    break;
                default:
                    break;

            }
        }



        ImageButton refresh = (ImageButton) v.findViewById(R.id.refresh);
        refresh.setOnClickListener(refreshOnClickListener);



        return v;
    }


    RadioButton.OnClickListener optionOnClickListener = new RadioButton.OnClickListener() {

        public void onClick(View v) {
            if(btn1.isChecked()){ //평일버튼 체크
                text.setText(data1);
            }
            else if(btn2.isChecked()){ //토요일버튼 체크
                text.setText(data2);
            }
            else if(btn3.isChecked()){ //일요일/공휴일 버튼 체크
                text.setText(data3);
            }
        }
    };

    ImageButton.OnClickListener refreshOnClickListener = new ImageButton.OnClickListener()
    {
        public void onClick(View v) {
            arriveList.clear();
            subway_iconView1.clearAnimation();
            subway_iconView2.clearAnimation();
            subway_iconView3.clearAnimation();
            subway_iconView4.clearAnimation();
            subway_iconView1.setVisibility(View.INVISIBLE);
            subway_iconView2.setVisibility(View.INVISIBLE);
            subway_iconView3.setVisibility(View.INVISIBLE);
            subway_iconView4.setVisibility(View.INVISIBLE);
            String ArriveData1;
            ArriveData1 = arriveData();
            for (int i = 0; i < arriveList.size(); i++) {
                ArriveData1 = arriveList.get(i);
                test.setText(arriveList.get(i));
                switch (ArriveData1){
                    case "4" :
                        subway_iconView1.setVisibility(View.VISIBLE);
                        subway_iconView2.setVisibility(View.INVISIBLE);
                        subway_iconView3.setVisibility(View.INVISIBLE);
                        subway_iconView4.setVisibility(View.INVISIBLE);
                        subway_iconView2.clearAnimation();
                        subway_iconView3.clearAnimation();
                        subway_iconView4.clearAnimation();
                        subway_iconView1.startAnimation(animation1);
                        break;
                    case "5" :
                        subway_iconView1.setVisibility(View.VISIBLE);
                        subway_iconView2.setVisibility(View.INVISIBLE);
                        subway_iconView3.setVisibility(View.INVISIBLE);
                        subway_iconView4.setVisibility(View.INVISIBLE);
                        subway_iconView2.clearAnimation();
                        subway_iconView3.clearAnimation();
                        subway_iconView4.clearAnimation();
                        subway_iconView1.startAnimation(animation1);
                        break;
                    case "3":
                        subway_iconView2.setVisibility(View.VISIBLE);
                        subway_iconView1.setVisibility(View.INVISIBLE);
                        subway_iconView3.setVisibility(View.INVISIBLE);
                        subway_iconView4.setVisibility(View.INVISIBLE);
                        subway_iconView1.clearAnimation();
                        subway_iconView3.clearAnimation();
                        subway_iconView4.clearAnimation();
                        subway_iconView2.startAnimation(animation2);
                        break;
                    case "0":
                        subway_iconView2.setVisibility(View.VISIBLE);
                        subway_iconView1.setVisibility(View.INVISIBLE);
                        subway_iconView3.setVisibility(View.INVISIBLE);
                        subway_iconView4.setVisibility(View.INVISIBLE);
                        subway_iconView1.clearAnimation();
                        subway_iconView3.clearAnimation();
                        subway_iconView4.clearAnimation();
                        subway_iconView2.startAnimation(animation2);
                        break;
                    case "1":
                        subway_iconView3.setVisibility(View.VISIBLE);
                        subway_iconView2.setVisibility(View.INVISIBLE);
                        subway_iconView1.setVisibility(View.INVISIBLE);
                        subway_iconView4.setVisibility(View.INVISIBLE);
                        subway_iconView2.clearAnimation();
                        subway_iconView1.clearAnimation();
                        subway_iconView4.clearAnimation();
                        subway_iconView3.startAnimation(animation3);
                        break;
                    case "2":
                        subway_iconView4.setVisibility(View.VISIBLE);
                        subway_iconView2.setVisibility(View.INVISIBLE);
                        subway_iconView3.setVisibility(View.INVISIBLE);
                        subway_iconView1.setVisibility(View.INVISIBLE);
                        subway_iconView2.clearAnimation();
                        subway_iconView3.clearAnimation();
                        subway_iconView1.clearAnimation();
                        subway_iconView4.startAnimation(animation4);
                        break;
                    default:
                        break;

                }
            }
        }
    };

    String getXmlDataSchedule(int WEEK_TAG){

        StringBuffer buffer = new StringBuffer();
        String queryUrl = "http://openapi.seoul.go.kr:8088/4162636246677573353243664d6e59/xml/SearchSTNTimeTableByIDService/1/1000/"+ STATION_CD +"/"+ WEEK_TAG +"/2/" ;

        try {

            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            is = url.openStream(); //url위치로 입력스트림 연결


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
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= parser.getName();//태그 이름 얻어오기
                        if(tag.equals("row")) ;// 첫번째 검색결과
                        else if(tag.equals("LEFTTIME")) {
                            parser.next();
                            if (parser.getText().equals("00:00:00")){
                                for(int i=0; i<30; i++)
                                    parser.next();
                            }
                            else if (!parser.getText().equals("00:00:00")) {
                                buffer.append("\n");
                                buffer.append(parser.getText());
                            }
                        }
                        else if(tag.equals("SUBWAYENAME")){
                            parser.next();
                            buffer.append(parser.getText() + "행");
                        }
                        else if(tag.equals("EXPRESS_YN")){
                            parser.next();
                            if(parser.getText().equals("D"))
                                buffer.append("(급행)");
                        }
                        else
                            parser.next();
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= parser.getName(); //태그 이름 얻어오기

                        if(tag.equals("row"));// 첫번째 검색결과종료..줄바꿈

                        break;
                }

                eventType= parser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return buffer.toString();//StringBuffer 문자열 객체 반환

    }

    String arriveData(){
        String updnLine = null;
        StringBuffer buffer = new StringBuffer();
        String queryUrl = "http://swopenapi.seoul.go.kr/api/subway/5551426a6667757336316b65416149/xml/realtimeStationArrival/0/50/" + STATION_NAME ;

        try {

            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            is = url.openStream(); //url위치로 입력스트림 연결


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
                        if(tag.equals("row")) ;// 첫번째 검색결과
                        else if(tag.equals("updnLine")) {
                            parser.next();
                            updnLine = parser.getText();
                        }
                        else if(tag.equals("arvlCd")) {
                            parser.next();
                            if(updnLine.equals("하행")) {
                                buffer.append(parser.getText());
                                arriveList.add(parser.getText());
                            }
                        }

                        else
                            parser.next();
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= parser.getName(); //태그 이름 얻어오기

                        if(tag.equals("row"));// 첫번째 검색결과종료..줄바꿈

                        break;
                }

                eventType= parser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return buffer.toString();//StringBuffer 문자열 객체 반환

    }


    String getXmlData(){

        StringBuffer buffer = new StringBuffer();
        String queryUrl = "http://openapi.seoul.go.kr:8088/" + KEY +"/"+ TYPE + "/" + SERVICE + "/" + START_INDEX + "/" + END_INDEX + "/" + STATION_CD + "/" ;

        try {

            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            is = url.openStream(); //url위치로 입력스트림 연결


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
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= parser.getName();//태그 이름 얻어오기

                        if(tag.equals("row")) ;// 첫번째 검색결과
                        else if(tag.equals("STATION_NM")){
                            buffer.append("역명 : ");
                            parser.next();
                            buffer.append(parser.getText());//STATION_NM 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("TEL")){
                            buffer.append("역 전화번호 : ");
                            parser.next();
                            buffer.append(parser.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("ELEVATOR")){
                            buffer.append("엘레베이터 유무 : ");
                            parser.next();
                            if(parser.getText() == null){
                                buffer.append("알 수 없음");
                            }
                            else {
                                buffer.append(parser.getText());
                            }
                            buffer.append("\n");
                        }
                        else if(tag.equals("ESCALATOR")){
                            buffer.append("에스컬레이터 유무 : ");
                            parser.next();
                            if(parser.getText() == null){
                                buffer.append("알 수 없음");
                            }
                            else {
                                buffer.append(parser.getText());
                            }
                            buffer.append("\n");
                        }
                        else if(tag.equals("TOILET")){
                            buffer.append("화장실 : ");
                            parser.next();
                            if(parser.getText() == null){
                                buffer.append("알 수 없음");
                            }
                            else {
                                buffer.append(parser.getText());
                            }
                            buffer.append("\n");
                        }
                        else
                            parser.next();
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= parser.getName(); //태그 이름 얻어오기

                        if(tag.equals("row")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈

                        break;
                }

                eventType= parser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        buffer.append("파싱 끝\n");

        return buffer.toString();//StringBuffer 문자열 객체 반환

    }

}