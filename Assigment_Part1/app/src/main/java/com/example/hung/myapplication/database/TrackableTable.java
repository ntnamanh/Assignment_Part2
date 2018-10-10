package com.example.hung.myapplication.database;

public class TrackableTable {

    public static final String TABLE_TRACKABLES = "trackables";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRITION = "description";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_CATEGORY  = "category";

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_TRACKABLES + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME + " TEXT,"+
                    COLUMN_DESCRITION + " TEXT,"+
                    COLUMN_URL + " TEXT, "+
                    COLUMN_CATEGORY + " TEXT" + ");";

    public static final String SQL_DELETE =
            "DROP TABLE " + TABLE_TRACKABLES;

    public static final String[] ALL_COLUMN = {COLUMN_ID,COLUMN_NAME,COLUMN_DESCRITION,COLUMN_URL,COLUMN_CATEGORY};
}
