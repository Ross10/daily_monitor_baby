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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;


public class Tab2Frag extends Fragment {
    private static final String TAG = "Tab2Fragment";

    private ImageButton pooBtn,peeBtn;
    private DatabaseReference databaseReference;
    private FirebaseUser userr;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab2,container,false);
        pooBtn = (ImageButton)view.findViewById(R.id.pooId);
        peeBtn = (ImageButton)view.findViewById(R.id.peeId);


        pooBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = traceTabActivity.getDateAndHour();
                Logs log = new Logs(date,2);
                userr = FirebaseAuth.getInstance().getCurrentUser();
                databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userr.getUid());
                databaseReference.child("Logs").child(log.getLogId()).setValue(log);



            }
        });


//        peeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String date = traceTabActivity.getDateAndHour();
//                Logs log = new Logs(date,1);
//                userr = FirebaseAuth.getInstance().getCurrentUser();
//                databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userr.getUid());
//                databaseReference.child("Logs").child(log.getLogId()).setValue(log);
//
//            }
//        });





        return view;
    }
}
