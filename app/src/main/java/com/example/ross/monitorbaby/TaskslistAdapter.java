package com.example.ross.monitorbaby;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Ross on 5/9/2017.
 */

public class TaskslistAdapter extends ArrayAdapter<Task>{

    private Activity context;
    private List<Task> taskList;

    public TaskslistAdapter(Activity context, List<Task> taskList) {
        super(context, R.layout.newlookfortasklist,taskList);
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listviewItem = inflater.inflate(R.layout.newlookfortasklist,null,true);

        TextView tv = (TextView) listviewItem.findViewById(R.id.taskNameId);
        ImageView iv = (ImageView)listviewItem.findViewById(R.id.priView);
        TextView time2  = (TextView)listviewItem.findViewById(R.id.timeThatClicked);

//        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());


        Task task = taskList.get(position);

        switch (task.getPriorty()){
                case 0:
                    iv.setImageResource(R.drawable.high_pri);
                    break;
                case 1:
                    iv.setImageResource(R.drawable.medium_pri);
                    break;
                case 2:
                    iv.setImageResource(R.drawable.low_pri);
                    break;
            }


        time2.setText(task.getDate());


        tv.setText(task.getTaskName());


        return listviewItem;
    }
}
