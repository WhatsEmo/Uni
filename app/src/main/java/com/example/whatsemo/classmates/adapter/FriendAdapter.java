package com.example.whatsemo.classmates.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.whatsemo.classmates.model.Friend;
import com.example.whatsemo.classmates.R;

import java.util.ArrayList;

/**
 * Created by WhatsEmo on 4/29/2016.
 */
public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private ArrayList<Friend> mDataset;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView friendName;
        public TextView lastMessage;

        public ViewHolder(View v) {
            super(v);
            friendName = (TextView) v.findViewById(R.id.user_name);
            lastMessage = (TextView) v.findViewById(R.id.last_message);
        }
    }

    public void add(int position, Friend friend) {
        mDataset.add(position, friend);
        notifyItemInserted(position);
    }

    public void remove(Friend friend) {
        int position = mDataset.indexOf(friend);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FriendAdapter(ArrayList<Friend> myDataset, Context context) {
        mContext = context;
        mDataset = myDataset;
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
        holder.friendName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friend.openChatActivity(mContext);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
