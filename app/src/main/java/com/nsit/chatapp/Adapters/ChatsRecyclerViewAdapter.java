package com.nsit.chatapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nsit.chatapp.DTO.ChatsDTO;
import com.nsit.chatapp.OneToOneChatScreen;
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
    public void onBindViewHolder(@NonNull final ChatsRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        final ChatsDTO chatsDTO = chatsDTOArrayList.get(i);
        // load image url here
        String imageURL = chatsDTO.getProfileImageURL();
        viewHolder.userProfileImageView.setImageResource(R.drawable.user_default_icon);
        if (chatsDTO.getPhoneNumber().equals("")){
            viewHolder.usernameTextView.setText(chatsDTO.getUsername());
        }
        else{
            viewHolder.usernameTextView.setText(chatsDTO.getPhoneNumber());
            System.out.println("Hey1"+viewHolder.usernameTextView.getText());
        }
        viewHolder.recentMessageTextView.setText("Hello Bro, How are you?");
        viewHolder.recentMessageTimeTextView.setText("Just Now");

        viewHolder.singleChatCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,OneToOneChatScreen.class);
                intent.putExtra("ChatDTO",chatsDTO);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return chatsDTOArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CardView singleChatCardView;
        private ImageView userProfileImageView;
        private TextView usernameTextView;
        private TextView recentMessageTextView;
        private TextView recentMessageTimeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            singleChatCardView = itemView.findViewById(R.id.singleChatCardView);
            userProfileImageView = itemView.findViewById(R.id.userProfileImageView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            recentMessageTextView = itemView.findViewById(R.id.recentMessageTextView);
            recentMessageTimeTextView = itemView.findViewById(R.id.recentMessageTimeTextView);
        }
    }
}
