package com.example.mymessenger.Models;

public class PostModel {
    private String senderName, text, senderUid, Url, job, place,profileUrl;

    public PostModel(String senderName, String text, String senderUid, String url, String job, String place,String profileUrl) {
        this.senderName = senderName;
        this.text = text;
        this.senderUid = senderUid;
        Url = url;
        this.job = job;
        this.place = place;
        this.profileUrl=profileUrl;
    }

    public PostModel() {
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
