package com.example.choijh.subwayapp;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amitshekhar.DebugDB;
import com.caverock.androidsvg.SVG;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.kakao.message.template.LinkObject;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.TextTemplate;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.network.*;
import com.kakao.util.helper.log.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


public class Subway_main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    PhotoView photoView;
    SVG svg;
    private Toast mCurrentToast;//포토비유 탭의 임시
    static final String PHOTO_TAP_TOAST_STRING = "Photo Tap! X: %.2f %% Y:%.2f %% ID: %d";//포토비유 탭의 임시
    ListView listview ;//커스텀뷰
    //ListViewMainArrivalAdapter adapter;//커스텀뷰
    String time_now;//현재시간
    DBOpenHelper help;
    private Subway_main.SectionsPagerAdapter mSectionsPagerAdapter;//탭레이아웃
    private ViewPager mViewPager;//탭레이아웃
    static String station_up_name = null;
    static String station_up_time = null;
    static String station_dn_name = null;
    static String station_dn_time = null;
    //    TextView ari;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway_main);
        arrival_time();
//        ari = (TextView) findViewById(R.id.arrival_time);
        // Adapter 생성//커스텀뷰
//        adapter = new ListViewMainArrivalAdapter() ;//커스텀뷰

        // 리스트뷰 참조 및 Adapter달기//커스텀뷰
        //listview = (ListView) findViewById(R.id.listview1);//커스텀뷰
        time_now = new SimpleDateFormat("HHmm").format(new Date());
        //draw_svg();
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

        try {
            svg = SVG.getFromAsset(getResources().getAssets(), "svg_seoul_subway_linemap.svg");
            PictureDrawable drawable = new PictureDrawable(svg.renderToPicture());
            photoView = findViewById(R.id.sub_map);

            photoView.setImageDrawable(drawable);
            photoView.setMinimumScale(0.5f);
            photoView.setMaximumScale(30.0f);

            photoView.post(new Runnable() {
                @Override
                public void run() {
                    float x = 2862f;
                    float y = 2790f;

                    float focalX = x * photoView.getRight() / svg.getDocumentWidth();
                    float focalY = y * photoView.getBottom() / svg.getDocumentHeight();
                    photoView.setScale(5.0f, focalX, focalY, false);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        photoView.setOnPhotoTapListener(new PhotoTapListener());
//        listview.setAdapter(adapter);//커스텀뷰//살릴것
        //탭레이아웃

//        System.out.println("aa를 제대로 가져왔는가? : " + aaa);

        mSectionsPagerAdapter = new Subway_main.SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.main_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));



        //ari.setText("aaaaaaaaaaaaaaaaaaaaaaaaa");

        System.out.println("onCreate 종료");
    }
    private void draw_svg(){
        new Thread(){
            @Override
            public void run(){

            }
        }.start();

    }

    private void arrival_time(){
        new Thread(){
            @Override
            public void run(){
//                TextView arrivalTime = (TextView) findViewById(R.id.arrival_time); //파싱된 결과확인! 이것은 파싱한 것이 나올 부분
                boolean inDESTSTATION_NAME = false, inARRIVETIME = false, inSUBWAYNAME = false;
                String DESTSTATION_NAME = null, ARRIVETIME = null, SUBWAYNAME = null;
                String statin_up_arrival = null;
                String statin_dn_arrival = null;
                String key = "684a69417161726133346d716f4771";
                String station_code = "1716";//병점
                String station_up = "1";//상행선
                String station_dn = "2";//하행선
                String week_code = "1";
                String station_time = null;
                //해야할 일
                //1. 평일 1, 토요일 2, 공휴일 3 => 오늘 날짜를 보고 평일인지 휴일인지 알게하는 함수 추가 할 것.
                //2. 새벽시간 차가 없을때 널값으로 줘서 이상한 값이 나오지 않게 할 것
                //나중에 해야할 일
                //1. 디비 받아서 호선별 색 이미지 달라지게 다른 색상의 이미지 추가
                //   호선별로 다른 색으로 바뀌게 하기
                //2. 출발지 임의로 하지 않고 즐겨찾기에서 코드로 받아서 하기 ex)병점 => 1716

                try {
//                    URL url = new URL("http://openapi.seoul.go.kr:8088/684a69417161726133346d716f4771/xml/SearchArrivalTimeOfLine2SubwayByIDService/1/5/1716/2/2");
                    URL url_up = new URL("http://openapi.seoul.go.kr:8088/" + key + "/xml/SearchArrivalTimeOfLine2SubwayByIDService/1/5/"
                            + station_code + "/" + station_up + "/" + week_code);
                    URL url_dn = new URL("http://openapi.seoul.go.kr:8088/" + key + "/xml/SearchArrivalTimeOfLine2SubwayByIDService/1/5/"
                            + station_code + "/" + station_dn + "/" + week_code);
                    XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = parserCreator.newPullParser();
                    parser.setInput(url_up.openStream(), null);
                    int parserEvent = parser.getEventType();
                    System.out.println("상행선 파싱 시작");
                    int i = 0;
                    while (parserEvent != XmlPullParser.END_DOCUMENT) {
                        switch (parserEvent) {
                            case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                                if (parser.getName().equals("DESTSTATION_NAME")) { // 종착지하철역명 만나면 내용을 받을수 있게 하자
                                    inDESTSTATION_NAME = true;
                                }else if (parser.getName().equals("ARRIVETIME")) { // 첫번째도착메세지행 만나면 내용을 받을수 있게 하자
                                    inARRIVETIME = true;
                                }else if (parser.getName().equals("SUBWAYNAME")) { // 첫번째도착메세지행 만나면 내용을 받을수 있게 하자
                                    inSUBWAYNAME = true;
                                }
                                break;
                            case XmlPullParser.TEXT:
                                if (inDESTSTATION_NAME) {
                                    DESTSTATION_NAME = parser.getText();
                                    inDESTSTATION_NAME = false;
                                }else if (inARRIVETIME) {
                                    ARRIVETIME = parser.getText();
                                    inARRIVETIME = false;
                                }else if (inSUBWAYNAME) {
                                    SUBWAYNAME = parser.getText();
                                    inSUBWAYNAME = false;
                                }
                                break;
                            case XmlPullParser.END_TAG:
                                if ("row".equals(parser.getName())) {
                                    ++i;
                                    if(i>2)
                                        break;
                                    station_time = ARRIVETIME.substring(0,2) + ARRIVETIME.substring(3,5);// 시간 형식 변환 hh:mm:ss=>hhmm
                                    int arrival_times = Integer.parseInt(station_time) - Integer.parseInt(time_now);
                                    if(Integer.parseInt(ARRIVETIME.substring(1,2)) - Integer.parseInt(time_now.substring(1,2)) == 1)// 시가 넘어갈 때 40이상 되는거 변화
                                        arrival_times -= 40;
                                    if (station_up_name == null){
                                        station_up_name = "";
                                        station_up_time = "";
                                        station_up_name = station_up_name + DESTSTATION_NAME + "행\n";
                                        station_up_time = station_up_time + arrival_times + "분 후\n";
                                    } else if(arrival_times == 0){
                                        station_up_name = station_up_name +DESTSTATION_NAME + "행 도착\n";
                                        station_up_time = station_up_time + arrival_times + "분 후\n";
                                    } else {
                                        station_up_name = station_up_name + DESTSTATION_NAME + "행\n";
                                        station_up_time = station_up_time + arrival_times + "분 후\n";
                                    }
                                }
                                break;
                        }
                        parserEvent = parser.next();
                    }
//                    aaa = statin_up_arrival;
//                    ari.setText("aaaaaaaaaaaaaaaaaaaaaaaaa");
//                    ari.setText(aaa);
                    parser.setInput(url_dn.openStream(), null);
                    parserEvent = parser.getEventType();
                    System.out.println("하행선 파싱 시작");
                    i = 0;
                    while (parserEvent != XmlPullParser.END_DOCUMENT) {
                        switch (parserEvent) {
                            case XmlPullParser.START_TAG:
                                if (parser.getName().equals("DESTSTATION_NAME")) {
                                    inDESTSTATION_NAME = true;
                                    System.out.println("여기는 진행되었음");
                                }else if (parser.getName().equals("ARRIVETIME")) {
                                    inARRIVETIME = true;
                                }
                                break;
                            case XmlPullParser.TEXT://parser가 내용에 접근했을때
                                if (inDESTSTATION_NAME) { //inbstatnNm이 true일 때 태그의 내용을 저장.
                                    DESTSTATION_NAME = parser.getText();
                                    inDESTSTATION_NAME = false;
                                }else if (inARRIVETIME) { //inarvlMsg2이 true일 때 태그의 내용을 저장.
                                    ARRIVETIME = parser.getText();
                                    inARRIVETIME = false;
                                }
                                break;
                            case XmlPullParser.END_TAG:
                                if ("row".equals(parser.getName())) {
                                    ++i;
                                    if(i>2)
                                        break;
                                    station_time = ARRIVETIME.substring(0,2) + ARRIVETIME.substring(3,5);
                                    int arrival_times = Integer.parseInt(station_time) - Integer.parseInt(time_now);
                                    if(Integer.parseInt(ARRIVETIME.substring(1,2)) - Integer.parseInt(time_now.substring(1,2)) == 1)
                                        arrival_times -= 40;
                                    if (station_dn_name == null){
                                        station_dn_name = "";
                                        station_dn_time = "";
                                        station_dn_name = station_dn_name + DESTSTATION_NAME + "행\n";
                                        station_dn_time = station_dn_time + arrival_times + "분 후\n";
                                    } else if(arrival_times == 0){
                                        station_dn_name = station_dn_name + DESTSTATION_NAME + "행 도착\n";
                                        station_dn_time = station_dn_time + arrival_times + "분 후\n";
                                    } else {
                                        station_dn_name = station_dn_name + DESTSTATION_NAME + "행\n";
                                        station_dn_time = station_dn_time + arrival_times + "분 후\n";
                                    }
                                }
                                break;
                        }
                        parserEvent = parser.next();
                    }//하행선 파싱 끝

//                    arrivalTime.setText(arrivalTime.getText() + station_time );
//                    adapter.addItem(SUBWAYNAME,statin_up_arrival, statin_dn_arrival) ;
                } catch (Exception e) {
//                    arrivalTime.setText("에러가..났습니다...");
                    e.printStackTrace();
                }
                System.out.println("도착시간 파싱 끝");
            }
        }.start();
    };
    private void moevTofullScreen(View view) {
        Intent intent = new Intent(Subway_main.this, Subway_fullScreen.class);
        startActivity(intent);
    }

    private class PhotoTapListener implements OnPhotoTapListener {//탭

        @Override
        public void onPhotoTap(ImageView view, float x, float y) {
            float xPercentage = x * 100f;
            float yPercentage = y * 100f;

//            showToast(String.format(PHOTO_TAP_TOAST_STRING, xPercentage, yPercentage, view == null ? 0 : view.getId()));
            moevTofullScreen(view);
        }
    }

    private void showToast(CharSequence text) {
        if (mCurrentToast != null) {
            mCurrentToast.cancel();
        }
        mCurrentToast = Toast.makeText(Subway_main.this, text, Toast.LENGTH_SHORT);
        mCurrentToast.show();
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

        if (id == R.id.action_searchbar) { // 검색 화면으로 이동
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

            KakaoLinkService.getInstance().sendDefault(this,params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
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

        help = new DBOpenHelper(getApplicationContext());
        help.open();
        help.create();

        if(!help.confirmTable()) {
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
                                        //searchAdapter.addItem(station_code, station_nm, line_num, fr_code); // 어댑터에 데이터 삽입
                                        help.insertColumn(station_code, station_nm, line_num, fr_code);
                                    }
                                    break;
                            }
                            parserEvent = parser.next();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    help.close();
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

    //탭레이아웃 시작


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Subway_main.PlaceholderFragment newInstance(int sectionNumber) {
            Subway_main.PlaceholderFragment fragment = new Subway_main.PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override//뷰레이아웃안에 내용부분에 영향을 끼치는 부분
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Subway_main aa = new Subway_main();
            View rootView = inflater.inflate(R.layout.fragment_content_subway_main_arrival, container, false);
            System.out.println("내부클래스 시작");
            Button button1 = (Button) rootView.findViewById(R.id.main_view_btn);
            button1.setBackgroundColor(Color.rgb(46,86,184));
            button1.setTextColor(Color.parseColor("#ffffff"));
            button1.setText("병점");
            TextView textView1 = (TextView) rootView.findViewById(R.id.main_view_up_station);
            TextView textView2 = (TextView) rootView.findViewById(R.id.main_view_up_time);
            TextView textView3 = (TextView) rootView.findViewById(R.id.main_view_dn_station);
            TextView textView4 = (TextView) rootView.findViewById(R.id.main_view_dn_time);
//            System.out.println(aaa);
            textView1.setText(station_up_name);
            textView2.setText(station_up_time);
            textView3.setText(station_dn_name);
            textView4.setText(station_dn_time);
            System.out.println("내부클래스 온크리에이트 종료");

            return rootView;
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return Subway_main.PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }

}
