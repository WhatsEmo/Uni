package com.example.whatsemo.classmates;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by WhatsEmo on 3/26/2016.
 */
public class TutorialActivity extends Activity {

    @Bind(R.id.addingClassesLayout)
    RelativeLayout addingClassesLayout;

    @Bind(R.id.addingInterestsLayout)
    RelativeLayout addingInterestsLayout;

    @Bind(R.id.addingGroupsLayout)
    RelativeLayout addingGroupsLayout;

    @Bind(R.id.addingSchoolsLayout)
    RelativeLayout addingSchoolsLayout;

    private List<String> userInterests;
    private List<String> userClasses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setContentView(R.layout.adding_layout);

        //Sets all layouts other than classes invisible first
        addingInterestsLayout.setVisibility(View.INVISIBLE);
        addingGroupsLayout.setVisibility(View.INVISIBLE);
        addingSchoolsLayout.setVisibility(View.INVISIBLE);

        //Intialize empty Lists
        userInterests = new ArrayList<String>();
        userClasses = new ArrayList<String>();
    }

    @Override
    public void onBackPressed(){
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}
