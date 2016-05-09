package com.example.whatsemo.classmates;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whatsemo.classmates.model.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by WhatsEmo on 5/8/2016.
 */
public class ProfileActivity extends Activity {

    @Bind(R.id.profile_picture)
    ImageView profilePictureImageView;

    @Bind(R.id.friend_name)
    TextView friendNameTextView;

    @Bind(R.id.friend_major)
    TextView friendMajorTextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity_layout);
        ButterKnife.bind(this);

        firedata = new Firebase(getResources().getString(R.string.database));
        imageHandler = new ImageHandler(this);

        friendId = getIntent().getStringExtra("friendID");
        friendName = getIntent().getStringExtra("friendName");
        appUser = getIntent().getParcelableExtra("appUser");
        isFriend = false;
        friendRequestSent = false;
        respondToRequest = false;

        setUpView();
    }

    private void setUpView() {

        friendNameTextView.setText(friendName);

        firedata.child(getResources().getString(R.string.database_users_key)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(friendId).child(getString(R.string.users_picture_key)).exists()) {
                    Bitmap bm = imageHandler.convertByteArrayToBitmap(dataSnapshot.child(getString(R.string.users_picture_key)).getValue().toString());
                    profilePictureImageView.setImageBitmap(bm);
                }

                //Prevents crashes
                if(dataSnapshot.child(friendId).child(getString(R.string.users_requests_key)).exists()) {
                    isFriend = dataSnapshot.child(friendId).child(getString(R.string.users_requests_key)).child(appUser.getUid()).exists();
                }

                if(isFriend){
                    addOrMessageFriendButton.setText("Message");
                }else if(dataSnapshot.child(appUser.getUid()).child("requests").child(friendId).exists()){
                    addOrMessageFriendButton.setText("Respond To Friend Request");
                    respondToRequest = true;
                }else{
                    //Prevents crashes
                    if(dataSnapshot.child(friendId).child(getString(R.string.users_requests_key)).exists()) {
                        friendRequestSent = dataSnapshot.child(friendId).child(getString(R.string.users_requests_key)).child(appUser.getUid()).exists();
                    }

                    if(friendRequestSent) {
                        addOrMessageFriendButton.setText("Friend Request Sent");
                    }else{
                        addOrMessageFriendButton.setText("Add Friend");
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    private void sendFriendRequest() {
        Map<String, Object> sentRequest = new HashMap<String,Object>();
        sentRequest.put(appUser.getUid(), appUser.getName());
        firedata.child(getResources().getString(R.string.database_users_key)).child(friendId).child(getString(R.string.users_requests_key)).child(appUser.getUid()).setValue(appUser.getName());
        addOrMessageFriendButton.setText("Friend Request Sent");
        friendRequestSent = true;
    }

    private void sendMessage() {
        Intent startChatIntent = new Intent(this, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("appUser", appUser);
        bundle.putString("friendID", friendId);
        bundle.putString("friendName", friendName);
        startChatIntent.putExtras(bundle);
        startActivity(startChatIntent);
        finish();
    }

    private void startNotification(){
        Intent startChatIntent = new Intent(this, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("appUser", appUser);
        bundle.putString("friendID", friendId);
        bundle.putString("friendName", friendName);
        startChatIntent.putExtras(bundle);
        startActivity(startChatIntent);
        finish();

    }

}
