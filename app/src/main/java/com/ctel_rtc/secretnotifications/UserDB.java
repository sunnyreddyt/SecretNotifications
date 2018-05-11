package com.ctel_rtc.secretnotifications;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by sunny on 12/21/2015.
 */
public class UserDB extends DBContext {

    // home Table Columns names
    static final String KEY_ID = "id";
    static final String KEY_TITLE = "title_id";
    static final String KEY_TEXT = "text_id";
    static final String KEY_USER_NAME = "username_id";
    Context context;

    public UserDB(Context cntxt) {
        super(cntxt);
        context = cntxt;
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void insertRecord(SavedModel reg) {

        ContentValues values = new ContentValues();


        values.put(KEY_TITLE, reg.getTitle());
        values.put(KEY_TEXT, reg.getText());
        values.put(KEY_USER_NAME, reg.getName());
        Log.e("inserted", reg.getTitle() + ":" + reg.getText()+":"+reg.getName());

        // Inserting Row
        database.insert(DataBaseHelper.TABLE_USER, null, values);
    }


    public SavedModel getHome() {
        String selectQuery = "SELECT * FROM " + DataBaseHelper.TABLE_USER;

        Cursor cursor = database.rawQuery(selectQuery, null);

        SavedModel home = new SavedModel();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            home.setTitle(cursor.getString(1));
            home.setText(cursor.getString(2));
            home.setName(cursor.getString(3));
        }
        if (cursor != null)
            cursor.close();
        return home;
    }


    // Deleting single contact
    public void deleteContact(SavedModel reg) {

        database.delete(DataBaseHelper.TABLE_USER, KEY_ID + " = ?",
                new String[]{String.valueOf(reg.getId())});
        database.close();
    }

    public void clearHomeTable() {

        database.execSQL("delete from " + DataBaseHelper.TABLE_USER);
    }

    // Getting contacts Count
    public int gethomeCount() {
        String countQuery = "SELECT  * FROM " + DataBaseHelper.TABLE_USER;

        Cursor cursor = database.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }


    public ArrayList<SavedModel> getList() {


        ArrayList<SavedModel> home_list = new ArrayList<SavedModel>();
        try {


            // Select All Query
            String selectQuery = "SELECT * FROM " + DataBaseHelper.TABLE_USER;
            System.out.println(selectQuery);
            // SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = database.rawQuery(selectQuery, null);

            // Looping through all rows and adding to list
            cursor.moveToFirst();
            if (cursor.moveToFirst()) {
                do {

                    SavedModel home = new SavedModel();

                    home.setTitle(cursor.getString(1));
                    home.setText(cursor.getString(2));
                    home.setName(cursor.getString(3));
                    // Adding contact to list
                    home_list.add(home);

                } while (cursor.moveToNext());
            }

            // return contact list
            cursor.close();


            return home_list;

        } catch (Exception e) {

            // TODO: handle exception
            Log.e("Stock options list", "" + e.getMessage());
        }
        return home_list;
    }


}
