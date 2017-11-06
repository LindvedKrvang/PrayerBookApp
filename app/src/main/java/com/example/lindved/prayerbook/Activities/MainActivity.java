package com.example.lindved.prayerbook.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.lindved.prayerbook.R;

public class MainActivity extends AppCompatActivity {

    Button btnGetAllPrayers;
    Button btnCreateNewPrayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeButtons();
        setOnClickListeners();
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
