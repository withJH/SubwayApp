package com.example.choijh.subwayapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.annotations.SerializedName;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;


public class Subway_detailPage extends AppCompatActivity {
    private Context mContext;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private DetailPagerAdapter mDetailPagerAdapter;

    String STATION_CD;

    Double x;
    Double y;
    String STATION_NM;

    MapView mapView;
    ArrayList<MapPOIItem> marker = new ArrayList<MapPOIItem>();

    ImageButton ibtn1;
    ImageButton ibtn2;
    ImageButton ibtn3;
    ImageButton ibtn4;
    ImageButton ibtn5;
    InputStream is = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway_detail);
        mContext = getApplicationContext();

        StrictMode.enableDefaults();

        Intent intent = getIntent();
        STATION_CD = intent.getStringExtra("station");
        if(STATION_CD != null)
            getStationLocation(STATION_CD);
        else
            getStationLocation("0151");

        mapView = new MapView(this);
        mapView.setDaumMapApiKey(" 61d3254080b5d9424b98a292a7984c8e");
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(x, y), true);
        RelativeLayout map = (RelativeLayout) findViewById(R.id.map_view);
        map.addView(mapView);

        ibtn1 = (ImageButton) findViewById(R.id.restaurant);
        ibtn2 = (ImageButton) findViewById(R.id.hospital);
        ibtn3 = (ImageButton) findViewById(R.id.hotel);
        ibtn4 = (ImageButton) findViewById(R.id.cafe);
        ibtn5 = (ImageButton) findViewById(R.id.bank);
        ibtn1.setOnClickListener(ibtnOnClickListener);
        ibtn2.setOnClickListener(ibtnOnClickListener);
        ibtn3.setOnClickListener(ibtnOnClickListener);
        ibtn4.setOnClickListener(ibtnOnClickListener);
        ibtn5.setOnClickListener(ibtnOnClickListener);


        DetailPageOneFragment fragment = new DetailPageOneFragment(); // Fragment 생성
        DetailPageTwoFragment fragment2 = new DetailPageTwoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("station", STATION_NM);
        bundle.putString("code", STATION_CD);// Key, Value
        fragment.setArguments(bundle);
        fragment2.setArguments(bundle);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);


        mDetailPagerAdapter = new DetailPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mDetailPagerAdapter);


        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));



    }


    ImageButton.OnClickListener ibtnOnClickListener = new ImageButton.OnClickListener() {

        public void onClick(View v) {
            switch(v.getId()){
                case R.id.restaurant:
                    setRestApi("FD6");
                    break;
                case R.id.hospital:
                    setRestApi("HP8");
                    break;
                case R.id.hotel:
                    setRestApi("AD5");
                    break;

                case R.id.cafe:
                    setRestApi("CE7");
                    break;
                case R.id.bank:
                    setRestApi("BK9");
                    break;

                default:
                    break;
            }
        }
    };

    public interface apiService {
        String base = "https://dapi.kakao.com/";

        @GET("/v2/local/search/category.json")
        Call<RestApi> getApi(@Header("Authorization") String key, @Query("category_group_code") String code, @Query("x") String x, @Query("y") String y, @Query("radius") int radius);
    }

    public class RestApi {
        String place_name;
        String x;
        String y;

        @SerializedName("documents")
        public List<restApi> data = new ArrayList();

        public class restApi {
            @SerializedName("place_name")
            public String place_name;
            @SerializedName("x")
            public String x;
            @SerializedName("y")
            public String y;

            public String getPlace_name() {
                return place_name;
            }

            public String getX() {
                return x;
            }

            public String getY() {
                return y;
            }
        }
    }
    public void setRestApi(String cateroty_gourp_code) {
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(apiService.base)
                .build();

        apiService api = retrofit.create(apiService.class);

        Call<RestApi> call = api.getApi("KakaoAK 02e90f45782fea188c0f2f655b06b7df", cateroty_gourp_code, String.valueOf(y), String.valueOf(x), 20000);

        call.enqueue(new Callback<RestApi>() {
            @Override
            public void onResponse(Call<RestApi> call, Response<RestApi> response) {
                if (response.isSuccessful()) {
                    // 성공적으로 서버에서 데이터 불러옴.

                    RestApi object = response.body();
                    List<RestApi.restApi> hospital = object.data;


                    mapView.removeAllPOIItems();

                    int i = 0;
                    for (RestApi.restApi hospital1 : hospital) {
                        double y = Double.parseDouble(hospital1.getY());
                        double x = Double.parseDouble(hospital1.getX());
                        marker.add(new MapPOIItem());
                        marker.get(i).setItemName(hospital1.getPlace_name());
                        marker.get(i).setTag(0);
                        marker.get(i).setMapPoint(MapPoint.mapPointWithGeoCoord(y, x));
                        marker.get(i).setMarkerType(MapPOIItem.MarkerType.BluePin);
                        marker.get(i).setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

                        mapView.addPOIItem(marker.get(i));
                        i++;
                    }

                } else {
                    // 서버와 연결은 되었으나, 오류 발생
                    Toast.makeText(getApplicationContext(), "서버와 연결 되었으나 실패", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<RestApi> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
    void getStationLocation(String STATION_CD){

        String queryUrl = "http://openapi.seoul.go.kr:8088/44714e494967757334386561554557/xml/SearchSTNInfoByIDService/1/1/"+STATION_CD+"/" ;

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
                        else if(tag.equals("STATION_NM")) {
                            parser.next();
                            STATION_NM = parser.getText();
                        }
                        else if(tag.equals("XPOINT_WGS")){
                            parser.next();
                            x = Double.parseDouble(parser.getText());
                        }
                        else if(tag.equals("YPOINT_WGS")){
                            parser.next();
                            y = Double.parseDouble(parser.getText());
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

    }
}
