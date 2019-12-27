package com.example.finalassignment.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Datum {
    @SerializedName("task_id")
    @Expose
    private String taskId;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("task_created")
    @Expose
    private String taskCreated;
    @SerializedName("task_name")
    @Expose
    private String taskName;
    @SerializedName("due_date")
    @Expose
    private String dueDate;

    public String getDueTime() {
        return dueTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    @SerializedName("due_time")
    @Expose
    private String dueTime;
    @SerializedName("task_status")
    @Expose
    private Boolean taskStatus;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskCreated() {
        return taskCreated;
    }

    public void setTaskCreated(String taskCreated) {
        this.taskCreated = taskCreated;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public Boolean getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Boolean taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }
}
