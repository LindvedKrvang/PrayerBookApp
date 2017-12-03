package com.example.lindved.prayerbook.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;

import com.example.lindved.prayerbook.R;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    private TextView txtLoginMessage;
    private Button btnGetAllPrayers;
    private Button btnCreateNewPrayer;
    private LoginButton btnLogin;
    private ProfilePictureView profilePictureView;

    private CallbackManager mCallbackManager;

    private String mUserId;
    private String mAuthToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);

        initializeLogin();

        initializeButtons();
        setOnClickListeners();

        setProfilePictureFromLocalStorage();

        setOnAccesTokenTracker();
    }

    private void setOnAccesTokenTracker() {
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(currentAccessToken == null){
                    //Code for what will happen when the user logs out.
                    Toast.makeText(getBaseContext(), "You logged out!", Toast.LENGTH_LONG).show();
                    SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.user_id), getString(R.string.no_user_id));
                    editor.commit();
                    profilePictureView.setVisibility(View.GONE);
                    btnCreateNewPrayer.setVisibility(View.GONE);
                    btnGetAllPrayers.setVisibility(View.GONE);
                    txtLoginMessage.setVisibility(View.VISIBLE);
                }else{
                    btnCreateNewPrayer.setVisibility(View.VISIBLE);
                    btnGetAllPrayers.setVisibility(View.VISIBLE);
                    txtLoginMessage.setVisibility(View.GONE);
                }
            }
        };
    }

    private void setProfilePictureFromLocalStorage() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String defaultValue = getString(R.string.no_user_id);
        mUserId = sharedPref.getString(getString(R.string.user_id), defaultValue);
        if(!mUserId.equals(defaultValue)){
            profilePictureView.setProfileId(mUserId);
            btnCreateNewPrayer.setVisibility(View.VISIBLE);
            btnGetAllPrayers.setVisibility(View.VISIBLE);
            txtLoginMessage.setVisibility(View.GONE);
        }else{
            profilePictureView.setVisibility(View.GONE);
            btnCreateNewPrayer.setVisibility(View.GONE);
            btnGetAllPrayers.setVisibility(View.GONE);
            txtLoginMessage.setVisibility(View.VISIBLE);
        }
    }

    private void initializeLogin(){
        txtLoginMessage = findViewById(R.id.txtLoginMessage);
        btnLogin = findViewById(R.id.btnLogin);
        profilePictureView = findViewById(R.id.profilePic);

        btnLogin.setReadPermissions("email", "public_profile", "user_friends");



        btnLogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>(){

            @Override
            public void onSuccess(LoginResult loginResult) {
                mUserId = loginResult.getAccessToken().getUserId();
                mAuthToken = loginResult.getAccessToken().getToken();

                profilePictureView.setProfileId(mUserId);
                profilePictureView.setVisibility(View.VISIBLE);

                //Saving the userId to localStorage for retrieval later.
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.user_id), mUserId);
                editor.commit();
            }

            @Override
            public void onCancel() {
                Log.e("LOGIN", "Login cancelled...");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("LOGIN", error.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void initializeButtons() {
        btnGetAllPrayers = findViewById(R.id.btnGetAllPrayers);
        btnCreateNewPrayer = findViewById(R.id.btnCreateNewPrayer);
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
        intent.putExtra(getString(R.string.user_id), mUserId);
        startActivity(intent);
    }

    private void goToPrayersActivity(){
        Intent intent = new Intent(this, PrayersActivity.class);
        startActivity(intent);
    }
}
