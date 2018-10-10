package com.example.hung.myapplication.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Nguyen My
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_FILE_NAME = "foodtruck.db";
    public static final int DB_VERSION = 1;



    public DatabaseHelper(Context context) {
        super(context, DB_FILE_NAME, null, DB_VERSION);
    }
    //Create Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TrackableTable.SQL_CREATE);
        db.execSQL(TrackingTable.SQL_CREATE);
        db.execSQL(PlanTable.SQL_CREATE);
    }
    //Drop table if exist
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TrackableTable.SQL_DELETE);
        db.execSQL(TrackingTable.SQL_DELETE);
        db.execSQL(PlanTable.SQL_DELETE);
        onCreate(db);
    }
}
