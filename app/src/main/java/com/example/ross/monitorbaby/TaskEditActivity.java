package com.example.ross.monitorbaby;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class TaskEditActivity extends AppCompatActivity {

    private String taskName;
    private int idx;
    private EditText nameOfTask;
    private RadioButton rbH, rbM,rbL;
    private RadioGroup rbg;
    private ImageButton done;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);
        nameOfTask = (EditText)findViewById(R.id.newMission);
        rbH = (RadioButton)findViewById(R.id.highRb);
        rbM = (RadioButton)findViewById(R.id.mediumRb);
        rbL = (RadioButton)findViewById(R.id.lowRb);
        done = (ImageButton)findViewById(R.id.done);
        rbg = (RadioGroup)findViewById(R.id.radioGroup);

    }


    public void doneWithAddingTask(View v){

        //get the radio button that perssed : 0 for high - 1 for medium - 2 for low.
        int radioButtonID = rbg.getCheckedRadioButtonId();
        View radioButton = rbg.findViewById(radioButtonID);

        idx = rbg.indexOfChild(radioButton);
        taskName = nameOfTask.getText().toString();

        Intent intent = new Intent();
        intent.putExtra("taskName", taskName);
        intent.putExtra("priority", idx);
        setResult(RESULT_OK, intent);
        finish();







    }







}
