package com.example.whatsemo.classmates.model;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.whatsemo.classmates.ChatActivity;
import com.example.whatsemo.classmates.ProfileActivity;

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

    public void openChatActivity(Context context, User user){
        Intent startProfileIntent = new Intent(context, ProfileActivity.class);

        Bundle bundle = new Bundle();
        bundle.putParcelable("appUser", user);
        bundle.putString("recipientId", courseID);
        bundle.putString("recipientName", courseName);
        startProfileIntent.putExtras(bundle);
        context.startActivity(startProfileIntent);
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) return true;
        if(o instanceof Course) return ((Course) o).getCourseID().equals(courseID);
        return false;
    }
}
