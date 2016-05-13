package com.example.whatsemo.classmates;

import android.app.Activity;
import android.os.Bundle;

import com.example.whatsemo.classmates.model.Friend;
import com.example.whatsemo.classmates.model.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by WhatsEmo on 5/8/2016.
 */
public class NotificationActivity extends Activity{

    private Firebase firedata;
    private ArrayList<Friend> notifications;
    private User appUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_layout);
        ButterKnife.bind(this);

        appUser = getIntent().getParcelableExtra("appUser");


        firedata = new Firebase(getString(R.string.database));

        Firebase userRef = firedata.child(getString(R.string.database_users_key)).child(appUser.getUid());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(getString(R.string.users_requests_key)).exists()){
                    for (Map.Entry<String, String> friendRequests : ((Map<String,String>) dataSnapshot.child(getString(R.string.users_requests_key))).entrySet()){
                        Friend friend = new Friend(friendRequests.getKey(),friendRequests.getValue());
                        notifications.add(friend);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
