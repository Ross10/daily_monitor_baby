package com.example.ross.monitorbaby;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ross on 5/26/2017.
 */
public class LogsAdapter extends ArrayAdapter<Logs> {

    private Activity context;
    private List<Logs> logsList;

    public LogsAdapter(Activity context, List<Logs> logsList) {
        super(context, R.layout.list_view_tab_logs,logsList);
        this.context = context;
        this.logsList = logsList;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listviewItem = inflater.inflate(R.layout.list_view_tab_logs,null,true);

        TextView tv = (TextView) listviewItem.findViewById(R.id.dandhlogs);

//        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());


        Logs log = logsList.get(position);


        tv.setText(log.getAmount());


        return listviewItem;
    }
}
