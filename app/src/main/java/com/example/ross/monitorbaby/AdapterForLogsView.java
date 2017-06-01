package com.example.ross.monitorbaby;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ross on 5/26/2017.
 */

public class AdapterForLogsView extends BaseAdapter {
    Context c;
    ArrayList<String> date_time_log;
    LayoutInflater inflater;


    public AdapterForLogsView(Context c, ArrayList<String> date_time_log){
        this.c = c;
        this.date_time_log = date_time_log;
    }


    @Override
    public int getCount() {
        return date_time_log.size();
    }


    @Override
    public Object getItem(int position) {
        return date_time_log.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(inflater ==null){
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(convertView==null){
            convertView = inflater.inflate(R.layout.list_view_tab_logs,parent,false);
        }

        TextView nameTxt = (TextView)convertView.findViewById(R.id.dandhlogs);

        String name = date_time_log.get(position);
        nameTxt.setText(name);



        return convertView;
    }
}
