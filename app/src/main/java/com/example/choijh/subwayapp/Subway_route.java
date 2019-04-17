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

public class Subway_route extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway_route);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_route);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //원래 툴바 타이틀(제목)없애줌

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_route);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //카톡 공유하기로 바꿀거임
                //버스 메인 화면으로 이동
                Intent intent = new Intent(Subway_route.this, Bus_main.class);
                startActivityForResult(intent, 1000);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_route);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_route);
        navigationView.setNavigationItemSelectedListener(this);

        //탭 부분
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_route);
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

        if (id == R.id.nav_bus) {
            //버스 메인 화면으로 이동
            Intent intent = new Intent(Subway_route.this, Bus_main.class);
            startActivityForResult(intent, 1000);
        } else if (id == R.id.nav_fav) {
            //즐겨찾기 화면으로 이동
            Intent intent = new Intent(Subway_route.this, common_favorites.class);
            startActivityForResult(intent, 1000);
        } else if (id == R.id.nav_lost) {
            //분실물 화면으로 이동
            Intent intent = new Intent(Subway_route.this, Subway_lost_property.class);
            startActivityForResult(intent, 1000);
        } else if (id == R.id.nav_manage) {
            //설정 화면으로 이동
            Intent intent = new Intent(Subway_route.this, common_setting.class);
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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment1 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment1() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment1 newInstance(int sectionNumber) {
            PlaceholderFragment1 fragment = new PlaceholderFragment1();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        //경로 내용 들어가는 곳
        //ARG_SECTION_NUMBER값에 따라 최단시간, 최소환승 나뉠듯
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_subway_route, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label_route);
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
            return PlaceholderFragment1.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }
}
