package com.example.lindved.prayerbook.Activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import com.example.lindved.prayerbook.R;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    private Button btnGetAllPrayers;
    private Button btnCreateNewPrayer;
    private LoginButton btnLogin;
    private TextView txtLoginInfo;

    private CallbackManager mCallbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This is for generating the keyHash code programmatically.
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.example.lindved.prayerbook",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }

//        FacebookSdk.setApplicationId(String.valueOf(R.string.facebook_app_id));
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);

        initializeLogin();

        initializeButtons();
        setOnClickListeners();

    }

    private void initializeLogin(){

        btnLogin = findViewById(R.id.btnLogin);
        txtLoginInfo = findViewById(R.id.txtLoginInfo);

        btnLogin.setReadPermissions("email", "public_profile", "user_friends");

        btnLogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>(){

            @Override
            public void onSuccess(LoginResult loginResult) {
                txtLoginInfo.setText("User ID: " + loginResult.getAccessToken().getUserId()
                + "\n" + "Auth Token: " + loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                txtLoginInfo.setText("Login attempt cancelled.");
            }

            @Override
            public void onError(FacebookException error) {
                txtLoginInfo.setText("Login attempt failed.");
                Log.e("LOGIN", error.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void initializeButtons() {
        btnGetAllPrayers = findViewById(R.id.btnGetAllPrayers);
        btnCreateNewPrayer = (Button) findViewById(R.id.btnCreateNewPrayer);
    }

    private void setOnClickListeners(){
        btnGetAllPrayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPrayersActivity();
            }
        });
        btnCreateNewPrayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCreateNewPrayer();
            }
        });
    }

    private void goToCreateNewPrayer() {
        Intent intent = new Intent(this, CreateNewPrayerActivity.class);
        startActivity(intent);
    }

    private void goToPrayersActivity(){
        Intent intent = new Intent(this, PrayersActivity.class);
        startActivity(intent);
    }
}
