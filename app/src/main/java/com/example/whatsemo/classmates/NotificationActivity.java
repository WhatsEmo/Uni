package com.example.whatsemo.classmates;

import android.app.Activity;
import android.os.Bundle;

import com.example.whatsemo.classmates.model.Friend;
import com.firebase.client.Firebase;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by WhatsEmo on 5/8/2016.
 */
public class NotificationActivity extends Activity{

    private Firebase firedata;
    private ArrayList<Friend> notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_layout);
        ButterKnife.bind(this);

        firedata = new Firebase(getString(R.string.database));
    }
}
