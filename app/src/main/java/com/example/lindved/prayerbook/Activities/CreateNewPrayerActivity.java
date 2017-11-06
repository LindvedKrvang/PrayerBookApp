package com.example.lindved.prayerbook.Activities;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.lindved.prayerbook.R;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateNewPrayerActivity extends AppCompatActivity {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String prayerURL = "http://prayerbook.azurewebsites.net/api/prayers";

    private Button btnCreatePrayer;
    private EditText txtSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_prayer);

        initialize();
        setListeners();
    }

    private void setListeners() {
        btnCreatePrayer.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                try {
                    createPrayer();
                    Log.v("TEST", "No exceptions thrown while creating prayer");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String getSubject(){
        String subject = txtSubject.getText().toString();
        String json =  "{\"subject\": \"" + subject + "\"}";
        return json;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createPrayer() throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, getSubject());
        Log.v("TEST", body.contentType().toString());
        Request request = new Request.Builder().url(prayerURL)
                .post(body).build();
        Log.v("TEST", request.body().toString() + " " + request.headers().toString());
//        Response response = client.newCall(request).execute();
//            Log.v("TEST", response.body().toString());
//
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
