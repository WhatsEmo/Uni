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
    private User appUser;
    private AppCompatActivity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        Firebase.setAndroidContext(this);
        firedata = new Firebase(getResources().getString(R.string.database));
        firedata.addAuthStateListener(new Firebase.AuthStateListener(){
            @Override
            public void onAuthStateChanged(AuthData authData){
                if(authData != null){
                    //user is logged on
                }else{
                    //user is not logged on
                    startLoginActivity();
                }
            }
        });


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

    private void checkTutorialDone(){

        String uid = firedata.getAuth().getUid();

        firedata.child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //if isTutorialDone exists then we start the tutorial
                if (dataSnapshot.child("isTutorialDone").exists()) {
                    //start tutorial
                    Intent startTutoiralIntent = new Intent(thisActivity, TutorialActivity.class);
                    startActivity(startTutoiralIntent);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}

