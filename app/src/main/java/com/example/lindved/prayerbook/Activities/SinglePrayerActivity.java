package com.example.lindved.prayerbook.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lindved.prayerbook.Entities.Prayer;
import com.example.lindved.prayerbook.R;

public class SinglePrayerActivity extends AppCompatActivity {

    private Prayer prayer;

    private Button btnBack;
    private TextView txtSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_prayer);
        initialize();

        setPrayer();


    }

    private void setPrayer() {
        prayer = (Prayer) getIntent().getSerializableExtra("Prayer");
        txtSubject.setText(prayer.getSubject());
    }

    private void initialize() {
        btnBack = findViewById(R.id.btnSinglePrayerBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtSubject = findViewById(R.id.txtSinglePrayerSubject);
        txtSubject.setMovementMethod(new ScrollingMovementMethod());
    }
}
