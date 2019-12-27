package com.example.finalassignment.models;

public class TaskResponse {
    private String id;
    private String tasks_name;
    private String tasks_date;
    private String tasks_time;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTasks_name() {
        return tasks_name;
    }

    public void setTasks_name(String tasks_name) {
        this.tasks_name = tasks_name;
    }

    public String getTasks_date() {
        return tasks_date;
    }

    public void setTasks_date(String tasks_date) {
        this.tasks_date = tasks_date;
    }

    public String getTasks_time() {
        return tasks_time;
    }

    public void setTasks_time(String tasks_time) {
        this.tasks_time = tasks_time;
    }



}
