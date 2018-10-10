package com.example.hung.myapplication.models;

import android.content.ContentValues;
import android.content.Context;
import com.example.hung.myapplication.controllers.Functions;
import com.example.hung.myapplication.database.PlanTable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrackingPlan {

    private static final String LOG_TAG = TrackingPlan.class.getName();
    private List<TrackingPlan.planInformation> list_plan = new ArrayList<>();
    private TrackingPlan.planInformation trackingRemind = null;
    private static Context context;
    private TrackingPlan(){

    }
    public static class planInformation{

        public String planId;
        public int trackableId;
        public String title;
        public Date targetStr;
        public Date targetEnd;
        public Date meetTime;
        public String currentLoca;
        public String meetLoca;

        public planInformation() {

        }

        public planInformation(String planId, int trackableId, String title, Date targetStr, Date targetEnd, Date meetTime, String currentLoca, String meetLoca) {
            this.planId = planId;
            this.trackableId = trackableId;
            this.title = title;
            this.targetStr = targetStr;
            this.targetEnd = targetEnd;
            this.meetTime = meetTime;
            this.currentLoca = currentLoca;
            this.meetLoca = meetLoca;
        }

        public String getPlanId() {
            return planId;
        }

        public void setPlanId() {
            this.planId = Functions.generate_random_String();
        }

        public int getTrackableId() {
            return trackableId;
        }

        public void setTrackableId(int trackableId) {
            this.trackableId = trackableId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Date getTargetStr() {
            return targetStr;
        }

        public void setTargetStr(Date targetStr) {
            this.targetStr = targetStr;
        }

        public Date getTargetEnd() {
            return targetEnd;
        }

        public void setTargetEnd(Date targetEnd) {
            this.targetEnd = targetEnd;
        }

        public Date getMeetTime() {
            return meetTime;
        }

        public void setMeetTime(Date meetTime) {
            this.meetTime = meetTime;
        }

        public String getCurrentLoca() {
            return currentLoca;
        }

        public void setCurrentLoca(String currentLoca) {
            this.currentLoca = currentLoca;
        }

        public String getMeetLoca() {
            return meetLoca;
        }

        public void setMeetLoca(String meetLoca) {
            this.meetLoca = meetLoca;
        }

        public ContentValues toValues() {
            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.MEDIUM);
            ContentValues values = new ContentValues();
            values.put(PlanTable.COLUMN_ID,planId);
            values.put(PlanTable.COLUMN_TRACKABLE_ID,trackableId);
            values.put(PlanTable.COLUMN_TITLE,title);
            values.put(PlanTable.COLUMN_TARGET_ST,dateFormat.format(targetStr));
            values.put(PlanTable.COLUMN_TARGET_END,dateFormat.format(targetEnd));
            values.put(PlanTable.COLUMN_MEET_TIME,dateFormat.format(meetTime));
            values.put(PlanTable.COLUMN_CURRENT_LOCATION,currentLoca);
            values.put(PlanTable.COLUMN_MEET_LOCATION,meetLoca);
            return values;
        }
    }


    private static class LazyHolder
    {
        static final TrackingPlan INSTANCE = new TrackingPlan();
    }

    public static TrackingPlan getSingletonInstance(Context context)
    {
        TrackingPlan.context = context;
        return TrackingPlan.LazyHolder.INSTANCE;
    }

    public List<planInformation> getListPlan() {
        return list_plan;
    }

    public void addToPlanRemind(TrackingPlan.planInformation planInformation) {
        this.trackingRemind = planInformation;
    }

    public void clearRemind () {
        this.trackingRemind = null;
    }

    public TrackingPlan.planInformation getTrackingRemind() {
        return trackingRemind;
    }
}
