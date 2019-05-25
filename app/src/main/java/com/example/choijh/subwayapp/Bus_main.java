package com.example.choijh.subwayapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Log;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;


public class Bus_main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener {
    FragmentPagerAdapter adapterViewPager;

    ImageButton ibtn1;
    ImageButton ibtn2;
    ImageButton ibtn3;
    ImageButton ibtn4;
    ImageButton ibtn5;

    private static final String LOG_TAG = "Bus_main";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION};
    String x;
    String y;

    MapView mapView;
    ArrayList<MapPOIItem> marker = new ArrayList<MapPOIItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //원래 툴바 타이틀(제목)없애줌

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //지하철 메인 화면으로 이동
                Intent intent = new Intent(Bus_main.this, Subway_main.class);
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

        // 지도 띄워주는 부분
        mapView = new MapView(this);

        ViewGroup mapViewContainer = findViewById(R.id.map_view1);
        mapViewContainer.addView(mapView);

        //뷰페이저 부분
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(vpPager);

        ibtn1 = (ImageButton) findViewById(R.id.cafe);
        ibtn2 = (ImageButton) findViewById(R.id.restaurant);
        ibtn3 = (ImageButton) findViewById(R.id.hospital);
        ibtn4 = (ImageButton) findViewById(R.id.bank);
        ibtn5 = (ImageButton) findViewById(R.id.hotel);
        ibtn1.setOnClickListener(ibtnOnClickListener);
        ibtn2.setOnClickListener(ibtnOnClickListener);
        ibtn3.setOnClickListener(ibtnOnClickListener);
        ibtn4.setOnClickListener(ibtnOnClickListener);
        ibtn5.setOnClickListener(ibtnOnClickListener);

        mapView.setCurrentLocationEventListener(this);

        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        }else {
            checkRunTimePermission();
        }
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
        getMenuInflater().inflate(R.menu.bus_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_bus_searchbar) {
            Intent intent = new Intent(Bus_main.this, Bus_search.class);
            startActivityForResult(intent, 1001);
        }

        if (id == R.id.action_bus_gps) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_subway) {
            //지하철 메인 화면으로 이동
            Intent intent = new Intent(Bus_main.this, Subway_main.class);
            startActivityForResult(intent, 1000);
        } else if (id == R.id.nav_fav2) {
            //즐겨찾기 화면으로 이동
            Intent intent = new Intent(Bus_main.this, common_favorites.class);
            startActivityForResult(intent, 1000);
        } else if (id == R.id.nav_lost2) {
            //분실물 화면으로 이동
            Intent intent = new Intent(Bus_main.this, Bus_lost_property.class);
            startActivityForResult(intent, 1000);
        } else if (id == R.id.nav_manage2) {
            //설정 화면으로 이동
            Intent intent = new Intent(Bus_main.this, common_setting.class);
            startActivityForResult(intent, 1000);
        } else if (id == R.id.nav_share2) {

        } else if (id == R.id.nav_send2) {
            //개발자 문의
            Intent intent = new Intent(Intent.ACTION_SEND);
            try {
                intent.setType("text/html");
                String[] address = {"ozdeveloper@naver.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, address);
                intent.putExtra(Intent.EXTRA_SUBJECT, "제목");
                intent.putExtra(Intent.EXTRA_TEXT, "어플 사용시 불편하신 사항이나 문의 내용을 입력해주세요.");
                intent.setPackage("com.google.android.gm");
                if(intent.resolveActivity(getPackageManager())!=null)
                    startActivity(intent);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ozdeveloper@naver.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "제목");
                intent.putExtra(Intent.EXTRA_TEXT, "어플 사용시 불편하신 사항이나 문의 내용을 입력해주세요.");
                startActivity(Intent.createChooser(intent, "Send Email"));
            }

            //Intent intent = new Intent(Bus_main.this, common_developer_question.class);
            //startActivityForResult(intent, 1000);
        }else if (id == R.id.nav_near) {
            //임시! 내 주변 버스
            Intent intent = new Intent(Bus_main.this, Bus_nearby.class);
            startActivityForResult(intent, 1000);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //뷰페이저
    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return BusMainFirstFragment.newInstance(0, "Page # 1");
                case 1:
                    return BusMainSecondFragment.newInstance(1, "Page # 2");
                case 2:
                    return BusMainThirdFragment.newInstance(2, "Page # 3");
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }

    //상권마커 띄우는 부분
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

        Call<RestApi> call = api.getApi("KakaoAK 02e90f45782fea188c0f2f655b06b7df", cateroty_gourp_code, x, y, 20000);

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

    // 여기서부터 내 위치 기반 지도 표시하는것
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        mapView.setShowCurrentLocationMarker(false);
    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters) {
        MapPoint.GeoCoordinate mapPointGeo = currentLocation.getMapPointGeoCoord();
        Log.i(LOG_TAG, String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, accuracyInMeters));
        x =  String.valueOf(mapPointGeo.longitude);
        y = String.valueOf(mapPointGeo.latitude);
    }


    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {
        mapReverseGeoCoder.toString();
        onFinishReverseGeoCoding(s);
    }

    @Override
    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {
        onFinishReverseGeoCoding("Fail");
    }

    private void onFinishReverseGeoCoding(String result) {
//        Toast.makeText(LocationDemoActivity.this, "Reverse Geo-coding : " + result, Toast.LENGTH_SHORT).show();
    }




    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {
                Log.d("@@@", "start");
                //위치 값을 가져올 수 있음
                mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

                    Toast.makeText(getApplicationContext(), "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                }else {

                    Toast.makeText(getApplicationContext(), "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED ) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);


        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(Bus_main.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(Bus_main.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(Bus_main.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(Bus_main.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }



    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Bus_main.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    /*
    //버스맵 풀화면으로 이동
    public void busFullMove(View v){
        Intent intent = new Intent(Bus_main.this, BusMapFull.class);
        startActivityForResult(intent, 1000);
    }
    */
}
