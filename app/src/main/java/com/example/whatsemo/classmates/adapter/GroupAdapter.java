package com.example.whatsemo.classmates.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.whatsemo.classmates.R;
import com.example.whatsemo.classmates.model.Group;
import com.example.whatsemo.classmates.model.User;

import java.util.ArrayList;

/**
 * Created by WhatsEmo on 4/29/2016.
 */
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    private ArrayList<Group> mDataset;
    private Context mContext;
    private User appUser;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView groupName;
        public TextView lastMessage;
        public ImageView itemIcon;
        public RelativeLayout layout;

        public ViewHolder(View v) {
                super(v);
            groupName = (TextView) v.findViewById(R.id.group_name);
            lastMessage = (TextView) v.findViewById(R.id.last_message);
            itemIcon = (ImageView) v.findViewById(R.id.group_icon);
            layout = (RelativeLayout) v.findViewById(R.id.group_item_layout);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public GroupAdapter(ArrayList<Group> myDataset, Context context, User user) {
        mContext = context;
        mDataset = myDataset;
        appUser = user;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public GroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Group group = mDataset.get(position);
        holder.groupName.setText(mDataset.get(position).getName());
        holder.groupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                group.openChatActivity(mContext, appUser);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void add(int position, Group group) {
        mDataset.add(position, group);
        notifyItemInserted(position);
    }


    public Group remove(int position) {
        final Group removedGroup = mDataset.remove(position);
        notifyItemRemoved(position);
        return removedGroup;
    }

    public void move(int fromPosition, int toPosition){
        final Group group = mDataset.remove(fromPosition);
        mDataset.add(toPosition, group);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(ArrayList<Group> groups){
        applyAndAnimateRemovals(groups);
        applyAndAnimateAdditions(groups);
        applyAndAnimateMovedItems(groups);
    }

    private void applyAndAnimateRemovals(ArrayList<Group> newGroups) {
        for (int i = mDataset.size() - 1; i >= 0; i--) {
            final Group group = mDataset.get(i);
            if (!newGroups.contains(group)) {
                remove(i);
            }
        }
    }

    private void applyAndAnimateAdditions(ArrayList<Group> newGroups) {
        for (int i = 0, count = newGroups.size(); i < count; i++) {
            final Group group = newGroups.get(i);
            if (!mDataset.contains(newGroups)) {
                add(i, group);
            }
        }
    }

    private void applyAndAnimateMovedItems(ArrayList<Group> newGroups) {
        for (int toPosition = newGroups.size() - 1; toPosition >= 0; toPosition--) {
            final Group group = newGroups.get(toPosition);
            final int fromPosition = mDataset.indexOf(group);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                move(fromPosition, toPosition);
            }
        }
    }
}
