package com.example.whatsemo.classmates.model;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.whatsemo.classmates.ChatActivity;

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

    public void openChatActivity(Context context, String uid, String appUserName){
        Intent startChatIntent = new Intent(context, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        bundle.putString("userName", appUserName);
        bundle.putString("friendName", getName());
        bundle.putString("friendID", this.getUid());
        startChatIntent.putExtras(bundle);
        context.startActivity(startChatIntent);
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) return true;
        if(o instanceof Friend) return ((Friend) o).getUid().equals(uid);
        return false;
    }
}
