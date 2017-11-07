package com.example.lindved.prayerbook.Activities;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lindved.prayerbook.R;

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
//    private static final String prayerURL = "http://prayerbook.azurewebsites.net/api/prayers";

    private Button btnCreatePrayer;
    private EditText txtSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_prayer);

        Log.v("TEST", getString(R.string.prayerURL));

        initialize();
        setListeners();
    }

    private void setListeners() {
        btnCreatePrayer.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        createPrayer();
                    }
                }).start();
            }
        });
    }

    private String getSubject(){
        //TODO RKL: Make validating so there always are content in the subject.
        String subject = txtSubject.getText().toString();
        return subject;
    }

    private void createPrayer() {
        OkHttpClient client = new OkHttpClient();

        JSONObject prayer = new JSONObject();
        try {
            prayer.put("subject", getSubject());
        } catch (JSONException e) {
            Log.v("TEST", "Create JSON exception");
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, prayer.toString());
        Log.v("TEST", "RequestBody created");

        Request request = new Request.Builder().url(getString(R.string.prayerURL)).post(body).build();

        try {
            Response response = client.newCall(request).execute();
            Log.v("TEST", "Request done. Got the response");
            Log.v("TEST", response.body().string());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtSubject.getText().clear();
                    showCreateCompleteMessage();
                }
            });
        } catch (IOException e) {
            Log.v("TEST", "Exception while doing request");
            e.printStackTrace();
        }
    }

    private void showCreateCompleteMessage() {
        Toast toast = Toast.makeText(this, "Successfully created prayer!", Toast.LENGTH_SHORT);
        toast.show();
    }

    private void initialize() {
        btnCreatePrayer = (Button) findViewById(R.id.btnNewPrayerCreate);
        txtSubject = (EditText) findViewById(R.id.txtNewPrayerSubject);
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
