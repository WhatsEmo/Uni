package com.example.whatsemo.classmates.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.whatsemo.classmates.R;
import com.example.whatsemo.classmates.model.Course;

import java.util.ArrayList;

/**
 * Created by WhatsEmo on 4/29/2016.
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    private ArrayList<Course> mDataset;
    private Context mContext;

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

    public Course remove(int position) {
        final Course removedCourse = mDataset.remove(position);
        notifyItemRemoved(position);
        return removedCourse;
    }

    public void move(int fromPosition, int toPosition){
        final Course course = mDataset.remove(fromPosition);
        mDataset.add(toPosition, course);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(ArrayList<Course> courses){
        applyAndAnimateRemovals(courses);
        applyAndAnimateAdditions(courses);
        applyAndAnimateMovedItems(courses);
    }

    private void applyAndAnimateRemovals(ArrayList<Course> newCourses) {
        for (int i = mDataset.size() - 1; i >= 0; i--) {
            final Course course = mDataset.get(i);
            if (!newCourses.contains(course)) {
                remove(i);
            }
        }
    }

    private void applyAndAnimateAdditions(ArrayList<Course> newCourses) {
        for (int i = 0, count = newCourses.size(); i < count; i++) {
            final Course course = newCourses.get(i);
            if (!mDataset.contains(course)) {
                add(i, course);
            }
        }
    }

    private void applyAndAnimateMovedItems(ArrayList<Course> newCourses) {
        for (int toPosition = newCourses.size() - 1; toPosition >= 0; toPosition--) {
            final Course course = newCourses.get(toPosition);
            final int fromPosition = mDataset.indexOf(course);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                move(fromPosition, toPosition);
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CourseAdapter(ArrayList<Course> myDataset, Context context) {
        mContext = context;
        mDataset = new ArrayList<>(myDataset);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CourseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
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
                course.openChatActivity(mContext);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
