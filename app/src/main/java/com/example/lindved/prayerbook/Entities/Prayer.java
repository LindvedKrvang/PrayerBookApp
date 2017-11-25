package com.example.lindved.prayerbook.Entities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lindved on 05-11-2017.
 */

public class Prayer implements Serializable {

    private int mId;
    private String mSubject;
    private List<Response> mResponses;

    public Prayer(){
        mResponses = new ArrayList<>();
    }

    public int getId(){
        return mId;
    }

    public String getSubject(){
        return mSubject;
    }

    public void setId(int id){
        mId = id;
    }

    public void setSubject(String subject){
        mSubject = subject;
    }

    public void addResponse(String responsesString){
        try {
            JSONArray array = new JSONArray(responsesString);
            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                Response response = new Response();
                response.setId(object.getInt("id"));
                response.setAnswer(object.getString("answer"));
                response.setPrayerId(object.getInt("prayerId"));
                mResponses.add(response);
            }
        } catch (JSONException e) {
            Log.e("RESPONSE", "Failed to convert from JSON to Response object while adding response to PrayerResponses");
            e.printStackTrace();
        }
    }

    public List<Response> getResponses(){
        return mResponses;
    }
}
