package com.example.hung.myapplication.database;

/**
 * @author Nguyen My
 */

public class TrackingTable {

    public static final String TABLE_TRACKING = "trackings";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TRACKABLE_ID = "trackableId";
    public static final String COLUMN_STOP_TIME = "stopTime";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_TRACKING + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_DATE + " TEXT NOT NULL,"+
                    COLUMN_TRACKABLE_ID + " INTEGER NOT NULL,"+
                    COLUMN_STOP_TIME + " TEXT NOT NULL, "+
                    COLUMN_LATITUDE + " TEXT NOT NULL, "+
                    COLUMN_LONGITUDE + " TEXT NOT NULL" + ");";

    public static final String SQL_DELETE =
            "DELETE TABLE " + TABLE_TRACKING;

    public static final String[] ALL_COLUMN = {COLUMN_ID,COLUMN_DATE,COLUMN_TRACKABLE_ID,COLUMN_STOP_TIME,COLUMN_LATITUDE,COLUMN_LONGITUDE};
}
