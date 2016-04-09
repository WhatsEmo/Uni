package com.example.whatsemo.classmates;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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
    ListView classesListView;

    @Bind(R.id.interestListView)
    ListView interestListView;

    @Bind(R.id.groupListView)
    ListView groupListView;

    @OnClick(R.id.addingClassesButton)
    public void addingClasses(){ addSomething(classesListView, classId, userClasses); }

    @OnClick(R.id.addingInterestsButton)
    public void addingInterests(){ addSomething(interestListView, interest, userInterests); }

    @OnClick(R.id.addingGroupsButton)
    public void addingGroups(){ addSomething(groupListView, groupName, userGroups); }

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

    private Firebase fireData;
    private String uid;
    private String schoolId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setContentView(R.layout.adding_layout);
        Firebase.setAndroidContext(this);

        fireData = new Firebase("https://uni-database.firebaseio.com/");
        fireData.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    //user is logged on
                    if(authData.getUid() != null){
                        uid = authData.getUid();

                    }else{
                        //if the Uid doesn't exist for some reason, we log the user out and exit the app
                        fireData.unauth();
                        onBackPressed();
                    }
                } else {
                    //This returns user to home screen
                    onBackPressed();
                }
            }
        });


        //Get school ID
        fireData.child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //Should always return a string.
                schoolId = (String) snapshot.child("school").getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        //Sets all layouts other than classes invisible first
        addingInterestsLayout.setVisibility(View.INVISIBLE);
        addingGroupsLayout.setVisibility(View.INVISIBLE);

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
    private void addSomething(final ListView viewList, TextView text, final List<String> stringList ){

        final Button newClass = new Button(this);
        newClass.setText(text.getText());

        stringList.add(text.getText().toString());


        newClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewList.removeView(newClass);
                stringList.remove(newClass.getText().toString());
            }
        });
        viewList.addView(newClass);
    }


    private void addToDatabase(String label){

        if(label == "class"){
            Firebase addingDataRef = fireData.child("users").child(uid); //firebase reference
            addingDataRef.setValue("classes",userClasses);

            title.setText("Uni: Adding Interests");
            addingClassesLayout.setVisibility(View.INVISIBLE);
            addingInterestsLayout.setVisibility(View.VISIBLE);
        }else if(label == "interest"){
            Firebase addingDataRef = fireData.child("users").child(uid); //firebase reference
            addingDataRef.setValue("interests",userInterests);


            title.setText("Uni: Adding Groups");
            addingInterestsLayout.setVisibility(View.INVISIBLE);
            addingGroupsLayout.setVisibility(View.VISIBLE);
        }else if(label == "group"){

            Firebase addingDataRef = fireData.child("users").child(uid); //firebase reference
            addingDataRef.setValue("isTutorialDone", null);

            finish();
        }
    }


    @Override
    public void onBackPressed(){
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}
