package com.example.lindved.prayerbook.Entities;

import java.io.Serializable;

/**
 * Created by Lindved on 05-11-2017.
 */

public class Prayer implements Serializable {

    private int mId;
    private String mSubject;

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
}
