package com.example.ross.monitorbaby;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ross on 6/16/2017.
 */

public class CalanderTasksListAdatper extends ArrayAdapter<CalanderTask> {

    private Activity context;
    private List<CalanderTask> taskList;

    public CalanderTasksListAdatper(Activity context, List<CalanderTask> taskList) {
        super(context, R.layout.inner_calanderview,taskList);
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listviewItem = inflater.inflate(R.layout.inner_calanderview,null,true);
        TextView tv = (TextView) listviewItem.findViewById(R.id.calanderData);

//        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());


        CalanderTask task = taskList.get(position);

        switch (task.getStatus()){

            case 1:
                tv.setTextColor(Color.RED);
                break;
            case 2:
                tv.setTextColor(Color.GREEN);
                break;
        }




        tv.setText(task.getTaskName());


        return listviewItem;
    }
}
