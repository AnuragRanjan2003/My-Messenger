package com.example.mymessenger.Models;

public class CommentModel {
    private String Name, Purl, Uid, Comment;

    public CommentModel(String name, String purl, String uid) {
        Name = name;
        Purl = purl;
        Uid = uid;
    }
    public CommentModel(){}

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPurl() {
        return Purl;
    }

    public void setPurl(String purl) {
        Purl = purl;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

}
