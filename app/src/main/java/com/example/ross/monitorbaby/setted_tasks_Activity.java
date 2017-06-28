package com.example.ross.monitorbaby;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Calendar;
import java.util.Date;

public class setted_tasks_Activity extends AppCompatActivity implements View.OnClickListener{

    private Button fteeth,fcrawl,fstand,fwalk,ftalk,fliketalk,flaugh,fsmile,fgames,plusBtn,minusBtn;
    private String dataToSet;
    private int currentYear,currentMonth,currentDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setted_tasks_);

        fteeth = (Button)findViewById(R.id.fteeth);
        fstand = (Button)findViewById(R.id.standing);
        fgames = (Button)findViewById(R.id.games);
        fcrawl = (Button)findViewById(R.id.crawl);
        fsmile = (Button)findViewById(R.id.firstSmile);
        fliketalk = (Button)findViewById(R.id.firstWord);
//        flaugh = (Button)findViewById(R.id.firstSmile);
//        plusBtn = (Button)findViewById(R.id.button3);
//        minusBtn = (Button)findViewById(R.id.button4);


        int[] dates = CalendarEvent.getDate(); // static function in Calendar event
        currentDay=dates[0];
       currentMonth =dates[1];
       currentYear =dates[2];

        fteeth.setOnClickListener(this);
        fcrawl.setOnClickListener(this);
        fstand.setOnClickListener(this);
       // fwalk.setOnClickListener(this);
       // ftalk.setOnClickListener(this);
        fliketalk.setOnClickListener(this);
        fsmile.setOnClickListener(this);
//        fsmile.setOnClickListener(this);
        fgames.setOnClickListener(this);
//        plusBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra("currentDay", currentDay);
        intent.putExtra("currentMonth", currentMonth);
        intent.putExtra("currentYear", currentYear);


        switch(v.getId()){
            case R.id.fteeth:
                dataToSet = "שן ראשונה";
                break;
            case R.id.standing:
                dataToSet = "עמידה ראשונה";
                break;
            case R.id.games:
                dataToSet = "משחק ראשון";
                break;

            case R.id.crawl:
                dataToSet = "זחילה!";
                break;

            case R.id.firstSmile:
                dataToSet = "החיוך הראשון";
                break;

            case R.id.firstWord:
                dataToSet = "המילה הראשונה";
                break;

        }
        intent.putExtra("name", dataToSet);
        setResult(RESULT_OK, intent);
        finish();
    }
}
