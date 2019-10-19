package com.example.mobileappproject;

public class Task {

    //@TODO
    //  Add more attributes (variables)
    //  Find a way to add these to the ListView, also make sure the plus arrow button (to add more tasks) will show up at the bottom of the ListView if there are tasks present.

    String taskName, taskDescription;
    int hour,minute;
    int month, day, year;
    //location variable
    boolean notification;
    boolean isComplete;

    //Constructor for Task without description
    public Task(String taskName, boolean notification, int hour, int minute, int month, int day, int year){
        this.taskName = taskName;
        this.notification = notification;

        this.hour = hour;
        this.minute = minute;

        this.month = month;
        this.day = day;
        this.year = year;

    }

    //Constructor for Task WITH description
    public Task(String taskName, String taskDescription, boolean notification, int hour, int minute, int month, int day, int year){
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

    public long getDate(){

        long date = 0;

        return date;
    }
}
