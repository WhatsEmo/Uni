package com.example.whatsemo.classmates;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whatsemo.classmates.adapter.ProfileCourseAdapter;
import com.example.whatsemo.classmates.adapter.ProfileInterestAdapter;
import com.example.whatsemo.classmates.model.Course;
import com.example.whatsemo.classmates.model.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by WhatsEmo on 5/8/2016.
 */
public class ProfileActivity extends AppCompatActivity {

    @Bind(R.id.profile_picture)
    ImageView profilePictureImageView;

    @Bind(R.id.friend_name)
    TextView friendNameTextView;

    @Bind(R.id.friend_major)
    TextView friendMajorTextView;

    @Bind(R.id.toolbar_friend_name)
    TextView toolbarFriendNameTextView;

    @Bind(R.id.profile_schedule_view)
    ImageView scheduleView;

    @Bind(R.id.profile_courses_view)
    RecyclerView coursesRecyclerView;

    @Bind(R.id.profile_interests_view)
    RecyclerView interestsRecyclerView;

    @OnClick(R.id.profile_back_button)
    public void finishActivity(){
        finish();
    }

    @Bind(R.id.add_or_message_friend)
    Button addOrMessageFriendButton;

    @OnClick(R.id.add_or_message_friend)
    public void sendRequest(){
        if(isFriend){
            sendMessage();
        }else if(respondToRequest) {
            startNotification();
        }else if(!friendRequestSent){
            sendFriendRequest();
        }
    }

    private Firebase firedata;
    private String friendId;
    private String friendName;
    private ImageHandler imageHandler;
    private User appUser;
    private boolean isFriend;
    private boolean friendRequestSent;
    private boolean respondToRequest;

    private ArrayList<Course> userCourses = new ArrayList<Course>();
    private ArrayList<String> userInterests = new ArrayList<String>();

    private LinearLayoutManager coursesLayoutManager;
    private LinearLayoutManager interestsLayoutManager;

    private ProfileCourseAdapter courseAdapter;
    private ProfileInterestAdapter interestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity_layout);
        ButterKnife.bind(this);

        firedata = new Firebase(getResources().getString(R.string.database));
        firedata.setAndroidContext(this);
        imageHandler = new ImageHandler(this);

        friendId = getIntent().getStringExtra("recipientId");
        friendName = getIntent().getStringExtra("recipientName");
        appUser = getIntent().getParcelableExtra("appUser");
        isFriend = false;
        friendRequestSent = false;
        respondToRequest = false;

        setUpView();

        firedata.child(getResources().getString(R.string.database_users_key)).child(friendId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child(getString(R.string.user_interests_key)).exists()) {
                    ArrayList<String> checkInterests = (ArrayList<String>) snapshot.child(getString(R.string.user_interests_key)).getValue();

                    if (userInterests.size() != checkInterests.size()) {
                        userInterests.clear();
                        userInterests.addAll(checkInterests);
                        interestAdapter.notifyDataSetChanged();
                    }
                }

                if (snapshot.child(getString(R.string.user_courses_key)).exists()) {
                    ArrayList<Course> checkCourses = new ArrayList<Course>();
                    for (Map.Entry<String, String> courseMapEntry : ((Map<String, String>) snapshot.child(getString(R.string.user_courses_key)).getValue()).entrySet()) {
                        Course course = new Course(courseMapEntry.getKey(), courseMapEntry.getValue());
                        checkCourses.add(course);
                    }

                    if (checkCourses.size() != userCourses.size()) {
                        userCourses.clear();
                        userCourses.addAll(checkCourses);
                        courseAdapter.notifyDataSetChanged();
                    }
                }
                setUpAdapters();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("Profile data check failed: " + firebaseError);
            }
        });
        setUpAdapters();
    }

    private void setUpView() {
        friendNameTextView.setText(friendName);
        toolbarFriendNameTextView.setText(friendName);

        firedata.child(getResources().getString(R.string.database_users_key)).child(friendId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Prevents crashes
                isFriend = dataSnapshot.child(getString(R.string.users_friends_key)).child(appUser.getUid()).exists();
                if (dataSnapshot.child(getString(R.string.users_picture_key)).exists() && isFriend) {
                    Bitmap bm = imageHandler.convertByteArrayToBitmap(dataSnapshot.child(getString(R.string.users_picture_key)).getValue().toString());
                    profilePictureImageView.setImageBitmap(bm);
                }

                if (isFriend) {
                    addOrMessageFriendButton.setText("Message");
                } else if (dataSnapshot.child(getString(R.string.users_requests_key)).child(appUser.getUid()).exists()) {
                    addOrMessageFriendButton.setText("Friend Request Sent");
                    addOrMessageFriendButton.setOnClickListener(null);
                    friendRequestSent = true;
                } else {
                    //Prevents crashes
                    if (dataSnapshot.child(getString(R.string.users_requests_key)).exists()) {
                        respondToRequest = dataSnapshot.child(getString(R.string.users_requests_key)).child(appUser.getUid()).exists();
                    }

                    // Hides the schedule and the profile pic if users are not friends
                    scheduleView.setVisibility(View.INVISIBLE);
                    profilePictureImageView.setImageResource(R.drawable.default_profile_pic);

                    if (friendRequestSent) {
                        addOrMessageFriendButton.setText("Friend Request Sent");
                    } else {
                        addOrMessageFriendButton.setText("Add Friend");
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void setUpAdapters(){
        //**********COURSES***********
        coursesLayoutManager = new LinearLayoutManager(this);
        coursesLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        coursesRecyclerView.setLayoutManager(coursesLayoutManager);

        courseAdapter = new ProfileCourseAdapter(userCourses, this, getSupportFragmentManager() , firedata, appUser, 1);
        coursesRecyclerView.setAdapter(courseAdapter);

        //**********INTEREST***********
        interestsLayoutManager = new LinearLayoutManager(this);
        interestsLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        interestsRecyclerView.setLayoutManager(interestsLayoutManager);

        interestAdapter = new ProfileInterestAdapter(userInterests, this, getSupportFragmentManager(), firedata, appUser, 1);
        interestsRecyclerView.setAdapter(interestAdapter);
    }


    private void sendFriendRequest() {
        Map<String, Object> sentRequest = new HashMap<String,Object>();
        sentRequest.put(appUser.getUid(), appUser.getName());
        firedata.child(getResources().getString(R.string.database_users_key)).child(friendId).child(getString(R.string.users_requests_key)).child(appUser.getUid()).setValue(appUser.getName());
        addOrMessageFriendButton.setText("Friend Request Sent");
        friendRequestSent = true;
    }

    private void sendMessage() {
        //Opens Chat Activity
        Intent startChatIntent = new Intent(this, ChatActivity.class);
        Bundle bundle = new Bundle();
        HashMap<String, String> members = new HashMap<>();

        members.put(friendId, friendName);

        bundle.putParcelable("appUser", appUser);
        bundle.putSerializable("members", members);
        bundle.putString("recipientId", friendId);
        bundle.putString("recipientName", friendName);
        startChatIntent.putExtras(bundle);
        startActivity(startChatIntent);
        finish();
    }

    private void startNotification(){
        Intent startChatIntent = new Intent(this, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("appUser", appUser);
        bundle.putString("recipientId", friendId);
        bundle.putString("recipientName", friendName);
        startChatIntent.putExtras(bundle);
        startActivity(startChatIntent);
        finish();

    }

}
