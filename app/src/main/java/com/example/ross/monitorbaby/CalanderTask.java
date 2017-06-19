package com.example.ross.monitorbaby;

import java.util.UUID;

/**
 * Created by Ross on 6/16/2017.
 */

public class CalanderTask {
    private String date;
    private String taskName;
    private int status;
    private String Id;




    public CalanderTask(){


    }

    public CalanderTask(String date, String taskName, int status){
        this.date = date;
        this.taskName = taskName;
        this.status = status;
        this.Id = UUID.randomUUID().toString();

    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
