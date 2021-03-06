package com.example.choijh.subwayapp;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.content.res.AppCompatResources;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class common_setting extends AppCompatActivity implements ListViewBtnAdapter.ListBtnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_setting);
        //Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar1); //툴바설정
        //setSupportActionBar(toolbar1); //액션바 대신 툴바 적용(오류;)
        //getSupportActionBar().setDisplayShowTitleEnabled(false); //원래 툴바 타이틀(제목)없애줌
        setTitle("설정");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Adapter 및 ListView 생성
        ListView listView;
        ListViewBtnAdapter adapter;
        ArrayList<ListViewBtnItem> items = new ArrayList<ListViewBtnItem>();

        //items 로드
        loadItemsFromDB(items);

        // Adapter 생성
        adapter = new ListViewBtnAdapter(common_setting.this, R.layout.listview_btn_item, items, this) ;

        // 리스트뷰 참조 및 Adapter달기
        listView = findViewById(R.id.listview_set);
        listView.setAdapter(adapter);

        // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // TODO : item click
            }
        }) ;
    }

    @Override
    public void onListBtnClick(int position) {
        Toast.makeText(this, (position + 1) + " Item is selected..", Toast.LENGTH_SHORT).show() ;
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout1);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //ArrayList에 데이터를 로드하는 loadItemsFromDB() 함수 추가
    public boolean loadItemsFromDB(ArrayList<ListViewBtnItem> list) {
        ListViewBtnItem item ;

        if (list == null) {
            list = new ArrayList<ListViewBtnItem>() ;
        }

        // 아이템 생성.
        //앱설정(즐겨찾기 초기화, 알림설정),
        // 앱 관리(공지사항, 도움말, 문의하기, 앱 버전, 개인정보 처리방침?, 오픈소스 라이선스,
        //이용약관, 위치기반 서비스 이용약관)
        item = new ListViewBtnItem() ;
        item.setIcon(AppCompatResources.getDrawable(common_setting.this, R.drawable.ic_star_border_black_24dp)) ;
        item.setText(" 즐겨찾기 초기화") ;
        list.add(item) ;

        item = new ListViewBtnItem() ;
        item.setIcon(AppCompatResources.getDrawable(this, R.drawable.ic_access_alarm_black_24dp)) ;
        item.setText(" 알림설정") ;
        list.add(item) ;

        item = new ListViewBtnItem() ;
        item.setIcon(AppCompatResources.getDrawable(this, R.drawable.ic_help_outline_black_24dp)) ;
        item.setText(" 공지사항") ;
        list.add(item) ;

        item = new ListViewBtnItem() ;
        item.setIcon(AppCompatResources.getDrawable(this, R.drawable.ic_help_black_24dp)) ;
        item.setText(" 도움말") ;
        list.add(item) ;

        item = new ListViewBtnItem() ;
        item.setIcon(AppCompatResources.getDrawable(this, R.drawable.ic_feedback_black_24dp)) ;
        item.setText(" 문의하기") ;
        list.add(item) ;

        item = new ListViewBtnItem() ;
        item.setIcon(AppCompatResources.getDrawable(this, R.drawable.ic_info_black_24dp)) ;
        item.setText(" 앱 버전") ;
        list.add(item) ;

        item = new ListViewBtnItem() ;
        item.setIcon(AppCompatResources.getDrawable(this, R.drawable.ic_sort_black_24dp)) ;
        item.setText(" 개인정보 처리방침") ;
        list.add(item) ;

        item = new ListViewBtnItem() ;
        item.setIcon(AppCompatResources.getDrawable(this, R.drawable.ic_verified_user_black_24dp)) ;
        item.setText(" 오픈소스 라이선스") ;
        list.add(item) ;

        item = new ListViewBtnItem() ;
        item.setIcon(AppCompatResources.getDrawable(this, R.drawable.ic_description_black_24dp)) ;
        item.setText(" 이용약관") ;
        list.add(item) ;

        item = new ListViewBtnItem() ;
        item.setIcon(AppCompatResources.getDrawable(this, R.drawable.ic_info_black_24dp)) ;
        item.setText(" 위치기반 서비스 이용약관") ;
        list.add(item) ;

        return true ;
    }
}
