package com.example.lindved.prayerbook.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.lindved.prayerbook.Adapters.PrayerAdapter;
import com.example.lindved.prayerbook.Entities.Prayer;
import com.example.lindved.prayerbook.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PrayersActivity extends AppCompatActivity {

//    private static String prayersURL = "http://prayerbook.azurewebsites.net/api/prayers";

    private ListView lstPrayers;
    private Button btnGetPrayers;
    private Button btnBack;
    private PrayerAdapter adapter;

    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayers);

        mUserId = getIntent().getExtras().getString(getString(R.string.user_id));

        initialize();
        getPrayers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPrayers();
    }

    private void initialize() {
        lstPrayers = (ListView) findViewById(R.id.lstPrayers);
        lstPrayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Prayer prayer = (Prayer) parent.getItemAtPosition(position);

                Log.v("TEST", prayer.getSubject());

                goToSinglePrayerActivity(prayer);
            }
        });

        btnGetPrayers = (Button) findViewById(R.id.btnGetPrayers);
        btnGetPrayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPrayers();
            }
        });
        btnBack = (Button) findViewById(R.id.btnPrayersBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void goToSinglePrayerActivity(Prayer prayer) {
        Intent intent = new Intent(this, SinglePrayerActivity.class);
        intent.putExtra(getString(R.string.ID), prayer.getId());
        intent.putExtra(getString(R.string.user_id), mUserId);
        startActivity(intent);
    }

    private void getPrayers(){
        if(isNetworkAvailable()){

            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder().url(getString(R.string.prayerURL)).build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    alertUserAboutError();
                    Log.v("TEST", "Response failed");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String jsonData = response.body().string();
                    Log.v("TEST", jsonData);
                    if(response.isSuccessful()){
                        Log.v("TEST", "Response is successful");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    displayPrayers(jsonData);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.v("TEST", "Failed to display prayers");
                                }
                            }
                        });
                    }
                }
            });
        }else{
            //TODO RKL: Show a message to the user explaining there is no internet.
            Log.v("TEST", "Network isn't available");
        }
    }

    private void displayPrayers(String jsonData) throws JSONException {
        JSONArray prayers = new JSONArray(jsonData);

        adapter = new PrayerAdapter(this, prayers);
        lstPrayers.setAdapter(adapter);
    }

    private void alertUserAboutError() {
        //TODO RKL: Create a message to the user about the error.
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
