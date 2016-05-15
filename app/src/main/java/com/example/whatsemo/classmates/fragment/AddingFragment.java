package com.example.whatsemo.classmates.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.whatsemo.classmates.R;
import com.example.whatsemo.classmates.model.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by WhatsEmo on 5/13/2016.
 */
public class AddingFragment extends DialogFragment {
    private final static int ADD_COURSES = 0;
    private final static int ADD_INTERESTS = 1;
    private final static int WINDOW_WIDTH = 245;
    private final static int WINDOW_HEIGHT = 285;
    private int addingType;
    private int windowWidth;
    private int windowHeight;
    private Firebase firebase;
    private User appUser;

    @Bind(R.id.addingClassesLayout)
    RelativeLayout addingClassesLayout;

    @Bind(R.id.addingInterestsLayout)
    RelativeLayout addingInterestsLayout;

    @Bind(R.id.addingGroupsLayout)
    RelativeLayout addingGroupsLayout;

    @Bind(R.id.classesListView)
    GridLayout classesListView;

    @Bind(R.id.interestsListView)
    GridLayout interestsListView;

    @OnClick(R.id.addingClassesButton)
    public void addingClasses(){ addClasses();}

    @OnClick(R.id.addingInterestsButton)
    public void addingInterests(){ addInterests(); }

    @Bind(R.id.classId)
    TextView classId;

    @Bind(R.id.interest)
    TextView interest;

    @Bind(R.id.addingLayoutTitle)
    TextView title;

    @Bind(R.id.finishAddingClasses)
    Button finishAddingClasses;

    @Bind(R.id.finishAddingInterests)
    Button finishAddingInterests;

    public AddingFragment(){
        //
    }

    public AddingFragment newInstance(int type, User user){
        addingType = type;
        appUser = user;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adding_layout, container, false);
        ButterKnife.bind(this, view);
        final float scale = getContext().getResources().getDisplayMetrics().density;
        windowWidth = (int) (WINDOW_WIDTH * scale + 0.5f);
        windowHeight = (int) (WINDOW_HEIGHT * scale + 0.5f);

        if(addingType == ADD_COURSES){
            addingInterestsLayout.setVisibility(View.GONE);
            addingGroupsLayout.setVisibility(View.GONE);
            finishAddingClasses.setVisibility(View.GONE);
            title.setText("Add Class");
        }else if(addingType == ADD_INTERESTS){
            addingClassesLayout.setVisibility(View.GONE);
            addingGroupsLayout.setVisibility(View.GONE);
            finishAddingInterests.setVisibility(View.GONE);
            title.setText("Add Interest");
        }

        firebase = new Firebase(getString(R.string.database));

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(windowWidth, windowHeight);
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(windowWidth, windowHeight);
    }


    private void addClasses() {
        String courseID = classId.getText().toString();
        if(courseID.equals("")) {
            return;
        }
        firebase.child(getString(R.string.database_schools_key))
                .child(appUser.getSid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String courseID = classId.getText().toString();
                        String courseName = (String) dataSnapshot
                                .child(getString(R.string.school_courses_key))
                                .child(courseID)
                                .child(getString(R.string.school_courses_name))
                                .getValue();

                        //Add course to User's database branch
                        firebase.child(getString(R.string.database_users_key))
                                .child(firebase.getAuth().getUid())
                                .child(getString(R.string.user_courses_key))
                                .child(courseID)
                                .setValue(courseName);

                        //Add person into school roster
                        firebase.child(getString(R.string.database_schools_key))
                                .child(appUser.getSid())
                                .child(getString(R.string.school_courses_key))
                                .child(courseID)
                                .child(getString(R.string.school_courses_enrolled_key))
                                .child(appUser.getUid())
                                .setValue(appUser.getName());

                        dismiss();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
    }

    private void addInterests() {
        final String addInterest = interest.getText().toString().toLowerCase();
        if(addInterest.equals("")) {
            return;
        }
        firebase.child(getString(R.string.database_users_key))
                .child(appUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<String> userInterest = new ArrayList<String>();
                        if(dataSnapshot.child(getString(R.string.user_interests_key)).exists()){
                            userInterest = (ArrayList<String>) dataSnapshot.child(getString(R.string.user_interests_key)).getValue();
                        }
                        userInterest.add(addInterest);
                        //Add interest to User's database branch
                        firebase.child(getString(R.string.database_users_key))
                                .child(appUser.getUid())
                                .child(getString(R.string.user_interests_key))
                                .setValue(userInterest);

                        //Add person into school interest map
                        firebase.child(getString(R.string.database_schools_key))
                                .child(appUser.getSid())
                                .child(getString(R.string.school_interests_key))
                                .child(addInterest)
                                .child(appUser.getUid())
                                .setValue(appUser.getName());

                        dismiss();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

    }

}
