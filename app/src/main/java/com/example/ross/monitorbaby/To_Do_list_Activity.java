package com.example.ross.monitorbaby;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.R.id.list;
import static android.R.id.progress;

public class To_Do_list_Activity extends AppCompatActivity{
    private TaskDbHelper db;
    private List<Task> list;
    //    private MyAdapter adapt, adapt2;
    private ListView listTask;
    private TaskslistAdapter adapterFour;
    private String [] mngtask;
    private Context appContext;
    private Task newTask;
    private int editChooseFlag,deleteChooseFlag,position,priority,saiedYes,taskProirty;
    private String taskName,taskId,taskDate,m_Text;
    private ImageButton deleteImg,editImg;
    private List<Task> taskList;
    private ArrayList<Task> taskisList;
    private List<Task> tasklistNew;
    private FirebaseAuth mAuth; //Returns an instance of this class corresponding to the default FirebaseApp instance when using getiInstance().
    private DatabaseReference mDatabaseReference;
    private ListingAdapter adapterson;
    private EditText toDoListEditText;
    Handler mainHandler = new Handler();
    private ImageView priorityImg;
    private ImageButton deleteTaskImg, editTaskImg;
    private ChildEventListener mChildEventListener;
    private ValueEventListener mValueEventListener;
    FirebaseUser userr;

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
        userr = FirebaseAuth.getInstance().getCurrentUser();
        editTaskImg = (ImageButton)findViewById(R.id.editTask) ;
        deleteTaskImg = (ImageButton)findViewById(R.id.deleteTask) ;

        db = new TaskDbHelper(this);
        list = db.getAllTasks();
        tasklistNew = new ArrayList<>();
        priorityImg = (ImageView)findViewById(R.id.priView);
//        adapt = new MyAdapter(this, R.layout.list_inner_view, list);
        listTask = (ListView) findViewById(R.id.listView1);
//        listTask.setAdapter(adapt);
        Log.d("the todo is :  hi","hi");
        mngtask =  new String[2];
        mngtask[0]= "EDIT";
        mngtask[1]= "DELETE";
        appContext = To_Do_list_Activity.this;
        adapterFour = new TaskslistAdapter(this,tasklistNew);
        editChooseFlag = deleteChooseFlag = 0;
        readAllDataFromFB();
        taskisList = new ArrayList<Task>();
//        deleteImg.setVisibility(View.INVISIBLE);
//        editImg.setVisibility(View.INVISIBLE);
//        itai = new Task("hello world",0);

        deleteTaskImg.setVisibility(View.INVISIBLE);
        editTaskImg.setVisibility(View.INVISIBLE);

        listTask.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                position = pos;
                // TODO Auto-generated method stub

////
                deleteTaskImg.setVisibility(View.VISIBLE);
                editTaskImg.setVisibility(View.VISIBLE);




                return true;
            }
        });



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
                    tasklistNew.add(todol);

                }

                //Firebase is asynchronous. So thats why we need to set or notify the adapter from within the callback.
                adapterFour = new TaskslistAdapter(To_Do_list_Activity.this,tasklistNew);
                listTask.setAdapter(adapterFour);


                adapterFour.notifyDataSetChanged();
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

                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                Task t1 = new Task(taskName,priority,currentDateTimeString);
//                FirebaseUser userr = FirebaseAuth.getInstance().getCurrentUser();
                if(userr!=null){
                    String name = userr.getDisplayName();
                    String email = userr.getEmail();
//                    if(email.equals("rrrr@rrr.com")){
                    //Toast.makeText(this, "hii", Toast.LENGTH_SHORT).show();
                    mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userr.getUid());
                    mDatabaseReference.child("Task").child(t1.getId()).setValue(t1);





                    tasklistNew.add(t1);
                    listTask.setAdapter(adapterFour);
                    adapterFour.notifyDataSetChanged();






//
//                    mChildEventListener = new ChildEventListener() {
//                        @Override
//                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//                            if(dataSnapshot.exists()){
//                                System.out.println("The " + dataSnapshot.getKey() + " score is " + dataSnapshot.getValue());
//                                fetchData(dataSnapshot);
//////                        Task todo =  dataSnapshot.getValue(Task.class);
////                        Log.d("the todo is : ", "jj");
//
//                            }
//                            adapterFour.notifyDataSetChanged();
//
//                        }
//
//                        @Override
//                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
////                    Task todo2 = dataSnapshot.getValue(Task.class);
//
//                        }
//
//                        @Override
//                        public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                        }
//
//                        @Override
//                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    };
//
//                    mDatabaseReference.addChildEventListener(mChildEventListener);

//                    }
                }





            }
        }
    }


//    private void fetchData(DataSnapshot datasnapshot){
////        taskisList.clear();
//        for(DataSnapshot ds : datasnapshot.getChildren()){
//            Task todol = ds.getValue(Task.class);
////            taskisList.add(todol);
//            tasklistNew.add(todol);
//        }
//
////        ArrayAdapter<Task> listviewAdapter = new ArrayAdapter<Task>(
////                this,
////                android.R.layout.simple_dropdown_item_1line,
////                tasklistNew
////        );
//
//        listTask.setAdapter(adapterFour);
//      adapterFour.notifyDataSetChanged();
//
//
//
//    }





    public void editEvent(View v){
        if(tasklistNew.size()>0){
            taskId = tasklistNew.get(position).getId();
            taskDate = tasklistNew.get(position).getDate();
            taskProirty = tasklistNew.get(position).getPriorty();


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("שינוי משימה");

// Set up the input
            final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            builder.setView(input);

// Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    m_Text = input.getText().toString();
                    tasklistNew.remove(position);

                    mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userr.getUid());
                    mDatabaseReference.child("Task").child(taskId).removeValue();

                    Task t1 = new Task(m_Text,taskProirty,taskDate);
//                FirebaseUser userr = FirebaseAuth.getInstance().getCurrentUser();
                    if(userr!=null) {
                        String name = userr.getDisplayName();
                        String email = userr.getEmail();
//                    if(email.equals("rrrr@rrr.com")){
                        //Toast.makeText(this, "hii", Toast.LENGTH_SHORT).show();
                        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userr.getUid());
                        mDatabaseReference.child("Task").child(t1.getId()).setValue(t1);
                    }


                    tasklistNew.add(t1);
                    listTask.setAdapter(adapterFour);
                    adapterFour.notifyDataSetChanged();


                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            adapterFour.notifyDataSetChanged();
            builder.show();


        }

        adapterFour.notifyDataSetChanged();
        deleteTaskImg.setVisibility(View.INVISIBLE);
        editTaskImg.setVisibility(View.INVISIBLE);

    }

    public void deleteEvent(View v){
        if(tasklistNew.size()>0){

            taskId = tasklistNew.get(position).getId();
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("ההודעה תימחק")
                    .setMessage("האם אתה בטוח שברצונך למחוק את המשימה?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            tasklistNew.remove(position);

                            adapterFour.notifyDataSetChanged();

                            mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userr.getUid());
                            mDatabaseReference.child("Task").child(taskId).removeValue();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();


        }




        deleteTaskImg.setVisibility(View.INVISIBLE);
        editTaskImg.setVisibility(View.INVISIBLE);



    }



}