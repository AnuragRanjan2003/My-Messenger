package com.example.mymessenger.Models;

public class ChatModel {
    String senderName, receiverName, Text,Date,Time;
    int ViewType;
    public static final int Layout_One = 1;
    public static final int Layout_Two = 2;

    public int getViewType() {
        return ViewType;
    }

    public void setViewType(int viewType) {
        ViewType = viewType;
    }

    public ChatModel(String senderName, String receiverName, String Text,String Date,String Time) {
        this.receiverName = receiverName;
        this.senderName = senderName;
        this.Text = Text;
        this.Date=Date;
        this.Time=Time;
    }

    public ChatModel() {
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
