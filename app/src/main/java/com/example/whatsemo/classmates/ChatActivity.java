package com.example.whatsemo.classmates;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        ButterKnife.bind(this);

        firedata = new Firebase(getResources().getString(R.string.database));

        senderUid = "621e31dd-c8e0-468d-abc3-c2556fbb0e74";
        recipientUid = "607d4e3a-03d7-44f6-b426-a5d85eb35b1e";

    }

    @OnClick(R.id.chatSendButton)
    public void sendMessage(){
        String message = messageInput.getText().toString();
        messageInput.setText("");
        Firebase chatRef = firedata.child("chats").child(senderUid+recipientUid);
        Firebase messageRef = chatRef.push();

        Map<String, String> messageData = new HashMap<String, String>();
        messageData.put("author", senderUid);
        messageData.put("message", message);

        messageRef.setValue(messageData);

    }
}
