package com.example.whatsemo.classmates;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

/**
 * Created by WhatsEmo on 3/5/2016.
 */
public class MainActivity extends AppCompatActivity {

    private Firebase firedata;
    private User appUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

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
        startActivity(intent);
    }
}

