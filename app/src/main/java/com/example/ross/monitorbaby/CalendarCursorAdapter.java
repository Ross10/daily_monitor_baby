package com.example.ross.monitorbaby;

/**
 * Created by Ross on 3/4/2017.
 */

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CursorAdapter;
import android.widget.TextView;


public class CalendarCursorAdapter extends CursorAdapter
{
    LayoutInflater inflater;
    int day;
    int month;
    int year;


    public CalendarCursorAdapter(Context context, Cursor c) {
        super(context, c,0);
        inflater = LayoutInflater.from(context);
        int[] dates = CalendarEvent.getDate(); // function in Calendar event
        day=dates[0];
        month =dates[1];
        year =dates[2];

    }

    public CalendarCursorAdapter(CalendarView.OnDateChangeListener onDateChangeListener, Cursor c) {
        super((Context) onDateChangeListener, c);
    }


    @Override
    // for each  element at the start do the follows

    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        return inflater.inflate(R.layout.calendar_events_custom,parent,false);
    }

    @Override
    // for each element do the follows
    public void bindView(View view, Context context, Cursor cursor)
    {
        TextView event = (TextView)view.findViewById(R.id.eventTextView); // get the event

        if( cursor.getInt(cursor.getColumnIndex(CalendarEvent.DAY)) >= day &&  cursor.getInt(cursor.getColumnIndex(CalendarEvent.MONTH)) >= month &&  cursor.getInt(cursor.getColumnIndex(CalendarEvent.YEAR)) >= year)
        {
            event.setTextColor(Color.GREEN);// green task

        }

        else
            event.setTextColor(Color.RED);// red task


        event.setText(cursor.getString(cursor.getColumnIndex(CalendarEvent.DESCRIPTION)));
    }

}
