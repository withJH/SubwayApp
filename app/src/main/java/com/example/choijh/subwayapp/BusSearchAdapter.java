package com.example.choijh.subwayapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class BusSearchAdapter extends BaseAdapter implements Filterable {
    private ArrayList<BusItem> set_items = new ArrayList<>(); // API로 불러온 기본 array
    private ArrayList<BusItem> filtered_items = set_items; // 검색해서 필터링을 진행할 array(초기에는 기본 array 복사)
    BusItem items;
    Filter filter;
    DBOpenHelper help;

    @Override
    public int getCount() {
        return filtered_items.size();
    }

    @Override
    public BusItem getItem(int position) {
        return filtered_items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_bus_list, parent, false);
        }

        final CheckBox favorite_star = (CheckBox) convertView.findViewById(R.id.bus_unfilled_star);
        TextView route_num = (TextView) convertView.findViewById(R.id.route_num);
        TextView route_type = (TextView) convertView.findViewById(R.id.route_type);
        TextView start_nodenm = (TextView) convertView.findViewById(R.id.start_nodenm);
        TextView end_nodenm = (TextView) convertView.findViewById(R.id.end_nodenm);
        final TextView route_id = (TextView) convertView.findViewById(R.id.route_id);

        help = new DBOpenHelper(context);
        items = filtered_items.get(position);

        favorite_star.setText(items.getFavorite());
        route_num.setText(items.getRouteno());
        route_type.setText(items.getRoutetp());
        start_nodenm.setText(items.getStartnodenm());
        end_nodenm.setText(items.getEndnodenm());
        route_id.setText(items.getRouteid());
        route_id.setVisibility(View.INVISIBLE);

        // Toast.makeText(context, Integer.toString(position), Toast.LENGTH_SHORT).show();

        favorite_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                help.open();
                if(favorite_star.isChecked()){
                    help.BusDB_update(route_id.getText().toString(),1);
                    //Toast.makeText(context, st_name.getText().toString(), Toast.LENGTH_SHORT).show();
                }else{
                    help.BusDB_update(route_id.getText().toString(),0);
                }
                help.close();
            }
        });

        favorite_star.setChecked(favorite_star.getText().toString().equals("1"));

        return convertView;
    }

    public void addItem(String route_id, String route_num, String route_type, String start_node, String end_node, String favorite) {

        BusItem item = new BusItem();

        item.setRouteid(route_id);
        item.setRouteno(route_num);
        item.setRoutetp(route_type);
        item.setStartnodenm(start_node);
        item.setEndnodenm(end_node);
        item.setFavorite(favorite);

        set_items.add(item);
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new BusListFilter() ;
        }

        return filter ;
    }

    private class BusListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                results.values = set_items;
                results.count = set_items.size();
            } else {
                ArrayList<BusItem> list = new ArrayList<>();

                for (BusItem item : set_items) {
                    if (item.getRouteno().contains(constraint.toString())) // 지하철 역 이름으로 검색
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

            filtered_items = (ArrayList<BusItem>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
