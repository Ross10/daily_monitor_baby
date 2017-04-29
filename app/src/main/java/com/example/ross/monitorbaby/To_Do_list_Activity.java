package com.example.ross.monitorbaby;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;
import static android.R.id.progress;

public class To_Do_list_Activity extends AppCompatActivity {
    private TaskDbHelper db;
    private List<Task> list;
    private MyAdapter adapt, adapt2;
    private ListView listTask;
    private String [] mngtask;
    private Context appContext;
    private Task newTask;
    private int editChooseFlag,deleteChooseFlag,position,priority;
    private String taskName;
    private ImageButton deleteImg,editImg;
    private List<Task> taskList;
    private ArrayList<Task> taskisList;
    private FirebaseAuth mAuth; //Returns an instance of this class corresponding to the default FirebaseApp instance when using getiInstance().
    private DatabaseReference mDatabaseReference;
    private ListingAdapter adapterson;
    private EditText toDoListEditText;
    Handler mainHandler = new Handler();
    private ImageView priorityImg;
    private ChildEventListener mChildEventListener;
    private ValueEventListener mValueEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to__do_list_);

//        Task t1 = new Task("new Task2",0);
//        FirebaseUser userr = FirebaseAuth.getInstance().getCurrentUser();
//        if(userr!=null){
//            String name = userr.getDisplayName();
//            String email = userr.getEmail();
//            if(email.equals("rrrr@rrr.com")){
//                Toast.makeText(this, "hii", Toast.LENGTH_SHORT).show();
//                mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userr.getUid());
//                mDatabaseReference.child("Task").child(t1.getIdretu()).setValue(t1);
//
//            }
//        }




        // connect with layout
//        deleteImg = (ImageButton)findViewById(R.id.deleteImg);
//        editImg = (ImageButton)findViewById(R.id.editImg);
//        toDoListEditText = (EditText) findViewById(R.id.toDoListEditText);
        db = new TaskDbHelper(this);
        list = db.getAllTasks();
        priorityImg = (ImageView)findViewById(R.id.priView);
        adapt = new MyAdapter(this, R.layout.list_inner_view, list);
        listTask = (ListView) findViewById(R.id.listView1);
        listTask.setAdapter(adapt);
        Log.d("the todo is :  hi","hi");
        mngtask =  new String[2];
        mngtask[0]= "EDIT";
        mngtask[1]= "DELETE";
        appContext = To_Do_list_Activity.this;

        editChooseFlag = deleteChooseFlag = 0;
        readAllDataFromFB();
        taskisList = new ArrayList<Task>();
//        deleteImg.setVisibility(View.INVISIBLE);
//        editImg.setVisibility(View.INVISIBLE);
//        itai = new Task("hello world",0);

        listTask.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                position = pos;
                // TODO Auto-generated method stub

////
//                deleteImg.setVisibility(View.VISIBLE);
//                editImg.setVisibility(View.VISIBLE);


                return true;
            }
        });

//

//        mValueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//               String taskName = dataSnapshot.getValue().toString();
//                adapt.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
//        mDatabaseReference.addValueEventListener(mValueEventListener);


  //      FirebaseUser userr = FirebaseAuth.getInstance().getCurrentUser();
//        if(userr!=null) {
//                mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");
//
//        }
//
//

    }

    //here we will read all the data from the FB - to the adapters every enterce to ToDolist - and that to save space on the Phone DB + to get the mission per user.

        public void readAllDataFromFB(){
            FirebaseUser userr = FirebaseAuth.getInstance().getCurrentUser();
            mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userr.getUid());
            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for(DataSnapshot ds : snapshot.child("Task").getChildren()) {
                        Task todol = ds.getValue(Task.class);
                        adapt.add(todol);

                    }
                    adapt.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
        }





    public void addTaskNow(View v) {

        Intent i = new Intent(this, TaskEditActivity.class);
        startActivityForResult(i, 1);


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                taskName = data.getStringExtra("taskName");
                priority = data.getIntExtra("priority",0);


                Task t1 = new Task(taskName,priority);
                FirebaseUser userr = FirebaseAuth.getInstance().getCurrentUser();
                if(userr!=null){
                    String name = userr.getDisplayName();
                    String email = userr.getEmail();
//                    if(email.equals("rrrr@rrr.com")){
                        //Toast.makeText(this, "hii", Toast.LENGTH_SHORT).show();
                        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userr.getUid());
                        mDatabaseReference.child("Task").child(t1.getIdretu()).setValue(t1);






                mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if(dataSnapshot.exists()){
                        System.out.println("The " + dataSnapshot.getKey() + " score is " + dataSnapshot.getValue());
                        fetchData(dataSnapshot);
//                        Task todo =  dataSnapshot.getValue(Task.class);
//                    adapt.notifyDataSetChanged();
                        Log.d("the todo is : ", "jj");
                    }


                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                    Task todo2 = dataSnapshot.getValue(Task.class);

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            mDatabaseReference.addChildEventListener(mChildEventListener);

//                    }
                }





            }
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
            ImageView iv = (ImageView)convertView.findViewById(R.id.priView);
            switch (current.getPriorty()){
                case 0:
                    iv.setImageDrawable(getDrawable(R.drawable.high_pri));
                    break;
                case 1:
                    iv.setImageDrawable(getDrawable(R.drawable.medium_pri));

                    break;
                case 2:
                    iv.setImageDrawable(getDrawable(R.drawable.low_pri));

                    break;
            }

            chk.setText(current.getTaskName());
            chk.setTag(current);

            return convertView;
        }





    }


    private void fetchData(DataSnapshot datasnapshot){
        taskisList.clear();
        for(DataSnapshot ds : datasnapshot.getChildren()){
            Task todol = ds.getValue(Task.class);
            taskisList.add(todol);


        }
       // System.out.println("TaskisList in 0 place is : " + taskisList.get(0) + " and in the first place is : " + taskisList.get(1));
//        adapt.add(taskisList.get(0));
//        adapt.notifyDataSetChanged();

//        adapt.add(taskisList.get(taskisList.size()-1));
//        adapt.notifyDataSetChanged();
        adapterson = new ListingAdapter(getApplicationContext(),taskisList);
        adapterson.notifyDataSetChanged();

    }





    public void doneButton(){

    }


}
