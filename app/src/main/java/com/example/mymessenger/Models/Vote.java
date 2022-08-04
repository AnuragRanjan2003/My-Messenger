package com.example.mymessenger.Models;

public class Vote {
    private String Uid;
    private int Upvote;
    private int DownVote;

    public Vote() {
    }

    public Vote(String Uid, int upvote, int downVote) {
        this.Uid = Uid;
        Upvote = upvote;
        DownVote = downVote;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public int getUpvote() {
        return Upvote;
    }

    public void setUpvote(int upvote) {
        Upvote = upvote;
    }

    public int getDownVote() {
        return DownVote;
    }

    public void setDownVote(int downVote) {
        DownVote = downVote;
    }
}
