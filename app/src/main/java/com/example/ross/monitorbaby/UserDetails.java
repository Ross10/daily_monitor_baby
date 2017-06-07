package com.example.ross.monitorbaby;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.UUID;

/**
 * Created by Ross on 6/6/2017.
 */

public class UserDetails {
    private DatabaseReference dbRef;
    private String Id;
    private int grt;

    public UserDetails(int grt){
        this.Id = UUID.randomUUID().toString();
        this.grt = grt;


    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public int getGrt() {
        return grt;
    }

    public void setGrt(int grt) {
        this.grt = grt;
    }

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
