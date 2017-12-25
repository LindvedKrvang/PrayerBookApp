package com.lindved.prayerbook.prayerbook.Entities;

/**
 * Created by Lindved on 25-11-2017.
 */

public class Response {
    private int mId;
    private String mAnswer;
    private int mPrayerId;
    private String mUserId;

    public int getId(){
        return mId;
    }

    public void setId(int id){
        mId = id;
    }

    public String getAnswer(){
        return mAnswer;
    }

    public void setAnswer(String answer){
        mAnswer = answer;
    }

    public int getPrayerId(){
        return  mPrayerId;
    }

    public void setPrayerId(int prayerId){
        mPrayerId = prayerId;
    }

    public void setUserId(String userId){
        mUserId = userId;
    }

    public String getUserId(){
        return mUserId;
    }
}
