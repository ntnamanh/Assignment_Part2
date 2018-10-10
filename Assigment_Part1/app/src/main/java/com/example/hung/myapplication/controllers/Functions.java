package com.example.hung.myapplication.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.hung.myapplication.database.DataSource;
import com.example.hung.myapplication.models.DurationAndDistance;
import com.example.hung.myapplication.models.TrackingPlan;
import com.example.hung.myapplication.models.TrackingService;
import com.example.hung.myapplication.views.PlanView;
import com.example.hung.myapplication.views.TrackingMapView;
import com.example.hung.myapplication.views.EventsView;
import com.example.hung.myapplication.models.TrackableService;
import com.example.hung.myapplication.R;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

/**
 * @author Nguyen My
 */

public class Functions  {

    @SuppressLint("SetTextI18n")
    public static void dialogTrackableDetail(final Context context, final Activity activity, final Dialog dialog, final TrackableService.TrackableInfo trackableInfo){
        dialog.setContentView(R.layout.dialog_trackable_detail);

        ImageView close = (ImageView)dialog.findViewById(R.id.image);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView Name = (TextView)dialog.findViewById(R.id.txtName);

        TextView Descip = (TextView)dialog.findViewById(R.id.txtDescription);

        TextView Url = (TextView)dialog.findViewById(R.id.txtUrl);

        TextView Cate = (TextView)dialog.findViewById(R.id.txtCategory);

        Button tracking = (Button)dialog.findViewById(R.id.btntrackinglist);

        Button make_tracking = (Button)dialog.findViewById(R.id.btnlocation);

        tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,EventsView.class);
                intent.putExtra("TrackableID",trackableInfo.id);
                context.startActivity(intent);

            }
        });

        make_tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,TrackingMapView.class);
                intent.putExtra("TrackableID",trackableInfo.id);
                intent.putExtra("TrackableName",trackableInfo.name);
                context.startActivity(intent);

            }
        });

        Name.setText(trackableInfo.name);

        Descip.setText("Description: "+trackableInfo.description);

        Url.setText("URL: "+trackableInfo.url);

        Cate.setText("Category: "+trackableInfo.category);

        dialog.show();
    }

    //show dialog delete update confirm
    public static void popUpUpdateDelete(final Context context, final Dialog dialog, final Activity activity, final String index){
        final DataSource dataSource = new DataSource(context);
        dataSource.open();
        dialog.setContentView(R.layout.dialog_del_edit_event_layout);
        ImageView cancel = (ImageView)dialog.findViewById(R.id.imagecance2);
        Button Delete = (Button)dialog.findViewById(R.id.btndelete_plan);
        Button Update = (Button)dialog.findViewById(R.id.btnupdate);
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSource.deleteTracking(index);
                Toast.makeText(context, "The plan is deleted!!!", Toast.LENGTH_SHORT).show();
                dataSource.close();
                activity.finish();
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(context, PlanView.class);
            intent.putExtra("status","edit");
            intent.putExtra("trackingId",index);
            context.startActivity(intent);
            dataSource.close();
            activity.finish();
            dialog.dismiss();
            }
        });
        dialog.show();
    }


    public static void dialogAlert(final Context context, final List<DurationAndDistance.duAndDisInfo> duAndDisInfoList) {

        final DataSource dataSource = new DataSource(context);
        dataSource.open();
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.alert_dialog);
        TextView txtTrackableName = dialog.findViewById(R.id.txtTruckName);
        txtTrackableName.setText(dataSource.getTrackableNameById(duAndDisInfoList.get(0).getTrackingInfo().trackableId));

        TextView txtEndTime = dialog.findViewById(R.id.txtTimeEnd);
        txtEndTime.setText(calculateTimeLeave(duAndDisInfoList.get(0).getTrackingInfo()));

        TextView txtWalkLenght = dialog.findViewById(R.id.txtWalkingLenght);
        txtWalkLenght.setText("Distance: " + String.valueOf(duAndDisInfoList.get(0).getDistance())+" km");

        TextView txtWalkTime = dialog.findViewById(R.id.txtwalkingTime);
        txtWalkTime.setText("Duration: " + String.valueOf(duAndDisInfoList.get(0).getDuration())+" minutes");

        ImageView cancel = dialog.findViewById(R.id.imageCancelAlert) ;
        Button addTracking = dialog.findViewById(R.id.btnAddAlert);
        Button skip = dialog.findViewById(R.id.btnDismiss);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duAndDisInfoList.remove(0);
                dialog.dismiss();
            }
        });

        addTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int trackingId = dataSource.getTrackingIdByObject(duAndDisInfoList.get(0).getTrackingInfo());
                Intent intent = new Intent(context, PlanView.class);
                intent.putExtra("trackingId",trackingId);
                intent.putExtra("status","insert");
                context.startActivity(intent);
                duAndDisInfoList.remove(0);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static String calculateTimeLeave(TrackingService.TrackingInfo trackingInfo) {

        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.MEDIUM);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(trackingInfo.date);
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + trackingInfo.stopTime);
        return dateFormat.format(calendar.getTime());
    }

    public static long current5MinutesAfter() {

        DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 1);
        return calendar.getTimeInMillis();
    }

    //This function will return the number of stop time in minutes.
    public static long monitorFoodTruck(Date date, int stoptime){

        if(stoptime <=0) {
            return 0;
        } else {
            //get the current time
            Calendar currentime = Calendar.getInstance();
            String date_move  = DateFormat.getDateInstance().format(date);
            String currentdate = DateFormat.getDateInstance().format(currentime.getTime());
            //check the current date and the selected date. if the use choose the date which is today the program will return the number of minutes
            if(currentdate.equals(date_move)){
                //check the current time is in range of start time and end time.
                if(System.currentTimeMillis()>= date.getTime() && System.currentTimeMillis()<= (date.getTime()+(stoptime*60000))){
                    return (((System.currentTimeMillis())-date.getTime())/60000);
                }
            }
        }
        return 0;
    }
    public static boolean check_availabledate(Date date){
        long number;
        //get the current time
        number = date.getTime() -System.currentTimeMillis();
        return number > 0;
    }

    //check date
    public static boolean check_date(String date){
        Calendar currentime = Calendar.getInstance();
        String currentdate = DateFormat.getDateInstance().format(currentime.getTime());
        return currentdate.equals(date);
    }

    @SuppressLint("DefaultLocale")
    public static void showTime(int hour, int min, TextView time) {
        String format;
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }
        time.setText(new StringBuilder().append(String.format("%02d",hour)).append(":").append(String.format("%02d",min)).append(":").append("00")
                .append(" ").append(format));
    }

    public static String generate_random_String() {
        Random generator = new Random();
        StringBuilder StringBuilder = new StringBuilder();
        char tempChar;
        for (int i = 0; i < 5; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            StringBuilder.append(tempChar);
        }
        return StringBuilder.toString();
    }
    public static boolean checkTimeInRange(String targetStart,String targetEnd,String meettime) throws ParseException {
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.MEDIUM);
        Date targetStartTime = dateFormat.parse(targetStart);
        Date meet_time = dateFormat.parse(meettime);
        Date targetEndTime = dateFormat.parse(targetEnd);
        return meet_time.getTime() >= targetStartTime.getTime() && meet_time.getTime() <= targetEndTime.getTime();
    }

    public static boolean checkWalkingTime(DurationAndDistance.duAndDisInfo duAndDisInfo) {
        Date date = duAndDisInfo.getTrackingInfo().getDate();
        int  stoptime = duAndDisInfo.getTrackingInfo().stopTime;
        long timeOfEnd = date.getTime() + (stoptime*60*1000);
        long timeWalk = System.currentTimeMillis() + (duAndDisInfo.getDuration()*60*1000);
        return timeWalk <= timeOfEnd;
    }
}


