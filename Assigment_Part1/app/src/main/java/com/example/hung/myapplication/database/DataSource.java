package com.example.hung.myapplication.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

import com.example.hung.myapplication.httphandler.GetJsonFile;
import com.example.hung.myapplication.models.TrackableService;
import com.example.hung.myapplication.models.TrackingPlan;
import com.example.hung.myapplication.models.TrackingService;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Nguyen My
 */

public class DataSource {
    private Context context;
    private SQLiteDatabase database;
    private SQLiteOpenHelper dbHelper;

    public DataSource(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(this.context);
        database = dbHelper.getWritableDatabase();

    }

    public void open(){
        database = dbHelper.getWritableDatabase();
    }
    public void close(){
        dbHelper.close();
    }

    public TrackableService.TrackableInfo createTrackableItem(TrackableService.TrackableInfo trackableInfo) {
        ContentValues trackableValues = trackableInfo.toValues();
        database.insert(TrackableTable.TABLE_TRACKABLES, null , trackableValues);
        return trackableInfo;
    }
    //seed data from file to database
    public void seedTrackableData(){
        long num = getTrackableItemCount();
        if(num == 0){
            TrackableService trackableService = TrackableService.getSingletonInstance(context);
            trackableService.logAll();
            for (TrackableService.TrackableInfo trackableInfo: trackableService.getTrackableList()) {
                try{
                    this.createTrackableItem(trackableInfo);
                }catch (SQLiteException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public TrackingService.TrackingInfo createTrackingItem(TrackingService.TrackingInfo trackingInfo){
        ContentValues trackingValues = trackingInfo.toValues();
        database.insert(TrackingTable.TABLE_TRACKING,null,trackingValues);
        return trackingInfo;
    }
    //seed data from file to database
    public void seedTrackingeData(){
        long num = getTrackingItemCount();
        if(num == 0){
            TrackingService trackingService = TrackingService.getSingletonInstance(context);
            trackingService.logAll();
            for (TrackingService.TrackingInfo trackingInfo: trackingService.getTrackingList()) {
                try{
                    this.createTrackingItem(trackingInfo);
                }catch (SQLiteException e){
                    e.printStackTrace();
                }
            }
        }
    }
    public TrackingPlan.planInformation createPlanItem(TrackingPlan.planInformation planInformation){
        ContentValues planValues = planInformation.toValues();
        database.insert(PlanTable.TABLE_PLANS,null,planValues);
        return planInformation;

    }
    //seed data from file to database
    public void seedPlanData(){
        long num = getPlanItemCount();
        if(num == 0){
            TrackingPlan trackingPlan = TrackingPlan.getSingletonInstance(context);
            for (TrackingPlan.planInformation planInformation: trackingPlan.getListPlan()) {
                try{
                    this.createPlanItem(planInformation);
                }catch (SQLiteException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public long getTrackableItemCount() {
        return DatabaseUtils.queryNumEntries(database, TrackableTable.TABLE_TRACKABLES);
    }
    public long getTrackingItemCount() {
        return DatabaseUtils.queryNumEntries(database, TrackingTable.TABLE_TRACKING);
    }
    public long getPlanItemCount() {
        return DatabaseUtils.queryNumEntries(database, PlanTable.TABLE_PLANS);
    }

    public List<TrackableService.TrackableInfo> getAllTrackableList(){
        List<TrackableService.TrackableInfo> trackbleList = new ArrayList<>();
        Cursor cursor = database.query(TrackableTable.TABLE_TRACKABLES,TrackableTable.ALL_COLUMN,
                null,null,null,null,null);
        while (cursor.moveToNext()){
            TrackableService.TrackableInfo trackableInfo = new TrackableService.TrackableInfo();
            trackableInfo.setId(cursor.getInt(cursor.getColumnIndex(TrackableTable.COLUMN_ID)));
            trackableInfo.setName(cursor.getString(cursor.getColumnIndex(TrackableTable.COLUMN_NAME)));
            trackableInfo.setDescription(cursor.getString(cursor.getColumnIndex(TrackableTable.COLUMN_DESCRITION)));
            trackableInfo.setUrl(cursor.getString(cursor.getColumnIndex(TrackableTable.COLUMN_URL)));
            trackableInfo.setCategory(cursor.getString(cursor.getColumnIndex(TrackableTable.COLUMN_CATEGORY)));
            trackbleList.add(trackableInfo);
        }

        return trackbleList;
    }
    public List<String> getAllTrackableNames() {
        List<String> trackableNames = new ArrayList<>();
        Cursor cursor = database.query(TrackableTable.TABLE_TRACKABLES,TrackableTable.ALL_COLUMN,
                null,null,null,null,null);
        while (cursor.moveToNext()){
            trackableNames.add(cursor.getString(cursor.getColumnIndex(TrackableTable.COLUMN_NAME)));
        }
        return trackableNames;
    }

    public List<String> getCategoryOfTrackable() {
        List<String> categoryList = new ArrayList<>();
        categoryList.add("All");
        Cursor cursor = database.query(TrackableTable.TABLE_TRACKABLES,TrackableTable.ALL_COLUMN,
                null,null,null,null,null);
        while (cursor.moveToNext()){
            if(!categoryList.contains(cursor.getString(cursor.getColumnIndex(TrackableTable.COLUMN_CATEGORY)))){
                categoryList.add(cursor.getString(cursor.getColumnIndex(TrackableTable.COLUMN_CATEGORY)));
            }
        }
        return categoryList;
    }

    public TrackableService.TrackableInfo retreiveTrackabkeByName(String name) {
        TrackableService.TrackableInfo trackbleList = new TrackableService.TrackableInfo();

        String[] names = {name};
        Cursor cursor = database.query(TrackableTable.TABLE_TRACKABLES,TrackableTable.ALL_COLUMN,
                TrackableTable.COLUMN_NAME + "=?",names,null,null,null);
        while (cursor.moveToNext()){
            if(cursor.getString(cursor.getColumnIndex(TrackableTable.COLUMN_NAME)).equals(name)){
                TrackableService.TrackableInfo trackableInfo = new TrackableService.TrackableInfo();
                trackableInfo.setId(cursor.getInt(cursor.getColumnIndex(TrackableTable.COLUMN_ID)));
                trackableInfo.setName(cursor.getString(cursor.getColumnIndex(TrackableTable.COLUMN_NAME)));
                trackableInfo.setDescription(cursor.getString(cursor.getColumnIndex(TrackableTable.COLUMN_DESCRITION)));
                trackableInfo.setUrl(cursor.getString(cursor.getColumnIndex(TrackableTable.COLUMN_URL)));
                trackableInfo.setCategory(cursor.getString(cursor.getColumnIndex(TrackableTable.COLUMN_CATEGORY)));
                return trackableInfo;
            }
        }
        return null;

    }

    @SuppressLint("Recycle")
    public List<String> retreiveTrackableInfoByCategory(String category) {
        List<String> nameList = new ArrayList<>();
        Cursor cursor = null;
        String[] categories = {category};
        if(category.equals("All")){
            cursor = database.query(TrackableTable.TABLE_TRACKABLES,TrackableTable.ALL_COLUMN,
                    null,null, null,null,null);
        }else {
            cursor = database.query(TrackableTable.TABLE_TRACKABLES,TrackableTable.ALL_COLUMN,
                    TrackableTable.COLUMN_CATEGORY + "=?",categories, null,null,null);
        }
        while (cursor.moveToNext()){
                nameList.add(cursor.getString(cursor.getColumnIndex(TrackableTable.COLUMN_NAME)));
        }
        return nameList;
    }

    @SuppressLint("Recycle")
    public List<Integer> getTrackableIdByCategory (String category) {
        List<Integer> nameList = new ArrayList<>();
        Cursor cursor = null;
        String[] categories = {category};
        if(category.equals("All")){
            cursor = database.query(TrackableTable.TABLE_TRACKABLES,TrackableTable.ALL_COLUMN,
                    null,null, null,null,null);
        }else {
            cursor = database.query(TrackableTable.TABLE_TRACKABLES,TrackableTable.ALL_COLUMN,
                    TrackableTable.COLUMN_CATEGORY + "=?",categories, null,null,null);
        }
        while (cursor.moveToNext()){
            nameList.add(cursor.getInt(cursor.getColumnIndex(TrackableTable.COLUMN_ID)));
        }
        return nameList;
    }


    public List<String> retreiveListDateByTrackableId(int trackableId) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        List<String> listDate = new ArrayList<>();
        String[] trackableIds = {String.valueOf(trackableId)};
        listDate.add("All Events");
        @SuppressLint("Recycle") Cursor cursor = database.query(TrackingTable.TABLE_TRACKING,TrackingTable.ALL_COLUMN,TrackingTable.COLUMN_TRACKABLE_ID + "=?",trackableIds,null,null,null);
        while (cursor.moveToNext()){

            Date date = null;
            try {
                date = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.MEDIUM).parse(cursor.getString(cursor.getColumnIndex(TrackingTable.COLUMN_DATE)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(!listDate.contains(dateFormat.format(date))){
                listDate.add(dateFormat.format(date));
            }
        }
        return listDate;
    }


    public List<TrackingService.TrackingInfo> getEventById(String trackingDate, int trackableId) {

        List<TrackingService.TrackingInfo> trackingInfoList = new ArrayList<>();
        String[] trackableIds = {String.valueOf(trackableId),trackingDate+"%"};
        @SuppressLint("Recycle") Cursor cursor = database.query(
                TrackingTable.TABLE_TRACKING,
                TrackingTable.ALL_COLUMN,TrackingTable.COLUMN_STOP_TIME + ">0 AND " +
                TrackingTable.COLUMN_TRACKABLE_ID + "=? AND " + TrackingTable.COLUMN_DATE + " LIKE ?",
                trackableIds, null,null,null);

        while (cursor.moveToNext()){
            Date date = null;
            try {
                date = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.MEDIUM).parse(cursor.getString(cursor.getColumnIndex(TrackingTable.COLUMN_DATE)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            TrackingService.TrackingInfo trackingInfo = new TrackingService.TrackingInfo();
            trackingInfo.setTrackableId(cursor.getInt(cursor.getColumnIndex(TrackingTable.COLUMN_TRACKABLE_ID)));
            trackingInfo.setDate(date);
            trackingInfo.setStopTime(cursor.getInt(cursor.getColumnIndex(TrackingTable.COLUMN_STOP_TIME)));
            trackingInfo.setLatitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(TrackingTable.COLUMN_LATITUDE))));
            trackingInfo.setLongitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(TrackingTable.COLUMN_LONGITUDE))));
            trackingInfoList.add(trackingInfo);
            }
        return trackingInfoList;
    }

    public int getTrackingIdByObject (TrackingService.TrackingInfo trackingInfo){
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.MEDIUM);
        String[] trackableIds = {String.valueOf(trackingInfo.getTrackableId()),dateFormat.format(trackingInfo.getDate())};
        Cursor cursor = database.query(
                TrackingTable.TABLE_TRACKING,
                TrackingTable.ALL_COLUMN,
                TrackingTable.COLUMN_TRACKABLE_ID +"=? AND "+ TrackingTable.COLUMN_DATE + "=?",
                trackableIds,null,null,null);
        while (cursor.moveToNext()){
            return cursor.getInt(cursor.getColumnIndex(TrackingTable.COLUMN_ID));
        }
        return 0;
    }

    public TrackingService.TrackingInfo getTrackingInfoById(int trackingId) {

        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.MEDIUM);
        String[] trackableIds = {String.valueOf(trackingId)};
        @SuppressLint("Recycle") Cursor cursor = database.query(
                TrackingTable.TABLE_TRACKING,
                TrackingTable.ALL_COLUMN,
                TrackingTable.COLUMN_ID + "=?",
                trackableIds,null,null,null);
        while (cursor.moveToNext()){
            Date date = null;
            try {
                date = dateFormat.parse(cursor.getString(cursor.getColumnIndex(TrackingTable.COLUMN_DATE)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            TrackingService.TrackingInfo trackingInfo = new TrackingService.TrackingInfo();
            trackingInfo.setTrackableId(cursor.getInt(cursor.getColumnIndex(TrackingTable.COLUMN_TRACKABLE_ID)));
            trackingInfo.setDate(date);
            trackingInfo.setStopTime(cursor.getInt(cursor.getColumnIndex(TrackingTable.COLUMN_STOP_TIME)));
            trackingInfo.setLatitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(TrackingTable.COLUMN_LATITUDE))));
            trackingInfo.setLongitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(TrackingTable.COLUMN_LONGITUDE))));
            return trackingInfo;
        }
        return  null;
    }

    public void addTracking(TrackingPlan.planInformation planInformation){
        this.createPlanItem(planInformation);
    }



    public List<TrackingPlan.planInformation> getListTrackingById(int trackableId, String date) {
        List<TrackingPlan.planInformation> trackingList = new ArrayList<>();
        String[] whereAgrs = {String.valueOf(trackableId), date + "%"};
        Cursor cursor = database.query(
                PlanTable.TABLE_PLANS,PlanTable.ALL_COLUMN,
                        PlanTable.COLUMN_TRACKABLE_ID + "=? AND " + PlanTable.COLUMN_TARGET_ST + " LIKE ?", whereAgrs,null,null,null,null);
        while(cursor.moveToNext()) {
            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);
            try {
                String Id = cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_TITLE));
                Date targetStart = dateFormat.parse(cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_TARGET_ST)));
                Date targetEnd = dateFormat.parse(cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_TARGET_END)));
                Date meetTime = dateFormat.parse(cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_MEET_TIME)));
                String currentLoca = cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_CURRENT_LOCATION));
                String meetLoca = cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_MEET_LOCATION));
                TrackingPlan.planInformation trackingInfo = new TrackingPlan.planInformation(Id, trackableId, title, targetStart, targetEnd, meetTime, currentLoca, meetLoca);
                trackingList.add(trackingInfo);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return trackingList;
    }

    public void deleteTracking(String trackingId) {
        String[] whereAgrs = {trackingId};
        database.delete(PlanTable.TABLE_PLANS,PlanTable.COLUMN_ID + "=?",whereAgrs);
    }


    public TrackingPlan.planInformation getTrackingById(String trackingId) {
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.MEDIUM);

        String[] whereArgs = {trackingId};
        Cursor cursor = database.query(PlanTable.TABLE_PLANS,PlanTable.ALL_COLUMN,PlanTable.COLUMN_ID + "=?",whereArgs,null,null,null);
        while(cursor.moveToNext()) {
            try {
                String Id = cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_ID));
                int trackableId = cursor.getInt(cursor.getColumnIndex(PlanTable.COLUMN_TRACKABLE_ID));
                String title = cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_TITLE));
                Date targetStart = dateFormat.parse(cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_TARGET_ST)));
                Date targetEnd = dateFormat.parse(cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_TARGET_END)));
                Date meetTime = dateFormat.parse(cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_MEET_TIME)));
                String currentLoca = cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_CURRENT_LOCATION));
                String meetLoca = cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_MEET_LOCATION));
                return new TrackingPlan.planInformation(Id, trackableId, title, targetStart, targetEnd, meetTime, currentLoca, meetLoca);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

//    public List<TrackingPlan.planInformation> getAllTracking() {
//        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.MEDIUM);
//        List<TrackingPlan.planInformation> List = new ArrayList<>();
//        Cursor cursor = database.query(PlanTable.TABLE_PLANS,PlanTable.ALL_COLUMN,PlanTable.COLUMN_ID + "=?",whereArgs,null,null,null);
//        while(cursor.moveToNext()) {
//            try {
//                String Id = cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_ID));
//                int trackableId = cursor.getInt(cursor.getColumnIndex(PlanTable.COLUMN_TRACKABLE_ID));
//                String title = cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_TITLE));
//                Date targetStart = dateFormat.parse(cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_TARGET_ST)));
//                Date targetEnd = dateFormat.parse(cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_TARGET_END)));
//                Date meetTime = dateFormat.parse(cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_MEET_TIME)));
//                String currentLoca = cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_CURRENT_LOCATION));
//                String meetLoca = cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_MEET_LOCATION));
//                return new TrackingPlan.planInformation(Id, trackableId, title, targetStart, targetEnd, meetTime, currentLoca, meetLoca);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }

    public void updateTrackingItem(TrackingPlan.planInformation planInformation) {
        String[] whereArgs = {planInformation.getPlanId()};
        ContentValues trackingConten = planInformation.toValues();
        database.update(PlanTable.TABLE_PLANS,trackingConten, PlanTable.COLUMN_ID + "=?",whereArgs);
    }


    public boolean checkDate(int trackableId){
        int count = 0;
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());
        String[] whereAgrs = {String.valueOf(trackableId), currentDate + "%"};
        Cursor cursor = database.query(
                TrackingTable.TABLE_TRACKING,TrackingTable.ALL_COLUMN,
                TrackingTable.COLUMN_TRACKABLE_ID + "=? AND " + TrackingTable.COLUMN_DATE + " LIKE ?", whereAgrs,null,null,null,null);
        while (cursor.moveToNext()){
            count++;
        }

        if(count==0) {
            return false;
        } else {
            return true;
        }
    }

    public List<TrackingService.TrackingInfo> locationOfTruck(int trackingId){
        List<TrackingService.TrackingInfo> routList = new ArrayList<>();
        Calendar currentime = Calendar.getInstance();
        String currentdate = DateFormat.getDateInstance(DateFormat.SHORT).format(currentime.getTime());
        String[] whereArgs = {String.valueOf(trackingId),currentdate+"%"};

        Cursor cursor = database.query(TrackingTable.TABLE_TRACKING, TrackingTable.ALL_COLUMN,
                TrackingTable.COLUMN_TRACKABLE_ID + "=? AND " + TrackingTable.COLUMN_STOP_TIME + " > 0 AND " + TrackingTable.COLUMN_DATE + " LIKE ?", whereArgs,
                null,null,null);

        while (cursor.moveToNext()){
            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.MEDIUM);
            try {
                TrackingService.TrackingInfo trackingInfo = new TrackingService.TrackingInfo();
                trackingInfo.setTrackableId(trackingId);
                trackingInfo.setDate(dateFormat.parse(cursor.getString(cursor.getColumnIndex(TrackingTable.COLUMN_DATE))));
                trackingInfo.setLatitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(TrackingTable.COLUMN_LATITUDE))));
                trackingInfo.setLongitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(TrackingTable.COLUMN_LONGITUDE))));
                trackingInfo.setStopTime(cursor.getInt(cursor.getColumnIndex(TrackingTable.COLUMN_STOP_TIME)));
                routList.add(trackingInfo);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return routList;
    }

    public List<TrackingService.TrackingInfo> getTrackingInfoFromToDay(List<Integer> trackableIdList){

        List<TrackingService.TrackingInfo> trackingInfoList = new ArrayList<>();
        for (Integer trackableId: trackableIdList) {
            String[] whereArgs = {String.valueOf(trackableId)};
            Cursor cursor = database.query(TrackingTable.TABLE_TRACKING,TrackingTable.ALL_COLUMN,TrackingTable.COLUMN_TRACKABLE_ID + "=? AND " + TrackingTable.COLUMN_STOP_TIME + ">0",whereArgs,null,null,null);
            while(cursor.moveToNext()){
                DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.MEDIUM);
                try {
                    Date date = dateFormat.parse(cursor.getString(cursor.getColumnIndex(TrackingTable.COLUMN_DATE)));
                    if(date.getTime()>= System.currentTimeMillis()){
                        TrackingService.TrackingInfo trackingItem = new TrackingService.TrackingInfo();
                        trackingItem.setDate(date);
                        trackingItem.setStopTime(cursor.getInt(cursor.getColumnIndex(TrackingTable.COLUMN_STOP_TIME)));
                        trackingItem.setTrackableId(cursor.getInt(cursor.getColumnIndex(TrackingTable.COLUMN_TRACKABLE_ID)));
                        trackingItem.setLatitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(TrackingTable.COLUMN_LATITUDE))));
                        trackingItem.setLongitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(TrackingTable.COLUMN_LONGITUDE))));
                        trackingInfoList.add(trackingItem);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }

        return trackingInfoList;
    }

    public String getTrackableNameById(int trackablId) {
        String name = null;
        String[] whereArgs = {String.valueOf(trackablId)};
        Cursor cursor = database.query(TrackableTable.TABLE_TRACKABLES, TrackableTable.ALL_COLUMN,
                TrackableTable.COLUMN_ID + "=?",whereArgs,null,null,null,null);
        while(cursor.moveToNext()){
            name = cursor.getString(cursor.getColumnIndex(TrackableTable.COLUMN_NAME));
        }
        return name;
    }

    public void readData(String category, Location location){
        List<Integer> trackableIds = getTrackableIdByCategory(category);
        List<TrackingService.TrackingInfo> trackingInfoList = getTrackingInfoFromToDay(trackableIds);

        for (TrackingService.TrackingInfo trackingInfo: trackingInfoList) {
            GetJsonFile getJsonFile = new GetJsonFile(trackingInfo,context,String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));
            getJsonFile.execute();
        }
    }

    public List<TrackingPlan.planInformation> retrieveCurrentDateTracking() {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        List<TrackingPlan.planInformation> planInformations = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        String date  = dateFormat.format(calendar.getTime());
        String[] whereArgs = {date+"%"};
        Cursor cursor = database.query(PlanTable.TABLE_PLANS,PlanTable.ALL_COLUMN,null, null, null,null,null );
        while (cursor.moveToNext()){
            DateFormat dateFormat1 = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.MEDIUM);
            try {
                String planId = cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_ID));
                Date meetDate = dateFormat1.parse(cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_MEET_TIME)));
                Date targetStart = dateFormat1.parse(cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_TARGET_ST)));
                Date targetEnd = dateFormat1.parse(cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_TARGET_END)));
                int trackableID = cursor.getInt(cursor.getColumnIndex(PlanTable.COLUMN_TRACKABLE_ID));
                String currentLoca = cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_CURRENT_LOCATION));
                String meetLoca = cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_MEET_LOCATION));
                String title = cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_TITLE));
                TrackingPlan.planInformation planInformation = new TrackingPlan.planInformation(
                        planId,trackableID,title,targetStart,targetEnd,meetDate,currentLoca,meetLoca);
                planInformations.add(planInformation);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return planInformations;
    }

    public TrackingPlan.planInformation getTrackingPlanById(String id) {
        String[] whereArgs ={id};
        Cursor cursor = database.query(PlanTable.TABLE_PLANS,PlanTable.ALL_COLUMN,PlanTable.COLUMN_ID + "=?",whereArgs,null,null,null);
        while(cursor.moveToNext()) {
            DateFormat dateFormat1 = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.MEDIUM);
            try {
                String planId = cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_ID));
                Date meetDate = dateFormat1.parse(cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_MEET_TIME)));
                Date targetStart = dateFormat1.parse(cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_TARGET_ST)));
                Date targetEnd = dateFormat1.parse(cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_TARGET_END)));
                int trackableID = cursor.getInt(cursor.getColumnIndex(PlanTable.COLUMN_TRACKABLE_ID));
                String currentLoca = cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_CURRENT_LOCATION));
                String meetLoca = cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_MEET_LOCATION));
                String title = cursor.getString(cursor.getColumnIndex(PlanTable.COLUMN_TITLE));
                TrackingPlan.planInformation planInformation = new TrackingPlan.planInformation(
                        planId,trackableID,title,targetStart,targetEnd,meetDate,currentLoca,meetLoca);
                return planInformation;
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return null;
    }


}
