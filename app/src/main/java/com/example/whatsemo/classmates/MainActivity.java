package com.example.whatsemo.classmates;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by WhatsEmo on 3/5/2016.
 */
public class MainActivity extends AppCompatActivity {

    //REMOVE THIS LATER
    @OnClick(R.id.logoutButton)
    public void logout(){
        firedata.unauth();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private Firebase firedata;
    private String uid;
    private User appUser;
    private QueryManager QM;
    private AppCompatActivity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisActivity = this;
        setContentView(R.layout.loading_screen);

        Firebase.setAndroidContext(this);
        firedata = new Firebase(getResources().getString(R.string.database));

        firedata.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    //user is logged on
                    uid = firedata.getAuth().getUid();
                    checkTutorialDone();
                    createUserObject();
                    setView();

                } else {
                    //user is not logged on
                    startLoginActivity();
                    setView();
                }
            }
        });


    }

    private void setView(){
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new BaseFragmentAdapter(getSupportFragmentManager(),
                MainActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private void startLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void createUserObject(){
        firedata.child(getResources().getString(R.string.database_users_key)).child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                QM = new QueryManager(firedata);
                // Populates a local object with user data obtained from the database
                String email = dataSnapshot.child(getResources().getString(R.string.user_email_key)).getValue(String.class);
                String name = dataSnapshot.child(getResources().getString(R.string.user_name_key)).getValue(String.class);
                String schoolId = dataSnapshot.child(getResources().getString(R.string.user_school_key)).getValue(String.class);
                List<String> interests = new ArrayList<String>();

                appUser = new User(uid, name, schoolId, email, interests);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    private void checkTutorialDone(){
        firedata.child(getResources().getString(R.string.database_users_key)).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //if isTutorialDone exists then we start the tutorial
                if (dataSnapshot.child("isTutorialDone").exists()) {
                    //start tutorial
                    Intent startTutorialIntent = new Intent(thisActivity, TutorialActivity.class);
                    startTutorialIntent.putExtra("user", appUser);
                    startActivity(startTutorialIntent);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}

