package com.example.choijh.subwayapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import net.daum.mf.map.api.MapView;

public class Bus_main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //원래 툴바 타이틀(제목)없애줌

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // 지도 띄워주는 부분
        MapView mapView = new MapView(this);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
            //버스 메인 화면으로 이동
            Intent intent = new Intent(Bus_main.this, Bus_main.class);
            startActivityForResult(intent, 1000);
        } else if (id == R.id.nav_fav2) {
            //즐겨찾기 화면으로 이동
            Intent intent = new Intent(Bus_main.this, subway_favorites.class);
            startActivityForResult(intent, 1000);
        } else if (id == R.id.nav_lost2) {
            //분실물 화면으로 이동
            Intent intent = new Intent(Bus_main.this, subway_lost_property.class);
            startActivityForResult(intent, 1000);
        } else if (id == R.id.nav_manage2) {
            //설정 화면으로 이동
            Intent intent = new Intent(Bus_main.this, common_setting.class);
            startActivityForResult(intent, 1000);
        } else if (id == R.id.nav_share2) {
            //Intent intent = new Intent(Bus_main.this, kakaolink.class);
            //startActivityForResult(intent, 1001);
            //공유하기
            //try {
            //  KakaoLink link=KakaoLink.getKakaoLink(this);
            //  KakaoTalkLinkMessageBuilder builder=link.createKakaoTalkLinkMessageBuilder();

            //메시지 추가
            // builder.addText("[지하철&버스 어플 : OZ] 같이 사용해요");

            //앱 실행버튼
            // builder.addAppButton("앱 실행하기");

            //메시지 발송
            //   link.sendMessage(builder,this);

            // } catch (KakaoParameterException e) {
            //     e.printStackTrace();
            // }
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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
