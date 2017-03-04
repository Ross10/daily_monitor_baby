package com.example.ross.monitorbaby;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;



import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ToggleButton;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.ross.monitorbaby.Register_Activity.MyPREFERENCES;


public class GpsHandler extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, AdapterView.OnItemClickListener
{

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private static final int MAX_SMS_MESSAGE_LENGTH = 160 ;
    private static final String SMS_SENT_INTENT_FILTER = "com.yourapp.sms_send";
    private static final String SMS_DELIVERED_INTENT_FILTER = "com.yourapp.sms_delivered";
    private Button findCordBtn;
    private ToggleButton toggle;
    private TextView addressEditText; //distanceEditText
    private TextView cordAdressTxtView,tempTextView,addressShownTextView;
    private ListView dialogListView;
    private int permissionCheck,sentSms;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation, location;
    private String mLastUpdateTime, m_Text,phoneNumber;
    private List<Address> addresses;
    private Context appContext;
    private  double[] geoAddressLocation;
    private ProgressBar progressBar;
    private ServiceConnection mConnection;
    private Boolean noSuchAddressFlag;
    private ImageButton gpsByGoogleMap;
    private SharedPreferences sharedPreferences;
    private ImageView gpsisOff;




    private LocalService mService;
    private boolean mBound,on ;

    Handler mainHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_handler);

        // connect with layout
        findCordBtn = (Button) findViewById(R.id.findCordBtn);
        toggle = (ToggleButton) findViewById(R.id.toggleButton);
        addressEditText = (TextView) findViewById(R.id.addressEditText);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        addressShownTextView = (TextView) findViewById(R.id.addressShownTextView);
        tempTextView = (TextView) findViewById(R.id.tempTextView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        gpsisOff = (ImageView)findViewById(R.id.gpsisOff);

        noSuchAddressFlag = false;
        sentSms =0;
        toggle.setText(R.string.TurnOnGps);
        appContext = GpsHandler.this;
        on = false;
        toggle.setChecked(sharedPreferences.getBoolean("toggleButton", false));
        phoneNumber = sharedPreferences.getString("phoneNum","noPhoneInserted");
        geoAddressLocation = new double[2];


        // run time permission
        permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);

        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            gpsisOff.setImageResource(R.drawable.gpsdisconnectedflled25);
            buildAlertMessageNoGps();
        }
        else{
            gpsisOff.setImageResource(R.drawable.gpsconnected);
        }

        // by default the connection flag is false
        mBound=false;


        boolean psResult = checkPlayServices(); // check the play services connection


        addressEditText.setText(sharedPreferences.getString("nannyAddress",getString(R.string.EditAddress)));

        if (psResult == false) {
            return;
        }


        //Connect to Google ApI
        connectGoogleAPI();


        checkAddress();




    }


    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }


    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    public void findCordinatesClick(View v)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.AddressChange);

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                addressEditText.setText(m_Text);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("nannyAddress",addressEditText.getText().toString());
                editor.commit();
                onOkClicking();

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

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {

        mGoogleApiClient.connect();

        if (ContextCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


            if(lastLocation==null)
                tempTextView.setText(R.string.PleaseTurnOnGps);

            else
            {
                tempTextView.setText("");

            }
        }




    }

    @Override
    public void onConnectionSuspended(int i)
    {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(appContext, "HIN6", Toast.LENGTH_LONG).show();


    }


    protected void createLocationRequest()//create location request
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    public void onToggleClick(View v)
    {


        on = ((ToggleButton) v).isChecked();

        sentSms=0;
        if(on){
            toggle.setText(R.string.TurnOffGps);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("toggleButton", true);
            editor.commit();

            startService();
        }else{
            toggle.setText(R.string.TurnOnGps2);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("toggleButton", false);
            editor.commit();
            stopService();
        }

    }

    public void onStopService(View v)
    {



    }


    private void updateUI(float[] results)
    {



        // create a new notification
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                        .setContentTitle(getString(R.string.alreadyThere))
                        .setContentText(getString(R.string.DistanceFrom)+ String.valueOf(results[0]) + getString(R.string.Meters));

        Intent notificationIntent = new Intent(this, GpsHandler.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

    }






    private boolean checkPlayServices()
    {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        1000).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),
                        R.string.CantLoginToGoogle, Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
    }



    public boolean isNumber(String str)// method to check if the input is numeric
    {
        if(str.equals(""))
            return true;

        try
        {
            double d = Double.parseDouble(str);
            if (d < 0)
                return false;

        }
        catch(NumberFormatException nfe)
        {

            return false;
        }

        return true;

    }



    public void connectGoogleAPI()
    {
        if (mGoogleApiClient == null)
        {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }


    public void showInGoogleMap( double[] address){
        Uri gmmIntentUri = Uri.parse("geo:" + address[0]+"," + address[1]+ "?q=" + addressEditText.getText().toString());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void startService(){

        mBound=true;


        mConnection = new ServiceConnection()
        {
            // Called when the connection with the service is established
            public void onServiceConnected(ComponentName className, IBinder service)
            {
                mService = ((LocalService.LocalBinder)service).getService();
            }

            // Called when the connection with the service disconnects unexpectedly
            public void onServiceDisconnected(ComponentName className)
            {
                mService=null;
            }
        };






        if (ActivityCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, R.string.NoPermission, Toast.LENGTH_LONG).show();
        }
        createLocationRequest();


        Intent intent = new Intent(appContext, LocalService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE); // bind to the Service



        Location loc =  LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        boolean isConn = mGoogleApiClient.isConnected();


        //check if the connection to google API is connected
        if(isConn)
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
        else{
            mGoogleApiClient.connect();
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
        }

    }

    public void stopService(){

        if (mGoogleApiClient.isConnected())
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }


        if (mBound)
        {
            unbindService(mConnection); // close Service connection
            mBound = false;
        }

        tempTextView.setText("");
    }


    @Override
    public void onLocationChanged(Location location)
    {


        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        boolean isSendNotification=false;


        int disFromTarget = 190;


        //The computed distance is stored in results[0]. If results has length 2 or greater,
        // the initial bearing is stored in results[1].
        // If results has length 3 or greater, the final bearing is stored in results[2].
        float[] results = new float[3];

        Location.distanceBetween(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude(),
                geoAddressLocation[0],geoAddressLocation[1],results);



        tempTextView.setText(getString(R.string.YourLenthFromDistance) +String.valueOf(results[0]));

        if(results[0]< disFromTarget && isSendNotification==false) // if inside the radius range
        {
            isSendNotification=true;
            if(!(sentSms>0)){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
                sendSms();
            }

            updateUI(results);

        }
    }


    public void onOkClicking(){
        progressBar.setVisibility(View.VISIBLE);

        new Thread() // a new thread to unblock the UI Thread.
        {
            public void run()
            {

                // run geocoder with new Thread
                Geocoder geocoder = new Geocoder(appContext, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocationName(addressEditText.getText().toString(), 50); //get cordinates from user adress name

                    if (addresses.size() <= 0) //if there are no address
                    {
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                if (addressEditText.getText().toString().equals("")){
                                    Toast.makeText(appContext, R.string.EnterAdressPlease, Toast.LENGTH_SHORT).show();
                                    noSuchAddressFlag=true;

                                }



                                else {
                                    Toast.makeText(appContext, R.string.AddressDosntExsists, Toast.LENGTH_SHORT).show();
                                    addressEditText.setText("");
                                    noSuchAddressFlag=true;

                                }

                                progressBar.setVisibility(View.INVISIBLE);

                            }
                        });

                        return;
                    } else if (addresses.size() == 1)// if we find only one address
                    {

                        geoAddressLocation[0] = addresses.get(0).getLatitude();
                        geoAddressLocation[1] = addresses.get(0).getLongitude();


                        mainHandler.post(new Runnable() {

                            public void run() {
                                progressBar.setVisibility(View.INVISIBLE);
                                addressShownTextView.setText(addresses.get(0).getAddressLine(0)+" "+addresses.get(0).getAddressLine(1));

                            }

                        });
                        return;
                    } else //more then one address
                    {

                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
                                builder.setTitle(R.string.PleaseChooseTheRightAddress);


                                //copy addresses to array
                                String[] arr = new String[addresses.size()];
                                for (int i = 0; i < addresses.size(); i++) {
                                    arr[i]= addresses.get(i).getAddressLine(0)+" "+addresses.get(i).getAddressLine(1)+" "+addresses.get(i).getAddressLine(2);
                                }


                                final ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(appContext, android.R.layout.select_dialog_multichoice, arr);


                                builder.setAdapter(aAdapter, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        progressBar.setVisibility(View.INVISIBLE);

                                        geoAddressLocation[0] = addresses.get(which).getLatitude();
                                        geoAddressLocation[1] = addresses.get(which).getLongitude();

                                        addressShownTextView.setText(aAdapter.getItem(which));
                                        AlertDialog.Builder builderInner = new AlertDialog.Builder(appContext);
                                        dialog.dismiss();


                                    }
                                });
                                builder.create();
                                builder.show();


                            }
                        });
                    }
                } catch (IOException ioException) {
                    Toast.makeText(appContext, R.string.Probelm, Toast.LENGTH_LONG).show();

                }
            }

        }.start();



    }

    // check the address
    public void checkAddress(){
        onOkClicking();
        if(noSuchAddressFlag){
            toggle.setEnabled(false);
        }else{
            toggle.setEnabled(true);
        }
    }


    public void openGoogleMap(View v){
        showInGoogleMap(geoAddressLocation);
    }


    private void sendSms() // send SMS when phone in radius
    {
        String message = getString(R.string.TheKidIsSafe);

        String phnNo =  phoneNumber ;//preferable use complete international number

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


    // dialog with user to get address
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.PleaseturnOnGpsDevice)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


}


