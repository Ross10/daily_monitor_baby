package com.example.ross.monitorbaby;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

public class Home_Page_Activity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton todo,calender,gallery,gps,lockUser,tracking;
    private GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__page_);

        gridview = (GridView)findViewById(R.id.gridview);

        // a potentially  time consuming task
        gridview.setAdapter(new ImageAdapter(Home_Page_Activity.this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent homeActivity = null;

                switch(position){ // navigation

                    case 0:
                        homeActivity = new Intent(Home_Page_Activity.this,AndroidCalendarView.class);
                        break;

                    case 1:
                        homeActivity = new Intent(Home_Page_Activity.this,GpsHandler.class);
                        break;

                    case 2:
                        homeActivity = new Intent(Home_Page_Activity.this,To_Do_list_Activity.class);
                        break;

                    case 3:
                        homeActivity = new Intent(Home_Page_Activity.this,Album_Activity.class);
                        break;

                    case 4:
                        homeActivity = new Intent(Home_Page_Activity.this,TrackingActivity.class);
                        break;

                    case 5:
                        homeActivity = new Intent(Home_Page_Activity.this,Login_Activity.class);
                        break;
                }

                if(homeActivity!=null)
                    startActivity(homeActivity);

            }


        });


    }


    @Override
    public void onClick(View v) {

//
    }
}
