package com.example.lindved.prayerbook.Activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lindved.prayerbook.Entities.Prayer;
import com.example.lindved.prayerbook.R;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SinglePrayerActivity extends AppCompatActivity {

    private Prayer prayer;

    private Button btnBack;
    private TextView txtSubject;
    private ImageView imgTrashcan;

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
        
        imgTrashcan = findViewById(R.id.imgSinglePrayerDelete);
        imgTrashcan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warnDeleteDialog();
            }
        });

        txtSubject = findViewById(R.id.txtSinglePrayerSubject);
        txtSubject.setMovementMethod(new ScrollingMovementMethod());
    }

    private void warnDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("The prayer will be deleted and can't be retrieved.").setTitle("Delete prayer");
        builder.setPositiveButton("Delete?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        deletePrayer();
                    }
                }).start();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deletePrayer() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(getString(R.string.prayerURL) + "/" + prayer.getId()).delete().build();

        try {
            Response response = client.newCall(request).execute();
            Log.v("TEST", "Request done. Got response");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showDeleteSuccesfull();
                    finish();
                }
            });
        } catch (IOException e) {
            Log.v("TEST", "Exception while doing request");
            e.printStackTrace();
        }
    }

    private void showDeleteSuccesfull(){
        Toast toast = Toast.makeText(this, "Successfully deleted prayer", Toast.LENGTH_SHORT);
        toast.show();
    }
}
