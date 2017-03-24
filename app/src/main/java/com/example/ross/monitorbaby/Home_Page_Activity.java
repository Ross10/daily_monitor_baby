package com.example.ross.monitorbaby;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

public class Home_Page_Activity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton doList_Btn ,calender_Btn,gallery_Btn,gps_Btn,tracking_Btn,chat_Btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__page_);
        doList_Btn = (ImageButton)findViewById(R.id.doList_Btn);
        calender_Btn = (ImageButton)findViewById(R.id.calanter_Btn);
        gallery_Btn = (ImageButton)findViewById(R.id.gallary_Btn);
        gps_Btn = (ImageButton)findViewById(R.id.gps_Btn);
        tracking_Btn = (ImageButton)findViewById(R.id.tracking_Btn);
        chat_Btn = (ImageButton)findViewById(R.id.chat_Btn);

        doList_Btn.setOnClickListener(this);
        calender_Btn.setOnClickListener(this);
        gallery_Btn.setOnClickListener(this);
        gps_Btn.setOnClickListener(this);
        tracking_Btn.setOnClickListener(this);
        chat_Btn.setOnClickListener(this);

    }




    @Override
    public void onClick(View v) {

        Intent nevigateTo = null;
        switch (v.getId()){
            case R.id.gps_Btn:
                nevigateTo = new Intent(this,GpsHandler.class);
                break;
            case R.id.doList_Btn:
                nevigateTo = new Intent(this,To_Do_list_Activity.class);
                break;

            case R.id.gallary_Btn:
                nevigateTo = new Intent(this,Album_Activity.class);
                break;

            case R.id.calanter_Btn:
                nevigateTo = new Intent(this,AndroidCalendarView.class);
                break;

            case R.id.tracking_Btn:
                nevigateTo = new Intent(this,TrackingActivity.class);
                break;

//            case R.id.chat_Btn:
//                nevigateTo = new Intent(this,To_Do_list_Activity.class);
//                break;
        }

        if(nevigateTo!=null){
            startActivity(nevigateTo);

        }

//
    }
}
