package com.example.sebastian.medi.Model;

import com.google.gson.annotations.SerializedName;

public class Event {

    @SerializedName("id")
    private int id;
    @SerializedName("startsAt")
    private String startDate;
    @SerializedName("endsAt")
    private String endDate;
    @SerializedName("comment")
    private String title;
    @SerializedName("userId")
    private int userId;

    public Event(int id, String startDate, String endDate, String title, int userId){
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

}
