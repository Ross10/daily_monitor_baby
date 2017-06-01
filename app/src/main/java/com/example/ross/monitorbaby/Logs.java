package com.example.ross.monitorbaby;

import android.content.Intent;

import java.util.UUID;

/**
 * Created by Ross on 5/26/2017.
 */

public class Logs {
    private String dateAndTime;
    private String logName;
    private String amount;
    private int kind; //1 for pee , 2 for poo.



    public Logs(String dateAndTime,String amount){
        this.dateAndTime = dateAndTime;
        this.amount = amount;
    }

    public Logs(String dateAndTime, int kind){
        this.dateAndTime = dateAndTime;
        this.kind = kind;
    }

    public Logs(){

    }



    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }


    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }


    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }



    public String getLogId() {
        return UUID.randomUUID().toString();
    }
}
