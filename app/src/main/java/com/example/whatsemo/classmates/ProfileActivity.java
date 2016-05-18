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

    @Bind(R.id.toolbar_friend_name)
    TextView toolbarFriendNameTextView;

    @OnClick(R.id.chat_back_button)
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
    }

    private void setUpView() {

        friendNameTextView.setText(friendName);
        toolbarFriendNameTextView.setText(friendName);
        Firebase newRef = firedata.child(getResources().getString(R.string.database_users_key)).child(friendId);
        newRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(getString(R.string.users_picture_key)).exists()) {
                    Bitmap bm = imageHandler.convertByteArrayToBitmap(dataSnapshot.child(getString(R.string.users_picture_key)).getValue().toString());
                    profilePictureImageView.setImageBitmap(bm);
                }

                //Prevents crashes
                isFriend = dataSnapshot.child(getString(R.string.users_friends_key)).child(appUser.getUid()).exists();


                if(isFriend){
                    addOrMessageFriendButton.setText("Message");
                }else if(dataSnapshot.child(getString(R.string.users_requests_key)).child(appUser.getUid()).exists()){
                    addOrMessageFriendButton.setText("Respond To Friend Request");
                    respondToRequest = true;
                }else{
                    //Prevents crashes
                    if(dataSnapshot.child(getString(R.string.users_requests_key)).exists()) {
                        friendRequestSent = dataSnapshot.child(getString(R.string.users_requests_key)).child(appUser.getUid()).exists();
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
