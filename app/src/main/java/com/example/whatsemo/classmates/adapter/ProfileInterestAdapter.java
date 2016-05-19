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
import com.example.whatsemo.classmates.model.User;
import com.firebase.client.Firebase;

import java.util.ArrayList;

/**
 * Created by WhatsEmo on 4/29/2016.
 */
public class ProfileInterestAdapter extends RecyclerView.Adapter<ProfileInterestAdapter.ViewHolder> {
    private ArrayList<String> mDataset;
    private Context mContext;
    private FragmentManager mfragmentManager;
    private Firebase mRef;
    private User appUser;
    private int mode;
    private final static int ADD_INTEREST = 1;
    private final static int HOME_PAGE = 0;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout layout;
//        public ImageView courseImage;
        public TextView courseName;

        public ViewHolder(View v) {
            super(v);
            layout = (LinearLayout) v.findViewById(R.id.home_course_item_layout);
//            courseImage = (ImageView) v.findViewById(R.id.course_item_image);
            courseName = (TextView) v.findViewById(R.id.course_item_name);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ProfileInterestAdapter(ArrayList<String> myDataset, Context context, FragmentManager fragmentManager, Firebase ref, User user, int mode) {
        mContext = context;
        mDataset = new ArrayList<>(myDataset);
        //Helps easily implement the "+" button in the recyclerview
        mfragmentManager = fragmentManager;
        mRef = ref;
        appUser = user;
        this.mode = mode;
        if (mode == HOME_PAGE) {mDataset.add("NULL");}
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProfileInterestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
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
        final String interest = mDataset.get(position);
        if(!(interest == null) && !interest.equals("NULL")) {
            holder.courseName.setText(interest);
            if (mode == HOME_PAGE) {
                holder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = "Delete Interest: ";
                        AlertDialog.Builder alertDialogBuilder = setUpDialogBuilder(title, interest, position);
                        DeleteItemDialog dialogFragment = new DeleteItemDialog();
                        dialogFragment = dialogFragment.createCustomDialog(alertDialogBuilder);
                        dialogFragment.show(mfragmentManager, "dialog");
                    }
                });
            }
        }else{
            //Add "+" button
            holder.courseName.setTextSize(24);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddingFragment newFragment = new AddingFragment();
                    newFragment = newFragment.newInstance(ADD_INTEREST, appUser);
                    newFragment.show(mfragmentManager, "dialog");
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

    public void add(int position, String interest) {
        mDataset.add(position, interest);
        notifyItemInserted(position);
    }

    public String remove(int position) {
        final String removedInterest = mDataset.remove(position);
        notifyItemRemoved(position);
        return removedInterest;
    }

    public void move(int fromPosition, int toPosition){
        final String interest = mDataset.remove(fromPosition);
        mDataset.add(toPosition, interest);
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
                String interest = mDataset.get(position);
                remove(position);
                ArrayList<String> pushToDatabase = new ArrayList<String>();
                pushToDatabase.addAll(mDataset);
                pushToDatabase.remove(position);
                mRef.child(mContext.getString(R.string.database_users_key))
                        .child(appUser.getUid())
                        .child(mContext.getString(R.string.user_interests_key))
                        .setValue(pushToDatabase);

                mRef.child(mContext.getString(R.string.database_schools_key))
                        .child(appUser.getSid())
                        .child(mContext.getString(R.string.school_interests_key))
                        .child(interest)
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
