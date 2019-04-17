package com.example.choijh.subwayapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class subway_favorites extends AppCompatActivity {
    ViewPager vpa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway_favorites);
        setTitle("즐겨찾기");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼

        vpa = (ViewPager)findViewById(R.id.vp);
        Button btn_first = (Button)findViewById(R.id.btn_first);
        Button btn_second = (Button)findViewById(R.id.btn_second);

        vpa.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        vpa.setCurrentItem(0);

        btn_first.setOnClickListener(movePageListener);
        btn_first.setTag(0);
        btn_second.setOnClickListener(movePageListener);
        btn_second.setTag(1);
    }

    View.OnClickListener movePageListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            int tag = (int) v.getTag();
            vpa.setCurrentItem(tag);
        }
    };
    private class pagerAdapter extends FragmentStatePagerAdapter
    {
        public pagerAdapter(androidx.fragment.app.FragmentManager fm)
        {
            super(fm);
        }
        @Override
        public androidx.fragment.app.Fragment getItem(int position)
        {
            switch(position)
            {
                case 0:
                    return new TabFragment1();
                case 1:
                    return new TabFragment2();
                default:
                    return null;
            }
        }
        @Override
        public int getCount()
        {
            return 2;
        }
    }
}
