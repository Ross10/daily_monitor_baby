package com.example.ross.monitorbaby;

/**
 * Created by Ross on 3/4/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;


public class CalendarDbHelper extends SQLiteOpenHelper
{
    public static final int DATABASE_VERSION =1;
    public static final String DATABASE_NAME = "CalendarEvent";


    // create table
    private static String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + CalendarEvent.TABLE_NAME + " ( "
            + CalendarEvent._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CalendarEvent.YEAR + " INTEGER, "
            + CalendarEvent.MONTH + " INTEGER, "
            + CalendarEvent.DAY + " INTEGER, "
            + CalendarEvent.DESCRIPTION + " TEXT, "
            + CalendarEvent.STATUS + " INTEGER )";


    public CalendarDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_ENTRIES);
    } // run creation

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Create the tables again
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }





}
