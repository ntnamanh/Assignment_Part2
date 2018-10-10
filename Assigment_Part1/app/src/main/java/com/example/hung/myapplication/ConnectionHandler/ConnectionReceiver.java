package com.example.hung.myapplication.ConnectionHandler;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.example.hung.myapplication.database.DataSource;
import com.example.hung.myapplication.gpsHandller.gpsHandler;
import com.example.hung.myapplication.httphandler.GetJsonFile;
import com.example.hung.myapplication.models.DurationAndDistance;
import com.example.hung.myapplication.models.TrackingService;

import java.util.List;

/**
 * @author Nguyen My
 */


public class ConnectionReceiver extends BroadcastReceiver {
    private gpsHandler gpsHandler;
    private DataSource dataSource;
    private DurationAndDistance durationAndDistance;

    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context,"Network is turned on/of",Toast.LENGTH_SHORT).show();
        if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            boolean noConnectivity = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY,false
            );
            if(noConnectivity) {
                Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show();
                this.durationAndDistance.clearAll();
            }else {
                Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
                this.durationAndDistance = DurationAndDistance.getSingletonInstance(context);
                if(this.durationAndDistance.getDuAndDisInfoList().isEmpty()){
                    gpsHandler = new gpsHandler(context);
                    dataSource =  new DataSource(context);
                    dataSource.open();
                    Location location = gpsHandler.getLocation();
                    if( location == null){
                        Toast.makeText(context,"GPS unable to get Value",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        List<Integer> trackableIds = dataSource.getTrackableIdByCategory("All");

                        List<TrackingService.TrackingInfo> trackingInfoList = dataSource.getTrackingInfoFromToDay(trackableIds);
                        if(trackingInfoList.isEmpty()){
                            Toast.makeText(context, "There is no Event of Truck from now on", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            for (TrackingService.TrackingInfo trackingInfo: trackingInfoList) {
                                @SuppressLint("DefaultLocale") GetJsonFile getJsonFile = new GetJsonFile(trackingInfo,context, String.format( "%.6f",location.getLatitude()),String.format( "%.6f",location.getLongitude()));
                                getJsonFile.execute();
                            }
                        }
                    }
                }
            }
        }

    }
}
