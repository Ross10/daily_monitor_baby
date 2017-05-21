package com.example.ross.monitorbaby;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

public class Home_Page_Activity extends AppCompatActivity implements View.OnClickListener {

    private Button doList_Btn ,calender_Btn,gallery_Btn,gps_Btn,tracking_Btn,chat_Btn,signOut_Btn,properties_Btn;
    private FirebaseAuth mauth;
    private GoogleApiClient mGoogleApiClient;
    private  Animation animAlpha;
    private  LinearLayout screen;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__page_);
        doList_Btn = (Button)findViewById(R.id.doList_Btn);
        calender_Btn = (Button)findViewById(R.id.calanter_Btn);
        gallery_Btn = (Button)findViewById(R.id.gallary_Btn);
        gps_Btn = (Button)findViewById(R.id.gps_Btn);
        tracking_Btn = (Button)findViewById(R.id.tracking_Btn);
        chat_Btn = (Button)findViewById(R.id.chat_Btn);
        signOut_Btn = (Button)findViewById(R.id.signOut_Btn);
        properties_Btn = (Button)findViewById(R.id.properties_Btn);
        mauth = FirebaseAuth.getInstance();

        doList_Btn.setOnClickListener(this);
        calender_Btn.setOnClickListener(this);
        gallery_Btn.setOnClickListener(this);
        gps_Btn.setOnClickListener(this);
        tracking_Btn.setOnClickListener(this);
        chat_Btn.setOnClickListener(this);
        signOut_Btn.setOnClickListener(this);
        properties_Btn.setOnClickListener(this);
        animAlpha = AnimationUtils.loadAnimation(this,R.anim.anim_alpha); // load the animation from the directory
        screen = (LinearLayout)findViewById(R.id.activity_home__page_);
        Animation animRotateIn_icon = AnimationUtils.loadAnimation(this, R.anim.rotate);
        Animation zoomin = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        screen.startAnimation(animRotateIn_icon);
    }




    @Override
    public void onClick(View v) {

        Intent nevigateTo = null;
        switch (v.getId()){

            case R.id.gps_Btn:
                gps_Btn.startAnimation(animAlpha);
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
            case R.id.signOut_Btn:
//                mauth.signOut();
                nevigateTo = new Intent(this,traceTabActivity.class);
                break;
            /*case R.id.properties_Btn:
                nevigateTo = new Intent(this,Prefernce_Activity.class);
                break;
            */
//
//            case R.id.chat_Btn:
//                nevigateTo = new Intent(this,ChatActivity.class);
//                break;
        }

        if(nevigateTo!=null){
            startActivity(nevigateTo);

        }

//
    }

//    private void sinout(){
//        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
//            @Override
//            public void onResult(@NonNull Status status) {
//
//            }
//        });
//    }


    @Override
    public void onBackPressed() {
    }
}
