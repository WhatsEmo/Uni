package com.example.whatsemo.classmates.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.whatsemo.classmates.R;
import com.example.whatsemo.classmates.model.Course;
import com.example.whatsemo.classmates.model.User;

import java.util.ArrayList;

/**
 * Created by WhatsEmo on 4/29/2016.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private ArrayList<Course> mDataset;
    private Context mContext;
    private User appUser;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView courseName;
        public TextView lastMessage;

        public ViewHolder(View v) {
                super(v);
            courseName = (TextView) v.findViewById(R.id.course_name);
            lastMessage = (TextView) v.findViewById(R.id.last_message);
        }
    }

    public void add(int position, Course course) {
        mDataset.add(position, course);
        notifyItemInserted(position);
    }

    public void remove(Course course) {
        int position = mDataset.indexOf(course);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ChatAdapter(ArrayList<Course> myDataset, Context context) {
        mContext = context;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Course course = mDataset.get(position);
        holder.courseName.setText(mDataset.get(position).getName());
        holder.courseName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                course.openChatActivity(mContext, appUser);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
