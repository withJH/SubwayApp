package com.example.choijh.subwayapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.amitshekhar.DebugDB;
import com.github.piasy.biv.BigImageViewer;
import com.github.piasy.biv.loader.glide.GlideImageLoader;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.TextTemplate;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;
import com.odsay.odsayandroidsdk.API;
import com.odsay.odsayandroidsdk.ODsayData;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.OnResultCallbackListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Subway_main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DBOpenHelper help;
    private Subway_main.SectionsPagerAdapter mSectionsPagerAdapter;//탭레이아웃
    private ViewPager mViewPager;//탭레이아웃
    static int pos = 0;
    ArrayList<String[]> station_DB_name;
    String gpsX;
    String gpsY;
    HashMap<String, PointF> stations = null;
    private com.github.piasy.biv.view.BigImageView mBigImageView;
    TextView gpsinfo;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BigImageViewer.initialize(GlideImageLoader.with(getApplicationContext()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway_main);
        stations = findStationXML();
        // 역, PointF HashMap 생성
        help = new DBOpenHelper(getApplicationContext());
        help.open();
        help.create();
        //ODsay 설정
        ODsayService odsayService = ODsayService.init(getApplicationContext(), "hdKe/5bDLhm0/zzg6Y2kUqPZy8hL2buzyMpDLGyAH/Y");
        odsayService.setReadTimeout(5000);
        odsayService.setConnectionTimeout(5000);
        //GPS 좌표
        LocationManager locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = LocationManager.GPS_PROVIDER;
        //if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        //    return;
        //}
        Location location = locationmanager.getLastKnownLocation(provider);

        //툴바
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //원래 툴바 타이틀(제목)없애줌

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //버스 메인 화면으로 이동
                Intent intent = new Intent(Subway_main.this, Bus_main.class);
                startActivityForResult(intent, 1000);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //getHashKey();
        getInfoFromAPI();
        DebugDB.getAddressLog();

        //GPS 좌표 찾는 함수
        showGPS(location);
        //반경내의 지하철 역 찾기
        odsayService.requestPointSearch(gpsY, gpsX, "5000", "2", onResultCallbackListener);

        mBigImageView = (com.github.piasy.biv.view.BigImageView) findViewById(R.id.sub_map);
        mBigImageView.showImage(Uri.parse("file:///android_asset/subway_map.png"));

        station_DB_name = getFavoriteDBname(getApplicationContext());

        mSectionsPagerAdapter = new Subway_main.SectionsPagerAdapter(this, getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.main_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tabs);

        int tabsize = 0;
        if (tabLayout.getTabCount() > station_DB_name.size()) {
            tabsize = station_DB_name.size();
        }
        else{
            tabsize = tabLayout.getTabCount();
        }
        for (int i = 0; i < tabsize; ++i) {
            tabLayout.getTabAt(i).setText(station_DB_name.get(i)[0]);//[0]이면 지하철 이름, [1]이면 지하철 호선
        }

//        tabLayout.setSelectedTabIndicatorColor(0xFAFAFA);//언더바 색상, 전체적으로만 지정 가능

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));//변화가 생겼을때
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {//탭이 바뀔때
            @Override
            public void onTabSelected(TabLayout.Tab tab) {//다른 탭을 클릭시
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {//지금 탭을 클릭시
                Toast.makeText(getApplicationContext(), "지금 탭 상세정보로 이동", Toast.LENGTH_LONG).show();
            }

            public void onTabUnselected(TabLayout.Tab tab) {//현재 탭에서 다른 탭으로 이동시 이전 탭에 영향을 주는 식
            }
        });


        System.out.println("onCreate 종료");

    }

    public HashMap<String, PointF> findStationXML() {
        HashMap<String, PointF> stations = new HashMap<>();
        boolean isTextPart = false;
        String text;
        String xy = "";
        AssetManager am = getResources().getAssets();
        InputStream is;
        try {
            // XML 파일 스트림 열기
            is = am.open("subway_linemap.svg");

            // XML 파서 초기화
            XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();

            // XML 파서에 파일 스트림 지정.
            parser.setInput(is, "UTF-8");

            int parserEvent = parser.getEventType();
            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("text")) {
                            isTextPart = true;
                            xy = parser.getAttributeValue(0);
                            break;
                        } else {
                            break;
                        }
                    case XmlPullParser.TEXT: //parser가 내용에 접근했을때
                        if (isTextPart) {
                            text = parser.getText();
//                            if (text.equals(gpsStation)) {
                            String[] data = xy.split(" ");
                            float imageX = Float.parseFloat(data[4]);
                            float imageY = Float.parseFloat(data[5].substring(0, data[5].length() - 1));
                            stations.put(text, new PointF(imageX, imageY));
//                            }
                            isTextPart = false;
                            break;
                        } else {
                            break;
                        }
                    case XmlPullParser.END_TAG: //parser가 종료태그 만났을 때
                        break;
                }
                parserEvent = parser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("파싱 결과 에러 2");
        }
        Log.d("findStationsXML", "Count = " + stations.size());
        return stations;
    }

    // 콜백 함수 구현
    OnResultCallbackListener onResultCallbackListener = new OnResultCallbackListener() {
        // 호출 성공 시 실행
        @Override
        public void onSuccess(ODsayData odsayData, API api) {
            Object gpsStation;
            try {
                if (api == API.POINT_SEARCH) {
                    gpsinfo = (TextView) findViewById(R.id.info);
                    if (odsayData.getJson().getJSONObject("result").getJSONArray("station").length() <= 0) {
                        gpsStation = "종로3가";
                        gpsinfo.setText("반경 안에 지하철역이 없습니다");
                    } else {
                        gpsStation = odsayData.getJson().getJSONObject("result").getJSONArray("station").getJSONObject(0).getString("stationName");
                        gpsinfo.setText("반경 안에 " + gpsStation + "역이 있습니다");
                        if(odsayData.getJson().getJSONObject("result").getJSONArray("station").length() > 1){
                            gpsinfo.setText(gpsinfo.getText() + "\n\n그 외의 가까운 역 : ");
                            for (int i = 1; i < odsayData.getJson().getJSONObject("result").getJSONArray("station").length(); i++){
                                gpsinfo.setText(gpsinfo.getText() + odsayData.getJson().getJSONObject("result").getJSONArray("station").getJSONObject(i).getString("stationName") );
                                if(i < odsayData.getJson().getJSONObject("result").getJSONArray("station").length() - 1 )
                                    gpsinfo.setText(gpsinfo.getText() + ", ");
                                else gpsinfo.setText(gpsinfo.getText() + " ");
                            }
                        }
                    }
                    if (stations != null) {
                        PointF stationPos = stations.get(gpsStation);
                        if (stationPos != null) {
                            mBigImageView.getSSIV().setScaleAndCenter(1.5f, stationPos);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // 호출 실패 시 실행
        @Override
        public void onError(int i, String s, API api) {
            if (api == API.POINT_SEARCH) {
                System.out.println("실행에러");
            }
        }
    };

    private void showGPS(Location location) {
        if (location != null) {
            gpsX = "" + location.getLatitude();
            gpsY = "" + location.getLongitude();
        }
    }
/*
    private ArrayList<String[]> gettcoorname(Context context) {//이거 지금 두개
        ArrayList<String[]> codes = new ArrayList<>();
        DBOpenHelper help = new DBOpenHelper(context);
        help.open();
        Cursor search_cursor = help.selectFavorite();
        try {
            while (search_cursor.moveToNext()) {
                String[] tmp = new String[2];
                String code = search_cursor.getString(search_cursor.getColumnIndex("station_code"));
                String name = search_cursor.getString(search_cursor.getColumnIndex("station_name"));
                String line = search_cursor.getString(search_cursor.getColumnIndex("station_line"));
                String fr = search_cursor.getString(search_cursor.getColumnIndex("station_fr"));
                String favorite = search_cursor.getString(search_cursor.getColumnIndex("favorite_station"));
                tmp[0] = name;
                tmp[1] = line;
                codes.add(tmp);
            }
        } catch (Exception ignored) {

        } finally {
            try {
                search_cursor.close();
            } catch (Exception ignored) {
            }
            try {
                help.close();
            } catch (Exception ignored) {
            }
        }
        return codes;
    }
*/
    private ArrayList<String[]> getFavoriteDBname(Context context) {//이거 지금 두개
        ArrayList<String[]> codes = new ArrayList<>();
        Cursor search_cursor = help.Station_SelectFavorite();
        try {
            while (search_cursor.moveToNext()) {
                String[] tmp = new String[2];
                String code = search_cursor.getString(search_cursor.getColumnIndex("station_code"));
                String name = search_cursor.getString(search_cursor.getColumnIndex("station_name"));
                String line = search_cursor.getString(search_cursor.getColumnIndex("station_line"));
                String fr = search_cursor.getString(search_cursor.getColumnIndex("station_fr"));
                String favorite = search_cursor.getString(search_cursor.getColumnIndex("favorite_station"));
                tmp[0] = name;
                tmp[1] = line;
                codes.add(tmp);
            }
        } catch (Exception ignored) {

        } finally {
            try {
                search_cursor.close();
            } catch (Exception ignored) {
            }
            try {
            } catch (Exception ignored) {
            }
        }
        return codes;
    }

    private void moevTofullScreen(View view) {
        Intent intent = new Intent(Subway_main.this, Subway_fullScreen.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.subway_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_subway_searchbar) { // 검색 화면으로 이동
            Intent intent = new Intent(Subway_main.this, Subway_search.class);
            startActivityForResult(intent, 1001);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_bus) {
            //버스 메인 화면으로 이동
            Intent intent = new Intent(Subway_main.this, Bus_main.class);
            startActivityForResult(intent, 1000);
        } else if (id == R.id.nav_fav) {
            //즐겨찾기 화면으로 이동
            Intent intent = new Intent(Subway_main.this, common_favorites.class);
            startActivityForResult(intent, 1000);
        } else if (id == R.id.nav_lost) {
            //분실물 화면으로 이동
            Intent intent = new Intent(Subway_main.this, Subway_lost_property.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            //설정 화면으로 이동
            Intent intent = new Intent(Subway_main.this, common_setting.class);
            startActivityForResult(intent, 1000);
        } else if (id == R.id.nav_share) {
            //공유하기
            TextTemplate params = TextTemplate.newBuilder("오즈(OZ)",
                    LinkObject.newBuilder().setWebUrl("market://details?id=com.example.choijh.subwayapp").setMobileWebUrl("market://details?id=com.example.choijh.subwayapp").build()).setButtonTitle("어플에서 보기").build();

            Map<String, String> serverCallbackArgs = new HashMap<String, String>();
            serverCallbackArgs.put("user_id", "${current_user_id}");
            serverCallbackArgs.put("product_id", "${shared_product_id}");

            KakaoLinkService.getInstance().sendDefault(this, params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    Logger.e(errorResult.toString());
                }

                @Override
                public void onSuccess(KakaoLinkResponse result) {
                    // 템플릿 밸리데이션과 쿼터 체크가 성공적으로 끝남. 톡에서 정상적으로 보내졌는지 보장은 할 수 없다. 전송 성공 유무는 서버콜백 기능을 이용하여야 한다.
                }
            });
        } else if (id == R.id.nav_send) {
            //개발자 문의 화면으로 이동
            Intent emailIntent = new Intent(Intent.ACTION_SEND);

            try {
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"hanshin@gmail.com"});
                emailIntent.setType("text/html");
                emailIntent.setPackage("com.google.android.gm");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[이끌]");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "불편하신 부분이나 문의 사항에 대해 입력해주세요.\n");
                if (emailIntent.resolveActivity(getPackageManager()) != null)
                    startActivity(emailIntent);

                startActivity(emailIntent);
            } catch (Exception e) {
                e.printStackTrace();

                emailIntent.setType("text/html");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"hanshin@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[이끌]");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "불편하신 부분이나 문의 사항에 대해 입력해주세요.\n");
                startActivity(Intent.createChooser(emailIntent, "Send Email"));
            }
        } else if (id == R.id.nav_route) {
            //임시! 지하철 경로
            Intent intent = new Intent(Subway_main.this, Subway_route.class);
            startActivityForResult(intent, 1000);
        } else if (id == R.id.nav_detail) {
            //임시! 지하철 상세페이지
            Intent intent = new Intent(Subway_main.this, Subway_detailPage.class);
            startActivityForResult(intent, 1000);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getInfoFromAPI() { // 꼭 네트워크 연결 후 사용(안 하면 안 나옴)
        if (!help.Station_ConfirmTable()) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        String key = "7a6c6556566a686338384f56675879"; // 지하철역 이름으로 검색 인증키

                        boolean inCD = false, inNAME = false, inNUM = false, inFR = false;
                        String station_code = null, station_nm = null, line_num = null, fr_code = null;

                        URL url = new URL("http://openapi.seoul.go.kr:8088/" + key
                                + "/xml/SearchInfoBySubwayNameService/1/716"); //검색 URL

                        XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
                        XmlPullParser parser = parserCreator.newPullParser();

                        parser.setInput(url.openStream(), null);

                        int parserEvent = parser.getEventType();
                        System.out.println("지하철역 이름을 통한 검색 API 파싱 중...");
                        while (parserEvent != XmlPullParser.END_DOCUMENT) {
                            switch (parserEvent) {
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
                                    } else {
                                        break;
                                    }
                                case XmlPullParser.TEXT: //parser가 내용에 접근했을때
                                    if (inCD) {
                                        station_code = parser.getText();
                                        inCD = false;
                                        break;
                                    } else if (inNAME) {
                                        station_nm = parser.getText();
                                        inNAME = false;
                                        break;
                                    } else if (inNUM) {
                                        line_num = parser.getText();
                                        inNUM = false;
                                        break;
                                    } else if (inFR) {
                                        fr_code = parser.getText();
                                        inFR = false;
                                        break;
                                    } else {
                                        break;
                                    }
                                case XmlPullParser.END_TAG: //parser가 종료태그 만났을 때 ex) </ul>
                                    if (parser.getName().equals("row")) {
                                        help.StationDB_insert(station_code, station_nm, line_num, fr_code, 0);
                                    }
                                    break;
                            }
                            parserEvent = parser.next();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    // kakao 키 해시값 받아오기(하단 Logcat부분에 KeyHash 검색
    private void getHashKey() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        String station_code;

        int position;

        String station_up_name;
        String station_up_time;
        String station_dn_name;
        String station_dn_time;

        public PlaceholderFragment() {
        }

        public static Subway_main.PlaceholderFragment newInstance(int sectionNumber, String code) {
            Subway_main.PlaceholderFragment fragment = new Subway_main.PlaceholderFragment();
            fragment.station_code = code;
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override//뷰레이아웃안에 내용부분에 영향을 끼치는 부분
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_content_subway_main_arrival, container, false);

            textView1 = (TextView) rootView.findViewById(R.id.main_view_up_station);
            textView2 = (TextView) rootView.findViewById(R.id.main_view_up_time);
            textView3 = (TextView) rootView.findViewById(R.id.main_view_dn_station);
            textView4 = (TextView) rootView.findViewById(R.id.main_view_dn_time);
            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            if (station_up_name != null)
                textView1.setText(station_up_name);
            if (station_up_time != null)
                textView2.setText(station_up_time);
            if (station_dn_name != null)
                textView3.setText(station_dn_name);
            if (station_dn_time != null)
                textView4.setText(station_dn_time);

            arrival_time();
        }

        @Override
        public void onPause() {
            super.onPause();
        }

        private JSONObject inputStreamToJsonObject(InputStream inputStreamObject) throws IOException, JSONException {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStreamObject, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            try {
                inputStreamObject.close();
            } catch (Exception ignored) {

            }
            return new JSONObject(responseStrBuilder.toString());
        }

        private void arrival_time() {
            new Thread() {
                @Override
                public void run() {
                    String time_now = new SimpleDateFormat("HHmm").format(new Date());
                    String subwayName = null;
                    String key = "684a69417161726133346d716f4771";
                    String station_up = "1";//상행선
                    String station_dn = "2";//하행선

                    station_up_name = "";
                    station_up_time = "";
                    station_dn_name = "";
                    station_dn_time = "";
                    if (station_code==""){
                        textView1.setText("즐겨찾기");
                        textView2.setText("없습니다");
                        return;
                    }
                    try {
                        String week_code = getWeekCode();
                        URL url_up = new URL("http://openapi.seoul.go.kr:8088/" + key + "/json/SearchArrivalTimeOfLine2SubwayByIDService/1/5/"
                                + station_code + "/" + station_up + "/" + week_code);
                        URL url_dn = new URL("http://openapi.seoul.go.kr:8088/" + key + "/json/SearchArrivalTimeOfLine2SubwayByIDService/1/5/"
                                + station_code + "/" + station_dn + "/" + week_code);
                        System.out.println("상행선 파싱 시작");
                        JSONObject upDownArray[] = {inputStreamToJsonObject(url_up.openStream()), inputStreamToJsonObject(url_dn.openStream())};
                        for (int idx = 0; idx < upDownArray.length; idx++) {
                            JSONArray updownArray = upDownArray[idx].getJSONObject("SearchArrivalTimeOfLine2SubwayByIDService").getJSONArray("row");
                            if (updownArray.length() == 0) {
                                textView1.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView1.setText("열차가");
                                        textView2.setText("없습니다.");
                                        textView3.setText("");
                                        textView4.setText("");
                                    }
                                });
                            } else {
                                // 그외 최대 두개까지만 표시함.
                                for (int i = 0; i < 2; i++) {
                                    JSONObject currentObject = updownArray.getJSONObject(i);
                                    if (subwayName == null) {
                                        // subwayName이 NULL일 경우
                                        subwayName = currentObject.getString("SUBWAYNAME");
                                    }
                                    String LEFTTIME = currentObject.getString("LEFTTIME");
                                    String DESTSTATION_NAME = currentObject.getString("DESTSTATION_NAME");
                                    String station_time = LEFTTIME.substring(0, 2) + LEFTTIME.substring(3, 5);// 시간 형식 변환 hh:mm:ss=>hhmm
                                    int arrival_times = Integer.parseInt(station_time) - Integer.parseInt(time_now);
                                    if (Integer.parseInt(LEFTTIME.substring(1, 2)) - Integer.parseInt(time_now.substring(1, 2)) == 1)// 시가 넘어갈 때 40이상 되는거 변화
                                        arrival_times -= 40;

                                    if (idx == 0) { // 상행선일 경우
                                        station_up_name = station_up_name + DESTSTATION_NAME + "행\n";
                                        if (arrival_times == 0) {
                                            station_up_time = station_up_time + "도착\n";
                                        } else {
                                            station_up_time = station_up_time + arrival_times + "분 후\n";
                                        }
                                    } else {
                                        station_dn_name = station_dn_name + DESTSTATION_NAME + "행\n";
                                        if (arrival_times == 0) {
                                            station_dn_time = station_dn_time + "도착\n";
                                        } else {
                                            station_dn_time = station_dn_time + arrival_times + "분 후\n";
                                        }
                                    }
                                    if (updownArray.length() == (i + 1)) {
                                        break;
                                    }
                                }
                            }
                        }
                        // UI 스레드에서 작업해야되므로
                        textView1.post(new Runnable() {
                            @Override
                            public void run() {
                                textView1.setText(station_up_name);
                                textView2.setText(station_up_time);
                                textView3.setText(station_dn_name);
                                textView4.setText(station_dn_time);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

        private String getWeekCode() {
            String week_code = "1";
            Calendar cal = Calendar.getInstance();
            int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
            if (day_of_week == 1)//일요일
                week_code = "3";
            else if (day_of_week == 2)//월요일
                week_code = "1";
            else if (day_of_week == 3)//화요일
                week_code = "1";
            else if (day_of_week == 4)//수요일
                week_code = "1";
            else if (day_of_week == 5)//목요일
                week_code = "1";
            else if (day_of_week == 6)//금요일
                week_code = "1";
            else if (day_of_week == 7)//토요일
                week_code = "2";
            return week_code;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        Context context;
        ArrayList<String> codes;
        Fragment[] fragments;

        SectionsPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            this.context = context;
            codes = getFavoriteDB(context);

            if(codes.size() == 0){
                fragments = new Fragment[1];
                fragments[0] = Subway_main.PlaceholderFragment.newInstance(1, "");//즐겨찾기 없음
            }
            else {
                fragments = new Fragment[codes.size()];
                for (int i = 0; i < codes.size(); i++) {
                    fragments[i] = Subway_main.PlaceholderFragment.newInstance(i + 1, codes.get(i));
                }
            }
        }

        @Override
        public Fragment getItem(int position) {//포자션에 해당하는 프래그먼트 반환
            // position이 0부터 시작
            return fragments[position];
        }

        @Override
        public int getCount() {//리턴되는 수에 따라 페이지의 수 결정
            return fragments.length;
        }

        private ArrayList<String> getFavoriteDB(Context context) {
            ArrayList<String> codes = new ArrayList<>();
            Cursor search_cursor = help.Station_SelectFavorite();
            try {
                while (search_cursor.moveToNext()) {
                    String code = search_cursor.getString(search_cursor.getColumnIndex("station_code"));
                    String name = search_cursor.getString(search_cursor.getColumnIndex("station_name"));
                    String line = search_cursor.getString(search_cursor.getColumnIndex("station_line"));
                    String fr = search_cursor.getString(search_cursor.getColumnIndex("station_fr"));
                    String favorite = search_cursor.getString(search_cursor.getColumnIndex("favorite_station"));
                    codes.add(code);
                }
            } catch (Exception ignored) {

            } finally {
                try {
                    search_cursor.close();
                } catch (Exception ignored) {
                }
                try {
                } catch (Exception ignored) {
                }
            }
            return codes;
        }
    }


}
