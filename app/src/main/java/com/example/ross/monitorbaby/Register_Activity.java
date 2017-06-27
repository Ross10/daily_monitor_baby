package com.example.ross.monitorbaby;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import com.google.android.gms.tasks.Task;
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
    private String userEmail,userPassword;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth; //Returns an instance of this class corresponding to the default FirebaseApp instance when using getiInstance().
    private DatabaseReference mDatabaseReference;
    private ProgressDialog progressDialog = null;
    private AlertDialog mDialog;
    private SharedPreferences preferences;
    private Boolean isRegistered;

   private FirebaseUser fireuser ; //Returns the currently signed-in FirebaseUser or null if there is none.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);
        isRegistered = false;

        fullname = (EditText)findViewById(R.id.fullNameEditText);
        passwd = (EditText)findViewById(R.id.passwdEditText);
        recPasswd = (EditText)findViewById(R.id.confirmPasswdEditText);
        email = (EditText)findViewById(R.id.emailText);
        nannyAddress = (EditText)findViewById(R.id.nannyAddressEditText);
        phoneNum = (EditText)findViewById(R.id.phoneNum);
        childName = (EditText)findViewById(R.id.ChildName);
        registerBtn = (Button)findViewById(R.id.registerBtn);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users");



    }


    public void registerPressed(View v){

        boolean isValid =  checkValidation();

        // save to shared reference
        if(isValid){



//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("Email",email.getText().toString());
//            editor.putString("Password",passwd.getText().toString());
//            editor.putString("nannyAddress",nannyAddress.getText().toString());
//            editor.putString("childName",childName.getText().toString());
//            editor.putString("phoneNum",phoneNum.getText().toString());
//            editor.putString("fullname",fullname.getText().toString());
//
//            editor.commit();
//            Toast.makeText(this, getString(R.string.Thanks) + fullname.getText().toString() +getString(R.string.YourDataSavedSuccsefuly2), Toast.LENGTH_LONG).show();

            progressDialog = ProgressDialog.show(Register_Activity.this,"אנא המתן..","העמוד בטעינה",true);
            userEmail =email.getText().toString();
            userPassword = passwd.getText().toString();


            executeSignUp(userEmail,userPassword);



        }

    }

    private void executeSignUp(String userEmail, String userPassword) {
        mDialog = AppHelper.buildAlertDialog("שמירת הנתונים מתבצעת..", "אנא המתן.." , false , this );
        mDialog.show();


        //Set the values to firebase :

        mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.dismiss();

                if(task.isSuccessful()){
                    //If the task sucsses to add a user to firebase DB and start HomeACtivity
                    addNewUser(task.getResult().getUser().getUid());
                    progressDialog.dismiss();
                    isRegistered = true;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isregister",isRegistered);
                    editor.commit();
                    Intent homePage = new Intent(Register_Activity.this,Home_Page_Activity.class);
                    startActivity(homePage);
                }else{
                    //if the task didnt sucsses show up an error
                    mDialog=AppHelper.buildAlertDialog("בעיה בהתחברות" , "אנא בדוק שהינך מחובר לאינטרנט", true , Register_Activity.this);
                    mDialog.show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void addNewUser(String uid) {

//        UserDetails userdetail = new UserDetails(1);
//        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");
        FirebaseUser userBase = mAuth.getCurrentUser();
//        mDatabaseReference.child(userBase.getUid()).setValue(userdetail);

        String emailUser =   email.getText().toString();
        String nannyuser =  nannyAddress.getText().toString();
        String childUser = childName.getText().toString();
        String PhoneNumUser = phoneNum.getText().toString();
        String fullNameUser =  fullname.getText().toString();
        User user = new User(fullNameUser,emailUser,"", new Date().getTime(),User.NO_URI,childUser,PhoneNumUser,nannyuser);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userBase.getUid()).child("userDetail");
        mDatabaseReference.setValue(user);
    int counter;
//        mDatabaseReference.child(uid).push();
//        mDatabaseReference.child(uid).setValue(user);
    }


    private boolean checkValidation() // validation method
    {
        int counter =0;
//        if(fullname.getText().toString().equals("")) // check full name
//        {
//            fullname.setError(getString(R.string.ReqiureField));
//            counter++;
//        }

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

//        else
//        {
//            if(!isValidEmail(email.getText().toString()))
//            {
//                email.setError(getString(R.string.ReqiureField));
//                counter++;
//            }
//        }

        // check match
        if(!(passwd.getText().toString().equals(recPasswd.getText().toString()))){
            passwd.setError(getString(R.string.PasswordNotEquals));
            recPasswd.setError(getString(R.string.PasswordNotEquals));
            counter++;
        }

        // check nanny address
//        if(nannyAddress.getText().toString().equals("")){
//            nannyAddress.setError(getString(R.string.AdviceToTurnOnGpsAndReqirment));
//            counter++;
//        }

        // check child name
//        if(childName.getText().toString().equals("")){
//            childName.setError(getString(R.string.ReqiureField));
//            counter++;
//        }

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
