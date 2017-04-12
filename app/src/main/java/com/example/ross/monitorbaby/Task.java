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

    private DatabaseReference dbRef;
    private String uniqueChatId;


    public Task(){

    }

    public Task(String taskName,int priorty){
        this.taskName = taskName;
        this.priorty = priorty;

    }


    public String getIdretu() { return UUID.randomUUID().toString();}



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
