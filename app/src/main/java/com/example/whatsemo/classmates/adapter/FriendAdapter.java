package com.example.whatsemo.classmates.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.whatsemo.classmates.R;
import com.example.whatsemo.classmates.fragment.NewGroupFragment;
import com.example.whatsemo.classmates.model.Friend;
import com.example.whatsemo.classmates.model.User;

import java.util.ArrayList;

/**
 * Created by WhatsEmo on 4/29/2016.
 */
public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private static final int SOCIAL_FRAGMENT_ADAPTER = 0;
    private static final int ADD_FRIENDS_TO_GROUP_ADATPER = 1;

    private ArrayList<Friend> mDataset;
    private Context mContext;
    private User appUser;
    private int adapterMode;
    private NewGroupFragment newGroupFragment;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView friendName;
        public TextView lastMessage;
        public ImageView itemIcon;
        public RelativeLayout layout;

        public ViewHolder(View v) {
            super(v);
            friendName = (TextView) v.findViewById(R.id.user_name);
            lastMessage = (TextView) v.findViewById(R.id.last_message);
            itemIcon = (ImageView) v.findViewById(R.id.user_icon);
            layout = (RelativeLayout) v.findViewById(R.id.friend_item_layout);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FriendAdapter(ArrayList<Friend> myDataset, Context context, User user, int mode) {
        mContext = context;
        mDataset = new ArrayList<>(myDataset);
        appUser = user;
        adapterMode = mode;
    }

    public FriendAdapter(ArrayList<Friend> myDataset, Context context, User user, int mode, NewGroupFragment newGroupFragment) {
        mContext = context;
        mDataset = new ArrayList<>(myDataset);
        appUser = user;
        adapterMode = mode;
        this.newGroupFragment = newGroupFragment;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Friend friend = mDataset.get(position);
        holder.friendName.setText(mDataset.get(position).getName());
        if (friend.getPicture() != null) {holder.itemIcon.setImageBitmap(friend.getPicture());}
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapterMode == SOCIAL_FRAGMENT_ADAPTER){
                    friend.openChatActivity(mContext, appUser);
                }
                else if (adapterMode == ADD_FRIENDS_TO_GROUP_ADATPER){
                    newGroupFragment.addToGroup(friend);
                }
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

    public void move(int fromPosition, int toPosition){
        final Friend friend = mDataset.remove(fromPosition);
        mDataset.add(toPosition, friend);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(ArrayList<Friend> friends){
        applyAndAnimateRemovals(friends);
        applyAndAnimateAdditions(friends);
        applyAndAnimateMovedItems(friends);
    }

    private void applyAndAnimateRemovals(ArrayList<Friend> newFriends) {
        for (int i = mDataset.size() - 1; i >= 0; i--) {
            final Friend friend = mDataset.get(i);
            if (!newFriends.contains(friend)) {
                remove(i);
            }
        }
    }

    private void applyAndAnimateAdditions(ArrayList<Friend> newFriends) {
        for (int i = 0, count = newFriends.size(); i < count; i++) {
            final Friend friend = newFriends.get(i);
            if (!mDataset.contains(friend)) {
                add(i, friend);
            }
        }
    }

    private void applyAndAnimateMovedItems(ArrayList<Friend> newFriends) {
        for (int toPosition = newFriends.size() - 1; toPosition >= 0; toPosition--) {
            final Friend friend = newFriends.get(toPosition);
            final int fromPosition = mDataset.indexOf(friend);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                move(fromPosition, toPosition);
            }
        }
    }
}
