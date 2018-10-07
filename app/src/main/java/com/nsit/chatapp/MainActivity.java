package com.nsit.chatapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nsit.chatapp.Adapters.ChatsRecyclerViewAdapter;
import com.nsit.chatapp.Adapters.MessagesListViewAdapter;
import com.nsit.chatapp.DTO.ChatsDTO;
import com.nsit.chatapp.DTO.MessageDTO;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView chatsRecyclerView;
    private EditText messageEditText;
    private Button sendMessageBtn;
    private RelativeLayout relativeLayout;
    private ImageView profileIconImageview;
    private ImageView notificationsImageView;
    private EditText searchConversationsEditText;
    private FloatingActionButton sendMessageFromContactsBtn;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference messageDatabaseReference;
    private DatabaseReference userDatabaseReference;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private String uniqueReference = null;

    private int count=1;
    private ArrayList<MessageDTO> messageDTOArrayList;

    private DatabaseReference getNewMessageDatabaseReference(){
        return messageDatabaseReference.push().getRef();
    }

    private void settingSharedPreferences(boolean isNotificationON){
        SharedPreferences sharedPreferences = getSharedPreferences("Settings",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isNotificationON",isNotificationON);
        editor.commit();
    }

    private boolean readFromSettingSharedPreference(){
        SharedPreferences sharedPreferences = getSharedPreferences("Settings",MODE_PRIVATE);
        return sharedPreferences.getBoolean("isNotificationON",true);
    }

    private void readFromSharedPreference(){
        SharedPreferences sharedPreferences = getSharedPreferences("User Details",MODE_PRIVATE);
        if (!sharedPreferences.contains("phoneNumber")){
            Intent intent = new Intent(this,LogInScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else{
            uniqueReference = sharedPreferences.getString("uniqueReference","");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readFromSharedPreference();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        chatsRecyclerView = findViewById(R.id.chatsRecyclerView);
//        messageEditText = findViewById(R.id.messageEditText);
//        sendMessageBtn = findViewById(R.id.sendMessageBtn);
        relativeLayout = findViewById(R.id.relativeLayout);
        profileIconImageview = findViewById(R.id.profileIconImageview);
        notificationsImageView = findViewById(R.id.notificationsImageView);
        searchConversationsEditText = findViewById(R.id.searchConversationsEditText);
        sendMessageFromContactsBtn = findViewById(R.id.sendMessageFromContactsBtn);
        messageDTOArrayList = new ArrayList<>();

        messageDatabaseReference = firebaseDatabase.getReference().child("Messages");
        userDatabaseReference = firebaseDatabase.getReference().child("Users");
        storageReference = FirebaseStorage.getInstance().getReference();

        // this download the image from file storage and uploads it to profileImageView
        storageReference.child("profile_images/"+firebaseAuth.getUid()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                System.out.println("Uri is :"+uri);
                Glide.with(MainActivity.this).load(uri).into(profileIconImageview);
            }
        });

        progressBar = new ProgressBar(MainActivity.this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(130,130);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        progressBar.setLayoutParams(params);
        relativeLayout.addView(progressBar);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ArrayList<ChatsDTO> chatsDTOArrayList = new ArrayList<>();
        chatsDTOArrayList.add(new ChatsDTO("","Papa","","","+91-8750400103"));
        chatsDTOArrayList.add(new ChatsDTO("","Manish Kumar","","",""));
        chatsDTOArrayList.add(new ChatsDTO("","Vikash Ola","","","+91-7593959201"));
        chatsDTOArrayList.add(new ChatsDTO("","Sanjay Kumar","","",""));
        chatsDTOArrayList.add(new ChatsDTO("","Vikash Ola","","","+91-7593959201"));
        chatsDTOArrayList.add(new ChatsDTO("","Vikash Ola","","","+91-7593959201"));
        chatsDTOArrayList.add(new ChatsDTO("","Vikash Ola","","",""));
        chatsDTOArrayList.add(new ChatsDTO("","Papa","","","+91-8750400103"));
        chatsDTOArrayList.add(new ChatsDTO("","Manish Kumar","","",""));
        chatsDTOArrayList.add(new ChatsDTO("","Vikash Ola","","","+91-7593959201"));
        chatsDTOArrayList.add(new ChatsDTO("","Sanjay Kumar","","",""));
        chatsDTOArrayList.add(new ChatsDTO("","Vikash Ola","","","+91-7593959201"));
        chatsDTOArrayList.add(new ChatsDTO("","Vikash Ola","","","+91-7593959201"));
        chatsDTOArrayList.add(new ChatsDTO("","Vikash Ola","","",""));
        ChatsRecyclerViewAdapter chatsRecyclerViewAdapter = new ChatsRecyclerViewAdapter(chatsDTOArrayList,this);

        chatsRecyclerView.setLayoutManager(layoutManager);
        chatsRecyclerView.setAdapter(chatsRecyclerViewAdapter);

//        messageDatabaseReference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                progressBar.setVisibility(View.VISIBLE);
//
//                MessageDTO messageDTO = dataSnapshot.getValue(MessageDTO.class);
//
//                if (messageDTO != null) {
//                    System.out.println("MessageDTO is : "+messageDTO.getMessage());
//                }
//
//                messageDTOArrayList.add(messageDTO);
//
//                count++;
//                adapter.notifyDataSetChanged();
//
//                progressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

//        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String message  = messageEditText.getText().toString().trim();
//                count=0;
//                if(message.length()>0){
//                    DatabaseReference newDatabaseReference = getNewMessageDatabaseReference();
//                    newDatabaseReference.child("username").setValue("Rajesh");
//                    newDatabaseReference.child("message").setValue(message);
//                    messageEditText.setText("");
//                    Toast.makeText(MainActivity.this,"Message Sent!",Toast.LENGTH_SHORT).show();
//                    System.out.println(messageDTOArrayList.size());
//                }
//                else{
//                    Toast.makeText(MainActivity.this,"Enter Message to send!",Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });

        profileIconImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open profile Activity
            }
        });

        notificationsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (readFromSettingSharedPreference()){
                    settingSharedPreferences(false);
                    notificationsImageView.setImageResource(R.drawable.notification_mute_icon);
                }
                else{
                    settingSharedPreferences(true);
                    notificationsImageView.setImageResource(R.drawable.notification_unmute_icon);
                }
            }
        });

        sendMessageFromContactsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

}
