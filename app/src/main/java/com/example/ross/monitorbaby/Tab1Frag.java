package com.example.ross.monitorbaby;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Tab1Frag extends Fragment {

    private static final String TAG = "Tab1Fragment";
    private ImageView bottle, upArrow,downArrow, ammountBottle,confirm;
    private TextView ammountText;
    private int currAmmountPos;
    private int[] ammountsArr;
    private int[] imagesArr;
    private DatabaseReference databaseReference;
    private FirebaseUser userr;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1,container,false);


        bottle = (ImageView)view.findViewById(R.id.iv_bottle);
        upArrow = (ImageView)view.findViewById(R.id.upArrow);
        downArrow = (ImageView)view.findViewById(R.id.downArrow);
        ammountBottle = (ImageView)view.findViewById(R.id.ammountBottle);
        confirm = (ImageView)view.findViewById(R.id.iv_confirm_bottle);
        ammountText = (TextView)view.findViewById(R.id.ammout);


//        upArrow.setOnClickListener(this);

        ammountsArr =new int[] {10,20,30,40,50,60,70,80,90,100,110,120,130,140,150,160,170,180,190,200,210,220,230,240,250};
        initAmmountImages();

        currAmmountPos = 14;

        upArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUpArrowClicked();
            }
        });

        downArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDownArrowClickede();
            }
        });



        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onConfirm();
            }
        });



        return view;
    }



    private void initAmmountImages() // create images array
    {
        imagesArr = new int[]{R.drawable.b10,R.drawable.b20,R.drawable.b30,R.drawable.b40,R.drawable.b50,R.drawable.b60,R.drawable.b70,R.drawable.b80,R.drawable.b90,R.drawable.b100,R.drawable.b110,R.drawable.b120,R.drawable.b130,R.drawable.b140,R.drawable.b150,R.drawable.b160,R.drawable.b170,R.drawable.b180,R.drawable.b190,R.drawable.b200,R.drawable.b210,R.drawable.b220,R.drawable.b230,R.drawable.b240,R.drawable.b250};
    }


    public void onDownArrowClickede()// check limits change text ammount and replace the image
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


    public void onUpArrowClicked() // check limits change text ammount and replace the image
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

    public void onConfirm(){
        String date = traceTabActivity.getDateAndHour();
        Logs log = new Logs(date,ammountText.getText().toString());
        userr = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userr.getUid());
        databaseReference.child("Logs").child(log.getLogId()).setValue(log);


    }


}



