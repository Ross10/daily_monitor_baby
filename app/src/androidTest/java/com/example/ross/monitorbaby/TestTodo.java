package com.example.ross.monitorbaby;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Test;
import static org.junit.Assert.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Ross on 6/20/2017.
 */

public class TestTodo {
    private FirebaseAuth mAuth; //Returns an instance of this class corresponding to the default FirebaseApp instance when using getiInstance().
    private DatabaseReference mDatabaseReference;
    private FirebaseUser userr;


    @Test
    public void createTodo() throws Exception{
        Calendar c = Calendar.getInstance();

        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        Task t1 = new Task("TestName",1,formattedDate);
        assertEquals(t1.getTaskName(),"TestName");


        t1.setPriorty(2);

        assertEquals(t1.getPriorty(),2);

    }
    @Test
    public void connectTouserDB(){
        userr = FirebaseAuth.getInstance().getCurrentUser();
        assertNotEquals(userr,"");
        assertNotEquals(userr,null);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userr.getUid());
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if(user!=null){
                    assertEquals(user.getChildName(),"a");
                    assertEquals(user.getNannyAddress(),"s");
                    assertEquals(user.getDisplayName(),"s");
                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        Task t2 = new Task("TestName2",1,formattedDate);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userr.getUid());
        mDatabaseReference.child("Task").child(t2.getId()).setValue(t2);


    }

}
