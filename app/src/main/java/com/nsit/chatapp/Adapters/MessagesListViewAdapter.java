package com.nsit.chatapp.Adapters;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nsit.chatapp.R;

import com.nsit.chatapp.DTO.MessageDTO;

import java.util.ArrayList;

public class MessagesListViewAdapter extends RecyclerView.Adapter<MessagesListViewAdapter.ViewHolder> {

    private ArrayList<MessageDTO> messageDTOArrayList;
    private Context context;
    private String currentUserUID;

    public MessagesListViewAdapter(ArrayList<MessageDTO> messageDTOArrayList, Context context, String currentUserUID) {
        this.messageDTOArrayList = messageDTOArrayList;
        this.context = context;
        this.currentUserUID = currentUserUID;
    }

    @Override
    public MessagesListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.message_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(MessagesListViewAdapter.ViewHolder holder, int position) {
        MessageDTO messageDTO = messageDTOArrayList.get(position);
        if (messageDTO != null){
            if (messageDTO.getFrom().equals(currentUserUID)){
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
                holder.friendMessageLinearLayout.setLayoutParams(layoutParams);
            }
            holder.friendMessageTextView.setText(messageDTO.getMessage());
            holder.friendMessageTimeTextView.setText(messageDTO.getTimeStamp());
        }
        else{
            Toast.makeText(context,"Start chat to see messages!",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return messageDTOArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout friendMessageLinearLayout;
        private TextView friendMessageTextView;
        private TextView friendMessageTimeTextView;

        ViewHolder(View itemView) {
            super(itemView);
            friendMessageLinearLayout = itemView.findViewById(R.id.friendMessageLinearLayout);
            friendMessageTextView = itemView.findViewById(R.id.friendMessageTextView);
            friendMessageTimeTextView = itemView.findViewById(R.id.friendMessageTimeTextView);
        }

    }
}
