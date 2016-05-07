package com.example.whatsemo.classmates.model;

import android.content.Context;
import android.content.Intent;

import com.example.whatsemo.classmates.ChatActivity;

/**
 * Created by WhatsEmo on 4/30/2016.
 */
public class Group extends DatabaseObject{
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

    @Override
    public String getName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void openChatActivity(Context context){
        Intent startChatIntent = new Intent(context, ChatActivity.class);
        context.startActivity(startChatIntent);
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) return true;
        if(o instanceof Group) return ((Group) o).getGroupID().equals(groupID);
        return false;
    }
}
