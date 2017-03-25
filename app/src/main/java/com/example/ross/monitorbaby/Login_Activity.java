package com.example.ross.monitorbaby;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.*;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class Login_Activity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = Login_Activity.class.getSimpleName();
    private static final int RC_SIGN_IN = 1;
    private String fullname;
    private ProgressDialog progressDialog = null;
    private String email;
    private String password;
    private SignInButton mSignInButton;
    private GoogleApiClient mGoogleApiClient;
    private EditText emailEditText;
    private EditText passwordEditText;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private android.app.AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        emailEditText = (EditText)findViewById(R.id.emailEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        mSignInButton = (SignInButton) findViewById(R.id.googleSignInBtn);
        mSignInButton.setSize(SignInButton.SIZE_STANDARD);


        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();




    }



    public void connect(View v){

        String email = emailEditText.getText().toString();
        String psswd = passwordEditText.getText().toString();
        progressDialog = ProgressDialog.show(Login_Activity.this,"אנא המתן..","העמוד בטעינה",true);

        mAuth.signInWithEmailAndPassword(email, psswd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

//                mDialog.dismiss();
                checkLoginSuccess(task, false);

            }
        });









        //get data from shared reference
//        SharedPreferences preferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);
//        email  = preferences.getString("Email","there is not such user");
//        password = preferences.getString("Password","there is no such a password");
//        fullname = preferences.getString("fullname","אנונימי");

        // check match
//        if(emailEditText.getText().toString().equals(email) && passwordEditText.getText().toString().equals(password)){
//            Toast.makeText(this," ברוך הבא " +  fullname, Toast.LENGTH_LONG).show();
//            Intent homePage = new Intent(this,Home_Page_Activity.class);
//            startActivity(homePage);
//        }else{
//            AlertDialog.Builder error = new AlertDialog.Builder(this);
//            error.setMessage(R.string.UsernameOrpasswprdInccorect);
//            error.setTitle(R.string.ErrorWithAttiudeToTheSystem);
//            error.setPositiveButton(R.string.OK3,null);
//            error.setCancelable(true);
//            error.create().show();
//
//        }




    }

    private void checkLoginSuccess(Task<AuthResult> task, boolean googleSign) {

        if(task.isSuccessful()){
            Intent homePage = new Intent(Login_Activity.this,Home_Page_Activity.class);
            startActivity(homePage);
        }else{
            //if the task didnt sucsses show up an error
            mDialog=AppHelper.buildAlertDialog("Email does not exsits" , "Failed to Authentication", true , Login_Activity.this);
            mDialog.show();
            progressDialog.dismiss();
        }

    }


    public void register(View v){
        Intent i = new Intent(this,Register_Activity.class);
        startActivity(i);



    }







    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "You got an Error", Toast.LENGTH_LONG).show();
    }




}
