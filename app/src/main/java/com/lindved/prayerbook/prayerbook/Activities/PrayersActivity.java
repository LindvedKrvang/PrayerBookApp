package com.lindved.prayerbook.prayerbook.Activities;

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
import android.widget.Toast;

import com.lindved.prayerbook.prayerbook.Adapters.PrayerAdapter;
import com.lindved.prayerbook.prayerbook.Entities.Prayer;
import com.lindved.prayerbook.prayerbook.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PrayersActivity extends AppCompatActivity {

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
        lstPrayers = findViewById(R.id.lstPrayers);
        lstPrayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Prayer prayer = (Prayer) parent.getItemAtPosition(position);
                goToSinglePrayerActivity(prayer);
            }
        });

        btnGetPrayers = findViewById(R.id.btnGetPrayers);
        btnGetPrayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPrayers();
            }
        });
        btnBack = findViewById(R.id.btnPrayersBack);
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
            displayToast(getString(R.string.getting_prayers));

            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder().url(getString(R.string.prayerURL)).build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String jsonData = response.body().string();
                    if(response.isSuccessful()){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    displayPrayers(jsonData);
                                    displayToast(getString(R.string.get_prayers_successfull));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    displayToast(getString(R.string.get_prayers_failed));
                                }
                            }
                        });
                    }
                }
            });
        }else{
            displayToast(getString(R.string.no_network_available));
        }
    }

    private void displayPrayers(String jsonData) throws JSONException {
        JSONArray prayers = new JSONArray(jsonData);

        adapter = new PrayerAdapter(this, prayers);
        lstPrayers.setAdapter(adapter);
    }

    private void alertUserAboutError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                displayToast(getString(R.string.get_prayers_failed));
            }
        });
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        return false;
    }

    private void displayToast(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
