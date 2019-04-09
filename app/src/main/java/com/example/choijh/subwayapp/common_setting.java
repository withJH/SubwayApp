package com.example.choijh.subwayapp;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class common_setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_setting);
        //Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar1); //툴바설정
        //setSupportActionBar(toolbar1); //액션바 대신 툴바 적용(오류;)
        //getSupportActionBar().setDisplayShowTitleEnabled(false); //원래 툴바 타이틀(제목)없애줌
        setTitle("설정");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼
        /*    <item android:title="앱 설정">
        <menu>
            <item
                android:id="@+id/fav_bye"
                android:icon="@drawable/ic_menu_share"
                android:title="즐겨찾기 초기화" />
            <item
                android:id="@+id/alarm_setting"
                android:icon="@drawable/ic_menu_send"
                android:title="알림설정" />
        </menu>
    </item>
    <item android:title="앱 관리">
        <menu>
            <item
                android:id="@+id/notice"
                android:icon="@drawable/ic_menu_share"
                android:title="공지사항" />
            <item
                android:id="@+id/help"
                android:icon="@drawable/ic_menu_send"
                android:title="도움말" />
            <item
                android:id="@+id/send3"
                android:icon="@drawable/ic_menu_send"
                android:title="문의하기" />
            <item
                android:id="@+id/app_ver"
                android:icon="@drawable/ic_menu_send"
                android:title="앱 버전" />
            <item
                android:id="@+id/info"
                android:icon="@drawable/ic_menu_send"
                android:title="개인정보 처리방침??" />
            <item
                android:id="@+id/license"
                android:icon="@drawable/ic_menu_send"
                android:title="오픈소스 라이선스" />
            <item
                android:id="@+id/info2"
                android:icon="@drawable/ic_menu_send"
                android:title="이용약관" />
            <item
                android:id="@+id/info3"
                android:icon="@drawable/ic_menu_send"
                android:title="위치기반 서비스 이용약관" />
        </menu>
    </item>*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
