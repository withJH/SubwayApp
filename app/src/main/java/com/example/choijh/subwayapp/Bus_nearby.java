package com.example.choijh.subwayapp;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

public class Bus_nearby extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_nearby);
        Toolbar toolbar = findViewById(R.id.toolbar_route);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //원래 툴바 타이틀(제목)없애줌

        DrawerLayout drawer = findViewById(R.id.drawer_layout_near);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view_near);
        navigationView.setNavigationItemSelectedListener(this);

        //탭뷰
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_near);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_subway) {
            //지하철 메인 화면으로 이동
            Intent intent = new Intent(Bus_nearby.this, Subway_main.class);
            startActivityForResult(intent, 1000);
        } else if (id == R.id.nav_fav2) {
            //즐겨찾기 화면으로 이동
            Intent intent = new Intent(Bus_nearby.this, common_favorites.class);
            startActivityForResult(intent, 1000);
        } else if (id == R.id.nav_lost2) {
            //분실물 화면으로 이동
            Intent intent = new Intent(Bus_nearby.this, Subway_lost_property.class);
            startActivityForResult(intent, 1000);
        } else if (id == R.id.nav_manage2) {
            //설정 화면으로 이동
            Intent intent = new Intent(Bus_nearby.this, common_setting.class);
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
            Intent intent = new Intent(Bus_nearby.this, Bus_nearby.class);
            startActivityForResult(intent, 1000);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout_near);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //탭
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
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_common_favorites, container, false);
            TextView textView = rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
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
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }
}
