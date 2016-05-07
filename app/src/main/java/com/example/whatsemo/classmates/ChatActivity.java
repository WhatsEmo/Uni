package com.example.whatsemo.classmates;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity {

    @Bind(R.id.toolbarLayout)
    RelativeLayout toolbarLayout;

    @Bind(R.id.messagesLayout)
    LinearLayout messagesLayout;

    @Bind(R.id.inputLayout)
    RelativeLayout inputLayout;

    @Bind(R.id.recipientName)
    TextView recipientName;

    @Bind(R.id.backButton)
    Button backButton;

    @Bind(R.id.profilePicture)
    ImageView profilePicture;

    @Bind(R.id.messageInput)
    EditText messageInput;

    @Bind(R.id.chatSendButton)
    Button sendButton;

    private String senderUid;
    private String recipientUid;

    private Firebase firedata;

    private Firebase chatRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        ButterKnife.bind(this);

        firedata = new Firebase(getResources().getString(R.string.database));

        senderUid = getIntent().getExtras().getString("user");
        System.out.println(senderUid);
        recipientUid = getIntent().getExtras().getString("friend");

        checkChatExists();

        chatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println(dataSnapshot.child("message").getValue());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

    private void checkChatExists(){
        String uid = firedata.getAuth().getUid();

        firedata.child("users").child(senderUid).child("friends").child(recipientUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child("chatId").exists()){
                    chatRef = firedata.child("chats").push();
                    firedata.child("users").child(senderUid).child("friends").child(recipientUid).child("chatId").setValue(chatRef.getKey());
                }
                else{
                    chatRef = firedata.child("chats").child(dataSnapshot.child("chatId").getValue().toString());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("Failed checking chat: " + firebaseError);
            }

        });
    }

    @OnClick(R.id.chatSendButton)
    public void sendMessage(){
        String message = messageInput.getText().toString();
        messageInput.setText("");

        Firebase messageRef = chatRef.push();

        Map<String, String> messageData = new HashMap<String, String>();
        messageData.put("author", senderUid);
        messageData.put("message", message);

        messageRef.setValue(messageData);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }
}

