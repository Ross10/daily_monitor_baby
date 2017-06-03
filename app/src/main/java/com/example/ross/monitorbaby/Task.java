package com.example.ross.monitorbaby;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.UUID;

/**
 * Created by Ross on 3/4/2017.
 */

// tasks management
public class Task {
    private String taskName;
    private int priorty;
    private String date;
    private String Id;


    private DatabaseReference dbRef;
    private String uniqueChatId;


    public Task(){

    }

    public Task(String taskName,int priorty,String date){
        this.taskName = taskName;
        this.priorty = priorty;
        this.date = date;
        this.Id = UUID.randomUUID().toString();

    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }


    //    public String getIdretu() { return UUID.randomUUID().toString();}



    public void setTaskName(String taskName){
        this.taskName = taskName;
    }

    public String getTaskName(){
        return this.taskName;
    }

    public void setPriorty(int priorty) {
        this.priorty = priorty;
    }

    public int getPriorty() {
        return priorty;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // Not including in FireBase database

    @Exclude
    public void setDatabaseReference(DatabaseReference databaseReference)
    {
        this.dbRef = databaseReference;
    }
    @Exclude
    public DatabaseReference getDatabaseReference()
    {
        return this.dbRef;
    }

}
