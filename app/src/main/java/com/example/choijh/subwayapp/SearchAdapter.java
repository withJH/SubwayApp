package com.example.choijh.subwayapp;

import android.content.Context;
import android.database.Cursor;
import android.os.Debug;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchAdapter extends BaseAdapter implements Filterable {
    private ArrayList<SubwayItem> set_items = new ArrayList<>(); // API로 불러온 기본 array
    private ArrayList<SubwayItem> filtered_items = set_items; // 검색해서 필터링을 진행할 array(초기에는 기본 array 복사)
    SubwayItem items;
    Filter filter;
    DBOpenHelper help ;

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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_subway_list, parent, false);
        }

        final CheckBox favorite_star = (CheckBox) convertView.findViewById(R.id.subway_unfilled_star);
        final TextView st_code = (TextView) convertView.findViewById(R.id.station_code);
        final TextView st_name = (TextView) convertView.findViewById(R.id.station_name);
        final ImageView st_line = (ImageView) convertView.findViewById(R.id.station_line);
        //TextView st_frcode = (TextView) convertView.findViewById(R.id.station_fr_code);

        help = new DBOpenHelper(context);
        items = filtered_items.get(position);
       // Toast.makeText(context, Integer.toString(position), Toast.LENGTH_SHORT).show();

        favorite_star.setText(items.getFavorite());
        st_code.setText(items.getSubway_code());
        st_name.setText(items.getSubway_name());
        changedLineImage(items, st_line);
        //st_frcode.setText(items.getFr_code());


        favorite_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                help.open();
                if(favorite_star.isChecked()){
                    help.StationDB_update(st_code.getText().toString(),1);
                    //Toast.makeText(context, st_name.getText().toString(), Toast.LENGTH_SHORT).show();
                }else{
                    help.StationDB_update(st_code.getText().toString(),0);
                }
                help.close();
            }
        });

        favorite_star.setChecked(favorite_star.getText().toString().equals("1"));

        return convertView;
    }

    public void addItem(String subway_code, String subway_name, String line, String fr_code, String favorite) {

        SubwayItem item = new SubwayItem();

        item.setSubway_code(subway_code);
        item.setSubway_name(subway_name);
        item.setLine(line);
        item.setFr_code(fr_code);
        item.setFavorite(favorite);

        set_items.add(item);
    }

    void changedLineImage(SubwayItem items, ImageView st_line) {
        if (items.getLine().equals("01호선")) {
            st_line.setImageResource(R.drawable.line_number1);
        } else if (items.getLine().equals("02호선")) {
            st_line.setImageResource(R.drawable.line_number2);
        } else if (items.getLine().equals("03호선")) {
            st_line.setImageResource(R.drawable.line_number3);
        } else if (items.getLine().equals("04호선")) {
            st_line.setImageResource(R.drawable.line_number4);
        } else if (items.getLine().equals("05호선")) {
            st_line.setImageResource(R.drawable.line_number5);
        } else if (items.getLine().equals("06호선")) {
            st_line.setImageResource(R.drawable.line_number6);
        } else if (items.getLine().equals("07호선")) {
            st_line.setImageResource(R.drawable.line_number7);
        } else if (items.getLine().equals("08호선")) {
            st_line.setImageResource(R.drawable.line_number8);
        } else if (items.getLine().equals("09호선")) {
            st_line.setImageResource(R.drawable.line_number9);
        } else if (items.getLine().equals("경의선")) {
            st_line.setImageResource(R.drawable.line_gj);
        } else if (items.getLine().equals("경춘선")) {
            st_line.setImageResource(R.drawable.line_gyeongchun);
        } else if (items.getLine().equals("경강선")) {
            st_line.setImageResource(R.drawable.line_gyeonggang);
        } else if (items.getLine().equals("분당선")) {
            st_line.setImageResource(R.drawable.line_bundang);
        } else if (items.getLine().equals("신분당선")) {
            st_line.setImageResource(R.drawable.line_dx);
        } else if (items.getLine().equals("수인선")) {
            st_line.setImageResource(R.drawable.line_suin);
        } else if (items.getLine().equals("인천선")) {
            st_line.setImageResource(R.drawable.line_incheon1);
        } else if (items.getLine().equals("인천2호선")) {
            st_line.setImageResource(R.drawable.line_incheon2);
        } else if (items.getLine().equals("용인경전철")) {
            st_line.setImageResource(R.drawable.line_ever);
        } else if (items.getLine().equals("의정부경전철")) {
            st_line.setImageResource(R.drawable.line_u);
        } else if (items.getLine().equals("우이신설경전철")) {
            st_line.setImageResource(R.drawable.line_ui);
        } else if (items.getLine().equals("공항철도")) {
            st_line.setImageResource(R.drawable.line_arex);
        } else if (items.getLine().equals("서해선")) {
            st_line.setImageResource(R.drawable.line_seohae);
        }
    }

    @Override
    public Filter getFilter() {

        if (filter == null) {
            filter = new SubwayListFilter() ;
        }

        return filter ;
    }

    private class SubwayListFilter extends Filter {

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

            filtered_items = (ArrayList<SubwayItem>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged() ;
            } else {
                notifyDataSetInvalidated() ;
            }
        }
    }
}