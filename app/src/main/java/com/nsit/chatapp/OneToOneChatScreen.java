package com.nsit.chatapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nsit.chatapp.Adapters.MessagesListViewAdapter;
import com.nsit.chatapp.DAO.CommonUtils;
import com.nsit.chatapp.DTO.ChatsDTO;
import com.nsit.chatapp.DTO.MessageDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class OneToOneChatScreen extends AppCompatActivity {

    private TextView friendName;
    private TextView friendOnlineStatus;
    private ImageView friendProfileImage;
    private RecyclerView oneToOneChatRecyclerView;
    private EditText messageEditText;
    private Button sendMessageBtn;
    private ChatsDTO chatsDTO;
    private ArrayList<MessageDTO> messageDTOArrayList = null;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressBar chatProgressBar;
    private MessagesListViewAdapter messagesListViewAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private String currentUserUID;
    private String friendUID;
    private String messageToSend;
    private String uniqueChatNode1;
    private String uniqueChatNode2;

    private DatabaseReference messagesDatabaseReference;

    private void addMessage(DatabaseReference newDatabaseReference){

        newDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println(dataSnapshot.toString());
                MessageDTO messageDTO = dataSnapshot.getValue(MessageDTO.class);
                messageDTOArrayList.add(messageDTO);
                System.out.println("Message added : "+messageDTO.toString());
                messagesListViewAdapter.notifyDataSetChanged();
                oneToOneChatRecyclerView.scrollToPosition(oneToOneChatRecyclerView.getAdapter().getItemCount()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void printPastMessages(){
        messagesDatabaseReference = firebaseDatabase.getReference().child("chats").child(uniqueChatNode1);

        System.out.println("MessageDatabase ref : "+messagesDatabaseReference);

        messagesDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    DatabaseReference messagesDatabaseReference = firebaseDatabase.getReference().child("chats").child(uniqueChatNode2);
                    messagesDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){
                                    MessageDTO messageDTO = dataSnapshot2.getValue(MessageDTO.class);
                                    messageDTOArrayList.add(messageDTO);
                                    messagesListViewAdapter.notifyDataSetChanged();
                                    oneToOneChatRecyclerView.scrollToPosition(oneToOneChatRecyclerView.getAdapter().getItemCount()-1);
                                    chatProgressBar.setVisibility(View.GONE);
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){
                            MessageDTO messageDTO = dataSnapshot2.getValue(MessageDTO.class);
                            messageDTOArrayList.add(messageDTO);
                            messagesListViewAdapter.notifyDataSetChanged();
                            oneToOneChatRecyclerView.scrollToPosition(oneToOneChatRecyclerView.getAdapter().getItemCount()-1);
                            chatProgressBar.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_to_one_chat_screen);

        chatsDTO = (ChatsDTO) getIntent().getSerializableExtra("ChatDTO");

        ImageView oneToOneChatBackBtn = findViewById(R.id.oneToOneChatBackBtn);
        friendName = findViewById(R.id.friendName);
        friendOnlineStatus = findViewById(R.id.friendOnlineStatus);
        friendProfileImage = findViewById(R.id.friendProfileImage);
        oneToOneChatRecyclerView = findViewById(R.id.oneToOneChatRecyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        sendMessageBtn = findViewById(R.id.sendMessageBtn);
        chatProgressBar = findViewById(R.id.chatProgressBar);

        firebaseDatabase = CommonUtils.getFirebaseDatabase();
        currentUserUID = Objects.requireNonNull(CommonUtils.getFirebaseAuth().getCurrentUser()).getUid();
        friendUID = chatsDTO.getUid();
        uniqueChatNode1 = currentUserUID + friendUID;
        uniqueChatNode2 =  friendUID + currentUserUID;
        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);

        messageDTOArrayList = new ArrayList<>();
        messagesListViewAdapter = new MessagesListViewAdapter(messageDTOArrayList,this,currentUserUID);

        messagesDatabaseReference = firebaseDatabase.getReference().child("chats").child(uniqueChatNode2);
        System.out.println("Message Db Ref : "+messagesDatabaseReference);


        oneToOneChatRecyclerView.setLayoutManager(layoutManager);
        oneToOneChatRecyclerView.setAdapter(messagesListViewAdapter);

        printPastMessages();

        friendName.setText(chatsDTO.getUsername());

        oneToOneChatBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("UniqueChatNode : " + uniqueChatNode1);
                messageToSend= messageEditText.getText().toString();
                messageEditText.setText("");
                if (messageToSend.length() > 0) {
                    readMessages(new FirebaseCallback() {
                        @Override
                        public void onCallback(DatabaseReference databaseReference) {
                            System.out.println("Database Ref in CallBack : " + databaseReference);
                            databaseReference = databaseReference.push();

                            DatabaseReference currentUserDatabaseReference = databaseReference.child(currentUserUID);

                            Date d = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                            String currentDateTimeString = sdf.format(d);

                            currentUserDatabaseReference.child("from").setValue(currentUserUID);
                            currentUserDatabaseReference.child("to").setValue(friendUID);
                            currentUserDatabaseReference.child("message").setValue(messageToSend);
                            currentUserDatabaseReference.child("timeStamp").setValue(currentDateTimeString);

                            DatabaseReference newDatabaseReference  = databaseReference;

                            addMessage(newDatabaseReference);

                        }
                    });
                }
                else{
                    Toast.makeText(OneToOneChatScreen.this,"Enter Message to send!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void readFriendIncomingMessage(final FirebaseCallbackForIncomingMessage FirebaseCallbackForIncomingMessage, DatabaseReference databaseReference){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        MessageDTO messageDTO = dataSnapshot.getValue(MessageDTO.class);
//                        messageDTOArrayList.add(messageDTO);
//                        messagesListViewAdapter.notifyDataSetChanged();
                FirebaseCallbackForIncomingMessage.onCallback(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMessages(final FirebaseCallback firebaseCallback){

        System.out.println("Unqiue ChatNode 1 : "+ uniqueChatNode1);
        System.out.println("Unqiue ChatNode 2 : " + uniqueChatNode2);

        databaseReference = firebaseDatabase.getReference().child("chats");
        final DatabaseReference newDatabaseReference1 = databaseReference.child(uniqueChatNode1);
        final DatabaseReference newDatabaseReference2 = databaseReference.child(uniqueChatNode2);

        System.out.println("NewDatabase1 : "+newDatabaseReference1);
        System.out.println("NewDatabase2 : "+newDatabaseReference2);

        newDatabaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    databaseReference = newDatabaseReference1;
                    System.out.println("Db Reference 1 : "+databaseReference);
                    firebaseCallback.onCallback(databaseReference);
                }
                else{
                    newDatabaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                databaseReference = databaseReference.child(uniqueChatNode2);
                            }
                            else{
                                databaseReference = databaseReference.child(uniqueChatNode2);
                            }
                            System.out.println("DB ref 2  : "+databaseReference);
                            firebaseCallback.onCallback(databaseReference);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(OneToOneChatScreen.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                            System.out.println("DatabaseError : "+databaseError);
                        }
                    });
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OneToOneChatScreen.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                System.out.println("DatabaseError : "+databaseError);
            }
        });

    }

    private interface FirebaseCallback{
        public void onCallback(DatabaseReference databaseReference);
    }

    private interface FirebaseCallbackForIncomingMessage{
        public void onCallback(DataSnapshot dataSnapshot);
    }

}
