package com.example.whatsemo.classmates.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        public TextView authorName;
        public TextView sentMessage;

        public ViewHolder(View v) {
                super(v);
            authorName = (TextView) v.findViewById(R.id.author_name);
            sentMessage = (TextView) v.findViewById(R.id.message);
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
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
        holder.authorName.setText(mDataset.get(position).getAuthor());
        holder.sentMessage.setText(mDataset.get(position).getMessage());
        //ADD MORE LATER
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
