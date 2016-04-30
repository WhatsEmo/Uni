package com.example.whatsemo.classmates.model;

/**
 * Created by WhatsEmo on 4/30/2016.
 */
public class Group {
    private String groupID;
    private String groupName;

    public Group(String groupID, String groupName) {
        this.groupID = groupID;
        this.groupName = groupName;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
