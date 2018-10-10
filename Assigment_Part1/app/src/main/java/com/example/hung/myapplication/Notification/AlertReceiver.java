package com.example.hung.myapplication.Notification;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.hung.myapplication.R;
import com.example.hung.myapplication.controllers.TrackableController;
import com.example.hung.myapplication.database.DataSource;
import com.example.hung.myapplication.models.TrackingPlan;
import com.example.hung.myapplication.views.PlanView;
import com.example.hung.myapplication.views.TrackableView;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.provider.Settings.System.getString;
import static com.example.hung.myapplication.Notification.App.CHANNEL_1_TD;

/**
 * @author Nguyen My
 */

public class AlertReceiver extends BroadcastReceiver {
    private DataSource dataSource;
    private NotificationManagerCompat notificationManagerCompat;
    private TrackingPlan trackingPlan;
    @Override
    public void onReceive(Context context, Intent intent) {
        List<TrackingPlan.planInformation> planInformationList = new ArrayList<>();
        dataSource = new DataSource(context);
        trackingPlan = TrackingPlan.getSingletonInstance(context);

        notificationManagerCompat = NotificationManagerCompat.from(context);
        planInformationList = dataSource.retrieveCurrentDateTracking();

        if(trackingPlan.getTrackingRemind()==null){
            if(!planInformationList.isEmpty()) {
                for (TrackingPlan.planInformation planInformation: planInformationList){
                    if((planInformation.meetTime.getTime() - System.currentTimeMillis())<=300000 &&(planInformation.meetTime.getTime() - System.currentTimeMillis()) >0 ){
                        notificationManagerCompat.notify(1,showNotification(context,planInformation));
                    }
                }
            }
        }
        else{
            TrackingPlan.planInformation planInformation = trackingPlan.getTrackingRemind();
            notificationManagerCompat.notify(1,showNotification(context,planInformation));
            trackingPlan.clearRemind();
        }
    }

    private Notification showNotification(Context context, TrackingPlan.planInformation planInformation) {
        DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        Intent broadcastRemind = new Intent(context,TrackableView.class);
        broadcastRemind.putExtra("remind",true);
        broadcastRemind.putExtra("planInfoId",planInformation.planId);
        PendingIntent actionIntentRemind = PendingIntent.getActivity(context,0,broadcastRemind,PendingIntent.FLAG_UPDATE_CURRENT);
        Intent activityIntent = new Intent(context,TrackableView.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,0,activityIntent,0);
        Notification notification = new NotificationCompat.Builder(context,CHANNEL_1_TD)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle("Tracking Alert")
                .setContentText("Your tracking at :"+ dateFormat.format(planInformation.meetTime))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.BLUE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .addAction(R.mipmap.ic_launcher,"Remind",actionIntentRemind)
                .build();
        return notification;
    }


}
