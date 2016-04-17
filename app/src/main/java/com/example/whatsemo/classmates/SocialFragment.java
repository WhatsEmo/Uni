package com.example.whatsemo.classmates;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.List;

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

    @Bind(R.id.coursesListView)
    ListView coursesListView;

    @Bind(R.id.interestsListView)
    ListView interestsListView;

    @Bind(R.id.groupsListView)
    ListView groupsListView;


    private static final String ARG_PAGE = "param1";

    private int mPage;

    private OnFragmentInteractionListener mListener;

    private Firebase ref;
    private List<String> userFriends;
    private List<String> userCourses;
    private List<String> userGroups;

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

        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.social_layout, container, false);
        ButterKnife.bind(this, view);
        ref = new Firebase(getResources().getString(R.string.database));


        if (ref.getAuth() != null){
            String uid = ref.getAuth().getUid();
            ref.child("users").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    userCourses.addAll(snapshot.child("courses").getValue(HashMap.class).values());
                    //userFriends.addAll(snapshot.child("friends").getValue(HashMap.class).values());
                    userGroups.addAll(snapshot.child("groups").getValue(HashMap.class).values());
                    setVisibility();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("The name read failed: " + firebaseError.getMessage());
                }
            });

        }
        else{
            System.out.println("Authentication failed");
        }



        return inflater.inflate(R.layout.social_layout, container, false);
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
        }
        */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setVisibility(){
        if(userCourses.isEmpty()){
            socialCoursesLayout.setVisibility(View.GONE);
        }else{
            socialCoursesLayout.setVisibility(View.VISIBLE);
        }

        if(userFriends.isEmpty()){
            socialFriendsLayout.setVisibility(View.GONE);
        }else{
            socialFriendsLayout.setVisibility(View.VISIBLE);
        }

        if(userGroups.isEmpty()){
            socialGroupsLayout.setVisibility(View.GONE);
        }else{
            socialGroupsLayout.setVisibility(View.VISIBLE);
        }
    }
}
