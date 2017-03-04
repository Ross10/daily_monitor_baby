package com.example.ross.monitorbaby;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

public class To_Do_list_Activity extends AppCompatActivity {
    private  TaskDbHelper db;
    private List<Task> list;
    private MyAdapter adapt, adapt2;
    private ListView listTask;
    private String [] mngtask;
    private Context appContext;
    private Task newTask;
    private int editChooseFlag,deleteChooseFlag,position;
    private ImageButton deleteImg,editImg;
    private List<Task> taskList;
    private EditText toDoListEditText;
    Handler mainHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to__do_list_);

        // connect with layout
        deleteImg = (ImageButton)findViewById(R.id.deleteImg);
        editImg = (ImageButton)findViewById(R.id.editImg);
        toDoListEditText = (EditText) findViewById(R.id.toDoListEditText);
        db = new TaskDbHelper(this);
        list = db.getAllTasks();
        adapt = new MyAdapter(this, R.layout.list_inner_view, list);
        listTask = (ListView) findViewById(R.id.listView1);
        listTask.setAdapter(adapt);
        mngtask =  new String[2];
        mngtask[0]= "EDIT";
        mngtask[1]= "DELETE";
        appContext = To_Do_list_Activity.this;
        editChooseFlag = deleteChooseFlag = 0;
        deleteImg.setVisibility(View.INVISIBLE);
        editImg.setVisibility(View.INVISIBLE);
//        itai = new Task("hello world",0);
        listTask.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                position = pos;
                // TODO Auto-generated method stub

//
                deleteImg.setVisibility(View.VISIBLE);
                editImg.setVisibility(View.VISIBLE);


                return true;
            }
        });



    }







    public void addTaskNow(View v) {

        if (toDoListEditText.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, R.string.DidntEnterNewTask,
                    Toast.LENGTH_LONG).show();
        } else {
            Task task = new Task(toDoListEditText.getText().toString());
            db.addTask(task);
            toDoListEditText.setText("");
            adapt.add(task);
            adapt.notifyDataSetChanged();
        }
    }

    // array adapter to connect the data
    public class MyAdapter extends ArrayAdapter<Task> {
        private Context context;
        private List<Task> taskList = new ArrayList<Task>();
        int layoutResourceId;
        public MyAdapter(Context context, int layoutResourceId,
                         List<Task> objects) {
            super(context, layoutResourceId, objects);
            this.layoutResourceId = layoutResourceId;
            this.taskList = objects;
            this.context = context;
        }
        /**
         * This method will define what the view inside the list view will
         * finally look like Here we are going to code that the checkbox state
         * is the status of task and check box text is the task name
         */


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView chk = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_inner_view,
                        parent, false);
                chk = (TextView) convertView.findViewById(R.id.textView2);
                convertView.setTag(chk);

            } else {
                chk = (TextView) convertView.getTag();
            }
            Task current = taskList.get(position);
            chk.setText(current.getTaskName());
            chk.setTag(current);
            Log.d("listener", String.valueOf(current.getId()));
            return convertView;
        }



    }


    public void onDelete(View v){
        db.deleteTask(list.get(position));
        adapt.remove(list.get(position));
        adapt.notifyDataSetChanged();
        deleteImg.setVisibility(View.INVISIBLE);
        editImg.setVisibility(View.INVISIBLE);

    }
    public void onEdit(View v){


// Set up the input

        callBuilder();
        adapt.notifyDataSetChanged();
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text


// Set up the buttons

        adapt.notifyDataSetChanged();

        adapt.notifyDataSetChanged();


    }


    public void callBuilder(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.ChangeTask);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                adapt.notifyDataSetChanged();

                newTask = new Task(input.getText().toString());
//                db.updateTask(list.get(which));
                adapt.notifyDataSetChanged();

                db.updateTask(newTask,list.get(position).getId());

                adapt.notifyDataSetChanged();


                To_Do_list_Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapt.notifyDataSetChanged();
                    }
                });



                adapt.notifyDataSetChanged();
                adapt = new MyAdapter(getApplicationContext(), 0, list);
                adapt.notifyDataSetChanged();

                adapt2 = new MyAdapter(getApplicationContext(), R.layout.list_inner_view, list);
                listTask.setAdapter(adapt2);

                adapt2.notifyDataSetChanged();





            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


}
