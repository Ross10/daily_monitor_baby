package com.example.ross.monitorbaby;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.appindexing.Thing;
//import com.google.android.gms.common.api.GoogleApiClient; // comment - check it



public class Album_Activity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_ACTIVITY_CALLBACK = 2;
    private Bitmap imageBitmap;
    private ImageView cameraIV;
    private File directory;
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private GridViewAdapter gridAdapter2;
    private String path;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_);
        //TODO load the album from the internal storage
        cameraIV = (ImageView) findViewById(R.id.cameraImageView);


        //testing area
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        directory = cw.getDir(getString(R.string.BabySaver), Context.MODE_PRIVATE);
        path = directory.getPath();

        //connect with layout
        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, getData());
        gridView.setAdapter(gridAdapter);


        //event listener for image click
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                //Create intent
                Intent intent = new Intent(getApplicationContext(),  DetailsActivity.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("image", item.getImage());

                //Start details activity
                startActivity(intent);
            }
        });
    }


    public void openCamera(View v) {
        Intent takePicIntante = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // create a new intent for cameraIV transfer between activities.
        if (takePicIntante.resolveActivity(getPackageManager()) != null)
            startActivityForResult(takePicIntante, REQUEST_IMAGE_CAPTURE);// callback function
    }


    //this function will call right after the startActivityForResult and it will happend after the camera will finish her job and want to transfer the data to our activity.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) // override the result function must implement with startActivityForResult
    {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");


            saveToInternalStorage(imageBitmap);
            String path = directory.getPath();
            gridAdapter2 = new GridViewAdapter(this, R.layout.grid_item_layout, getData());
            gridView.setAdapter(gridAdapter2);
            gridAdapter2.notifyDataSetChanged();
            gridAdapter.notifyDataSetChanged();




        } else if (requestCode == REQUEST_ACTIVITY_CALLBACK) {

            Toast.makeText(this, R.string.ErrorWhileCapture, Toast.LENGTH_SHORT).show();
        }


    }

    //save data to internal storage
    private String saveToInternalStorage(Bitmap bitmapImage)
    {

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
// you can get seconds by adding  "...:ss" to it
        //date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));

        String imageID = date.format(currentLocalTime);


        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        directory = cw.getDir(getString(R.string.bSaver), Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, imageID);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }





    private ArrayList<ImageItem> getData() // getting array on ImageItems to display at the grid view
    {

        final ArrayList<ImageItem> imageItems = new ArrayList<>();

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++)
        {
            File f = new File(listOfFiles[i].toString());
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
                imageItems.add(new ImageItem(bitmap, listOfFiles[i].getName()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }


        return imageItems;
    }




}
