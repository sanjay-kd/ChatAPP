package com.nsit.chatapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Objects;

public class ProfileInfoSignUp extends AppCompatActivity {

    private ImageView userPhotoImageView;
    private EditText usernameEditText;
    private String completePhoneNumber;
    String username;

    private void saveUserDetails(){
        SharedPreferences sharedPreferences = getSharedPreferences("User Details",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username",username);
        editor.putString("phoneNumber",completePhoneNumber);
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info_sign_up);

        completePhoneNumber = getIntent().getStringExtra("phoneNumber");

        userPhotoImageView = findViewById(R.id.userPhotoImageView);
        usernameEditText = findViewById(R.id.usernameEditText);
        Button continueBtn = findViewById(R.id.continueBtn);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameEditText.getText().toString();
                saveUserDetails();
                Intent intent = new Intent(ProfileInfoSignUp.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
