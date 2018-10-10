package com.example.hung.myapplication.database;

public class PlanTable {

    public static final String TABLE_PLANS = "plans";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TRACKABLE_ID = "trackableId";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TARGET_ST = "targetStart";
    public static final String COLUMN_TARGET_END = "targetEnd";
    public static final String COLUMN_MEET_TIME = "meetTime";
    public static final String COLUMN_CURRENT_LOCATION = "currentLocation";
    public static final String COLUMN_MEET_LOCATION = "meetLocation";

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_PLANS + "(" +
                    COLUMN_ID + " TEXT PRIMARY KEY," +
                    COLUMN_TRACKABLE_ID + " INTERGER,"+
                    COLUMN_TITLE + " TEXT NOT NULL,"+
                    COLUMN_TARGET_ST + " TEXT NOT NULL, "+
                    COLUMN_TARGET_END + " TEXT NOT NULL, "+
                    COLUMN_MEET_TIME + " TEXT NOT NULL, "+
                    COLUMN_CURRENT_LOCATION + " TEXT NOT NULL, "+
                    COLUMN_MEET_LOCATION + " TEXT NOT NULL" + ");";

    public static final String SQL_DELETE =
            "DELETE TABLE " + TABLE_PLANS;

    public static final String[] ALL_COLUMN = {COLUMN_ID,COLUMN_TRACKABLE_ID,COLUMN_TITLE,COLUMN_TARGET_ST,COLUMN_TARGET_END,COLUMN_MEET_TIME,COLUMN_CURRENT_LOCATION,COLUMN_MEET_LOCATION};
}
