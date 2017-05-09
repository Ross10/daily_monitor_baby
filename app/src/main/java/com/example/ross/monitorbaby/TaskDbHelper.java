package com.example.ross.monitorbaby;

/**
 * Created by Ross on 3/4/2017.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class TaskDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION =1;
    private static final String DATABASE_NAME = "taskManager";

    // Database Name
    private static final String TABLE_TASKS = "tasks";
//    private To_Do_list_Activity.MyAdapter adapt;
    // tasks Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TASKNAME = "taskName";


    public TaskDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_TASKS + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_TASKNAME+ " TEXT )";
        db.execSQL(sql);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed on Upgrade
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);

        // Create the tables again
        onCreate(db);

    }


    public void addTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values  = new ContentValues();
        // task name
        values.put(KEY_TASKNAME,task.getTaskName());
        // Inserting Row
        db.insert(TABLE_TASKS,null,values);
        // Closing database connection
        db.close();
    }


    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<Task>();
// Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
               // task.setId(cursor.getInt(0));
                task.setTaskName(cursor.getString(1));
// Adding contact to list
                taskList.add(task);
            } while (cursor.moveToNext());
        }
// return task list
        return taskList;
    }



    public void updateTask(Task task, int t) {
// updating row

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TASKNAME, task.getTaskName());
        db.update(TABLE_TASKS, values, KEY_ID + " = " +t ,null);




    }


    public boolean deleteTask(Task task){

        Log.d("DEBUG",task.getTaskName());

        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.delete(TABLE_TASKS,"taskName = ?",new String[]{task.getTaskName()});
            return true;

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;


    }
}
