package com.example.hung.myapplication.Notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import com.example.hung.myapplication.R;
import com.example.hung.myapplication.database.DataSource;
import com.example.hung.myapplication.models.DurationAndDistance;
import com.example.hung.myapplication.views.PlanView;
import com.example.hung.myapplication.views.TrackableView;
import java.util.List;
import static com.example.hung.myapplication.Notification.App.CHANNEL_1_TD;

/**
 * @author Nguyen My
 */

public class NotificationReceiver extends BroadcastReceiver {
    private List<DurationAndDistance.duAndDisInfo> duAndDisInfoList;
    private DurationAndDistance.duAndDisInfo duAndDisInfo;
    private DataSource dataSource;

    @Override
    public void onReceive(Context context, Intent intent) {

        DurationAndDistance durationAndDistance = DurationAndDistance.getSingletonInstance(context);
        this.dataSource = new DataSource(context);
        dataSource.open();
        String category = intent.getStringExtra("category");
        this.duAndDisInfoList = durationAndDistance.getCurrentList();
        if(this.duAndDisInfoList.isEmpty()){
            this.duAndDisInfoList =  durationAndDistance.getDuAndDisInfoListBy(category);
        }
        else{
            this.duAndDisInfo = this.duAndDisInfoList.get(0);
            this.duAndDisInfoList.remove(0);
            String name = this.dataSource.getTrackableNameById(this.duAndDisInfo.getTrackingInfo().getTrackableId());
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            Notification notification = showNotification(context, name);
            notificationManagerCompat.notify(1, notification);
            dataSource.close();
        }
    }

    private Notification showNotification(Context context, String message) {
        int trackingId = dataSource.getTrackingIdByObject(this.duAndDisInfo.getTrackingInfo());
        Intent broadcastAdd = new Intent(context,PlanView.class);
        broadcastAdd.putExtra("status","insert");
        broadcastAdd.putExtra("trackingId", trackingId);
        PendingIntent actionIntentAdd = PendingIntent.getActivity(context,0,broadcastAdd,PendingIntent.FLAG_CANCEL_CURRENT);
        Intent activityIntent = new Intent(context,TrackableView.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,0,activityIntent,0);
        Notification notification = new NotificationCompat.Builder(context,CHANNEL_1_TD)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle("Trucking Suggestion: " + message)
                .setContentText("Do you want to add it to your tracking list ?")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.BLUE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .addAction(R.mipmap.ic_launcher,"Accept", actionIntentAdd)
                .build();
        return notification;
    }


}
