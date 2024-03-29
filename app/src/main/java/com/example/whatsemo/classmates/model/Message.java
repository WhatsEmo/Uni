package com.example.whatsemo.classmates.model;

import android.graphics.Bitmap;

/**
 * Created by WhatsEmo on 5/7/2016.
 */
public class Message {
    private char mode;
    private String authorUid;
    private String authorName;
    private String message;
    private String timeStamp;
    private Bitmap bm;

    public Message(String authorUid, String authorName, char mode,  String message, String timeStamp) {
        this.authorUid = authorUid;
        this.authorName = authorName;
        this.mode = mode;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public Message(String authorUid, String authorName, char mode,  String message, String timeStamp, Bitmap bm) {
        this.authorUid = authorUid;
        this.authorName = authorName;
        this.mode = mode;
        this.message = message;
        this.timeStamp = timeStamp;
        this.bm = bm;
    }

    public String getAuthorUid() { return authorUid; }

    public void setAuthorUid(String authorUid) { this.authorUid = authorUid;}

    public String getAuthorName() {return authorName;}

    public void setAuthor(String authorName) {
        this.authorName = authorName;
    }

    public char getMode() { return mode; }

    public void setMode(char mode) { this.mode = mode; }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Bitmap getBm() {
        return bm;
    }

    public void setBm(Bitmap bm) {
        this.bm = bm;
    }
}
