package com.example.ross.monitorbaby;

/**
 * Created by Ross on 6/10/2017.
 */
import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;


public class MyService extends Service {
    static final int MY_PERMISSIONS_COARSE_LOCATION=1;

    // location variable to compare with the current location
    private Location targetLocation,mCurrentLocation;
    private static final String SMS_SENT_INTENT_FILTER = "com.yourapp.sms_send";
    private static final String SMS_DELIVERED_INTENT_FILTER = "com.yourapp.sms_delivered";
    private int sentSms;
    private LocationManager locationManager;

    private float distance;
    private String mLastUpdateTime;

    // location listener
    private LocationListener locationListener = new LocationListener() {


        // when the location changed, compare the current location with the target location , by distance defferance !
        @Override
        public void onLocationChanged(Location location) {
            mCurrentLocation = location;
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            boolean isSendNotification=false;



            if(targetLocation.distanceTo(location)>distance){
                isSendNotification=false;
                sentSms=0;

            }

            if(targetLocation.distanceTo(location)<=distance && isSendNotification==false) // if inside the radius range
            {
                isSendNotification=true;
                if(!(sentSms>0)){
                    sendSms();
                }

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(MyService.this)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("עוד רגע שם....")
                                .setContentText(" אתה במרחק של : " + targetLocation.distanceTo(location) + " מטר מהיעד ");
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(
                                Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(1, mBuilder.build());



            }
//
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    // constructor
    public MyService()
    {


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                return;

            }
        }
        locationManager.removeUpdates(locationListener);
        locationManager = null;


    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // init the locationManager
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // init the target location
        targetLocation =new Location("");
        //get lon and lat from the intent , and convert from string to float
        String lat= intent.getExtras().getString("lat");
        String lon= intent.getExtras().getString("lon");
        // set the lat and lon to the "target location" variable
        targetLocation.setLatitude((float)Float.valueOf(lat));
        targetLocation.setLongitude((float)Float.valueOf(lon));
        // get the distance from the intent , convert from string to float
        String distanceString=intent.getExtras().getString("distance");
        distance=(float)Float.valueOf(distanceString);

        // check permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED)
            {
                return START_NOT_STICKY;

            }
            if( checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                return START_NOT_STICKY;

            }

        }

        // start the location listener with 1 sec or 1 metter
        //   locationManager.removeUpdates(locationListener);

        Criteria criteria=new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        criteria.setAltitudeRequired(true);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setCostAllowed(true);
        String best=locationManager.getBestProvider(criteria, false);
        locationManager.requestLocationUpdates(best, 1, 1,locationListener);
        return START_STICKY;
    }



    private void sendSms() // send SMS when phone in radius
    {
        String message = getString(R.string.TheKidIsSafe);

        String phnNo =  "0587000097" ;//preferable use complete international number

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
                SMS_SENT_INTENT_FILTER), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(
                SMS_DELIVERED_INTENT_FILTER), 0);



        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phnNo, null, message, sentPI, deliveredPI);
            Toast.makeText(getApplicationContext(), R.string.MessageSentSuccesfuly,
                    Toast.LENGTH_LONG).show();
            sentSms++;
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }

    }

}