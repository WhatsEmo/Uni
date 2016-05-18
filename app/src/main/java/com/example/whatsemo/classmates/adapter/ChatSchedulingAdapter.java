package com.example.whatsemo.classmates.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.whatsemo.classmates.R;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by WhatsEmo on 5/17/2016.
 */
public class ChatSchedulingAdapter extends RecyclerView.Adapter<ChatSchedulingAdapter.ViewHolder> {
    private ArrayList<String> mDataset;
    Map<String, Bitmap> allProfilePics;
    Map<String, ArrayList<Boolean>> allFreeTimesOnCertainDay;
    private int day;
    private Context mContext;
    private Firebase mRef;
    private final static int ADD_INTEREST = 1;
    private final static int STARTING_HOUR = 8;
    private final static int IN_CHAT = 1;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout layout;
        public ImageView profilePicture;
        public RecyclerView schedule;

        public ViewHolder(View v) {
            super(v);
            layout = (LinearLayout) v.findViewById(R.id.individual_scheduling_layout);
            profilePicture = (ImageView) v.findViewById(R.id.individual_profile_picture);
            schedule = (RecyclerView) v.findViewById(R.id.individual_schedules);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ChatSchedulingAdapter(ArrayList<String> userIds, Context context, Firebase ref, int day, Map<String, Bitmap> allPics, Map<String, ArrayList<Boolean>> allFreeTime) {
        mContext = context;
        mDataset = new ArrayList<>(userIds);
        mRef = ref.child(mContext.getString(R.string.database_users_key));
        allProfilePics = allPics;
        allFreeTimesOnCertainDay = allFreeTime;
        this.day = day;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChatSchedulingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_user_schedules, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        // If it's not the "+" button, then add in regular functions
        String userId = mDataset.get(position);
        if(allProfilePics.get(userId) != null){
            holder.profilePicture.setImageBitmap(allProfilePics.get(userId));
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.schedule.setLayoutManager(layoutManager);

        ArrayList<Boolean> freeTime = allFreeTimesOnCertainDay.get(userId);

        holder.schedule.setAdapter(new SchedulingAdapter(freeTime, mContext, mRef, day, IN_CHAT));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void set(int position, String userId) {
        mDataset.set(position, userId);
        notifyItemChanged(position);
    }

    public void add(int position, String userId) {
        mDataset.add(position, userId);
        notifyItemInserted(position);
    }

    public String remove(int position) {
        final String removedUserId = mDataset.remove(position);
        notifyItemRemoved(position);
        return removedUserId;
    }

    public void move(int fromPosition, int toPosition){
        final String userId = mDataset.remove(fromPosition);
        mDataset.add(toPosition, userId);
        notifyItemMoved(fromPosition, toPosition);
    }
}
