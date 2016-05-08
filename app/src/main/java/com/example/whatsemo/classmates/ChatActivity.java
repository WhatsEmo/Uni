package com.example.whatsemo.classmates;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.whatsemo.classmates.adapter.MessageAdapter;
import com.example.whatsemo.classmates.model.Message;
import com.firebase.client.ChildEventListener;
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

public class ChatActivity extends AppCompatActivity {

    @Bind(R.id.toolbarLayout)
    RelativeLayout toolbarLayout;

    //@Bind(R.id.messagesLayout)
    //LinearLayout messagesLayout;

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

    @Bind(R.id.messagesView)
    RecyclerView messagesRecyclerView;

    private String senderUid;
    private String recipientUid;

    private Firebase firedata;
    private Firebase chatRef;

    private ArrayList<Message> messages;

    private MessageAdapter messagesAdapter;
    private RecyclerView.LayoutManager messagesLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        ButterKnife.bind(this);

        firedata = new Firebase(getResources().getString(R.string.database));

        senderUid = getIntent().getExtras().getString("user");
        System.out.println(senderUid);
        recipientUid = getIntent().getExtras().getString("friend");
        messages = new ArrayList<Message>();

        checkChatExists();

        //User linear layout manager that starts from bottom up
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        messagesLayoutManager = layoutManager;
        messagesRecyclerView.setLayoutManager(messagesLayoutManager);

        //Set up adapter
        messagesAdapter = new MessageAdapter(messages, this);
        messagesRecyclerView.setAdapter(messagesAdapter);

    }

    private void checkChatExists(){
        String uid = firedata.getAuth().getUid();

        firedata.child("users").child(senderUid).child("friends").child(recipientUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child("chatId").exists()){
                    //If the chat hasn't been created yet, create a new chatID
                    chatRef = firedata.child("chats").push();
                    firedata.child("users").child(senderUid).child("friends").child(recipientUid).child("chatId").setValue(chatRef.getKey());
                    firedata.child("users").child(recipientUid).child("friends").child(senderUid).child("chatId").setValue(chatRef.getKey());
                }
                else{
                    //If chat has been created, add it into the chat List
                    chatRef = firedata.child("chats").child(dataSnapshot.child("chatId").getValue().toString());
                    firedata.child("chats").child(dataSnapshot.child("chatId").getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Map<String, Map<String, String>> allChatMessages = (Map<String, Map<String, String>>) dataSnapshot.getValue();
                            for(Map.Entry<String,  Map<String,String>> messageInfo : allChatMessages.entrySet()){
                                Message newMessage = new Message(messageInfo.getValue().get("author"), messageInfo.getValue().get("message"), "TIME");
                                messages.add(newMessage);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
                chatRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String author = dataSnapshot.child("author").getValue().toString();
                        String message = dataSnapshot.child("message").getValue().toString();
                        String timeStamp = dataSnapshot.child("timestamp").getValue().toString();

                        Message newMessage = new Message(author,message,timeStamp);
                        messages.add(newMessage);
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

