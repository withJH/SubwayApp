package com.example.choijh.subwayapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class BusAdapter extends BaseAdapter{
    /* 아이템을 세트로 담기 위한 어레이 */
    private ArrayList<BusItem> bItems = new ArrayList<>();

    @Override
    public int getCount() {
        return bItems.size();
    }

    @Override
    public BusItem getItem(int position) {
        return bItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /* 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성 */
    public void addItem(String st, String x, String y, String dt) {

        BusItem bItem = new BusItem();

        /* BusItem 아이템을 setting한다. */
        bItem.setBusDistance(dt);
        bItem.setBusStation(st);
        bItem.setBusx(x);
        bItem.setBusy(y);

        /* mItems에 BusItem 추가한다. */
        bItems.add(bItem);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_subway_list, parent, false);
        }

        CheckBox favorite_star = (CheckBox) convertView.findViewById(R.id.unfilled_star);
        TextView st_code = (TextView) convertView.findViewById(R.id.station_code);
        TextView st_name = (TextView) convertView.findViewById(R.id.station_name);
        ImageView st_line = (ImageView) convertView.findViewById(R.id.station_line);
        //TextView st_frcode = (TextView) convertView.findViewById(R.id.station_fr_code);

        favorite_star.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });
  /*      SubwayItem items = filtered_items.get(position);

        st_code.setText(items.getSubway_code());
        st_name.setText(items.getSubway_name());

        changedLineImage(items, st_line);
        //st_frcode.setText(items.getFr_code());
*/
        return convertView;
    }
}
