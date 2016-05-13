package com.example.whatsemo.classmates.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.whatsemo.classmates.R;
import com.example.whatsemo.classmates.model.Friend;
import com.example.whatsemo.classmates.model.User;
import com.firebase.client.Firebase;

import java.util.ArrayList;

/**
 * Created by WhatsEmo on 5/12/2016.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private User appUser;
    private ArrayList<Friend> mDataset;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView friendName;
        public TextView classesInCommon;
        public Button acceptButton;
        public Button declineButton;

        public ViewHolder(View v) {
            super(v);
            friendName = (TextView) v.findViewById(R.id.user_name);
            classesInCommon = (TextView) v.findViewById(R.id.classes_in_common);
            acceptButton = (Button) v.findViewById(R.id.accept_button);
            declineButton = (Button) v.findViewById(R.id.decline_button);
        }
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public NotificationAdapter(ArrayList<Friend> myDataset, Context context, User user) {
        mContext = context;
        mDataset = new ArrayList<>(myDataset);
        appUser = user;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Friend friend = mDataset.get(position);
        holder.friendName.setText(mDataset.get(position).getName());
        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase modifyData = new Firebase(mContext.getString(R.string.database)).child("users");
                //deletes request
                modifyData.child(appUser.getUid())
                        .child("requests")
                        .child(friend.getUid())
                        .setValue(null);
                //Add to appUser friend list
                modifyData.child(appUser.getUid())
                        .child("friends")
                        .child(friend.getUid())
                        .child("name")
                        .setValue(friend.getName());
                //Add appUser to friend's friend list
                modifyData.child(friend.getUid())
                        .child("friends")
                        .child(appUser.getUid())
                        .child("name")
                        .setValue(appUser.getName());
                remove(position);
            }
        });
        holder.declineButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Firebase modifyData = new Firebase(mContext.getString(R.string.database));
                //Deletes the request
                modifyData.child("users")
                        .child(appUser.getUid())
                        .child("requests")
                        .child(friend.getUid())
                        .setValue(null);
                remove(position);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void add(int position, Friend friend) {
        mDataset.add(position, friend);
        notifyItemInserted(position);
    }


    public Friend remove(int position) {
        final Friend removedFriend = mDataset.remove(position);
        notifyItemRemoved(position);
        return removedFriend;
    }
}