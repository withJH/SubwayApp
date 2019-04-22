package com.example.choijh.subwayapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchAdapter extends BaseAdapter implements Filterable {
    private ArrayList<SubwayItem> set_items = new ArrayList<>(); // API로 불러온 기본 array
    private ArrayList<SubwayItem> filtered_items = set_items; // 검색해서 필터링을 진행할 array(초기에는 기본 array 복사)
    Filter filter;

    @Override
    public int getCount() {
        return filtered_items.size();
    }

    @Override
    public SubwayItem getItem(int position) {
        return filtered_items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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
        TextView st_line = (TextView) convertView.findViewById(R.id.station_line);
        //TextView st_frcode = (TextView) convertView.findViewById(R.id.station_fr_code);

        favorite_star.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });
        SubwayItem items = filtered_items.get(position);

        st_code.setText(items.getSubway_code());
        st_name.setText(items.getSubway_name());
        st_line.setText(items.getLine());
        //st_frcode.setText(items.getFr_code());

        return convertView;
    }

    public void addItem(String subway_code, String subway_name, String line, String fr_code) {

        SubwayItem item = new SubwayItem();

        item.setSubway_code(subway_code);
        item.setSubway_name(subway_name);
        item.setLine(line);
        item.setFr_code(fr_code);

        set_items.add(item);
    }

    @Override
    public Filter getFilter() {

        if (filter == null) {
            filter = new ListFilter() ;
        }

        return filter ;
    }

    private class ListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults() ;

            if (constraint == null || constraint.length() == 0) {
                results.values = set_items;
                results.count = set_items.size();
            } else {
                ArrayList<SubwayItem> list = new ArrayList<>();

                for (SubwayItem item : set_items) {
                    if (item.getSubway_name().contains(constraint.toString())) // 지하철 역 이름으로 검색
                    {
                        list.add(item);
                    }
                }

                results.values = list;
                results.count = list.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            filtered_items = (ArrayList<SubwayItem>) results.values ;

            if (results.count > 0) {
                notifyDataSetChanged() ;
            } else {
                notifyDataSetInvalidated() ;
            }
        }
    }
}