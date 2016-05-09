package com.example.whatsemo.classmates;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.example.whatsemo.classmates.model.User;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
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
    TextView recipientNameTextView;

    @Bind(R.id.messageInput)
    EditText messageInput;

    @Bind(R.id.chatSendButton)
    Button sendButton;

    @Bind(R.id.profile_picture)
    ImageView profilePicture;

    @Bind(R.id.messagesView)
    RecyclerView messagesRecyclerView;

    @OnClick(R.id.chat_back_button)
    public void finishActivity(){
        finish();
    }

    private User sender;
    private String senderUid;
    private String recipientUid;
    private String senderName;
    private String recipientName;
    private Bitmap bm;

    private Firebase firedata;
    private Firebase chatRef;

    private ArrayList<Message> messages;
    private ImageHandler imageHandler;

    private MessageAdapter messagesAdapter;
    private RecyclerView.LayoutManager messagesLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        ButterKnife.bind(this);

        firedata = new Firebase(getResources().getString(R.string.database));

        imageHandler = new ImageHandler(this);

        sender = getIntent().getExtras().getParcelable("appUser");

        senderUid = sender.getUid();
        senderName = sender.getName();
        recipientUid = getIntent().getExtras().getString("friendID");
        recipientName = getIntent().getExtras().getString("friendName");

        recipientNameTextView.setText(recipientName);

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

        firedata.child("users").child(recipientUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child("friends").child(senderUid).child("chatId").exists()) {
                    //If the chat hasn't been created yet, create a new chatID
                    chatRef = firedata.child("chats").push();
                    firedata.child("users").child(senderUid).child("friends").child(recipientUid).child("chatId").setValue(chatRef.getKey());
                    firedata.child("users").child(recipientUid).child("friends").child(senderUid).child("chatId").setValue(chatRef.getKey());
                } else {
                    //If chat has been created, add it into the chat List
                    chatRef = firedata.child("chats").child(dataSnapshot.child("friends").child(senderUid).child("chatId").getValue().toString());
                }

                if (dataSnapshot.child(getString(R.string.users_picture_key)).exists()) {
                    bm = imageHandler.convertByteArrayToBitmap(dataSnapshot.child(getString(R.string.users_picture_key)).getValue().toString());
                    profilePicture.setImageBitmap(bm);
                }

                chatRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String authorName;
                        char mode;
                        String authorId = dataSnapshot.child("author").getValue().toString();
                        String message = dataSnapshot.child("message").getValue().toString();
                        String timeStamp = dataSnapshot.child("timestamp").getValue().toString();
                        Message newMessage;

                        if (authorId.equals(senderUid)) {
                            authorName = senderName;
                            mode = 'r';
                        } else {
                            authorName = recipientName;
                            mode = 'l';
                        }


                        if (mode != 'r') {
                            newMessage = new Message(authorId, authorName, mode, message, timeStamp, bm);
                        } else {
                            newMessage = new Message(authorId, authorName, mode, message, timeStamp);
                        }
                        messages.add(newMessage);
                        messagesAdapter.notifyDataSetChanged();
                        messagesRecyclerView.smoothScrollToPosition(messagesAdapter.getItemCount());
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
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
        //STILL NEEDS TO CHECK IF IT'S JUST SPACES
        if(!messageInput.getText().toString().equals("")) {
            String message = messageInput.getText().toString();
            messageInput.setText("");

            Firebase messageRef = chatRef.push();
            java.util.Date date = new java.util.Date();
            //Calendar calendar = Calendar.getInstance();
            //int timeNow = calendar.get(Calendar.SECOND);

            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
            String timeNow = sdf.format(date);

            Map<String, String> messageData = new HashMap<String, String>();
            messageData.put("author", senderUid);
            messageData.put("message", message);
            messageData.put("timestamp", timeNow);

            messageRef.setValue(messageData);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }
}

