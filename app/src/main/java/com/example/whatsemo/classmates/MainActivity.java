package com.example.whatsemo.classmates;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

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
    static final int DID_USER_PRESS_BACK_BUTTON = 0;
    static final int USER_PRESSED_BACK_BUTTON = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        Firebase.setAndroidContext(this);
        firedata = new Firebase("https://uni-database.firebaseio.com/");
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
        startActivityForResult(intent, DID_USER_PRESS_BACK_BUTTON);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        /*
            DID_USER_PRESS_BACK_BUTTON = 0
            Basically if the user pressed the back button in LoginActivity and didn't log in
            we don't use the MainActivity without auth code
         */

        if(requestCode == DID_USER_PRESS_BACK_BUTTON){
            if(resultCode == USER_PRESSED_BACK_BUTTON) {
                this.finish();
            }
        }
        //We do nothing if the user actually logged in

    }
}

