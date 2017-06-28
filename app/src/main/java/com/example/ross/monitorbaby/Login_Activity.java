package com.example.ross.monitorbaby;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.*;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.example.ross.monitorbaby.Register_Activity.MyPREFERENCES;

public class Login_Activity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = Login_Activity.class.getSimpleName();
    private static final int RC_SIGN_IN = 1;
    private String fullname;
    private ProgressDialog progressDialog = null;
    private String email;
    private String password;
    private ImageButton mSignInButton;
    private GoogleApiClient mGoogleApiClient;
    private EditText emailEditText;
    private EditText passwordEditText;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private android.app.AlertDialog mDialog;
    private View viewLayout;
    private Boolean loginSucced;
    private SharedPreferences preferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        emailEditText = (EditText)findViewById(R.id.emailEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        mSignInButton = (ImageButton) findViewById(R.id.googleSignInBtn);
        LayoutInflater layoutInflater = getLayoutInflater();
        viewLayout =layoutInflater.inflate(R.layout.login_toast, (ViewGroup) findViewById(R.id.custom_layout));

        registerUser();
//        mSignInButton.setSize(SignInButton.SIZE_STANDARD);
        loginSucced = false;

        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
//         Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
//
//
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener()
        {


            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(Login_Activity.this, R.string.errorconnection, Toast.LENGTH_LONG).show();

            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();





    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed
                Toast.makeText(this, R.string.googlesigninfailed, Toast.LENGTH_LONG).show();
            }

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        mDialog = AppHelper.buildAlertDialog(getString(R.string.loading), getString(R.string.homepageimmed) ,false, this);
        mDialog.show();
        // Get credentials and check if sucsess
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        mDialog.dismiss();
                        checkLoginSuccess(task, true);
                    }
                });
    }


    @Override
    protected void onStop() {
        super.onStop();

    }

    public void connect(View v){

        String email = emailEditText.getText().toString();
        String psswd = passwordEditText.getText().toString();
        progressDialog = ProgressDialog.show(Login_Activity.this,getString(R.string.pleasewait),getString(R.string.pageonload),true);

        if(email.equals("")|| psswd.equals("")){
//            Toast.makeText(this, "ERRRORRRRRR", Toast.LENGTH_LONG).show();

            mDialog=AppHelper.buildAlertDialog(getString(R.string.connectionprob) , getString(R.string.pleasefillalldata), true , Login_Activity.this);
            mDialog.show();
            progressDialog.dismiss();


        }else{
            mAuth.signInWithEmailAndPassword(email, psswd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

//                mDialog.dismiss();
                    checkLoginSuccess(task, false);

                }
            });

        }








    }

    private void checkLoginSuccess(Task<AuthResult> task, boolean googleSign) {

        if(task.isSuccessful()){
            loginSucced = true;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isregister",loginSucced);
            editor.commit();
            registerUser();
        }else{
            //if the task didnt sucsses show up an error
            mDialog=AppHelper.buildAlertDialog(getString(R.string.unexisteemailadress) , getString(R.string.failedtoauto), true , Login_Activity.this);
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
        Toast.makeText(this, R.string.connectionfaildd, Toast.LENGTH_LONG).show();
    }



    public void googleSignIn(View v){
        signIn();

    }

    public void forgotPass(View v){
        Intent i = new Intent(this,ResetPasswordActivity.class);
        startActivity(i);

    }

    public void registerUser(){
        SharedPreferences preferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);
        Boolean isregis  = preferences.getBoolean("isregister",false);
        if (isregis == true){
            Toast toast1 = Toast.makeText(this,"Toast:Gravity.TOP",Toast.LENGTH_SHORT);
            toast1.setGravity(Gravity.CENTER,0,0);
            toast1.setView(viewLayout);
            toast1.show();



            Intent i = new Intent(this,Home_Page_Activity.class);
            startActivity(i);

        }
    }


    @Override
    public void onBackPressed() {

    }




}
