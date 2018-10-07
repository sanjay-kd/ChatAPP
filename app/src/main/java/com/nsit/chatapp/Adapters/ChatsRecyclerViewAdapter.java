package com.nsit.chatapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nsit.chatapp.DTO.ChatsDTO;
import com.nsit.chatapp.R;


import java.util.ArrayList;

public class ChatsRecyclerViewAdapter extends RecyclerView.Adapter<ChatsRecyclerViewAdapter.ViewHolder> {

    private ArrayList<ChatsDTO> chatsDTOArrayList;
    private Context context;

    public ChatsRecyclerViewAdapter(ArrayList<ChatsDTO> chatsDTOArrayList, Context context) {
        this.chatsDTOArrayList = chatsDTOArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.chats_single_ui,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        ChatsDTO chatsDTO = chatsDTOArrayList.get(i);
        // load image url here
        String imageURL = chatsDTO.getProfileImageURL();
        viewHolder.userProfileImageView.setImageResource(R.drawable.user_default_icon);
        if (chatsDTO.getPhoneNumber().equals("")){
            viewHolder.usernameTextView.setText(chatsDTO.getUsername());
        }
        else{
            viewHolder.usernameTextView.setText(chatsDTO.getPhoneNumber());
        }
        viewHolder.recentMessageTextView.setText("Hello Bro, How are you?");
        viewHolder.recentMessageTimeTextView.setText("Just Now");
    }

    @Override
    public int getItemCount() {
        return chatsDTOArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView userProfileImageView;
        private TextView usernameTextView;
        private TextView recentMessageTextView;
        private TextView recentMessageTimeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userProfileImageView = itemView.findViewById(R.id.userProfileImageView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            recentMessageTextView = itemView.findViewById(R.id.recentMessageTextView);
            recentMessageTimeTextView = itemView.findViewById(R.id.recentMessageTimeTextView);
        }
    }
}
