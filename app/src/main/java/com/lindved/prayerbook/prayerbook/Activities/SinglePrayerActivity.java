package com.lindved.prayerbook.prayerbook.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lindved.prayerbook.prayerbook.Adapters.ResponseAdapter;
import com.lindved.prayerbook.prayerbook.Entities.Prayer;
import com.lindved.prayerbook.prayerbook.R;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.lindved.prayerbook.prayerbook.Activities.CreateNewPrayerActivity.JSON;

public class SinglePrayerActivity extends AppCompatActivity {

    private Prayer prayer;
    private String mUserId;

    private ListView lstResponses;
    private Button btnBack;
    private Button btnAnswer;
    private TextView txtAnswerHeadline;
    private TextView txtSubject;
    private ImageView imgTrashcan;
    private ProfilePictureView picProfile;

    private ResponseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_prayer);
        initialize();

        mUserId = getIntent().getExtras().getString(getString(R.string.user_id));

        setPrayer();
    }

    private void setPrayer() {
        int id = (int) getIntent().getSerializableExtra("ID");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(getString(R.string.prayerURL) + "/" + id).build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //TODO RKL: Alert user about failure.
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                Log.v("TEST", "This is the single prayer: " + jsonData);
                try {
                    JSONObject object = new JSONObject(jsonData);
                    prayer = new Prayer();
                    prayer.setId(object.getInt(getString(R.string.json_id)));
                    prayer.setSubject(object.getString(getString(R.string.json_subject)));
                    prayer.addResponse(object.getString("responses"));
                    prayer.setUserId(object.getString(getString(R.string.JSON_user_id)));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.v("TEST", prayer.toString());
                            txtSubject.setText(prayer.getSubject());
                            picProfile.setProfileId(prayer.getUserId());
                            picProfile.setVisibility(View.VISIBLE);
                            populateResponseList();
                            setAnswerHeadline();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void setAnswerHeadline() {
        if(!prayer.getResponses().isEmpty()){
            txtAnswerHeadline.setText(R.string.answers);
        }else{
            txtAnswerHeadline.setText(R.string.no_responses);
        }
    }

    private void populateResponseList() {
        adapter = new ResponseAdapter(this, prayer.getResponses());
        lstResponses.setAdapter(adapter);
    }

    private void initialize() {
        btnBack = findViewById(R.id.btnSinglePrayerBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAnswer = findViewById(R.id.btnSinglePrayerAnswer);
        btnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerDialog();
            }
        });
        
        imgTrashcan = findViewById(R.id.imgSinglePrayerDelete);
        imgTrashcan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warnDeleteDialog();
            }
        });

        txtAnswerHeadline = findViewById(R.id.txtSinglePrayerAnswerHeader);

        txtSubject = findViewById(R.id.txtSinglePrayerSubject);
        txtSubject.setMovementMethod(new ScrollingMovementMethod());

        lstResponses = findViewById(R.id.lstSinglePrayerAnswers);

        picProfile = findViewById(R.id.picProfile);
        picProfile.setVisibility(View.GONE);
    }

    private void warnDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("The prayer will be deleted and can't be retrieved.").setTitle("Delete prayer");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
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

    private void answerDialog(){
        //TODO RKL: Refactor this method into smaller methods.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View view = inflater.inflate(R.layout.response_dialog,null);

        @SuppressLint("WrongViewCast") final EditText responseText = view.findViewById(R.id.txtRDResponse);

        builder.setView(view).setPositiveButton("Answer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(responseText.getText().toString().isEmpty()){
                    //TODO RKL: Make no answer entered message.
                }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            OkHttpClient client = new OkHttpClient();
                            JSONObject response = new JSONObject();
                            try {
                                response.put(getString(R.string.JSON_prayer_id), prayer.getId());
                                response.put(getString(R.string.JSON_answer), responseText.getText());
                                response.put(getString(R.string.JSON_user_id), mUserId);
                            } catch (JSONException e) {
                                Log.v("TEST", "Create JSON exception");
                                e.printStackTrace();
                            }
                            RequestBody body = RequestBody.create(JSON, response.toString());
                            Log.v("TEST", "RequestBody created");

                            Request request = new Request.Builder().url(getString(R.string.responseURL)).post(body).build();

                            try {
                                Response response1 = client.newCall(request).execute();
                                Log.v("TEST", "Request done. Got the response");
                                //TODO RKL: Clean up this mess.
                                JSONObject object = new JSONObject(response1.body().string());
                                prayer.addResponse(object);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //TODO RKL: Find a better to solution than repopulate entire list everytime.
                                        populateResponseList();
                                    }
                                });

                            } catch (IOException e) {
                                //TODO RKL: Exception handling
                                e.printStackTrace();
                            } catch (JSONException e) {
                                //TODO RKL: Exception handling.
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                //TODO RKL: Find a way to not close the dialog if an error occur.
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
            }
        });

        Dialog dialog = builder.create();

        dialog.show();
    }
}
