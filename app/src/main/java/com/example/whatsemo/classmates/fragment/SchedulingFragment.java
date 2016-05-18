package com.example.whatsemo.classmates.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.whatsemo.classmates.R;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by WhatsEmo on 5/13/2016.
 */
public class SchedulingFragment extends DialogFragment {
    private final static int FRIEND_CHAT = 0;
    private final static int GROUP_CHAT = 1;
    private static final int AMOUNT_OF_HOURS_TO_DISPLAY = 13;
    private final static int STARTING_HOUR = 8;
    private final static int WINDOW_WIDTH = 245;
    private final static int WINDOW_HEIGHT = 285;
    private int chatType;
    private int windowWidth;
    private int windowHeight;
    private int day;
    private Firebase firebase;
    private Firebase triggerRef;
    private String appUserId;
    private ArrayList<String> listOfFriends;
    private ArrayList<Boolean> freeTimeOfEveryone;

    @Bind(R.id.monday_button)
    TextView mondayButton;

    @Bind(R.id.tuesday_button)
    TextView tuesdayButton;

    @Bind(R.id.wednesday_button)
    TextView wednesdayButton;

    @Bind(R.id.thursday_button)
    TextView thursdayButton;

    @Bind(R.id.friday_button)
    TextView fridayButton;

    @OnClick(R.id.monday_button)
    public void setMondayButton(){
        day = Calendar.MONDAY;
        setTextColors(day);
        for(String userID : listOfFriends){
            triggerRef = triggerRef.child(userID).child(getString(R.string.user_groups_key));
            triggerRef.setValue("\n0");
            triggerRef.setValue(null);
        }
    }

    @OnClick(R.id.tuesday_button)
    public void setTuesdayButton(){
        day = Calendar.TUESDAY;
        setTextColors(day);
        for(String userID : listOfFriends){
            triggerRef = triggerRef.child(userID).child(getString(R.string.user_groups_key));
            triggerRef.setValue("\n0");
            triggerRef.setValue(null);
        }
    }

    @OnClick(R.id.wednesday_button)
    public void setWednesdayButton(){
        day = Calendar.WEDNESDAY;
        setTextColors(day);
        for(String userID : listOfFriends){
            triggerRef = triggerRef.child(userID).child(getString(R.string.user_groups_key));
            triggerRef.setValue("\n0");
            triggerRef.setValue(null);
        }
    }

    @OnClick(R.id.thursday_button)
    public void setThursdayButton(){
        day = Calendar.THURSDAY;
        setTextColors(day);
        for(String userID : listOfFriends){
            triggerRef = triggerRef.child(userID).child(getString(R.string.user_groups_key));
            triggerRef.setValue("\n0");
            triggerRef.setValue(null);
        }
    }

    @OnClick(R.id.friday_button)
    public void setFridayButton(){
        day = Calendar.FRIDAY;
        setTextColors(day);
        for(String userID : listOfFriends){
            triggerRef = triggerRef.child(userID).child(getString(R.string.user_groups_key));
            triggerRef.setValue("\n0");
            triggerRef.setValue(null);
        }
    }

    public SchedulingFragment(){
        //
    }

    public SchedulingFragment newInstance(int type, String userID, ArrayList<String> friends){
        chatType = type;
        appUserId = userID;
        listOfFriends = friends;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scheduling_layout, container, false);
        ButterKnife.bind(this, view);
        final float scale = getContext().getResources().getDisplayMetrics().density;
        windowWidth = (int) (WINDOW_WIDTH * scale + 0.5f);
        windowHeight = (int) (WINDOW_HEIGHT * scale + 0.5f);
        getDialog().getWindow().setTitle(getString(R.string.scheduling_assistant));

        firebase = new Firebase(getString(R.string.database));
        freeTimeOfEveryone = new ArrayList<>(Collections.nCopies(AMOUNT_OF_HOURS_TO_DISPLAY, false));

        if(chatType == FRIEND_CHAT){

        }else if(chatType == GROUP_CHAT){

        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(windowWidth, windowHeight);
    }

    private void setTextColors(int date) {
        mondayButton.setTextColor(getResources().getColor(R.color.gray));
        tuesdayButton.setTextColor(getResources().getColor(R.color.gray));
        wednesdayButton.setTextColor(getResources().getColor(R.color.gray));
        thursdayButton.setTextColor(getResources().getColor(R.color.gray));
        fridayButton.setTextColor(getResources().getColor(R.color.gray));
        switch (date){
            case Calendar.MONDAY:
                mondayButton.setTextColor(getResources().getColor(R.color.realOrange));
                break;
            case Calendar.TUESDAY:
                tuesdayButton.setTextColor(getResources().getColor(R.color.realOrange));
                break;
            case Calendar.WEDNESDAY:
                wednesdayButton.setTextColor(getResources().getColor(R.color.realOrange));
                break;
            case Calendar.THURSDAY:
                thursdayButton.setTextColor(getResources().getColor(R.color.realOrange));
                break;
            case Calendar.FRIDAY:
                fridayButton.setTextColor(getResources().getColor(R.color.realOrange));
                break;
            default:
                break;
        }
    }

}
