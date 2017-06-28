package com.example.ross.monitorbaby;

/**
 * Created by Ross on 3/4/2017.
 */

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.ross.monitorbaby.R.id.calendar;


public class CalendarEvent implements BaseColumns
{
    //fields
    private int id;
    private int year;
    private int month;
    private int day;
    private int status;
    private String description;


    public static final String TABLE_NAME = "Events";
    // tasks Table Columns names
    //public static final String ID = "id";
    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String DAY = "day";
    public static final String DESCRIPTION = "description";
    public static final String STATUS = "status";

    //ctor
    public CalendarEvent(int id, int year, int month, int day, int status, String description)
    {
        this.id=id;
        this.year=year;
        this.month = month;
        this.day = day;
        this.status = status;
        this.description = description;
    }



    // getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }





    public static int[] getDate() // get current date
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String currDDate = sdf.format(new Date(String.valueOf(Calendar.getInstance().getTime())));
        String parts[] = currDDate.split("/");

        int datesArr[]=new int[3];
        datesArr[0] = Integer.parseInt(parts[0]);
        datesArr[1] = Integer.parseInt(parts[1]);
        datesArr[2] = Integer.parseInt(parts[2]);

        return datesArr;
    }

}
