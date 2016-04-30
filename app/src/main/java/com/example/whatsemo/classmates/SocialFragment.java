package com.example.whatsemo.classmates;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
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

    @Bind(R.id.chat_search)
    EditText chatSearchEditText;

    private static final String ARG_PAGE = "param2";

    private int mPage;

    private OnFragmentInteractionListener mListener;

    private ArrayAdapter<String> adapter;

    private Firebase ref;
    private Map<String, String> courseMap;
    private List<String> userFriendIds = new ArrayList<String>();
    private List<String> userFriendNames = new ArrayList<String>();
    private List<String> userCourseIds = new ArrayList<String>();
    private List<String> userCourseNames = new ArrayList<String>();
    private List<String> userGroupIds = new ArrayList<String>();
    private List<String> userGroupNames = new ArrayList<String>();

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
            ref.child(getResources().getString(R.string.database_users_key)).child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    // Retrieves (key, value) data from friebase and stores them in proportional Java Lists
                    //Map<String, String> map = (Map<String, String>) snapshot.child(getResources().getString(R.string.user_courses_key)).getValue(Map.class);

                    System.out.println(snapshot.child(getResources().getString(R.string.user_courses_key)).getValue());
                    courseMap = (Map<String, String>) snapshot.child(getResources().getString(R.string.user_courses_key)).getValue();
                    //userCourseIds = (List<String>) map.keySet();
                    //userCourseNames = (List<String>) map.values();

                    //Map<String, String> friendMap = (Map<String, String>) snapshot.child("friends").getValue();
                    //userFriendIds = (List<String>) map.keySet();
                    //userFriendNames = (List<String>) map.values();

                    //Map<String, String> groupMap = (Map<String, String>) snapshot.child(getResources().getString(R.string.user_groups_key)).getValue();
                    //userGroupIds = (List<String>) map.keySet();
                    //userGroupNames = (List<String>) map.values();
                    setVisibility();
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

        socialCoursesLayout.setVisibility(View.VISIBLE);
        populate(courseMap, coursesRecyclerView);
        //populate(userCourseNames, coursesRecyclerView, getResources().getString(R.string.user_courses_key));

        if(userFriendNames == null || userFriendNames.isEmpty()){
            socialFriendsLayout.setVisibility(View.GONE);
        }else{
            socialFriendsLayout.setVisibility(View.VISIBLE);
            populate(userFriendNames, friendRecyclerView, "friends");
        }

        if(userFriendNames == null || userFriendNames.isEmpty()){
            socialGroupsLayout.setVisibility(View.GONE);
        }else{
            socialGroupsLayout.setVisibility(View.VISIBLE);
            populate(userFriendNames, groupsRecyclerView, getResources().getString(R.string.user_groups_key));
        }
    }

    private void populate(Map<String, String> map, RecyclerView recyclerView){
        for (String key : map.keySet()){
            Button bttn = new Button(this.getContext());
            bttn.setText(map.get(key));
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
}
