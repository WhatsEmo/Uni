package com.example.whatsemo.classmates.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.example.whatsemo.classmates.ChatActivity;
import com.example.whatsemo.classmates.ProfileActivity;

import java.util.HashMap;

/**
 * Created by WhatsEmo on 4/29/2016.
 */
public class Friend extends DatabaseObject{
    private String uid;
    private String name;
    private Bitmap picture;

    public Friend(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public Friend(String uid, String name, Bitmap picture) {
        this.uid = uid;
        this.name = name;
        this.picture = picture;
    }

    public String getUid() {

        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Bitmap getPicture() { return picture; }

    public void setPicture(Bitmap picture) { this.picture = picture; }

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
        HashMap<String, String> members = new HashMap<>();

        members.put(this.getUid(), this.getName());

        bundle.putParcelable("appUser", user);
        bundle.putSerializable("members", members);
        bundle.putString("recipientId", this.getUid());
        bundle.putString("recipientName", this.getName());
        startChatIntent.putExtras(bundle);
        context.startActivity(startChatIntent);
    }

    public void openProfileActivity(Context context, User user){
        //Opens ProfileActivity
        Intent startProfileActivity = new Intent(context, ProfileActivity.class);
        startProfileActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putParcelable("appUser", user);
        bundle.putString("recipientId", this.getUid());
        bundle.putString("recipientName", this.getName());
        startProfileActivity.putExtras(bundle);
        context.startActivity(startProfileActivity);
    }

    public void addToGroup(Context context){}

    @Override
    public boolean equals(Object o) {
        if(o == this) return true;
        if(o instanceof Friend) return ((Friend) o).getUid().equals(uid);
        return false;
    }
}
