package com.example.ross.monitorbaby;
import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, AdapterView.OnItemClickListener{


    Button btSearch,btStart,btStop;
    EditText ed;
    TextView tvAddress;
    ImageView startNevigate;

    // Geocoder
    Geocoder geocoder;

    // alertDialog
    AlertDialog.Builder addressesDialog;

    // distance value from the editText
    String Dist;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation, location;

    // lan and lot that will set to the location variable
    float lat,lon;

    // list of addresses in case we have more than one address from the GeoCoder
    List<Address> lstAdresses;

    // permissions
    static final int MY_PERMISSIONS_REQUEST_LOCATION=1;
    static final int MY_PERMISSIONS_COARSE_LOCATION=1;
    boolean ch;
    // max result of the adrresses in GeoCoder
    static final int MAX_RESULT=50;
    // private static final String TAG=MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btSearch =(Button)findViewById(R.id.button);
        btStart=(Button)findViewById(R.id.btStart);
        btStop=(Button)findViewById(R.id.btStop);
        ed=(EditText)findViewById(R.id.editText);
        tvAddress =(TextView)findViewById(R.id.textView2);
        startNevigate = (ImageView)findViewById(R.id.imageView4);


        // geoCoder initial
        geocoder=new Geocoder(MainActivity.this, Locale.getDefault());
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        Dist=ed.getText().toString();

        // in Search Button , run a new thread to search coordinates , using AsyncTask
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed.getText().toString().isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Please type target",
                            Toast.LENGTH_LONG).show();
                }
                else
                    new GeoThread(MainActivity.this).execute();

            }
        });

        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tvAddress.getText().toString().equals(""))
                {
                    Toast.makeText(MainActivity.this, "Please type destination",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    // with start service button , send to the service class , the land,lot and the distance !
                    Intent intent = new Intent(MainActivity.this, MyService.class);
                    intent.putExtra("lat", "" + lat);
                    intent.putExtra("lon", "" + lon);
                    intent.putExtra("distance", "150");

                    //request permission
                    askPermessions();
                    // start the service
                    startService(intent);
                }
            }
        });

        // stop service
        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyService.class);
                stopService(intent);

            }
        });


        startNevigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInGoogleMap(lstAdresses);


            }
        });

        connectGoogleAPI();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }


    private class GeoThread extends AsyncTask<Object, Object, List<Address>>
    {
        private ProgressDialog dialog;
        private Context context;
        public GeoThread(Context context)
        {
            this.context=context;
        }

        // method that return list of the addresses
        private List<Address> searchAddresses()
        {
            geocoder = new Geocoder(MainActivity.this);
            try
            {
                return  geocoder.getFromLocationName(ed.getText().toString(), MAX_RESULT);
            }
            catch (IOException e)
            {
            }
            return null;
        }

        // on the background , go and get the addresses , and then cancle the loading dialog
        @Override
        protected List<Address> doInBackground(Object... params)
        {
            return searchAddresses();
        }

        protected void onPreExecute()
        {
            // loading dialog is waiting until the search is finished!
            this.dialog = new ProgressDialog(context);
            this.dialog.setMessage("Loading...");
            this.dialog.setCancelable(true);
            this.dialog.setOnCancelListener(new DialogInterface.OnCancelListener(){
                @Override
                public void onCancel(DialogInterface dialog)
                {
                    // cancel AsyncTask
                    cancel(false);
                }
            });

            this.dialog.show();

        }
        protected void onPostExecute(List<Address> result)
        {
            lstAdresses = result;
            if (lstAdresses ==null)
            {
                return;

            }
            if(lstAdresses.size()>1)
            {
                // if the list size is more than 1 result , init a array from type charSequence in order to put them in the dialog
                // and then if the user choosed any item , set the text into the textView of the target !

                addressesDialog = new AlertDialog.Builder(MainActivity.this);
                final CharSequence items[]=new CharSequence[lstAdresses.size()];
                for(int i = 0; i< lstAdresses.size(); i++)
                {
                    Address location= lstAdresses.get(i);
                    String str="";
                    for(int k=0;k<=location.getMaxAddressLineIndex();k++)
                        str+=location.getAddressLine(k)+", ";
                    items[i]=str;
                }
                ch=false;
                addressesDialog.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface d, int n) {
                        ch=true;
                        tvAddress.setText(""+items[n]);
                        lat=(float) lstAdresses.get(n).getLatitude();
                        lon=(float) lstAdresses.get(n).getLongitude();
                    }

                });
                if(!ch)
                {
                    tvAddress.setText(""+items[0]);
                    lat=(float) lstAdresses.get(0).getLatitude();
                    lon=(float) lstAdresses.get(0).getLongitude();

                }
                addressesDialog.setNegativeButton("בחר", null);
                addressesDialog.setTitle("אנא בחר את הכתובת הרצויה, אם לא מצאת, אנא נסה לדייק בחיפוש:");
                addressesDialog.show();
            }
            else {

                Address location= lstAdresses.get(0);
                // GET THE NAME OF THE ADDRESS UNTIL THE FIRST NULL
                String str="";
                for(int k=0;k<=location.getMaxAddressLineIndex();k++)
                    str+=location.getAddressLine(k)+", ";
                tvAddress.setText(str);
                lat=(float) lstAdresses.get(0).getLatitude();
                lon=(float) lstAdresses.get(0).getLongitude();

            }
            dialog.dismiss();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(MainActivity.this, MyService.class);
        stopService(intent);
    }

    // method that ask permissions
    private void askPermessions()
    {
        ActivityCompat.requestPermissions(MainActivity.this
                ,new  String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);
        ActivityCompat.requestPermissions(MainActivity.this
                ,new  String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                MY_PERMISSIONS_COARSE_LOCATION);

    }


    public void showInGoogleMap( List address){
        Uri gmmIntentUri = Uri.parse("geo:" + lstAdresses.get(0).getLatitude()+"," + lstAdresses.get(0).getLongitude()+ "?q=" +  ed.getText().toString());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
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
}
