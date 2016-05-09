package com.example.whatsemo.classmates;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import butterknife.Bind;
import butterknife.ButterKnife;

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

    private Firebase firedata;
    private String friendId;
    private ImageHandler imageHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity_layout);
        ButterKnife.bind(this);

        firedata = new Firebase(getResources().getString(R.string.database));
        imageHandler = new ImageHandler(this);

        friendId = getIntent().getStringExtra("friendID");
        friendNameTextView.setText(getIntent().getStringExtra("friendName"));

        setUpView();
    }

    private void setUpView() {
        firedata.child(getResources().getString(R.string.database_users_key)).child(friendId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("picture").exists()) {
                    Bitmap bm = imageHandler.convertByteArrayToBitmap(dataSnapshot.child("picture").getValue().toString());
                    profilePictureImageView.setImageBitmap(bm);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}
