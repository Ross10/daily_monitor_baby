package com.example.ross.monitorbaby;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ClanaderviewActiviry extends AppCompatActivity {

    private static final String TAG = "CalanderAcitivty";
    private CalendarView mCalendarView;
    private ImageButton star_events,addEventID,eye;
    private int focusedYear,currentDay,currentYear,currentMonth;
    private int focusedMonth;
    private int focusedDay;
    private String date;
    private FirebaseAuth mAuth; //Returns an instance of this class corresponding to the default FirebaseApp instance when using getiInstance().
    private DatabaseReference mDatabaseReference;
    FirebaseUser userr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clanaderview_activiry);
        mCalendarView = (CalendarView)findViewById(R.id.calendarView);
        star_events = (ImageButton)findViewById(R.id.star_events);
        addEventID = (ImageButton)findViewById(R.id.addEventID);
        eye = (ImageButton)findViewById(R.id.eye);
        userr = FirebaseAuth.getInstance().getCurrentUser();



        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){


            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                date = dayOfMonth + "/" + (month+1) + "/" + year;
                focusedDay = dayOfMonth;
                focusedMonth = month+1;
                focusedYear = year;
                Log.d(TAG,"onSelectedDate :  mm/dd/yyyy" + date);
                Toast.makeText(ClanaderviewActiviry.this, "onSelectedDate :  mm/dd/yyyy" + date, Toast.LENGTH_SHORT).show();





            }
        });






    }


    public void addEvent(View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("הוסף אירוע");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the button
        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userr.getUid()).child("Calander");


                int[] dates = CalendarEvent.getDate(); // function in Calendar event
                int day=dates[0];
                int month =dates[1];
                int year =dates[2];
                int status =0;


                if(day>=focusedDay && month>=focusedMonth && year>=focusedYear){
                    status = 1;
                }

                else{
                    status = 2;
                }
                CalanderTask t1 = new CalanderTask(date,input.getText().toString(),status);
                mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userr.getUid()).child("Calander").child(t1.getId());

                mDatabaseReference.setValue(t1);






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



    public void onEyeClicked(View v){
        Intent intent = new Intent(ClanaderviewActiviry.this,CalanderViewHelper.class);
        intent.putExtra("date",date);
        startActivity(intent);
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
                String whereClause = CalendarEvent.YEAR+" = "+currentYear +" AND "+ CalendarEvent.MONTH +" = "+currentMonth + " AND "+ CalendarEvent.DAY +" = "+ currentDay;

            }
        }
    }
}
