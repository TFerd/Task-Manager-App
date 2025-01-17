package com.example.mobileappproject;

import java.io.Serializable;
import java.util.Calendar;

public class MyTask implements Serializable {      //The serializable basically means it can be passed through an Intent, which is important when setting up notifications


    private String taskName, taskDescription;
    private int hour,minute;
    private int id;
    private int month, day, year;
    private boolean notification;
    private boolean isComplete;
    private boolean hasBeenNotified = false;

    //Location Variables
    private String locationId = "";
    private double lat, lng;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    //Constructor for MyTask without description
    public MyTask(int id, String taskName, boolean notification, int hour, int minute, int month, int day, int year){
        this.id = id;

        this.taskName = taskName;
        this.notification = notification;

        this.hour = hour;
        this.minute = minute;

        this.month = month;
        this.day = day;
        this.year = year;

    }

    //Constructor for MyTask WITH description
    public MyTask(int id, String taskName, String taskDescription, boolean notification, int hour, int minute, int month, int day, int year){
        this.id = id;

        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.notification = notification;

        this.hour = hour;
        this.minute = minute;

        this.month = month;
        this.day = day;
        this.year = year;

    }




    //Getters and Setters
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }
    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setComplete(){
        isComplete = true;
    }

    public void setIncomplete(){
        isComplete = false;
    }

    public boolean hasBeenNotified() {
        return hasBeenNotified;
    }

    public void setHasBeenNotified(boolean hasBeenNotified) {
        this.hasBeenNotified = hasBeenNotified;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationId() {
        return locationId;
    }

    public int getId(){
        return this.id;
    }

    public boolean isComplete() {return isComplete;}

    public long getDate(){
        long date = 0;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, this.getYear());
        calendar.set(Calendar.MONTH, this.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, this.getDay());
        calendar.set(Calendar.HOUR, this.getHour());
        calendar.set(Calendar.MINUTE, this.getMinute());
        calendar.set(Calendar.AM_PM, Calendar.AM);

        date = calendar.getTimeInMillis();

        return date;
    }
}
