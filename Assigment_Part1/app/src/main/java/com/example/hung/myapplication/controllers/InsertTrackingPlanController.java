package com.example.hung.myapplication.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.hung.myapplication.database.DataSource;
import com.example.hung.myapplication.gpsHandller.gpsHandler;
import com.example.hung.myapplication.models.TrackingPlan;
import com.example.hung.myapplication.R;
import com.example.hung.myapplication.models.TrackingService;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * @author Nguyen My
 */


public class InsertTrackingPlanController implements View.OnClickListener {
    private Context context;
    private DataSource dataSource;
    private TrackingService.TrackingInfo trackingInfo;
    private TextView timePlan,targetStart,targetEnd,txtMeetLoca,txtCurrentLoca;
    private EditText title;
    private Activity activity;

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public InsertTrackingPlanController(Context context, Activity activity) {

        this.context = context;
        this.activity = activity;
        this.dataSource = new DataSource(context);
        this.dataSource.open();
        gpsHandler gt = new gpsHandler(context);
        Location location = gt.getLocation();
        if( location == null){
            Toast.makeText(context,"GPS unable to get Value",Toast.LENGTH_SHORT).show();
        }
        int index = activity.getIntent().getIntExtra("trackingId", -1);
        this.trackingInfo = dataSource.getTrackingInfoById(index);
        this.timePlan = activity.findViewById(R.id.txttimeplan);
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.MEDIUM);
        this.targetStart = activity.findViewById(R.id.txttargetstart);
        this.targetStart.setText(dateFormat.format(trackingInfo.date));
        this.targetEnd = activity.findViewById(R.id.txttargetend);
        this.targetEnd.setText(Functions.calculateTimeLeave(trackingInfo));
        this.txtCurrentLoca = activity.findViewById(R.id.txtcurrentlocation);
        this.txtCurrentLoca.setText(String.format("%.6f",location.getLatitude())+" , " + String.format("%.6f",location.getLongitude()));
        this.txtMeetLoca = activity.findViewById(R.id.txtmeetlocation);
        this.txtMeetLoca.setText(trackingInfo.latitude+" , "+ trackingInfo.longitude);
        this.title =activity.findViewById(R.id.edittitle);
        CancelNotification(context,1);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.txttimeplan){

            Calendar currentT = Calendar.getInstance();
            int hour = currentT.get(Calendar.HOUR);
            int minute = currentT.get(Calendar.MINUTE);

            TimePickerDialog.OnTimeSetListener timeDataset = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    Functions.showTime(hourOfDay, minute, timePlan);
                }
            };

            TimePickerDialog timePickerDialog = new TimePickerDialog(context, timeDataset, hour, minute, false);
            Objects.requireNonNull(timePickerDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            timePickerDialog.show();
        }

        if(v.getId()==R.id.btnadd_plan){
            if(TextUtils.isEmpty(this.title.getText())){
                Toast.makeText(context, "The title field is empty!!!", Toast.LENGTH_SHORT).show();
                this.title.requestFocus();

            }

            if(timePlan.getText().equals("Click Me!!!")){
                Toast.makeText(context, "Please choose meet time!!!", Toast.LENGTH_SHORT).show();
                this.title.requestFocus();
            }

            try {

                DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.MEDIUM);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(DateFormat.getDateInstance(DateFormat.SHORT).format(trackingInfo.date)).append(" ").append(timePlan.getText().toString());
                Date targetStartT = dateFormat.parse(targetStart.getText().toString());
                Date targetEndT = dateFormat.parse(targetEnd.getText().toString());
                Date meetTime = dateFormat.parse(stringBuilder.toString());

                if(Functions.checkTimeInRange(targetStart.getText().toString(),targetEnd.getText().toString(), stringBuilder.toString())){
                    //trackingPlan.add(currentLoca,txtMeetLoca.getText().toString() );
                    TrackingPlan.planInformation planInformation = new TrackingPlan.planInformation();
                    planInformation.setPlanId();
                    planInformation.setTitle(title.getText().toString());
                    planInformation.setTrackableId(trackingInfo.trackableId);
                    planInformation.setTargetStr(targetStartT);
                    planInformation.setTargetEnd(targetEndT);
                    planInformation.setMeetTime(meetTime);
                    planInformation.setCurrentLoca(txtCurrentLoca.getText().toString());
                    planInformation.setMeetLoca(txtMeetLoca.getText().toString());
                    dataSource.addTracking(planInformation);
                    dataSource.close();
                    Toast.makeText(context, "Add successfull", Toast.LENGTH_SHORT).show();
                    activity.finish();
                }
                else{
                    Toast.makeText(context, "Your meet time should be in range of target start time and target end time !!!", Toast.LENGTH_SHORT).show();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }
    public static void CancelNotification(Context ctx, int notifyId) {
        String  s = Context.NOTIFICATION_SERVICE;
        NotificationManager mNM = (NotificationManager) ctx.getSystemService(s);
        mNM.cancel(notifyId);
    }
}
