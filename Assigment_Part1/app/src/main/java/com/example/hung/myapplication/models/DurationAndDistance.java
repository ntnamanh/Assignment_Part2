package com.example.hung.myapplication.models;

import android.content.Context;
import android.support.annotation.NonNull;
import com.example.hung.myapplication.controllers.Functions;
import com.example.hung.myapplication.database.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Nguyen My
 */

public class DurationAndDistance {
    private static final String LOG_TAG = TrackingService.class.getName();
    private List<duAndDisInfo> duAndDisInfoList = new ArrayList<>();
    private List<duAndDisInfo> currentList = new ArrayList<>();
    private static Context context;

    public DurationAndDistance() {

    }

    public class duAndDisInfo implements Comparable{
        private TrackingService.TrackingInfo trackingInfo;
        private int duration;
        private double distance;

        public duAndDisInfo() {

        }

        public duAndDisInfo(TrackingService.TrackingInfo trackingInfo, String duration, String distance) {
            this.trackingInfo = trackingInfo;
            String[] du = duration.split(" ");
            this.duration = Integer.parseInt(du[0]);
            String[] di = distance.split(" ");
            this.distance = Double.parseDouble(di[0]);
        }

        public TrackingService.TrackingInfo getTrackingInfo() {
            return trackingInfo;
        }

        public void setTrackingInfo(TrackingService.TrackingInfo trackingInfo) {
            this.trackingInfo = trackingInfo;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            String[] du = duration.split(" ");
            this.duration = Integer.parseInt(du[0]);
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            String[] di = distance.split(" ");
            this.distance = Double.parseDouble(di[0]);
        }



        @Override
        public int compareTo(@NonNull Object o) {
            return  (Double.compare(this.getDistance(), ((duAndDisInfo) o).getDistance()));
        }
    }

    private static class LazyHolder
    {
        static final DurationAndDistance INSTANCE = new DurationAndDistance();
    }

    public static DurationAndDistance getSingletonInstance(Context context)
    {
        DurationAndDistance.context = context;
        return DurationAndDistance.LazyHolder.INSTANCE;
    }

    public void addToDurationList(TrackingService.TrackingInfo trackingInfo,String duration,String distance){
        duAndDisInfo duAndDisInfo = new duAndDisInfo(trackingInfo,duration,distance);
        if(Functions.checkWalkingTime(duAndDisInfo)){
            duAndDisInfoList.add(duAndDisInfo);
        }
        Collections.sort(duAndDisInfoList);
        this.currentList = new ArrayList<>(duAndDisInfoList);
    }

    public List<duAndDisInfo> getDuAndDisInfoList() {
        return duAndDisInfoList;
    }

    public void clearDurationList() {
        this.duAndDisInfoList = new ArrayList<>();
    }

    public List<duAndDisInfo> getDuAndDisInfoListBy(String category) {
        DataSource dataSource = new DataSource(context);
        List<Integer> trackablIdList =  dataSource.getTrackableIdByCategory(category);
        List<duAndDisInfo> durationAndDistance = new ArrayList<>();
        if(category.equals("All")){
            this.currentList = new ArrayList<>(duAndDisInfoList);
            return this.currentList;
        }
        for (Integer i: trackablIdList){
            for (duAndDisInfo duAndDisInfo: duAndDisInfoList ) {
                if(duAndDisInfo.getTrackingInfo().getTrackableId() == i){
                    this.currentList.add(duAndDisInfo);
                }
            }
        }
        return this.currentList;
    }

    public List<duAndDisInfo> getCurrentList() {
        return this.currentList;
    }

    public void clearAll() {
        this.duAndDisInfoList = new ArrayList<>();
        this.currentList = new ArrayList<>();
    }





}
