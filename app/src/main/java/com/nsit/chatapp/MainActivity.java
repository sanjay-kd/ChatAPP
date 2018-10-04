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
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nsit.chatapp.Adapters.MessagesListViewAdapter;
import com.nsit.chatapp.DTO.MessageDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private RecyclerView messageRecyclerView;
    private EditText messageEditText;
    private Button sendMessageBtn;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private int count=1;
    private ArrayList<MessageDTO> messageDTOArrayList;

    private DatabaseReference getNewDatabaseReference(){
        return databaseReference.push().getRef();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageRecyclerView = findViewById(R.id.messageRecyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        sendMessageBtn = findViewById(R.id.sendMessageBtn);
        messageDTOArrayList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Messages");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        final MessagesListViewAdapter adapter = new MessagesListViewAdapter(messageDTOArrayList,this,count);

        messageRecyclerView.setLayoutManager(layoutManager);
        messageRecyclerView.setAdapter(adapter);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                MessageDTO messageDTO = new MessageDTO((HashMap<String, String>) Objects.requireNonNull(dataSnapshot.getValue()));
                messageDTOArrayList.add(messageDTO);

                System.out.println("Username is "+messageDTO.getUsername());
                System.out.println("Message is "+messageDTO.getMessage());

                System.out.println("************");
                System.out.println("Count is : "+dataSnapshot.toString());

                count++;
                adapter.notifyDataSetChanged();
//                assert messageDTO != null;
//                System.out.println(messageDTO.getUsername());
//                System.out.println(messageDTO.getMessage());
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

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message  = messageEditText.getText().toString().trim();
                count=0;
                if(message.length()>0){
                    DatabaseReference newDatabaseReference = getNewDatabaseReference();
                    newDatabaseReference.child("username").setValue("Sanjay");
                    newDatabaseReference.child("message").setValue(message);
                    messageEditText.setText("");
                    Toast.makeText(MainActivity.this,"Message Sent!",Toast.LENGTH_SHORT).show();
                    System.out.println(messageDTOArrayList.size());
                }
                else{
                    Toast.makeText(MainActivity.this,"Enter Message to send!",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
