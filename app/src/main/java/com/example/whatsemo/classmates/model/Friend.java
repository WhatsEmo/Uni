package com.example.whatsemo.classmates.model;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.whatsemo.classmates.ChatActivity;
import com.example.whatsemo.classmates.ProfileActivity;

/**
 * Created by WhatsEmo on 4/29/2016.
 */
public class Friend extends DatabaseObject{
    private String uid;
    private String name;

    public Friend(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public String getUid() {

        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void openChatActivity(Context context, User user){
        //Opens Chat Activity
        Intent startChatIntent = new Intent(context, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("appUser", user);
        bundle.putString("friendID", this.getUid());
        bundle.putString("friendName", getName());
        startChatIntent.putExtras(bundle);
        context.startActivity(startChatIntent);
    }

    public void openProfileActivity(Context context, User user){
        //Opens ProfileActivity
        Intent startProfileActivity = new Intent(context, ProfileActivity.class);
        startProfileActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putParcelable("appUser", user);
        bundle.putString("friendID", this.getUid());
        bundle.putString("friendName", this.getName());
        startProfileActivity.putExtras(bundle);
        context.startActivity(startProfileActivity);
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) return true;
        if(o instanceof Friend) return ((Friend) o).getUid().equals(uid);
        return false;
    }
}
