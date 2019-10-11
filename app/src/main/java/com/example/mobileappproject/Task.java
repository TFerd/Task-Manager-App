package com.example.mobileappproject;

public class Task {

    //@TODO
    //  Add more attributes (variables)
    //  Find a way to add these to the ListView, also make sure the plus arrow button (to add more tasks) will show up at the bottom of the ListView if there are tasks present.

    String taskName, taskDescription;
    //time variable
    //location variable
    boolean notification;

    //Constructor for Task without description
    public Task(String taskName, boolean notification){
        this.taskName = taskName;
        this.notification = notification;

    }

    //Constructor for Task WITH description
    public Task(String taskName, String taskDescription, boolean notification){
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.notification = notification;

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
}
