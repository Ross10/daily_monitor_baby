package com.example.ross.monitorbaby;



import android.content.ContentValues;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TrackingActivity extends AppCompatActivity
{

    private ListView lvlogs;
    private Cursor c;
    private ImageView diaper, bottle, upArrow,downArrow, ammountBottle,confirm, shit,pee;
    private TextView ammountText;
    private TrackingCursoAdapter tca;
    private SQLiteDatabase db1;
    TrackingDbHelper trackDbHalper;
    private int currAmmountPos;
    private int[] ammountsArr;
    private int[] imagesArr;
    private String kidName;
    SharedPreferences preferences;

    private boolean pushBottleFlag ,pushDIaperFlag; // flags for the main images


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracking_layout);

        final Context context =getApplicationContext();

        // get data from shared reference
        preferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);
        trackDbHalper = new TrackingDbHelper(this);

        // connect items with layout
        lvlogs = (ListView) findViewById(R.id.logs_lv_id);
        diaper = (ImageView)findViewById(R.id.iv_diaper);
        bottle = (ImageView)findViewById(R.id.iv_bottle);
        upArrow = (ImageView)findViewById(R.id.iv_upArrow);
        downArrow = (ImageView)findViewById(R.id.iv_downArrow);
        ammountBottle = (ImageView)findViewById(R.id.iv_ammountBottle);
        confirm = (ImageView)findViewById(R.id.iv_confirm_bottle);
        shit = (ImageView)findViewById(R.id.shit_iv_id);
        pee = (ImageView)findViewById(R.id.pee_iv_id);
        ammountText = (TextView)findViewById(R.id.tv_ammout);
        kidName = preferences.getString("childName","אנונימי");
        pushBottleFlag = false;
        pushDIaperFlag = false;

        //ammounts array
        ammountsArr =new int[] {10,20,30,40,50,60,70,80,90,100,110,120,130,140,150,160,170,180,190,200,210,220,230,240,250};
        initAmmountImages();

        currAmmountPos = 14;
        ammountText.setText(String.valueOf(ammountsArr[currAmmountPos])); // 150cc


        // connect the list view to the cursor adapter
        SQLiteDatabase db1 = trackDbHalper.getReadableDatabase();
        c = db1.query(LogsTracking.TABLE_NAME, null, null, null, null, null, null);
        tca = new TrackingCursoAdapter(this, c);
        lvlogs.setAdapter(tca);



        lvlogs.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){


            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                Cursor cursor = (Cursor) lvlogs.getItemAtPosition(position);

                return true;

            }
        });

    }



    public void onUpArrowClicked(View view) // check limits change text ammount and replace the image
    {
        if(currAmmountPos+1 >= ammountsArr.length)
            return;

        if(ammountsArr[currAmmountPos+1] <= ammountsArr[ammountsArr.length-1])
        {
            currAmmountPos++;
            ammountText.setText(String.valueOf(ammountsArr[currAmmountPos])); // set new ammount

            ammountBottle.setImageResource(imagesArr[currAmmountPos]); // set new image
        }
    }


    public void onDownArrowClicked(View view)// check limits change text ammount and replace the image
    {
        if(currAmmountPos-1 < 0)
            return;
        if(ammountsArr[currAmmountPos-1] >= ammountsArr[0])
        {
            currAmmountPos--;
            ammountText.setText(String.valueOf(ammountsArr[currAmmountPos]));

            ammountBottle.setImageResource(imagesArr[currAmmountPos]);
        }
    }


    public void onConfirmBottleClick(View view)
    {
        String logDate = getDateAndHour(); // private function to get date and hour


        //open DB access
        TrackingDbHelper dbHelper = new TrackingDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Passing params to table
        ContentValues values = new ContentValues();
        String log = kidName+getString(R.string.Ate)+ ammountText.getText().toString()+ getString(R.string.CC)+getString(R.string.OnDate) + logDate ;

        pushBottleFlag=false;
        hideBottleElements();

        diaper.setEnabled(true);
        values.put(LogsTracking.Description,log);

        //insert to DB
        db.insert(LogsTracking.TABLE_NAME,null,values);

        //refresh listView
        db = dbHelper.getReadableDatabase();
        Cursor c = db.query(LogsTracking.TABLE_NAME,null,null,null,null,null,LogsTracking._ID+" DESC");
        tca.changeCursor(c);



    }


    private void initAmmountImages() // create images array
    {
        imagesArr = new int[]{R.drawable.b10,R.drawable.b20,R.drawable.b30,R.drawable.b40,R.drawable.b50,R.drawable.b60,R.drawable.b70,R.drawable.b80,R.drawable.b90,R.drawable.b100,R.drawable.b110,R.drawable.b120,R.drawable.b130,R.drawable.b140,R.drawable.b150,R.drawable.b160,R.drawable.b170,R.drawable.b180,R.drawable.b190,R.drawable.b200,R.drawable.b210,R.drawable.b220,R.drawable.b230,R.drawable.b240,R.drawable.b250};
    }


    private String getDateAndHour()
    {
        Calendar c= Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm a");
        String str=sdf.format(c.getTime());

        return str;
    }


    public void onBottleClick(View view)
    {
        if(!pushBottleFlag) //first push
        {
            pushBottleFlag=true;
            showBottleElements();
            diaper.setEnabled(false); // diaper will be unclickable
        }

        else
        {
            pushBottleFlag=false;
            hideBottleElements();
            diaper.setEnabled(true);// diaper will be clickable
        }
    }


    public void onDiaperClick(View view)
    {
        if(!pushDIaperFlag) //first push
        {
            pushDIaperFlag=true;
            showDiaperElements();
            bottle.setEnabled(false); // diaper will be unclickable
        }

        else
        {
            pushDIaperFlag=false;
            hideDiaperElements();
            bottle.setEnabled(true);// diaper will be clickable
        }
    }


    private void hideBottleElements()
    {
        //invisible bottle elements
        ammountBottle.setVisibility(View.INVISIBLE);
        upArrow.setVisibility(View.INVISIBLE);
        downArrow.setVisibility(View.INVISIBLE);
        confirm.setVisibility(View.INVISIBLE);
        ammountText.setVisibility(View.INVISIBLE);
    }


    private void showBottleElements()
    {
        //visible bottle elements
        ammountBottle.setVisibility(View.VISIBLE);
        upArrow.setVisibility(View.VISIBLE);
        downArrow.setVisibility(View.VISIBLE);
        confirm.setVisibility(View.VISIBLE);
        ammountText.setVisibility(View.VISIBLE);

    }

    private void hideDiaperElements()
    {
        //invisible bottle elements
        pee.setVisibility(View.INVISIBLE);
        shit.setVisibility(View.INVISIBLE);

    }


    private void showDiaperElements()
    {
        //visible bottle elements
        pee.setVisibility(View.VISIBLE);
        shit.setVisibility(View.VISIBLE);


    }


    public void onShitClick(View view)
    {
        tzrahim(getString(R.string.Poo));
        hideDiaperElements();
    }

    public void onPeeClick(View view)
    {
        tzrahim(getString(R.string.Pee));
        hideDiaperElements();
    }


    private void tzrahim(String action)
    {
        String logDate = getDateAndHour(); // get date

        //open access to DB
        TrackingDbHelper dbHelper = new TrackingDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        String log = kidName+getString(R.string.Didon)+ action+ getString(R.string.DidOn2) + logDate ;

        pushDIaperFlag=false; // reset the flag
        bottle.setEnabled(true);

        hideBottleElements();

        values.put(LogsTracking.Description,log);

        //insert to DB and refresh LV
        db.insert(LogsTracking.TABLE_NAME,null,values);
        db = dbHelper.getReadableDatabase();
        Cursor c = db.query(LogsTracking.TABLE_NAME,null,null,null,null,null,LogsTracking._ID+" DESC");
        tca.changeCursor(c);

    }

}