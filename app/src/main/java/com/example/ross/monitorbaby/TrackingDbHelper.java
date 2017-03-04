package com.example.ross.monitorbaby;

/**
 * Created by Ross on 3/4/2017.
 */


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class TrackingDbHelper extends SQLiteOpenHelper
{

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "LogsTracking.db";

    //drop table
    private static final String SQL_DELETE_ENTRIES ="DROP TABLE IF EXISTS " + LogsTracking.TABLE_NAME;

    //create table
    private static final String SQL_CREATE_ENTRIES= "CREATE TABLE " + LogsTracking.TABLE_NAME + " (" +LogsTracking._ID +
            " INTEGER PRIMARY KEY," + LogsTracking.Description + ");";


    public TrackingDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }
}
