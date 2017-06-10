package com.example.ross.monitorbaby;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Ross on 6/10/2017.
 */

public class GpsService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,"Service started",Toast.LENGTH_LONG).show();
        return START_STICKY;
    }


    @Override
    public void onDestroy() {

        Toast.makeText(this,"Service Destroyed",Toast.LENGTH_LONG).show();


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

