package com.example.choijh.subwayapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.api.Response;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.SortedList;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class Bus_main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FragmentPagerAdapter adapterViewPager;
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
        MapView mapView = new MapView(this);

        ViewGroup mapViewContainer = findViewById(R.id.map_view1);
        mapViewContainer.addView(mapView);

        //뷰페이저 부분
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(vpPager);
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
        if (id == R.id.action_settings) {
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
    /*
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

        Call<RestApi> call = api.getApi("KakaoAK 42bd29682d64b314bb271a1bf48606f9", cateroty_gourp_code, "127.033257", "37.206906", 20000);

        call.enqueue(new SortedList.Callback<RestApi>() {
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
                    Toast.makeText(getContext(), "서버와 연결 되었으나 실패", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<RestApi> call, Throwable t) {
                Toast.makeText(getContext(), "실패", Toast.LENGTH_SHORT).show();
            }
        });*/

    //버스맵 풀화면으로 이동
    public void busFullMove(View v){
        Intent intent = new Intent(Bus_main.this, BusMapFull.class);
        startActivityForResult(intent, 1000);
    }
}
