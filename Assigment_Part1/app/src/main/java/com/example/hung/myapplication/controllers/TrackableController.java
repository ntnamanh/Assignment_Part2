package com.example.hung.myapplication.controllers;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hung.myapplication.Notification.AlertReceiver;
import com.example.hung.myapplication.Notification.NotificationReceiver;
import com.example.hung.myapplication.R;
import com.example.hung.myapplication.database.DataSource;
import com.example.hung.myapplication.models.DurationAndDistance;
import com.example.hung.myapplication.models.TrackableService;
import com.example.hung.myapplication.models.TrackingPlan;
import java.util.List;

/**
 * @author Nguyen My
 */

public class TrackableController implements View.OnClickListener,AdapterView.OnItemClickListener,Spinner.OnItemSelectedListener {
    private DurationAndDistance durationAndDistancel;
    private TrackingPlan trackingPlan;
    private List<DurationAndDistance.duAndDisInfo> duAndDisInfoList;
    private ListView trackablelist;
    private Spinner dropDownList;
    private Dialog dialog;
    private Context context;
    private Activity activity;
    private int timeRemind;
    private DataSource dataSource;

    public TrackableController(){

    }

    public TrackableController(Context context , Activity activity) {
        this.context = context;
        this.activity = activity;
        this.trackingPlan = TrackingPlan.getSingletonInstance(context);
        this.durationAndDistancel = DurationAndDistance.getSingletonInstance(context);
        this.dataSource = new DataSource(context);
        this.trackablelist = (ListView)this.activity.findViewById(R.id.listviewtrackable);
        this.dialog = new Dialog(context);
        this.dropDownList = (Spinner)activity.findViewById(R.id.category_spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item,dataSource.getCategoryOfTrackable());
        this.dropDownList.setAdapter(arrayAdapter);
        this.duAndDisInfoList = this.durationAndDistancel.getDuAndDisInfoListBy(dropDownList.getSelectedItem().toString());
        startAlarm();
        startNotification();
        CancelNotification(context,1);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSuggest){
            if(this.duAndDisInfoList.isEmpty()){
                this.duAndDisInfoList = this.durationAndDistancel.getDuAndDisInfoListBy(dropDownList.getSelectedItem().toString());
                if(this.duAndDisInfoList.isEmpty()){
                    Toast.makeText(context, "Distance matrix api key is expired!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Functions.dialogAlert(context,this.duAndDisInfoList);
                }
            }
            else {
                Functions.dialogAlert(context,this.duAndDisInfoList);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TrackableService.TrackableInfo trackableInfo = dataSource.retreiveTrackabkeByName(parent.getItemAtPosition(position).toString());
            Functions.dialogTrackableDetail(this.context,this.activity,dialog,trackableInfo);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String category = parent.getItemAtPosition(position).toString();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this.context, android.R.layout.simple_list_item_1, dataSource.retreiveTrackableInfoByCategory(category));
        trackablelist.setAdapter(arrayAdapter);
        this.duAndDisInfoList = this.durationAndDistancel.getDuAndDisInfoListBy(dropDownList.getSelectedItem().toString());

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void startAlarm(){

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,NotificationReceiver.class);
        intent.putExtra("category",dropDownList.getSelectedItem().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,1,intent,0);
        if(alarmManager !=null){
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime() + 30000,30000,pendingIntent);
        }
    }

    private void startNotification(){

//        Boolean remind = activity.getIntent().getBooleanExtra("remind",false);
//        if(remind) {
//            Log.d("dm", "startNotification: ");
//            String planId = activity.getIntent().getStringExtra("planInfoId");
//            TrackingPlan.planInformation planInformation = dataSource.getTrackingPlanById(planId);
//            PopupRemindTime(planInformation);
//        } else {
            remindAlarm(5);
       // }
    }

    private void remindAlarm(int timeRemind) {
        AlarmManager alarmManager2 = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,1,intent,0);
        if (alarmManager2 != null) {
            alarmManager2.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime() + (timeRemind*60*1000),(timeRemind*60*1000),pendingIntent);
        }
    }

    private void PopupRemindTime(final TrackingPlan.planInformation planInformation) {

        this.dialog.setContentView(R.layout.remind_config_dialog);
        final EditText editRemindTime = this.dialog.findViewById(R.id.editRemindTime);
        Button set = this.dialog.findViewById(R.id.btnSetRemindTime);
        Button cancel = this.dialog.findViewById(R.id.btnCancelRemind);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editRemindTime.getText().toString().isEmpty()){
                    Toast.makeText(context, "Remind time can not be blank", Toast.LENGTH_SHORT).show();
                }
                else{
                    trackingPlan.addToPlanRemind(planInformation);
                    remindAlarm(Integer.parseInt(editRemindTime.getText().toString()));
                    dialog.dismiss();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackingPlan.clearRemind();
                remindAlarm(1000);
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    public static void CancelNotification(Context ctx, int notifyId) {
        String  s = Context.NOTIFICATION_SERVICE;
        NotificationManager mNM = (NotificationManager) ctx.getSystemService(s);
        mNM.cancel(notifyId);
    }
}
