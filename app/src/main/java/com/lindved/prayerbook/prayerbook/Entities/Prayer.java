package com.lindved.prayerbook.prayerbook.Entities;

import android.util.Log;

import com.lindved.prayerbook.prayerbook.R;

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
    private String mUserId;

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

    public void setUserId(String userId){
        mUserId = userId;
    }

    public String getUserId(){
        return mUserId;
    }

    public void addResponse(String responsesString){
        Log.v("TEST", "Using the JSONArray overload");
        try {
            JSONArray array = new JSONArray(responsesString);
            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                //TODO RKL: Refactor this to be used by both overloads.
                Response response = new Response();
                response.setId(object.getInt("id"));
                response.setAnswer(object.getString("answer"));
                response.setPrayerId(object.getInt("prayerId"));
                response.setUserId(object.getString("userId"));
                mResponses.add(response);
            }
        } catch (JSONException e) {
            Log.e(String.valueOf(R.string.response), String.valueOf(R.string.error_convert_JSON_Response_in_Prayer));
            e.printStackTrace();
        }
    }

    public void addResponse(JSONObject object){
        Log.v("TEST", "Using the JSONObject overload");
        Response response = new Response();
        try {
            //TODO RKL: Refactor this to be used by both overloads.
            response.setId(object.getInt("id"));
            response.setAnswer(object.getString("answer"));
            response.setPrayerId(object.getInt("prayerId"));
            response.setUserId(object.getString("userId"));
            mResponses.add(response);
        } catch (JSONException e) {
            Log.e(String.valueOf(R.string.response), String.valueOf(R.string.error_convert_JSON_Response_in_Prayer));
            e.printStackTrace();
        }
    }

    public List<Response> getResponses(){
        return mResponses;
    }
}
