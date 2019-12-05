package com.example.mobileappproject;


import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;

import java.util.ArrayList;


public class CalendarFragment extends Fragment {

    private static final String TAG = "CalendarFragment";
    CalendarView Kalendar;
    public int Yr,Mth,DoM;
    ListView Scroll;

    ArrayList<MyTask> myTaskArrayC;

    public CalendarFragment() {
        Log.d(TAG, "constructed.");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "created.");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        Kalendar = view.findViewById(R.id.KCalendar);

        final DBSQLiteOpenHelper db = new DBSQLiteOpenHelper(view.getContext());

        myTaskArrayC = new ArrayList<>();


        //Now you can use findViewById()
        //Make sure you start it like v.findViewById()
        Scroll = (ListView) view.findViewById(R.id.scroll);

        Kalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Yr = year; Mth = month; DoM = dayOfMonth;
                Log.d(TAG, "Date selected: " + Yr + " " + Mth + " " +DoM);

                myTaskArrayC = fillTasks(db);
                final CustomAdapter adapter = new CustomAdapter(myTaskArrayC, getContext());
                Scroll.setAdapter(adapter);
            }
        });

        return view;
    }

    private ArrayList<MyTask> fillTasks(DBSQLiteOpenHelper db) {
        ArrayList<MyTask> myTasks = new ArrayList<>();
        Cursor res = db.getAllData();
        if (res.getCount() == 0) {
            return myTasks;
        }
        while (res.moveToNext()) {
            int id = res.getInt(0);
            String name = res.getString(1);
            String desc = res.getString(2);
            int hour = res.getInt(3);
            int minute = res.getInt(4);
            int month = res.getInt(5);
            int day = res.getInt(6);
            int year = res.getInt(7);
            boolean notify = res.getInt(8) > 0;
            boolean complete = res.getInt(9) > 0;
            if(year == Yr && month == Mth && day == DoM)
            {
                MyTask oldMyTask = new MyTask(id, name, desc, notify, hour, minute, month, day, year);
                if (complete){
                    oldMyTask.setComplete();
                }
                else {
                    oldMyTask.setIncomplete();
                }
                myTasks.add(oldMyTask);
            }
        }
        return myTasks;
    }
}
