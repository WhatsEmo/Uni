package com.example.whatsemo.classmates.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.whatsemo.classmates.R;
import com.example.whatsemo.classmates.fragment.AddingFragment;
import com.example.whatsemo.classmates.fragment.DeleteItemDialog;
import com.example.whatsemo.classmates.model.Course;
import com.example.whatsemo.classmates.model.User;
import com.firebase.client.Firebase;

import java.util.ArrayList;

/**
 * Created by WhatsEmo on 4/29/2016.
 */
public class ProfileCourseAdapter extends RecyclerView.Adapter<ProfileCourseAdapter.ViewHolder> {
    private ArrayList<Course> mDataset;
    private Context mContext;
    private FragmentManager mfragmentManager;
    private Firebase mRef;
    private User appUser;
    private final static int ADD_COURSES = 0;
    private static final int NOT_REMOVED = 0;
    private static final int REMOVED = 1;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout layout;
        public ImageView courseImage;
        public TextView courseName;

        public ViewHolder(View v) {
            super(v);
            layout = (LinearLayout) v.findViewById(R.id.home_course_item_layout);
            courseImage = (ImageView) v.findViewById(R.id.course_item_image);
            courseName = (TextView) v.findViewById(R.id.course_item_name);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ProfileCourseAdapter(ArrayList<Course> myDataset, Context context, FragmentManager fragmentManager, Firebase ref, User user) {
        mContext = context;
        mDataset = new ArrayList<>(myDataset);
        //Helps easily implement the "+" button in the recyclerview
        mDataset.add(new Course(null,null));
        mfragmentManager = fragmentManager;
        mRef = ref;
        appUser = user;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProfileCourseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_course_item, parent, false);
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
        final Course course = mDataset.get(position);
        if(course.getCourseID() != null) {
            holder.courseName.setText(mDataset.get(position).getName());
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = "Delete Course: ";
                    String message = String.format("%s (%s)", course.getName(), course.getCourseID());
                    AlertDialog.Builder alertDialogBuilder = setUpDialogBuilder(title, message, position);
                    DeleteItemDialog dialogFragment = new DeleteItemDialog();
                    dialogFragment = dialogFragment.createCustomDialog(alertDialogBuilder);
                    dialogFragment.show(mfragmentManager, "dialog");
                }
            });
        }else{
            //Add "+" button
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddingFragment newFragment = new AddingFragment();
                    newFragment = newFragment.newInstance(ADD_COURSES, appUser);
                    newFragment.show(mfragmentManager, "ASSHOLE");
                    /*
                    mfragmentManager.beginTransaction()
                            .replace(R.id.pop_up_fragment, newFragment)
                            .addToBackStack(null)
                            .commit();*/
                }
            });
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
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

    private AlertDialog.Builder setUpDialogBuilder(String dialogTitle, String dialogMessage, final int position){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder.setMessage(dialogMessage);

        alertDialogBuilder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String courseID = mDataset.get(position).getCourseID();
                remove(position);
                mRef.child(mContext.getString(R.string.database_users_key))
                        .child(appUser.getUid())
                        .child(mContext.getString(R.string.user_courses_key))
                        .child(courseID)
                        .setValue(null);

                mRef.child(mContext.getString(R.string.database_schools_key))
                        .child(appUser.getSid())
                        .child(mContext.getString(R.string.school_courses_key))
                        .child(courseID)
                        .child(mContext.getString(R.string.school_courses_enrolled_key))
                        .child(appUser.getUid())
                        .setValue(null);
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return alertDialogBuilder;
    }

}
