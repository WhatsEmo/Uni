package com.example.whatsemo.classmates.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.whatsemo.classmates.ImageHandler;
import com.example.whatsemo.classmates.R;
import com.example.whatsemo.classmates.adapter.ChatSchedulingAdapter;
import com.example.whatsemo.classmates.adapter.SchedulingAdapter;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by WhatsEmo on 5/13/2016.
 */
public class SchedulingFragment extends DialogFragment {
    private final static int DISPLAY_IN_CHAT = 2;
    private static final int AMOUNT_OF_HOURS_TO_DISPLAY = 13;
    private final static int STARTING_HOUR = 8;
    private int windowWidth;
    private int windowHeight;
    private int day;
    private Firebase firebase;
    private Firebase triggerRef;
    private String appUserId;
    private ArrayList<String> listOfFriends;
    private ArrayList<Boolean> freeTimeOfEveryone;
    private ArrayList<Integer> freeTimeInHours;
    private Map<String, Bitmap> allProfilePics;
    private Map<String, ArrayList<Boolean>> allFreeTimesOnCertainDay;
    private ImageHandler imageHandler;
    private ValueEventListener scheduleListener;

    private ChatSchedulingAdapter chatSchedulingAdapter;
    private LinearLayoutManager linearLayoutManager;

    private SchedulingAdapter allSchedulingAdapter;
    private LinearLayoutManager allLinearLayoutManager;

    @Bind(R.id.schedules_recylcer_view)
    RecyclerView schedulesRecyclerView;

    @Bind(R.id.everyones_free_time_recycler_view)
    RecyclerView allFreeTimeRecyclerView;

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
            Firebase newRef = triggerRef.child(userID).child(getString(R.string.user_groups_key)).child("1");
            newRef.setValue("\n0");
            newRef.setValue(null);
        }
    }

    @OnClick(R.id.tuesday_button)
    public void setTuesdayButton(){
        day = Calendar.TUESDAY;
        setTextColors(day);
        for(String userID : listOfFriends){
            Firebase newRef = triggerRef.child(userID).child(getString(R.string.user_groups_key)).child("1");
            newRef.setValue("\n0");
            newRef.setValue(null);
        }
    }

    @OnClick(R.id.wednesday_button)
    public void setWednesdayButton(){
        day = Calendar.WEDNESDAY;
        setTextColors(day);
        for(String userID : listOfFriends){
            Firebase newRef = triggerRef.child(userID).child(getString(R.string.user_groups_key)).child("1");
            newRef.setValue("\n0");
            newRef.setValue(null);
        }
    }

    @OnClick(R.id.thursday_button)
    public void setThursdayButton(){
        day = Calendar.THURSDAY;
        setTextColors(day);
        for(String userID : listOfFriends){
            Firebase newRef = triggerRef.child(userID).child(getString(R.string.user_groups_key)).child("1");
            newRef.setValue("\n0");
            newRef.setValue(null);
        }
    }

    @OnClick(R.id.friday_button)
    public void setFridayButton(){
        day = Calendar.FRIDAY;
        setTextColors(day);
        for(String userID : listOfFriends){
            Firebase newRef = triggerRef.child(userID).child(getString(R.string.user_groups_key)).child("1");
            newRef.setValue("\n0");
            newRef.setValue(null);
        }
    }

    public SchedulingFragment(){
        //
    }

    public SchedulingFragment newInstance(String userID, ArrayList<String> friends){
        appUserId = userID;
        listOfFriends = friends;
        return this;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        scheduleListener = firebase.child(getString(R.string.database_users_key)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Both these lists are used to check the free time of all users
                freeTimeOfEveryone = new ArrayList<Boolean>(Collections.nCopies(AMOUNT_OF_HOURS_TO_DISPLAY, false));
                freeTimeInHours.clear();
                for (String friendId : listOfFriends) {
                    if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) {
                        day = Calendar.MONDAY;
                    }
                    if (dataSnapshot.child(friendId).child(getString(R.string.user_schedule_key)).child(Integer.toString(day)).exists()) {
                        ArrayList<Integer> userFreeTimeInHours = dataSnapshot.child(friendId)
                                .child(getString(R.string.user_schedule_key))
                                .child(Integer.toString(day))
                                .getValue(ArrayList.class);
                        ArrayList<Boolean> userFreeTime = new ArrayList<Boolean>(Collections.nCopies(AMOUNT_OF_HOURS_TO_DISPLAY, false));
                        boolean isArrayNull = freeTimeInHours.isEmpty();
                        if (isArrayNull) {
                            freeTimeInHours.addAll(userFreeTimeInHours);
                        } else {
                            for (int counter = freeTimeInHours.size() - 1; counter >= 0; counter--) {
                                Integer value = freeTimeInHours.get(counter);
                                if (!userFreeTimeInHours.contains(value)) {
                                    freeTimeInHours.remove(counter);
                                }
                            }
                        }
                        if (userFreeTimeInHours == null) {
                            freeTimeInHours.clear();
                            break;
                        }
                        for (Integer time : userFreeTimeInHours) {
                            userFreeTime.set(time - STARTING_HOUR, true);
                        }
                        allFreeTimesOnCertainDay.put(friendId, userFreeTime);
                    } else {
                        freeTimeOfEveryone = new ArrayList<Boolean>(Collections.nCopies(AMOUNT_OF_HOURS_TO_DISPLAY, false));
                        freeTimeInHours.clear();
                        allFreeTimesOnCertainDay.put(friendId, new ArrayList<Boolean>(Collections.nCopies(AMOUNT_OF_HOURS_TO_DISPLAY, false)));
                    }


                    if (dataSnapshot.child(friendId).child("picture").exists()) {
                        Bitmap bm = imageHandler.convertByteArrayToBitmap(dataSnapshot.child(friendId).child("picture").getValue().toString());
                        allProfilePics.put(friendId, bm);
                    } else {
                        allProfilePics.put(friendId, null);
                    }
                }

                linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                schedulesRecyclerView.setLayoutManager(linearLayoutManager);

                chatSchedulingAdapter = new ChatSchedulingAdapter(listOfFriends, getContext(), firebase, day, allProfilePics, allFreeTimesOnCertainDay);
                schedulesRecyclerView.setAdapter(chatSchedulingAdapter);

                allLinearLayoutManager = new LinearLayoutManager(getActivity());
                allLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                allFreeTimeRecyclerView.setLayoutManager(allLinearLayoutManager);

                for (Integer hours : freeTimeInHours) {
                    freeTimeOfEveryone.set(hours - STARTING_HOUR, true);
                }

                allSchedulingAdapter = new SchedulingAdapter(freeTimeOfEveryone, getContext(), firebase, day, DISPLAY_IN_CHAT);
                allFreeTimeRecyclerView.setAdapter(allSchedulingAdapter);


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scheduling_layout, container, false);
        ButterKnife.bind(this, view);
        getDialog().getWindow().setTitle(getString(R.string.scheduling_assistant));

        firebase = new Firebase(getString(R.string.database));
        triggerRef = firebase.child(getString(R.string.database_users_key));
        freeTimeOfEveryone = new ArrayList<>(Collections.nCopies(AMOUNT_OF_HOURS_TO_DISPLAY, false));
        freeTimeInHours = new ArrayList<Integer>();
        allProfilePics = new HashMap<String, Bitmap>();
        allFreeTimesOnCertainDay = new HashMap<String, ArrayList<Boolean>>();
        day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        imageHandler = new ImageHandler(this.getActivity());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        firebase.child(getString(R.string.database_users_key)).removeEventListener(scheduleListener);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
                mondayButton.setTextColor(getResources().getColor(R.color.realOrange));
                break;
        }
    }

}
