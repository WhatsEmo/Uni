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
import com.example.whatsemo.classmates.model.Message;

import java.util.ArrayList;

/**
 * Created by WhatsEmo on 4/29/2016.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private ArrayList<Message> mDataset;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public RelativeLayout messageLayout;
        public TextView authorName;
        public TextView sentMessage;
        public TextView timeStamp;
        public ImageView authorProfileImage;

        public ViewHolder(View v) {
            super(v);
            messageLayout = (RelativeLayout) v.findViewById(R.id.chat_message_layout);
            authorName = (TextView) v.findViewById(R.id.author_name);
            sentMessage = (TextView) v.findViewById(R.id.message);
            timeStamp = (TextView) v.findViewById(R.id.time_stamp);
            authorProfileImage = (ImageView) v.findViewById(R.id.author_profile_image);
        }
    }

    public void add(int position, Message message) {
        mDataset.add(position, message);
        notifyItemInserted(position);
    }

    public void remove(Message message) {
        int position = mDataset.indexOf(message);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MessageAdapter(ArrayList<Message> myDataset, Context context) {
        mContext = context;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v;
        if (viewType == 1){
             v = LayoutInflater.from(parent.getContext()).inflate(R.layout.right_message_item, parent, false);
        }
        else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_message_item, parent, false);
        }
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Message message = mDataset.get(position);
        holder.authorName.setText(mDataset.get(position).getAuthorName());
        holder.sentMessage.setText(mDataset.get(position).getMessage());
        holder.timeStamp.setText(mDataset.get(position).getTimeStamp());
        if(mDataset.get(position).getMode() == 'l'){
            holder.authorProfileImage.setImageBitmap(mDataset.get(position).getBm());
        }
        //ADD MORE LATER
    }

    @Override
    public int getItemViewType(int position) {
        if(mDataset.get(position).getMode() == 'r'){
            return 1;
        }
        else {
            return 0;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
