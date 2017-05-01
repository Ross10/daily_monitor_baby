package com.example.ross.monitorbaby;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Calendar;
import java.util.Date;

public class setted_tasks_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setted_tasks_);

        int[] dates = CalendarEvent.getDate(); // static function in Calendar event
        int currentDay=dates[0];
       int currentMonth =dates[1];
       int currentYear =dates[2];

    String sa = "hi bro";
        Intent intent = new Intent();
        intent.putExtra("currentDay", currentDay);
        intent.putExtra("currentMonth", currentMonth);
        intent.putExtra("currentYear", currentYear);
        intent.putExtra("name", sa);
        setResult(RESULT_OK, intent);
        finish();
    }
}
