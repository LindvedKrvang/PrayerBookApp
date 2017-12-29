package com.lindved.prayerbook.prayerbook.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lindved.prayerbook.prayerbook.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateNewPrayerActivity extends AppCompatActivity {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private Button btnCreatePrayer;
    private Button btnBack;
    private EditText txtSubject;

    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_prayer);

        mUserId = getIntent().getExtras().getString(getString(R.string.user_id));

        initialize();
        setListeners();
    }

    private void initialize() {
        btnCreatePrayer = findViewById(R.id.btnNewPrayerCreate);
        btnBack = findViewById(R.id.btnNewPrayerBack);
        txtSubject = findViewById(R.id.txtNewPrayerSubject);
    }

    private void setListeners() {
        btnCreatePrayer.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                handleCreatePrayerButton();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void handleCreatePrayerButton(){
        if(!isSubjectPresent()){
            displayToast(getString(R.string.enter_subject));
            return;
        }
        if(isNetworkAvailable()){
            displayToast(getString(R.string.creating_prayer));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    createPrayer();
                }
            }).start();
        }else{
            displayToast(getString(R.string.no_network_available));
        }
    }

    private boolean isSubjectPresent(){
        String subject = txtSubject.getText().toString();
        if(subject.isEmpty()){
            return false;
        }
        return true;
    }

    private String getSubject(){
        String subject = txtSubject.getText().toString();
        return subject;
    }

    private void createPrayer() {

            OkHttpClient client = new OkHttpClient();
            JSONObject prayer = new JSONObject();

            try {
                prayer.put(getString(R.string.json_subject), getSubject());
                prayer.put(getString(R.string.user_id), mUserId);
            } catch (JSONException e) {
                Log.e(getString(R.string.error_creating_prayer), getString(R.string.create_json_exception));
                e.printStackTrace();
            }

            RequestBody body = RequestBody.create(JSON, prayer.toString());
            Request request = new Request.Builder().url(getString(R.string.prayerURL)).post(body).build();

            try {
                Response response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtSubject.getText().clear();
                            displayToast(getString(R.string.create_prayer_success));
                        }
                    });
                }
            } catch (IOException e) {
                Log.e(getString(R.string.error_response), getString(R.string.request_exception));
                e.printStackTrace();
            }

    }

    private void displayToast(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
