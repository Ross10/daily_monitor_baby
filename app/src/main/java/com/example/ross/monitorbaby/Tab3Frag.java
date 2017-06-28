package com.example.ross.monitorbaby;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Tab3Frag extends Fragment {
    private static final String TAG = "Tab3Fragment";
    private DatabaseReference mDatabaseReference;
    private FirebaseUser userr;
    private ArrayList<String> logArray;
    private String childname;
    private LogsAdapter adapter;

    ArrayList<String> stringAarray;
    private ListView lv;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        logArray = new ArrayList<>(); //Without it it can be duplicate values 1.
        View view = inflater.inflate(R.layout.fragment_tab3,container,false);

        lv = (ListView)view.findViewById(R.id.logsListView);
//        AdapterForLogsView adapter = new AdapterForLogsView(this.getActivity(),)




//        btn3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getActivity(), "TESTING BUTTON CLICK 3",Toast.LENGTH_SHORT).show();
//            }
//        });
        readAllDataFromFB();
        return view;
    }



    public void readAllDataFromFB(){
        logArray.clear(); //Without it it can be duplicate values 2.
        FirebaseUser userr = FirebaseAuth.getInstance().getCurrentUser();
        userr.getDisplayName();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userr.getUid()).child("userDetail");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if(user!=null){
                    childname = user.getChildName();


                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userr.getUid()).child("Logs");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    Logs newLog = ds.getValue(Logs.class);
                    if(newLog.getKind() ==0){
                        logArray.add(childname + " אכל : " + newLog.getAmount() + " בשעה : " +  newLog.getDateAndTime());

                    }
                    else if(newLog.getKind()==1){
                        logArray.add(childname + " עשה פיפי בשעה : " +  newLog.getDateAndTime());

                    }

                    else{
                        logArray.add(childname + " עשה קקי בשעה : " +  newLog.getDateAndTime());
                    }

                }

                if(logArray.size()>0){

//                    adapter = new LogsAdapter(Tab3Frag.this,logArray);
                    ArrayAdapter<String> listviewAdapter = new ArrayAdapter<String>(
                            getActivity(),
                            android.R.layout.simple_dropdown_item_1line,
                            logArray


                    );
                    lv.setAdapter(listviewAdapter);


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }
}
