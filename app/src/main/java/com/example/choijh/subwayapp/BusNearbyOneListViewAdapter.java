package com.example.choijh.subwayapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import com.example.choijh.subwayapp.ListVO1;

public class BusNearbyOneListViewAdapter extends BaseAdapter {
    private ArrayList<ListVO1> listVO = new ArrayList<ListVO1>() ;
    public BusNearbyOneListViewAdapter() {
            }

    @Override
    public int getCount() {
                return listVO.size();
            }

            // ** 이 부분에서 리스트뷰에 데이터를 넣어줌 **
            @Override
    public View getView(int position, View convertView, ViewGroup parent) {
                //postion = ListView의 위치      /   첫번째면 position = 0
                final int pos = position;
                final Context context = parent.getContext();


                if (convertView == null) {
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        convertView = inflater.inflate(R.layout.listview_bus_nearby_one, parent, false);
                    }

                TextView station = (TextView) convertView.findViewById(R.id.near_station_name) ;
                TextView distance = (TextView) convertView.findViewById(R.id.near_distance) ;
                TextView bus = (TextView) convertView.findViewById(R.id.near_bus) ;


                ListVO1 listViewItem = listVO.get(position);

                // 아이템 내 각 위젯에 데이터 반영
                station.setText(listViewItem.getSN());
                distance.setText(listViewItem.getD());
                bus.setText(listViewItem.getB());

                //리스트뷰 클릭 이벤트
                convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                                Toast.makeText(context, (pos+1)+"번째 리스트가 클릭되었습니다.", Toast.LENGTH_SHORT).show();
                            }
            });

                return convertView;
            }


            @Override
    public long getItemId(int position) {
                return position ;
            }


            @Override
    public Object getItem(int position) {
                return listVO.get(position) ;
            }

    // 데이터값 넣어줌
            public void addVO(String station, String distance, String bus) {
                ListVO1 item = new ListVO1();

                item.setSN(station);
                item.setD(distance);
                item.setB(bus);

                listVO.add(item);
            }
}
