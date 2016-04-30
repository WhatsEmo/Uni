package com.example.whatsemo.classmates.model;

import android.content.Context;
import android.content.Intent;

import com.example.whatsemo.classmates.ChatActivity;

/**
 * Created by WhatsEmo on 4/30/2016.
 */
public class Course extends DatabaseObject{
    private String courseID;
    private String courseName;

    public Course(String courseID, String courseName) {
        this.courseID = courseID;
        this.courseName = courseName;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    @Override
    public String getName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void openChatActivity(Context context){
        Intent startChatIntent = new Intent(context, ChatActivity.class);
        context.startActivity(startChatIntent);
    }
}
