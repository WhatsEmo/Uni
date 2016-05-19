package com.example.whatsemo.classmates;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.widget.ImageButton;

import com.example.whatsemo.classmates.adapter.SearchResultsAdapter;
import com.example.whatsemo.classmates.model.Course;
import com.example.whatsemo.classmates.model.Friend;
import com.example.whatsemo.classmates.model.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by WhatsEmo on 4/29/2016.
 */
public class SearchActivity extends Activity {

    @Bind(R.id.search_results)
    RecyclerView searchResultsRecyclerView;

    @Bind(R.id.search_back_button)
    ImageButton searchBackButton;

    @Bind(R.id.activity_action_search)
    SearchView searchInfo;

    @OnClick(R.id.search_back_button)
    public void back(){
        onBackPressed();
    }

    private Firebase firedata;
    private String userID;
    private User appUser;
    private ArrayList<Course> userCourses;
    private ArrayList<Friend> searchResults;
    private Map<String, List<Friend>> interestQuery;
    private ArrayList<Friend> classmates;
    private RecyclerView.LayoutManager searchLayoutManager;
    private SearchResultsAdapter searchResultAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        ButterKnife.bind(this);
        createSearchBar();

        interestQuery = new HashMap<String, List<Friend>>();
        userCourses = new ArrayList<Course>();
        searchResults = new ArrayList<Friend>();
        classmates = new ArrayList<Friend>();

        appUser = getIntent().getExtras().getParcelable("appUser");

        firedata = new Firebase(getResources().getString(R.string.database));
        firedata.setAndroidContext(this);
        userID = firedata.getAuth().getUid();


        // use a linear layout manager
        searchLayoutManager = new LinearLayoutManager(this);
        searchResultsRecyclerView.setLayoutManager(searchLayoutManager);

        //Listener to grab User data.
        //WILL DELETE AFTER WE IMPLEMENT USER PASSING THROUGH ACTIVITIES
        //Create a new reference to allow a new ValueEventLisnter
        Firebase currentUserData = firedata.child(getResources().getString(R.string.database_users_key)).child(userID);
        currentUserData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getCurrentUserData(dataSnapshot);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //Listener for when someone adds a new interst in the user's school
        //***************************CHANGE CHILD "UCI" TO USER SCHOOL************************************
        //Create a new reference to allow a new ValueEventLisnter
        Firebase interestData = firedata.child(getResources().getString(R.string.database_schools_key)).child("uci").child(getResources().getString(R.string.school_interests_key));
        interestData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //What we're grabbing from DB:
                //Map<interestName, Map<UID, Name>>
                Map<String, Map<String, String>> getData;
                if (dataSnapshot.exists()) {
                    getData = (Map<String, Map<String, String>>) dataSnapshot.getValue();
                    if(getData != null) {
                        for (Map.Entry<String, Map<String, String>> data : getData.entrySet()) {
                            List<Friend> checkUpdates = new ArrayList<Friend>();
                            for (Map.Entry<String, String> innerData : data.getValue().entrySet()) {
                                //Prevents the user him/herself from being added into the list
                                if (!innerData.getKey().equals(userID)) {
                                    Friend friend = new Friend(innerData.getKey(), innerData.getValue());
                                    checkUpdates.add(friend);
                                }
                            }

                            //Check to prevent crashes and increase speed
                            if (interestQuery.get(data.getKey()) == null) {
                                interestQuery.put(data.getKey(), checkUpdates);
                            } else if (interestQuery.get(data.getKey()).size() != checkUpdates.size()) {
                                //If there is a difference, then we will update the data we have
                                interestQuery.get(data.getKey()).clear();
                                interestQuery.get(data.getKey()).addAll(checkUpdates);
                            }
                            //Else nothing happens
                        }
                    }

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //Listener for when someone adds a new class in the user's school
        //***************************CHANGE CHILD "UCI" TO USER SCHOOL************************************
        //Create a new reference to allow a new ValueEventLisnter
        Firebase coursesData = firedata.child(getResources().getString(R.string.database_schools_key)).child("uci").child(getResources().getString(R.string.school_courses_key));
        coursesData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Friend> queryClassmates = new ArrayList<Friend>();
                for (Course course : userCourses) {
                    Map<String, String> roster = (Map<String, String>) dataSnapshot.child(course.getCourseID()).child("roster").getValue();
                    for (HashMap.Entry<String, String> data : roster.entrySet()) {
                        //If the UID is the current app user, then we don't add him/her into the list
                        if (!data.getKey().equals(userID)) {
                            Friend friend = new Friend(data.getKey(), data.getValue());
                            //Prevents adding duplicate friends
                            if(!queryClassmates.contains(friend)) {
                                queryClassmates.add(friend);
                            }
                        }
                    }
                }

                //If empty, just add all the classmates
                if (classmates.isEmpty()) {
                    classmates.addAll(queryClassmates);
                } else if (classmates.size() != queryClassmates.size()) {
                    classmates.clear();
                    classmates.addAll(queryClassmates);
                }

                // specify an adapter
                //Put this here since it will be the default amount shown when the user enters the Activity.
                searchResultAdapter = new SearchResultsAdapter(classmates, getApplicationContext(), appUser, searchInfo);
                searchResultsRecyclerView.setAdapter(searchResultAdapter);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                searchResultAdapter = new SearchResultsAdapter(classmates, getApplicationContext(), appUser, searchInfo);
                searchResultsRecyclerView.setAdapter(searchResultAdapter);
            }
        });
    }

    /*
        Gets needed User data like courses and possibly interests (later on)
     */
    public void getCurrentUserData(DataSnapshot snapshot){
        Map<String, String> getData;
        boolean hasCourses = snapshot.child(getResources().getString(R.string.user_courses_key)).exists();
        boolean hasInterests = snapshot.child(getResources().getString(R.string.user_interests_key)).exists();

        if(hasCourses){
            getData = (Map<String, String>) snapshot.child(getResources().getString(R.string.user_courses_key)).getValue();
            for(Map.Entry<String, String> data: getData.entrySet()){
                Course course = new Course(data.getKey(), data.getValue());

                if(!userCourses.contains(course)){
                    userCourses.add(course);
                }
            }

        }else{
            //Tell User that they need to add courses first
        }
        /*
        if(hasInterests){
            getData = (Map<String, String>) snapshot.child(getResources().getString(R.string.user_interests_key)).getValue();
            for(Map.Entry<String, String> data: getData.entrySet()){
                Interest interest = new Interest(data.getKey(), data.getValue());

                if(!userInterests.contains(interest)){
                    userInterests.add(interest);
                }
            }

        }*/

    }

    /*
        Sets up the SearchView and adds an Input Listener
     */
    private void createSearchBar(){
        final SearchView searchView = searchInfo;
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                final ArrayList<Friend> filteredFriends = filterQuery(query);
                searchResultAdapter.animateTo(filteredFriends);
                searchResultsRecyclerView.scrollToPosition(0);
                searchResultAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }
    /*
        Filters the friends list while searching.
        Example: I type "i" then query = "i"
        Looking for: Names that have "i" and interests with "i"
        Objects used: ArrayList<Friend> classmates and Map<String,List<Friend>> interestQuery.
     */
    private ArrayList<Friend> filterQuery(String query) {
        final String lowercaseQuery = query.toLowerCase();

        final ArrayList<Friend> filteredFriends = new ArrayList<>();
        for (final Friend friend : classmates) {
            final String text = friend.getName().toLowerCase();
            if (text.contains(lowercaseQuery)) {
                filteredFriends.add(friend);
                //Need this statement or it will add duplicate Friend Objects if friend has a matching interest.
                continue;
            }

            for(String key : interestQuery.keySet()){
                if(key.toLowerCase().contains(lowercaseQuery)){
                    if(interestQuery.get(key).contains(friend)){
                        filteredFriends.add(friend);
                        //Need this statement or it will add duplicate Friend Objects if friend has multiple matching interests
                        break;
                    }
                }
            }
        }
        return filteredFriends;
    }
}
