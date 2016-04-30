package com.example.whatsemo.classmates.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.whatsemo.classmates.R;
import com.example.whatsemo.classmates.model.Group;

import java.util.ArrayList;

/**
 * Created by WhatsEmo on 4/29/2016.
 */
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    private ArrayList<Group> mDataset;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView groupName;
        public TextView lastMessage;

        public ViewHolder(View v) {
                super(v);
            groupName = (TextView) v.findViewById(R.id.group_name);
            lastMessage = (TextView) v.findViewById(R.id.last_message);
        }
    }

    public void add(int position, Group group) {
        mDataset.add(position, group);
        notifyItemInserted(position);
    }

    public void remove(Group group) {
        int position = mDataset.indexOf(group);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public GroupAdapter(ArrayList<Group> myDataset, Context context) {
        mContext = context;
        mDataset = myDataset;
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
                group.openChatActivity(mContext);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
