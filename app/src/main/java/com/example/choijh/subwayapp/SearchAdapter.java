package com.example.choijh.subwayapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchAdapter extends BaseAdapter {

    /* 아이템을 세트로 담기 위한 어레이 */
    private ArrayList<SubwayItem> set_items = new ArrayList<>();

    @Override
    public int getCount() {
        return set_items.size();
    }

    @Override
    public SubwayItem getItem(int position) {
        return set_items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();

        /* 'listview_custom' Layout을 inflate하여 convertView 참조 획득 */
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_subway_list, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        TextView st_code = (TextView) convertView.findViewById(R.id.station_code);
        TextView st_name = (TextView) convertView.findViewById(R.id.station_name);
        TextView st_line = (TextView) convertView.findViewById(R.id.station_line);
        TextView st_frcode = (TextView) convertView.findViewById(R.id.station_fr_code);

        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        SubwayItem items = getItem(position);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */
        st_code.setText(items.getSubway_code());
        st_name.setText(items.getSubway_name());
        st_line.setText(items.getLine());
        st_frcode.setText(items.getFr_code());

        /* (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)  */


        return convertView;
    }

    /* 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성 */
    public void addItem(String subway_code, String subway_name, String line, String fr_code) {

        SubwayItem item = new SubwayItem();

        /* MyItem에 아이템을 setting한다. */
        item.setSubway_code(subway_code);
        item.setSubway_name(subway_name);
        item.setLine(line);
        item.setFr_code(fr_code);

        /* mItems에 MyItem을 추가한다. */
        set_items.add(item);

    }
}