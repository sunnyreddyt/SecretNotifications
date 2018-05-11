package com.ctel_rtc.secretnotifications;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by MADHU on 7/9/2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {


    // Database Tables
    public static final String DATABASE_NAME = "savedLocations";
    private static final int DATABASE_VERSION = 1;
    // home table name

    public static final String TABLE_USER = "locationTable";

    private static String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + UserDB.KEY_ID + " INTEGER PRIMARY KEY ," + UserDB.KEY_TITLE + " TEXT," + UserDB.KEY_TEXT + " TEXT,"
            + UserDB.KEY_USER_NAME +" TEXT);";

    private static DataBaseHelper instance;

    public static synchronized DataBaseHelper getHelper(Context context) {
        if (instance == null)
            instance = new DataBaseHelper(context);
        return instance;
    }

    private DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
