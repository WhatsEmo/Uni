package com.example.whatsemo.classmates;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.whatsemo.classmates.model.User;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Bind(R.id.classesListView)
    GridLayout classesListView;

    @Bind(R.id.interestsListView)
    GridLayout interestsListView;

    @Bind(R.id.groupsListView)
    GridLayout groupsListView;

    @OnClick(R.id.addingClassesButton)
    public void addingClasses(){ addClasses();}

    @OnClick(R.id.addingInterestsButton)
    public void addingInterests(){ addInterests(); }

    @OnClick(R.id.addingGroupsButton)
    public void addingGroups(){ addGroups(); }

    @OnClick(R.id.finishAddingClasses)
    public void finishAddingClasses(){ addToDatabase("class"); }

    @OnClick(R.id.finishAddingInterests)
    public void finishAddingInterests(){ addToDatabase("interest"); }

    @OnClick(R.id.finishAddingGroups)
    public void finishAddingGroups(){ addToDatabase("group"); }

    @Bind(R.id.classId)
    TextView classId;

    @Bind(R.id.interest)
    TextView interest;

    @Bind(R.id.groupName)
    TextView groupName;

    @Bind(R.id.addingLayoutTitle)
    TextView title;

    private List<String> userInterests;
    private List<String> userClasses;
    private List<String> userGroups;

    private Map<String, String> userCourses;

    private Firebase fireData;

    private User user;

    private QueryManager qm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adding_layout);
        ButterKnife.bind(this);
        Firebase.setAndroidContext(this);

        user = getIntent().getExtras().getParcelable("user");

        fireData = new Firebase(getResources().getString(R.string.database));

        qm = new QueryManager();
        //Sets all layouts other than classes invisible first
        addingInterestsLayout.setVisibility(View.GONE);
        addingGroupsLayout.setVisibility(View.GONE);

        //Intialize empty Lists
        userInterests = new ArrayList<String>();
        userClasses = new ArrayList<String>();
        userGroups = new ArrayList<String>();

        //Set the Title
        title.setText("Uni: Adding Classes");
    }

    //Template function to add buttons
    //Example: If we add an Interest, we use interestListView and interest TextView
    //Then we add it to the Interest ListView to show the user.
    //If user clicks on interest button, then they delete it from the list
    private void addClasses(){
        Button newClass = new Button(this);
        newClass.setText(classId.getText());

        userClasses.add(classId.getText().toString());

        newClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classesListView.removeView(v);
                userClasses.remove(classId.getText().toString());
            }
        });
        classesListView.addView(newClass);
        classId.setText("");
    }

    private void addInterests(){
        Button newInterest = new Button(this);
        newInterest.setText(interest.getText());

        userInterests.add(interest.getText().toString().toLowerCase());


        newInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interestsListView.removeView(v);
                userInterests.remove(interest.getText().toString());
            }
        });
        interestsListView.addView(newInterest);
        interest.setText("");
    }

    private void addGroups(){
        Button newGroup = new Button(this);
        newGroup.setText(groupName.getText());

        userGroups.add(groupName.getText().toString());


        newGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupsListView.removeView(v);
                userGroups.remove(groupName.getText().toString());
            }
        });
        groupsListView.addView(newGroup);
        groupName.setText("");
    }

    private void addToDatabase(String label){

        if(label.equals("class")){
            if(!userClasses.isEmpty()) {
                System.out.println(user.getName());
                qm.updateRoster(user, "courses", userClasses);
                qm.setUserCourses(user, userClasses);

                title.setText("Uni: Adding Interests");
                addingClassesLayout.setVisibility(View.GONE);
                addingInterestsLayout.setVisibility(View.VISIBLE);
            }
        }
        else if(label.equals("interest")) {
            if(!userInterests.isEmpty()) {
                qm.setListData(user, "interests", userInterests);
                qm.updateRoster(user, "interests", userInterests);

                Firebase tutorialRef = fireData.child(getResources().getString(R.string.database_users_key)).child(user.getUid()).child("isTutorialDone");; //firebase reference
                tutorialRef.setValue(null);

                finish();
            }
        }

        /*
            title.setText("Uni: Adding Groups");
            addingInterestsLayout.setVisibility(View.GONE);
            addingGroupsLayout.setVisibility(View.VISIBLE);
        }
        else if(label.equals("group")){
            Firebase addingDataRef = fireData.child(getResources().getString(R.string.database_users_key)).child(uid).child("groups"); //firebase reference
            addingDataRef.setValue(userGroups);

            Firebase makingNull = fireData.child(getResources().getString(R.string.database_users_key)).child(uid).child("isTutorialDone");; //firebase reference
            makingNull.setValue(null);

            finish();
        }
        */
    }

    @Override
    public void onBackPressed(){
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}
