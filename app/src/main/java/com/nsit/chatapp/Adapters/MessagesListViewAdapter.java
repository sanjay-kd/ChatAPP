package com.nsit.chatapp.Adapters;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nsit.chatapp.R;

import com.nsit.chatapp.DTO.MessageDTO;

import java.util.ArrayList;

public class MessagesListViewAdapter extends RecyclerView.Adapter<MessagesListViewAdapter.ViewHolder> {

    private ArrayList<MessageDTO> messageDTOArrayList;
    private Context context;

    public MessagesListViewAdapter(ArrayList<MessageDTO> messageDTOArrayList, Context context) {
        this.messageDTOArrayList = messageDTOArrayList;
        this.context = context;
    }

    @Override
    public MessagesListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.message_layout,null,false));
    }

    @Override
    public void onBindViewHolder(MessagesListViewAdapter.ViewHolder holder, int position) {
        holder.messageTextView.setText(messageDTOArrayList.get(position).getMessage());
        holder.usernameTextView.setText(messageDTOArrayList.get(position).getUsername());
        holder.timeTextView.setText("11:45 PM");
    }

    @Override
    public int getItemCount() {
        return messageDTOArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView messageTextView;
        private TextView usernameTextView;
        private TextView timeTextView;

        ViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
        }

    }
}
