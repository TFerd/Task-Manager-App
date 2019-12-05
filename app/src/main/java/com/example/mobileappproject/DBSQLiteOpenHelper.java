package com.example.mobileappproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import java.util.Date;


/*
    CREATES A DATABASE FOR TASKS TO STORE INBETWEEN USAGE
 */
public class DBSQLiteOpenHelper extends SQLiteOpenHelper {
    private final static String TAG = "DBSQLiteOpenHelper : ";
    private final static String DATABASE_NAME = "tasks.db";
    private final static String TABLE_NAME = "task_table";
    public final static String id = "id";
    private int key = 0;
    private final static String col_1 = "NAME";
    private final static String col_2 = "DESCRIPTION";
    private final static String col_3 = "HOUR";
    private final static String col_4 = "MINUTE";
    private final static String col_5 = "MONTH";
    private final static String col_6 = "DAY";
    private final static String col_7 = "YEAR";
    private final static String col_8 = "NOTIFY";
    private final static String col_9 = "COMPLETE";
    private final static String col_10 = "LOCATION";

    private static final String col_11 = "LAT";
    private static final String col_12 = "LNG";

    public DBSQLiteOpenHelper(@Nullable Context context) {

        super(context, DATABASE_NAME, null, 1);

        Log.i(TAG, new Date().toString() + " Database was created");

        SQLiteDatabase db = this.getWritableDatabase();

    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.i(TAG, " Reached On Create");

        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + id + " INTEGER PRIMARY KEY AUTOINCREMENT," + col_1 + " TEXT," + col_2 + " TEXT," + col_3 + " INTEGER,"
                + col_4 + " INTEGER," + col_5 + " INTEGER," + col_6 + " INTEGER," + col_7 + " INTEGER," + col_8 + " BOOL," + col_9 + " BOOL," + col_10 + " TEXT,"
                + col_11 + " REAL," + col_12 + " Real"+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }


    public boolean insertData(String name, String description, int hour, int minute, int month, int day, int year, boolean notify, boolean complete,
                              String location, double lat, double lng) {

        Log.i(TAG, "Insert Data Reached");

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //cv.put(id, key);
        cv.put(col_1, name);
        cv.put(col_2, description);
        cv.put(col_3, hour);
        cv.put(col_4, minute);
        cv.put(col_5, month);
        cv.put(col_6, day);
        cv.put(col_7, year);
        cv.put(col_8, notify);
        cv.put(col_9, complete);
        cv.put(col_10, location);
        cv.put(col_11, lat);
        cv.put(col_12, lng);

        long result = db.insert(TABLE_NAME, null, cv);

        //this.key += 1;

        if (result == -1) {

            Log.i(TAG, "Failed to insert data");

            return false;

        } else {
            Log.i(TAG, "Data successfully added");

            return true;
        }
    }

    // Returns full list of database
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);

        return res;
    }

    public boolean updateData(int key, String name, String description, int hour, int minute, int month, int day, int year, boolean notify, boolean complete,
                              String location, double lat, double lng) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(col_1, name);
            cv.put(col_2, description);
            cv.put(col_3, hour);
            cv.put(col_4, minute);
            cv.put(col_5, month);
            cv.put(col_6, day);
            cv.put(col_7, year);
            cv.put(col_8, notify);
            cv.put(col_9, complete);
            cv.put(col_10, location);
            cv.put(col_11, lat);
            cv.put(col_12, lng);

            int results = db.update(TABLE_NAME, cv, "id = ?", new String[]{key+""});

            if (results == 0) {
                Log.i(TAG, "UnSuccessfully updated table");
            }
            else {
                Log.i(TAG, "Successfully updated table");
            }
            return true;
        } catch(Exception e){
            Log.i(TAG, "Error in updating table");
            return false;
        }
    }

    public boolean updateData(int key, String location){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(col_10, location);

        int results = db.update(TABLE_NAME, cv, "id = ?" + key, new String[]{key+""});
        return true;
    }

    public void deleteData(String key){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, "id = ?", new String[] { key });

        Log.i(TAG, "deleteData: ");
    }

    public Cursor where(String whereClause){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] {whereClause}, null,null,null,null,null );

        return cursor;
    }

    public Cursor selectFrom(String where){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE NAME = ?", new String[]{where});

        return cursor;

    }

    public Cursor selectFrom(int where){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE id = ?", new String[]{where+""});

        return cursor;

    }

    /*
    public boolean insertLocationInto(String locationId, String name){
        Log.i(TAG, "insertLocationInto: called");
        
        SQLiteDatabase db = this.getWritableDatabase();
        
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + col_10 + ") " +
                "VALUES (" + locationId + ")" + " WHERE " + col_1 + " = " + name);
        
        return true;

    } */

    public boolean insertLocationInto(int key, String locationId){
        Log.i(TAG, "insertLocationInto: called");

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(col_10, locationId);

        int results = db.update(TABLE_NAME, cv, "id = ?", new String[]{key+""});

        return true;

    }

    public boolean insertLatLngInto(int key, double lat, double lng){
        Log.i(TAG, "insertLocationInto: called");

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(col_11, lat);
        cv.put(col_12, lng);

        int results = db.update(TABLE_NAME, cv, "id = ?", new String[]{key+""});

        return true;

    }

}