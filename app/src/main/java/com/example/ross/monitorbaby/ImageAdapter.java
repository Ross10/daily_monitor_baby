package com.example.ross.monitorbaby;

/**
 * Created by Ross on 3/4/2017.
 */


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;



public class ImageAdapter extends BaseAdapter
{
    private Context mContext;

    public ImageAdapter(Context c){
        this.mContext = c;
    }

    public ImageAdapter(){

    }

    // getters
    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    // for each element do the follows
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView == null){
            imageView = new ImageView(this.mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(250,250));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(20, 20, 20, 20);

        }else{
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }


    public Integer[] mThumbIds = {
//            R.drawable.calanderci, R.drawable.gpsci, R.drawable.todoci, R.drawable.photo_albumcir, R.drawable.trackerci,R.drawable.logoutdoor

    };
}
