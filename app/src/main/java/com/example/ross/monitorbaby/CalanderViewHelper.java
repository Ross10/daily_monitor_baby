package com.example.ross.monitorbaby;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.telephony.SmsManager;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CalanderViewHelper extends AppCompatActivity {
    private static final String SMS_SENT_INTENT_FILTER = "com.yourapp.sms_send";
    private static final String SMS_DELIVERED_INTENT_FILTER = "com.yourapp.sms_delivered";
    private TextView theDate;
    private Button btnGoToCalander;
    private FirebaseAuth mAuth; //Returns an instance of this class corresponding to the default FirebaseApp instance when using getiInstance().
    private DatabaseReference mDatabaseReference;
    private FirebaseUser userr;
    private List<CalanderTask> tasklistNew;
    private ListView listTask;
    private int[] dates;
    private int pos;
    private String date,phonenum,taskId;
    private CalanderTasksListAdatper adapterFour;
    private View viewLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calander_view_helper);
        userr = FirebaseAuth.getInstance().getCurrentUser();
        theDate = (TextView)findViewById(R.id.date);
        btnGoToCalander = (Button)findViewById(R.id.backTocalander);
        tasklistNew = new ArrayList<>();
        adapterFour = new CalanderTasksListAdatper(this,tasklistNew);
        listTask = (ListView) findViewById(R.id.clanderViewListView);
        LayoutInflater layoutInflater = getLayoutInflater();
        viewLayout =layoutInflater.inflate(R.layout.smssent, (ViewGroup) findViewById(R.id.greenV2));
        Intent incomingIntent = getIntent();
        date = incomingIntent.getStringExtra("date");

        theDate.setText(date);


        readData();

        listTask.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                view.setSelected(true);
                Context wrapper = new ContextThemeWrapper(getApplicationContext(), R.style.popupMenuStyle);
                PopupMenu popupMenu = new PopupMenu(wrapper,view);
                popupMenu.getMenuInflater().inflate(R.menu.manufortasks,popupMenu.getMenu());
                mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userr.getUid()).child("userDetail");
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);

                        if(user!=null){
                            phonenum = user.getPhoneNumber();
                        }


                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });


                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.item1){
                            deletethis();
                        }
                        else if(item.getItemId()==R.id.item2){
//                            editthisone();
                        }
                        else{
                            ActivityCompat.requestPermissions(CalanderViewHelper.this,new String[]{android.Manifest.permission.SEND_SMS},1);
                            sendSms();
                            //sms sender
                        }
//                        Toast.makeText(getApplication(),"Item Clicked: "+item.getTitle() + "item id is : " + item,Toast.LENGTH_SHORT).show();
                        return true;
                    } });
                popupMenu.show();




                return false;
            }
        });


        btnGoToCalander.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalanderViewHelper.this,ClanaderviewActiviry.class);
                startActivity(intent);
            }
        });



    }

    public void readData() {
        dates = CalendarEvent.getDate(); // function in Calendar event
        FirebaseUser userr = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userr.getUid());
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.child("Calander").getChildren()) {
                    CalanderTask task1 = ds.getValue(CalanderTask.class);
                    if (task1.getDate().equals(date)){
                        tasklistNew.add(task1);

                    }

                }

                //Firebase is asynchronous. So thats why we need to set or notify the adapter from within the callback.
                adapterFour = new CalanderTasksListAdatper(CalanderViewHelper.this,tasklistNew);
                listTask.setAdapter(adapterFour);


                adapterFour.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    private void sendSms() // send SMS when phone in radius
    {


        String message = "משימה : "+ tasklistNew.get(pos).getTaskName();
        Boolean sendSuccses = false;

        String phnNo =  phonenum ;//preferable use complete international number
        if(phnNo == ""){
            phnNo = "0587000097";
        }

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
                SMS_SENT_INTENT_FILTER), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(
                SMS_DELIVERED_INTENT_FILTER), 0);


        while(!sendSuccses){
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phnNo, null, message, sentPI, deliveredPI);
                sendSuccses = true;
                Toast toast1 = Toast.makeText(this,"Toast:Gravity.TOP",Toast.LENGTH_SHORT);
                toast1.setGravity(Gravity.CENTER,0,0);
                toast1.setView(viewLayout);
                toast1.show();
            } catch (Exception ex) {
//                Toast.makeText(getApplicationContext(),ex.getMessage(),
//                        Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }

        }


    }


    public void deletethis(){
        if(tasklistNew.size()>0){

            taskId = tasklistNew.get(pos).getId();
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("המשימה תימחק")
                    .setMessage("האם אתה בטוח שברצונך למחוק את המשימה?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            tasklistNew.remove(pos);

                            adapterFour.notifyDataSetChanged();

                            mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userr.getUid());
                            mDatabaseReference.child("Calander").child(taskId).removeValue();
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



    }

}