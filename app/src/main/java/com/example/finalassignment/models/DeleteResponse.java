package com.example.finalassignment.models;

import com.google.gson.annotations.Expose;

public class DeleteResponse {
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Expose
    private String status;

    @Expose
    private String message;
}
