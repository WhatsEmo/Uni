package com.example.whatsemo.classmates;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.whatsemo.classmates.adapter.CourseAdapter;
import com.example.whatsemo.classmates.adapter.FriendAdapter;
import com.example.whatsemo.classmates.adapter.GroupAdapter;
import com.example.whatsemo.classmates.model.Course;
import com.example.whatsemo.classmates.model.DatabaseObject;
import com.example.whatsemo.classmates.model.Friend;
import com.example.whatsemo.classmates.model.Group;
import com.example.whatsemo.classmates.model.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SocialFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SocialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SocialFragment extends Fragment {

    @Bind(R.id.SocialCoursesLayout)
    RelativeLayout socialCoursesLayout;

    @Bind(R.id.SocialFriendsLayout)
    RelativeLayout socialFriendsLayout;

    @Bind(R.id.SocialGroupsLayout)
    RelativeLayout socialGroupsLayout;

    @Bind(R.id.coursesRecyclerView)
    RecyclerView coursesRecyclerView;

    @Bind(R.id.friendRecyclerView)
    RecyclerView friendRecyclerView;

    @Bind(R.id.groupsRecyclerView)
    RecyclerView groupsRecyclerView;

    @Bind(R.id.action_search)
    SearchView actionSearch;

    private static final String ARG_PAGE = "param2";

    private int mPage;

    private OnFragmentInteractionListener mListener;


    private Firebase ref;
    private User appUser;
    private Comparator<DatabaseObject> customSorter;
    private Map<String, String> retrieveDataMap;

    private ArrayList<Friend> userFriends;
    private ArrayList<Course> userCourses;
    private ArrayList<Group> userGroups;


    private CourseAdapter coursesAdapter;
    private RecyclerView.LayoutManager coursesLayoutManager;


    private FriendAdapter friendsAdapter;
    private RecyclerView.LayoutManager friendsLayoutManager;

    private GroupAdapter groupsAdapter;
    private RecyclerView.LayoutManager groupsLayoutManager;


    public SocialFragment() {
        // Required empty public constructor
    }

    public static SocialFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
                SocialFragment fragment = new SocialFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        appUser = ((MainActivity)getActivity()).getUser();
        System.out.println(appUser.getName());

        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.social_layout, container, false);

        ButterKnife.bind(this, view);

        customSorter = new Comparator<DatabaseObject>() {
            @Override
            public int compare(DatabaseObject lhs, DatabaseObject rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        };

        userFriends = new ArrayList<Friend>();
        userCourses = new ArrayList<Course>();
        userGroups = new ArrayList<Group>();



        ref = new Firebase(getResources().getString(R.string.database));

        if (ref.getAuth() != null){
            String uid = ref.getAuth().getUid();
            ref.child(getResources().getString(R.string.database_users_key)).child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    //Used to prevent crashes from happening
                    boolean hasCourses = snapshot.child(getResources().getString(R.string.user_courses_key)).exists();
                    boolean hasFriends = snapshot.child(getResources().getString(R.string.user_friends_key)).exists();
                    boolean hasGroups = snapshot.child(getResources().getString(R.string.user_groups_key)).exists();

                    if (hasCourses) {
                        retrieveDataMap = (Map<String, String>) snapshot.child(getResources().getString(R.string.user_courses_key)).getValue();
                        for (Map.Entry<String, String> entry : retrieveDataMap.entrySet()) {
                            Course course = new Course(entry.getKey(), entry.getValue());
                            if(!userCourses.contains(course)){
                                userCourses.add(course);
                            }
                        }
                    }
                    if (hasFriends) {
                        retrieveDataMap = (Map<String, String>) snapshot.child(getResources().getString(R.string.user_friends_key)).getValue();
                        for (Map.Entry<String, String> entry : retrieveDataMap.entrySet()) {
                            Friend friend = new Friend(entry.getKey(), entry.getValue());
                            if(!userFriends.contains(friend)){
                                userFriends.add(friend);
                            }
                        }

                    }
                    if (hasGroups) {
                        retrieveDataMap = (Map<String, String>) snapshot.child(getResources().getString(R.string.user_groups_key)).getValue();
                        for (Map.Entry<String, String> entry : retrieveDataMap.entrySet()) {
                            Group group = new Group(entry.getKey(), entry.getValue());
                            if(!userGroups.contains(group)){
                                userGroups.add(group);
                            }
                        }
                    }
                    setVisibility();
                    setUpAdapters();
                    createSearchBar();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("The name read failed: " + firebaseError.getMessage());
                }
            });

        }
        else{
            System.out.println("Social: Authentication failed");
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setVisibility(){

        if(userCourses == null || userCourses.isEmpty()){
            socialCoursesLayout.setVisibility(View.GONE);
        }
        else{
            socialCoursesLayout.setVisibility(View.VISIBLE);
        }

        if(userFriends == null || userFriends.isEmpty()){
            socialFriendsLayout.setVisibility(View.GONE);
        }else{
            socialFriendsLayout.setVisibility(View.VISIBLE);
        }

        if(userGroups == null || userGroups.isEmpty()){
            socialGroupsLayout.setVisibility(View.GONE);
        }else{
            socialGroupsLayout.setVisibility(View.VISIBLE);
        }
    }
/*
    private void populate(List<DatabaseObject> userData, RecyclerView recyclerView){
        for (DatabaseObject data : userData){
            Button bttn = new Button(this.getContext());
            bttn.setText(data.getName());
            bttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Add stuff later
                }
            });

            recyclerView.addView(bttn);
        }
    }

    private void populate(List<String> list, RecyclerView recyclerView, String label){
        //Helps populate all ListViews
        for(String courses : list){
            Button bttn = new Button(this.getContext());
            bttn.setText(courses);
            bttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Add stuff later
                }
            });

            recyclerView.addView(bttn);
        }
    }
*/
    /*
        Sets up RecycleViewAdapters and a LayoutManager
     */
    private void setUpAdapters(){
        /*
            *************COURSES*************
         */
        // use a linear layout manager
        coursesLayoutManager = new LinearLayoutManager(this.getActivity());
        coursesRecyclerView.setLayoutManager(coursesLayoutManager);

        // specify an adapter
        coursesAdapter = new CourseAdapter(userCourses, getActivity());
        coursesRecyclerView.setAdapter(coursesAdapter);

        /*
            *************FRIENDS*************
         */

        // use a linear layout manager
        friendsLayoutManager = new LinearLayoutManager(this.getActivity());
        friendRecyclerView.setLayoutManager(friendsLayoutManager);

        // specify an adapter
        friendsAdapter = new FriendAdapter(userFriends, getContext(), appUser.getUid());
        friendRecyclerView.setAdapter(friendsAdapter);

        /*
            *************GROUPS*************
         */

        // use a linear layout manager
        groupsLayoutManager = new LinearLayoutManager(this.getContext());
        groupsRecyclerView.setLayoutManager(groupsLayoutManager);

        // specify an adapter
        groupsAdapter = new GroupAdapter(userGroups, getContext());
        groupsRecyclerView.setAdapter(groupsAdapter);
    }

    private void createSearchBar(){
        final SearchView searchView = actionSearch;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //***********COURSES***********
                final ArrayList<Course> filteredCourses = filterCourses(userCourses, query);
                coursesAdapter.animateTo(filteredCourses);
                coursesRecyclerView.scrollToPosition(0);

                //***********FRIENDS***********
                final ArrayList<Friend> filteredFriends = filterFriends(userFriends, query);
                friendsAdapter.animateTo(filteredFriends);
                friendRecyclerView.scrollToPosition(0);

                //***********GROUPS***********
                final ArrayList<Group> filteredGroups = filterGroups(userGroups, query);
                groupsAdapter.animateTo(filteredGroups);
                groupsRecyclerView.scrollToPosition(0);
/*
                if(!filteredCourses.isEmpty() && coursesRecyclerView.getVisibility() == View.GONE){
                    coursesRecyclerView.setVisibility(View.VISIBLE);
                }else {
                    coursesRecyclerView.setVisibility(View.GONE);
                }
                if(!filteredFriends.isEmpty() && friendRecyclerView.getVisibility() == View.GONE){
                    friendRecyclerView.setVisibility(View.VISIBLE);
                }else {
                    friendRecyclerView.setVisibility(View.GONE);
                }
                if(!filteredGroups.isEmpty() && groupsRecyclerView.getVisibility() == View.GONE){
                    groupsRecyclerView.setVisibility(View.VISIBLE);
                }else {
                    groupsRecyclerView.setVisibility(View.GONE);
                }
*/
                coursesAdapter.notifyDataSetChanged();
                friendsAdapter.notifyDataSetChanged();
                groupsAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private ArrayList<Course> filterCourses(ArrayList<Course> courses, String query) {
        query = query.toLowerCase();

        final ArrayList<Course> filteredCourses = new ArrayList<>();
        for (Course object : courses) {
            final String text = object.getName().toLowerCase();
            if (text.contains(query)) {
                filteredCourses.add(object);
            }
        }
        return filteredCourses;
    }


    private ArrayList<Friend> filterFriends(ArrayList<Friend> friends, String query) {
        query = query.toLowerCase();

        final ArrayList<Friend> filteredFriends = new ArrayList<>();
        for (Friend object : friends) {
            final String text = object.getName().toLowerCase();
            if (text.contains(query)) {
                filteredFriends.add(object);
            }
        }
        return filteredFriends;
    }


    private ArrayList<Group> filterGroups(ArrayList<Group> groups, String query) {
        query = query.toLowerCase();

        final ArrayList<Group> filteredGroups = new ArrayList<>();
        for (Group object : groups) {
            final String text = object.getName().toLowerCase();
            if (text.contains(query)) {
                filteredGroups.add(object);
            }
        }
        return filteredGroups;
    }

}
