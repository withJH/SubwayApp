package com.example.choijh.subwayapp;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;

public class common_favorites extends AppCompatActivity {

    /**
     * The {@link androidx.viewpager.widget.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * androidx.fragment.app.FragmentStatePagerAdapter.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_favorites);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        ListView subway_favorite;
        SearchAdapter searchAdapter = new SearchAdapter(); // 어댑터
        DBOpenHelper help;

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
            View rootView = inflater.inflate(R.layout.fragment_common_favorites_subway, container, false);
            subway_favorite = (ListView) rootView.findViewById(R.id.subway_label);

            help = new DBOpenHelper(getContext());
            help.open();
            getFavoriteSubwyaDB();
            return rootView;
        }

        private void getFavoriteSubwyaDB() {

            Cursor search_cursor = help.Station_SelectFavorite();
            while(search_cursor.moveToNext()){
                String code = search_cursor.getString(search_cursor.getColumnIndex("station_code"));
                String name = search_cursor.getString(search_cursor.getColumnIndex("station_name"));
                String line = search_cursor.getString(search_cursor.getColumnIndex("station_line"));
                String fr = search_cursor.getString(search_cursor.getColumnIndex("station_fr"));
                String favorite = search_cursor.getString(search_cursor.getColumnIndex("favorite_station"));
                searchAdapter.addItem(code, name, line, fr, favorite); // 어댑터에 데이터 삽입
            }
            subway_favorite.setAdapter(searchAdapter); // 리스트뷰에 데이터 올리기
            search_cursor.close();
        }


    }

    public static class PlaceholderFragment2 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        ListView bus_favorite;
        SearchAdapter searchAdapter = new SearchAdapter(); // 어댑터
        DBOpenHelper help;

        public PlaceholderFragment2() {
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
            View rootView = inflater.inflate(R.layout.fragment_common_favorites_bus, container, false);
            bus_favorite = (ListView)rootView.findViewById(R.id.bus_label);

            help = new DBOpenHelper(getContext());
            help.open();
            //getFavoriteSubwyaDB();
            getFavoriteBusDB();
            return rootView;
        }

       private void getFavoriteBusDB(){
            DBOpenHelper help = new DBOpenHelper(getContext());
            help.open();

            Cursor search_cursor = help.Bus_SelectFavorite();
            while(search_cursor.moveToNext()){
                String code = search_cursor.getString(search_cursor.getColumnIndex("station_code"));
                String name = search_cursor.getString(search_cursor.getColumnIndex("station_name"));
                String line = search_cursor.getString(search_cursor.getColumnIndex("station_line"));
                String fr = search_cursor.getString(search_cursor.getColumnIndex("station_fr"));
                String favorite = search_cursor.getString(search_cursor.getColumnIndex("favorite_station"));
                searchAdapter.addItem(code, name, line, fr, favorite); // 어댑터에 데이터 삽입
            }
            bus_favorite.setAdapter(searchAdapter); // 리스트뷰에 데이터 올리기
            search_cursor.close();
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
            switch (position) {
                case 0:
                    return PlaceholderFragment.newInstance(position + 1);
                case 1:
                    return PlaceholderFragment2.newInstance(position + 1);

            }
            return null;

            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }


}