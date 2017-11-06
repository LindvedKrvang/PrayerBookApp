package com.example.lindved.prayerbook.Entities;

/**
 * Created by Lindved on 05-11-2017.
 */

public class Prayer {

    private int id;
    private String subject;

    public int getId(){
        return id;
    }

    public String getSubject(){
        return subject;
    }

    public void setSubject(String subject){
        this.subject = subject;
    }
}
