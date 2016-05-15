package com.example.whatsemo.classmates.fragment;

import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.whatsemo.classmates.R;
import com.example.whatsemo.classmates.adapter.FriendAdapter;
import com.example.whatsemo.classmates.model.Friend;
import com.example.whatsemo.classmates.model.User;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by marcelo on 5/13/2016.
 */
public class NewGroupFragment extends DialogFragment {

    @Bind(R.id.new_group_name)
    EditText newGroupName;

    @Bind(R.id.search_members)
    android.support.v7.widget.SearchView searchView;

    @Bind(R.id.friends_result_view)
    RecyclerView recyclerView;

    @OnClick(R.id.confirm_group_button)
    public void confirmGroup(){
        addToDatabase();
    }

    private String groupName;
    private HashMap<String, String> members;
    private ArrayList<Friend> friendList;
    private User appUser;

    private FriendAdapter friendAdapter;

    private Firebase firedata;

    public NewGroupFragment(){
        // Required empty public constructor
    }

    /*
    public NewGroupFragment(ArrayList<Friend> friends){
        friendList = friends;
    }
    */

    public static NewGroupFragment newInstance(ArrayList<Friend> friends, User appUser) {
        NewGroupFragment fragment = new NewGroupFragment();

        // Get arguments passed in, if any
        Bundle args = fragment.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        // Add parameters to the argument bundle
        args.putSerializable("friends", friends);
        args.putParcelable("user", appUser);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        friendList = (ArrayList<Friend>) args.getSerializable("friends");
        appUser = args.getParcelable("user");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_group_layout, container, false);
        ButterKnife.bind(this, view);

        firedata = new Firebase(getResources().getString(R.string.database));

        members = new HashMap<String, String>();

        setUpAdapter();
        setUpSearch();

        return view;
    }

    private void setUpAdapter(){
        RecyclerView.LayoutManager recyclerViewLayout = new LinearLayoutManager(this.getActivity().getApplicationContext());
        recyclerView.setLayoutManager(recyclerViewLayout);

        friendAdapter = new FriendAdapter(friendList, getActivity(), appUser, 1);
        recyclerView.setAdapter(friendAdapter);
    }

    private void setUpSearch(){

        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                final ArrayList<Friend> filteredFriends = filterQuery(query);
                friendAdapter.animateTo(filteredFriends);
                recyclerView.scrollToPosition(0);
                friendAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private ArrayList<Friend> filterQuery(String query) {
        final String lowercaseQuery = query.toLowerCase();

        final ArrayList<Friend> filteredFriends = new ArrayList<>();
        for (final Friend friend : friendList) {
            final String text = friend.getName().toLowerCase();
            if (text.contains(lowercaseQuery)) {
                filteredFriends.add(friend);
            }
        }
        return filteredFriends;
    }

    private void addToDatabase(){
        Firebase groupRef = firedata.child("groups").push();

        groupName = newGroupName.getText().toString();

        if(!groupName.isEmpty()) {
            firedata.child("groups").child(groupRef.getKey()).child("name").setValue(groupName);
            firedata.child("groups").child(groupRef.getKey()).child("members").setValue(members);
            NewGroupFragment.this.dismiss();
        }
        else {
            newGroupName.setText("Please enter a name!");
            newGroupName.setTextColor(Color.parseColor("#0627FF"));
        }
    }
}
