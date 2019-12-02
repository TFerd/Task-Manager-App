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


    public DBSQLiteOpenHelper(@Nullable Context context) {

        super(context, DATABASE_NAME, null, 1);

        Log.i(TAG, new Date().toString() + " Database was created");

        SQLiteDatabase db = this.getWritableDatabase();

    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.i(TAG, " Reached On Create");

        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + id + " INTEGER PRIMARY KEY AUTOINCREMENT," + col_1 + " TEXT," + col_2 + " TEXT," + col_3 + " INTEGER,"
                + col_4 + " INTEGER," + col_5 + " INTEGER," + col_6 + " INTEGER," + col_7 + " INTEGER," + col_8 + " BOOL," + col_9 + " BOOL," + col_10 + " TEXT" +")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }


    public boolean insertData(String name, String description, int hour, int minute, int month, int day, int year, boolean notify, boolean complete, String location) {

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

    public boolean updateData(int key, String name, String description, int hour, int minute, int month, int day, int year, boolean notify, boolean complete, String location) {

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

    public void deleteData(String key){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, "id = ?", new String[] { key });

        Log.i(TAG, "deleteData: ");
    }

    public Cursor where(String whereClause, String equals){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] {whereClause}, null,null,null,null,null );

        return cursor;
    }

    }