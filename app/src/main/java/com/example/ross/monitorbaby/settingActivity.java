package com.example.ross.monitorbaby;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class settingActivity extends AppCompatActivity {
    private EditText phoneNumProp,childNameProp,nannyAddressProp,emailProp,fullNameProp;
    private FirebaseAuth mAuth; //Returns an instance of this class corresponding to the default FirebaseApp instance when using getiInstance().
    private DatabaseReference mDatabaseReference;
    private FirebaseUser userr;
    private View viewLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        phoneNumProp= (EditText)findViewById(R.id.phoneNumProp);
        childNameProp= (EditText)findViewById(R.id.ChildNameProp);
        nannyAddressProp= (EditText)findViewById(R.id.nannyAddressProp);
        emailProp= (EditText)findViewById(R.id.emailProp);
        fullNameProp= (EditText)findViewById(R.id.fullNameProp);

        userr = FirebaseAuth.getInstance().getCurrentUser();
        LayoutInflater layoutInflater = getLayoutInflater();
        viewLayout =layoutInflater.inflate(R.layout.detailschanged_toast, (ViewGroup) findViewById(R.id.greenV));


        readData();





    }


    public void readData(){

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userr.getUid()).child("userDetail");

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if(user!=null){
                    if(user.getDisplayName()!=null){
                        fullNameProp.setText(user.getDisplayName());

                    }
                    if(user.getChildName()!=null){
                        childNameProp.setText(user.getChildName());
                    }

                    if(user.getNannyAddress()!=null){
                        nannyAddressProp.setText(user.getNannyAddress());
                    }
                    if(user.getPhoneNumber()!=null){
                        phoneNumProp.setText(user.getPhoneNumber());
                    }
                    if(user.getEmail()!=null){
                        emailProp.setText(user.getEmail());
                    }
                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    public void changeDetails(View view) {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userr.getUid()).child("userDetail");

            mDatabaseReference.child("childName").setValue(childNameProp.getText().toString());
            mDatabaseReference.child("displayName").setValue(fullNameProp.getText().toString());
            mDatabaseReference.child("email").setValue(emailProp.getText().toString());
            mDatabaseReference.child("nannyAddress").setValue(nannyAddressProp.getText().toString());
            mDatabaseReference.child("phoneNumber").setValue(phoneNumProp.getText().toString());

            Toast toast1 = Toast.makeText(this,"Toast:Gravity.TOP",Toast.LENGTH_SHORT);
            toast1.setGravity(Gravity.CENTER,0,0);
            toast1.setView(viewLayout);
            toast1.show();




    }


}
