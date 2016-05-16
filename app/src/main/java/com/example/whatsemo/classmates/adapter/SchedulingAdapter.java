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
import com.firebase.client.Firebase;

import java.util.ArrayList;

/**
 * Created by WhatsEmo on 4/29/2016.
 */
public class SchedulingAdapter extends RecyclerView.Adapter<SchedulingAdapter.ViewHolder> {
    private ArrayList<Boolean> mDataset;
    private ArrayList<Integer> freeTimeInHours;
    private Context mContext;
    private Firebase mRef;
    private final static int ADD_INTEREST = 1;
    private final static int STARTING_HOUR = 8;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public RelativeLayout layout;
        public ImageView orangeCircle;
        public TextView timeOfDay;

        public ViewHolder(View v) {
            super(v);
            layout = (RelativeLayout) v.findViewById(R.id.schedule_item_layout);
            orangeCircle = (ImageView) v.findViewById(R.id.orange_dot);
            timeOfDay = (TextView) v.findViewById(R.id.time_of_day);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SchedulingAdapter(ArrayList<Boolean> myDataset, Context context, Firebase ref, ArrayList<Integer> passToDatabase) {
        mContext = context;
        mDataset = new ArrayList<>(myDataset);
        mRef = ref;
        freeTimeInHours = passToDatabase;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SchedulingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false);
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
        int time = STARTING_HOUR+position;
        if(time > 12){
            time = time-12;
        }
        holder.timeOfDay.setText(Integer.toString(time));

        //If user is free, then make the orange circle appear
        if(freeTime){
            holder.orangeCircle.setVisibility(View.VISIBLE);
        }else{
            holder.orangeCircle.setVisibility(View.INVISIBLE);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean freeTime = mDataset.get(position);
                mDataset.set(position, !freeTime);
                //If user is free, then make the orange circle appear
                if(!freeTime){
                    holder.orangeCircle.setVisibility(View.VISIBLE);
                    freeTimeInHours.add(position + STARTING_HOUR);
                    mRef.child(mContext.getString(R.string.database_users_key))
                            .child(mRef.getAuth().getUid())
                            .child(mContext.getString(R.string.user_schedule_key))
                            .setValue(freeTimeInHours);
                }else{
                    holder.orangeCircle.setVisibility(View.INVISIBLE);
                    freeTimeInHours.remove(freeTimeInHours.indexOf(position + STARTING_HOUR));
                    mRef.child(mContext.getString(R.string.database_users_key))
                            .child(mRef.getAuth().getUid())
                            .child(mContext.getString(R.string.user_schedule_key))
                            .setValue(freeTimeInHours);
                }
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void add(int position, Boolean freeTime) {
        mDataset.add(position, freeTime);
        notifyItemInserted(position);
    }

}
