package com.example.whatsemo.classmates.model;

/**
 * Created by WhatsEmo on 4/30/2016.
 */
public class Course {
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

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
