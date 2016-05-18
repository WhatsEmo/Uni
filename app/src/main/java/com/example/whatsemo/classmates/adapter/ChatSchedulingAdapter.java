package com.example.whatsemo.classmates.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.whatsemo.classmates.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by WhatsEmo on 5/17/2016.
 */
public class ChatSchedulingAdapter extends RecyclerView.Adapter<ChatSchedulingAdapter.ViewHolder> {
    private ArrayList<String> mDataset;
    private 
    private Map<String, >
    private Context mContext;
    private Firebase mRef;
    private final static int ADD_INTEREST = 1;
    private final static int STARTING_HOUR = 8;

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
    public ChatSchedulingAdapter(ArrayList<String> userIds, Context context, Firebase ref, int day) {
        mContext = context;
        mDataset = new ArrayList<>(userIds);
        mRef = ref.child(mContext.getString(R.string.database_users_key))
                .child(ref.getAuth().getUid())
                .child(mContext.getString(R.string.user_schedule_key))
                .child(Integer.toString(day));
        freeTimeInHours = new ArrayList<Integer>();
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    freeTimeInHours.addAll(dataSnapshot.getValue(ArrayList.class));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
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
        final Boolean freeTime = mDataset.get(position);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void set(int position, Boolean freeTime) {
        mDataset.set(position, freeTime);
        notifyItemChanged(position);
    }

    public void add(int position, Boolean freeTime) {
        mDataset.add(position, freeTime);
        notifyItemInserted(position);
    }

    public Boolean remove(int position) {
        final Boolean removedFreeTime = mDataset.remove(position);
        notifyItemRemoved(position);
        return removedFreeTime;
    }

    public void move(int fromPosition, int toPosition){
        final Boolean freeTime = mDataset.remove(fromPosition);
        mDataset.add(toPosition, freeTime);
        notifyItemMoved(fromPosition, toPosition);
    }
}
