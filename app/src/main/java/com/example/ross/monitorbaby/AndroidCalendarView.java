package com.example.ross.monitorbaby;
import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AndroidCalendarView extends AppCompatActivity
{
    //private CalendarDbHelper db;
    //private List<CalendarView> list;
    private CalendarCursorAdapter cca;
    private SQLiteDatabase db1;
    Cursor c;
    CalendarDbHelper calDbHelper;
    private ListView lVtasksList;

    private int focusedYear,currentDay,currentYear,currentMonth;
    private int focusedMonth;
    private int focusedDay;
    private ImageView add;
    private ImageView edit;
    private ImageView delete;
    private String desc,sa;

    CalendarView calendar;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override






    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //sets the main layout of the activity
        setContentView(R.layout.activity_android_calendar_view);

        calDbHelper = new CalendarDbHelper(this);
        lVtasksList = (ListView) findViewById(R.id.listViewId);
        add = (ImageView)findViewById(R.id.addEventID);
        edit = (ImageView)findViewById(R.id.EditEventId);
        delete = (ImageView)findViewById(R.id.deleteEventID);
        desc="";
        //initializes the calendarview
        initializeCalendar();



        int[] dates = CalendarEvent.getDate(); // static function in Calendar event
        focusedDay=dates[0];
        focusedMonth =dates[1];
        focusedYear =dates[2];

        //on list view item click listener
        lVtasksList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){


            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                Cursor cursor = (Cursor) lVtasksList.getItemAtPosition(position);
                desc =  cursor.getString(cursor.getColumnIndexOrThrow(CalendarEvent.DESCRIPTION));// get the evet description

                if(desc.equals(""))
                {

                    return false;
                }

                add.setVisibility(view.INVISIBLE);
                edit.setVisibility(view.VISIBLE);
                delete.setVisibility(view.VISIBLE);
                return true;

            }
        });


        //update the list view
        String whereClause = CalendarEvent.YEAR+" = "+focusedYear +" AND "+ CalendarEvent.MONTH +" = "+focusedMonth + " AND "+ CalendarEvent.DAY +" = "+ focusedDay;
        String[] tableColumns = new String[] {CalendarEvent.DESCRIPTION};

        //get data and refresh the list view
        SQLiteDatabase db1 = calDbHelper.getReadableDatabase();
        c = db1.query(CalendarEvent.TABLE_NAME, null, whereClause, null, null, null, null);
        cca = new CalendarCursorAdapter(getApplicationContext(),c);
        lVtasksList.setAdapter(cca);





    }

    private void initializeCalendar()
    {
        calendar = (CalendarView) findViewById(R.id.calendar);

        // sets whether to show the week number.
        calendar.setShowWeekNumber(true);
        // sets the first day of week according to CalendarEvent.
        calendar.setFirstDayOfWeek(1);


//        //The background color for the selected week.
//        calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.green));
//
//        //sets the color for the dates of an unfocused month.
//        calendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.transparent));
//
//        //sets the color for the separator line between weeks.
//        calendar.setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));
//
//
//        //sets the color for the vertical bar shown at the beginning and at the end of the selected date.
//        calendar.setSelectedDateVerticalBar(R.color.darkgreen);

        //sets the listener to be notified upon selected date change.
        calendar.setOnDateChangeListener(new OnDateChangeListener() {
            //show the selected date as a toast
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day)
            {
                add.setVisibility(view.VISIBLE);
                edit.setVisibility(view.INVISIBLE);
                delete.setVisibility(view.INVISIBLE);
                focusedDay = day;
                focusedMonth = month+1;
                focusedYear = year;

                String whereClause = CalendarEvent.YEAR+" = "+focusedYear +" AND "+ CalendarEvent.MONTH +" = "+focusedMonth + " AND "+ CalendarEvent.DAY +" = "+ focusedDay;
                String[] tableColumns = new String[] {CalendarEvent.DESCRIPTION};

                SQLiteDatabase db1 = calDbHelper.getReadableDatabase();
                db1 = calDbHelper.getReadableDatabase(); // get readable access from DB
                Cursor c1 = db1.query(CalendarEvent.TABLE_NAME,null,whereClause,null,null,null,null); // query from DB
                cca.changeCursor(c1);

                if(c1.getCount()==0) // if there is no events for this date
                    Toast.makeText(getApplicationContext(), R.string.noTaskForToday, Toast.LENGTH_SHORT).show();

            }
        });
    }



    public void addEvent(View view)
    {
        String whereClause = CalendarEvent.YEAR+" = "+focusedYear +" AND "+ CalendarEvent.MONTH +" = "+focusedMonth + " AND "+ CalendarEvent.DAY +" = "+ focusedDay;
        addEditDelete(getString(R.string.EnterEvent),whereClause,"add","");

    }

    public void deleteEvent(View view)
    {
        String whereClause = CalendarEvent.YEAR+" = "+focusedYear +" AND "+ CalendarEvent.MONTH +" = "+focusedMonth + " AND "+ CalendarEvent.DAY +" = "+ focusedDay+" AND "+CalendarEvent.DESCRIPTION+" = "+"'"+ desc+"'";
        addEditDelete("",whereClause,getString(R.string.Delete),desc);

        SQLiteDatabase db1 = calDbHelper.getReadableDatabase();
        db1 = calDbHelper.getReadableDatabase(); // get readable access from DB
        Cursor c1 = db1.query(CalendarEvent.TABLE_NAME, null, whereClause, null, null, null, null); // query from DB
        cca.changeCursor(c1);


    }


    public void editEvent(View view)
    {
        String whereClause = CalendarEvent.YEAR+" = "+focusedYear +" AND "+ CalendarEvent.MONTH +" = "+focusedMonth + " AND "+ CalendarEvent.DAY +" = "+ focusedDay+" AND "+CalendarEvent.DESCRIPTION+" = "+"'"+ desc+"'";
        addEditDelete(getString(R.string.EditEvent),whereClause,getString(R.string.Edit),desc);
    }


    //function that handle with add edit and delete actions
    public void addEditDelete(String title, final String whereClause, final String action, String desc)
    {
        if(action=="fromSet"){
            final EditText input = new EditText(this);
            input.setText(sa);

            CalendarDbHelper dbHelper = new CalendarDbHelper(getApplicationContext()); // Create pointer to DB
            SQLiteDatabase db = dbHelper.getWritableDatabase(); // Open writable access.

            ContentValues values = new ContentValues();
            //push data into DB row
            values.put(CalendarEvent.YEAR, focusedYear);
            values.put(CalendarEvent.MONTH, focusedMonth);
            values.put(CalendarEvent.DAY, focusedDay);
            values.put(CalendarEvent.STATUS, 1);
            values.put(CalendarEvent.DESCRIPTION, input.getText().toString());


            db.insert(CalendarEvent.TABLE_NAME, null, values);




        }else{

            if(action != getString(R.string.Delete2))
            {
                //dialog for getting the event description
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(title);

                // Set up the input
                final EditText input = new EditText(this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the button
                builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        CalendarDbHelper dbHelper = new CalendarDbHelper(getApplicationContext()); // Create pointer to DB
                        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Open writable access.



                        ContentValues values = new ContentValues();
                        //push data into DB row
                        values.put(CalendarEvent.YEAR, focusedYear);
                        values.put(CalendarEvent.MONTH, focusedMonth);
                        values.put(CalendarEvent.DAY, focusedDay);
                        values.put(CalendarEvent.STATUS, 1);
                        values.put(CalendarEvent.DESCRIPTION, input.getText().toString());
                        //TODO: add status and event description


                        if (action == "add" )
                        {
                            // Inserting Row to DB
                            db.insert(CalendarEvent.TABLE_NAME, null, values);
                        }


                        if (action == "edit")
                        {
                            db.update(CalendarEvent.TABLE_NAME,values,whereClause,null);
                        }

                        SQLiteDatabase db1 = calDbHelper.getReadableDatabase();
                        db1 = calDbHelper.getReadableDatabase(); // get readable access from DB
                        Cursor c1 = db1.query(CalendarEvent.TABLE_NAME, null, whereClause, null, null, null, null); // query from DB
                        cca.changeCursor(c1);

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }

            else // delete
            {
                CalendarDbHelper dbHelper = new CalendarDbHelper(getApplicationContext()); // Create pointer to DB
                SQLiteDatabase db = dbHelper.getWritableDatabase(); // Open writable access.

                db.delete(CalendarEvent.TABLE_NAME, whereClause,null);

                SQLiteDatabase db1 = calDbHelper.getReadableDatabase();
                db1 = calDbHelper.getReadableDatabase(); // get readable access from DB
                Cursor c1 = db1.query(CalendarEvent.TABLE_NAME, null, whereClause, null, null, null, null); // query from DB
                cca.changeCursor(c1);

            }

        }


    }


    public void openNewCases(View v){
        Intent i = new Intent(this, setted_tasks_Activity.class);
        startActivityForResult(i,1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                currentDay = data.getIntExtra("currentDay",0);
                currentMonth = data.getIntExtra("currentMonth",0);
                currentYear = data.getIntExtra("currentYear",0);
                sa = data.getStringExtra("name");
                String whereClause = CalendarEvent.YEAR+" = "+currentYear +" AND "+ CalendarEvent.MONTH +" = "+currentMonth + " AND "+ CalendarEvent.DAY +" = "+ currentDay;


                addEditDelete(null,whereClause,"fromSet",null);

            }
        }
    }




}
