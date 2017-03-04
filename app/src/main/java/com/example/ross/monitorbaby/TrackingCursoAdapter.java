package com.example.ross.monitorbaby;

/**
 * Created by Ross on 3/4/2017.
 */


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class TrackingCursoAdapter extends CursorAdapter
{
    LayoutInflater inflater;

    public TrackingCursoAdapter(Context context, Cursor c) {
        super(context, c,0);
        inflater = LayoutInflater.from(context);
    }

    @Override
    // for each element at start do the follows

    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        return inflater.inflate(R.layout.tracking_logs_custom,parent,false);
    }

    @Override
    // for each element do the follows

    public void bindView(View view, Context context, Cursor cursor) {
        TextView log = (TextView)view.findViewById(R.id.logsTextView);

        log.setText(cursor.getString(cursor.getColumnIndex(LogsTracking.Description)));

    }
}
