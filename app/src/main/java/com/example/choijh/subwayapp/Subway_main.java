package com.example.choijh.subwayapp;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.caverock.androidsvg.SVG;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
//import uk.co.senab.photoview.PhotoViewAttacher;

public class Subway_main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    PhotoView photoView;
    SVG svg;
    //ImageView m_imageview; //확대축소를 위한 부분
    //PhotoViewAttacher mAttacher;//확대축소를 위한 부분

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway_main);

       // m_imageview = (ImageView) findViewById(R.id.sub_map);//확대축소를 위한 부분
        //mAttacher = new PhotoViewAttacher(m_imageview);//확대축소를 위한 부분

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //원래 툴바 타이틀(제목)없애줌

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //버스 메인 화면으로 이동
                Intent intent = new Intent(Subway_main.this, Bus_main.class);
                startActivityForResult(intent, 1000);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //getHashKey();
        try {
            svg = SVG.getFromAsset(this.getAssets(), "svg_seoul_subway_linemap.svg");
            PictureDrawable drawable = new PictureDrawable(svg.renderToPicture());


            photoView = (PhotoView) findViewById(R.id.sub_map);

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
//        ((ScalableSVGImageView) findViewById(R.id.imageView1)).internalSetImageAsset("svg_seoul_subway_linemap.svg");
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
        getMenuInflater().inflate(R.menu.subway_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_search_layout) { // 검색 화면으로 이동
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
            startActivityForResult(intent, 1000);
        } else if (id == R.id.nav_manage) {
            //설정 화면으로 이동
            Intent intent = new Intent(Subway_main.this, common_setting.class);
            startActivityForResult(intent, 1000);
        } else if (id == R.id.nav_share) {
            //공유하기
        } else if (id == R.id.nav_send) {
            //개발자 문의 화면으로 이동
            Intent emailIntent = new Intent(Intent.ACTION_SEND);

            try {
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"hanshin@gmail.com"});
                emailIntent.setType("text/html");
                emailIntent.setPackage("com.google.android.gm");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT,"[이끌]");
                emailIntent.putExtra(Intent.EXTRA_TEXT,"불편하신 부분이나 문의 사항에 대해 입력해주세요.\n");
                if(emailIntent.resolveActivity(getPackageManager())!=null)
                    startActivity(emailIntent);

                startActivity(emailIntent);
            } catch (Exception e) {
                e.printStackTrace();

                emailIntent.setType("text/html");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"hanshin@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT,"[이끌]");
                emailIntent.putExtra(Intent.EXTRA_TEXT,"불편하신 부분이나 문의 사항에 대해 입력해주세요.\n");
                startActivity(Intent.createChooser(emailIntent, "Send Email"));
            }
        } else if (id == R.id.nav_route) {
            //임시! 지하철 경로
            Intent intent = new Intent(Subway_main.this, Subway_route.class);
            startActivityForResult(intent, 1000);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // kakao 키 해시값 받아오기(하단 Logcat부분에 KeyHash 검색
    private void getHashKey(){
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


}
