package com.example.ross.monitorbaby;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.*;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Map;

public class Register_Activity extends AppCompatActivity {
    private EditText fullname,passwd,email,recPasswd,nannyAddress,phoneNum,childName;
    private Button registerBtn;
    private FirebaseDatabase mFireDatabase;
    private String userEmail,userPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedPreferences;
    private AlertDialog mDialog;
    private FirebaseUser fireuser ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);

        // connect elements from layout
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Hi");

        mFireDatabase = FirebaseDatabase.getInstance();

        fireuser = mAuth.getCurrentUser();
        fullname = (EditText)findViewById(R.id.fullNameEditText);
        passwd = (EditText)findViewById(R.id.passwdEditText);
        recPasswd = (EditText)findViewById(R.id.confirmPasswdEditText);
        email = (EditText)findViewById(R.id.emailText);
        nannyAddress = (EditText)findViewById(R.id.nannyAddressEditText);
        phoneNum = (EditText)findViewById(R.id.phoneNum);
        childName = (EditText)findViewById(R.id.ChildName);

        registerBtn = (Button)findViewById(R.id.registerBtn);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

    }


    public void registerPressed(View v){

        boolean isValid =  checkValidation();

        // save to shared reference
        if(isValid){



            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Email",email.getText().toString());
            editor.putString("Password",passwd.getText().toString());
            editor.putString("nannyAddress",nannyAddress.getText().toString());
            editor.putString("childName",childName.getText().toString());
            editor.putString("phoneNum",phoneNum.getText().toString());
            editor.putString("fullname",fullname.getText().toString());

            editor.commit();
            Toast.makeText(this, getString(R.string.Thanks) + fullname.getText().toString() +getString(R.string.YourDataSavedSuccsefuly2), Toast.LENGTH_LONG).show();

            userEmail =email.getText().toString();
            userPassword = passwd.getText().toString();

            sendToLogin();


        }

    }

    private void sendToLogin() {
        String displayName = fullname.getText().toString();
        String userEmail = email.getText().toString();
        String child = childName.getText().toString();
        String phone = phoneNum.getText().toString();
        String nanny = nannyAddress.getText().toString();
        String Id = mDatabaseReference.push().getKey();
//        mDatabaseReference.child("users").child("Name").setValue(fullname.getText().toString());
        User user = new User(displayName, userEmail, "", new Date().getTime(), User.NO_URI,child,phone,nanny);
        mDatabaseReference.child(Id).push();
        mDatabaseReference.child(Id).setValue(user);

        mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {

                mDialog.dismiss();

                if(task.isSuccessful()){
                    // If task success add a user to firebase db and start the MainActivity
                    addNewUser(task.getResult().getUser().getUid());

                    Intent login = new Intent(Register_Activity.this,Login_Activity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(login);

                }else {
                    // If the task not succeeded show an error
                    mDialog = AppHelper.buildAlertDialog(getString(R.string.register),//change it - the register !!! TODO
                            task.getException().getMessage(), true, Register_Activity.this);
                    mDialog.show();
                }
            }
        });
        // direct to login

//String childName , String phoneNumber, String nannyAddress
    }

    // Add a new user to Firebase DB
    private void addNewUser(String userId) {

        String displayName = fullname.getText().toString();
        String userEmail = email.getText().toString();
        String child = childName.getText().toString();
        String phone = phoneNum.getText().toString();
        String nanny = nannyAddress.getText().toString();
        String Id = mDatabaseReference.push().getKey();
        User user = new User(displayName, userEmail, "", new Date().getTime(), User.NO_URI,child,phone,nanny);
        mDatabaseReference.child(Id).push();
        mDatabaseReference.child(Id).setValue(user);
    }


    private boolean checkValidation() // validation method
    {
        int counter =0;
        if(fullname.getText().toString().equals("")) // check full name
        {
            fullname.setError(getString(R.string.ReqiureField));
            counter++;
        }

        //
        if(passwd.getText().toString().equals(""))// check password
        {
            passwd.setError(getString(R.string.ReqiureField));
            counter++;
        }


        if(recPasswd.getText().toString().equals(""))// check pass
        {
            recPasswd.setError(getString(R.string.ReqiureField));
            counter++;
        }


        if(email.getText().toString().equals("")) // email validation
        {
            email.setError(getString(R.string.ReqiureField));
            counter++;
        }

        else
        {
            if(!isValidEmail(email.getText().toString()))
            {
                email.setError(getString(R.string.ReqiureField));
                counter++;
            }
        }

        // check match
        if(!(passwd.getText().toString().equals(recPasswd.getText().toString()))){
            passwd.setError(getString(R.string.PasswordNotEquals));
            recPasswd.setError(getString(R.string.PasswordNotEquals));
            counter++;
        }

        // check nanny address
        if(nannyAddress.getText().toString().equals("")){
            nannyAddress.setError(getString(R.string.AdviceToTurnOnGpsAndReqirment));
            counter++;
        }

        // check child name
        if(childName.getText().toString().equals("")){
            childName.setError(getString(R.string.ReqiureField));
            counter++;
        }

        // check is numeric
        if(!isNumeric(phoneNum.getText().toString()) || phoneNum.getText().toString().equals("")){
            if( phoneNum.getText().toString().equals("")){
                phoneNum.setError(getString(R.string.ReqiureField));
            }
            else{
                phoneNum.setError(getString(R.string.LegalPhoneNumber));
            }

            counter++;
        }



        if(counter>0){
            return false;
        }

        return true;
    }



    public boolean isNumeric(String str)
    {
        try
        {
            int d = Integer.parseInt(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }




}
