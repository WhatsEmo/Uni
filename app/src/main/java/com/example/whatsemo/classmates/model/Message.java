package com.example.whatsemo.classmates.model;

/**
 * Created by WhatsEmo on 5/7/2016.
 */
public class Message {
    private String author;
    private String message;
    private String timeStamp;

    public Message(String author, String message, String timeStamp) {
        this.author = author;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

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
}
