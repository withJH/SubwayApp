package com.example.choijh.subwayapp;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
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

    MapView mapView;
    ArrayList<MapPOIItem> marker = new ArrayList<MapPOIItem>();

    ImageButton ibtn1;
    ImageButton ibtn2;
    ImageButton ibtn3;
    ImageButton ibtn4;
    ImageButton ibtn5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway_detail);
        mContext = getApplicationContext();

        StrictMode.enableDefaults();


        mapView = new MapView(this);
        mapView.setDaumMapApiKey(" 61d3254080b5d9424b98a292a7984c8e");
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.206906, 127.033257), true);
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

        Call<RestApi> call = api.getApi("KakaoAK 02e90f45782fea188c0f2f655b06b7df", cateroty_gourp_code, "127.033257", "37.206906", 20000);

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
}
