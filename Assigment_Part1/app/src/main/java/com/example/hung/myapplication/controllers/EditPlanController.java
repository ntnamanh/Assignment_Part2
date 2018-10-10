package com.example.hung.myapplication.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.hung.myapplication.database.DataSource;
import com.example.hung.myapplication.models.TrackingPlan;
import com.example.hung.myapplication.R;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * @author Nguyen My
 */


public class EditPlanController implements View.OnClickListener {
    private Context context;
    private Activity activity;
    private TrackingPlan.planInformation planInformation;
    private DataSource dataSource;
    private TextView txtTimePlan,txtTargetStartT,txtTargerEndT;
    private EditText title;
    private String date;

    public EditPlanController(Context context,  Activity activity) {

        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.MEDIUM);
        this.context = context;
        this.activity = activity;
        dataSource = new DataSource(context);
        dataSource.open();
        String trackingId = activity.getIntent().getStringExtra("trackingId");

        this.planInformation =  dataSource.getTrackingById(trackingId);

        this.title = (EditText)activity.findViewById(R.id.edittitle);
        this.title.setText(planInformation.title);

        TextView txtMeetLoca = (TextView) activity.findViewById(R.id.txtmeetlocation);
        txtMeetLoca.setText(planInformation.meetLoca);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        this.txtTimePlan = (TextView) activity.findViewById(R.id.txttimeplan);
        this.txtTimePlan.setText(simpleDateFormat.format(planInformation.meetTime));

        //this.dropDownList = (Spinner)activity.findViewById(R.id.spinnerlatlong);
        TextView txtcurrentLoca = (TextView)activity.findViewById(R.id.txtcurrentlocation);
        txtcurrentLoca.setText(planInformation.currentLoca);

        txtTargetStartT = (TextView) activity.findViewById(R.id.txttargetstart);
        txtTargetStartT.setText(dateFormat.format(this.planInformation.targetStr));

        txtTargerEndT = (TextView) activity.findViewById(R.id.txttargetend);
        txtTargerEndT.setText(dateFormat.format(this.planInformation.targetEnd));

        this.date = DateFormat.getDateInstance(DateFormat.SHORT).format(this.planInformation.targetEnd);

    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.txttimeplan){
            Calendar currentime = Calendar.getInstance();
            int hour = currentime.get(Calendar.HOUR);
            int minute = currentime.get(Calendar.MINUTE);
            TimePickerDialog.OnTimeSetListener timeDataset = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    Functions.showTime(hourOfDay, minute, txtTimePlan);
                }
            };
            TimePickerDialog timePickerDialog = new TimePickerDialog(context, timeDataset, hour, minute, false);
            Objects.requireNonNull(timePickerDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            timePickerDialog.show();
        }
        if(v.getId() == R.id.btnadd_plan){
            if(TextUtils.isEmpty(title.getText())) {
                Toast.makeText(context, "The title field is empty!!!", Toast.LENGTH_SHORT).show();
                title.requestFocus();
            }
            if(txtTimePlan.getText().equals("Click Me!!!")){
                Toast.makeText(context, "Please choose meet time!!!", Toast.LENGTH_SHORT).show();
                txtTimePlan.requestFocus();
            }

            try {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(date).append(" ").append(txtTimePlan.getText().toString());
                Date meetTime = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.MEDIUM).parse(stringBuilder.toString());

                if(Functions.checkTimeInRange(this.txtTargetStartT.getText().toString(),this.txtTargerEndT.getText().toString(),stringBuilder.toString())){
                    planInformation.setTitle(title.getText().toString());
                    planInformation.setMeetTime(meetTime);
                    dataSource.updateTrackingItem(planInformation);
                    activity.finish();
                    Toast.makeText(context, "Update successfull", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context, "Your meet time should be in range of target start time and target end time !!!", Toast.LENGTH_SHORT).show();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
