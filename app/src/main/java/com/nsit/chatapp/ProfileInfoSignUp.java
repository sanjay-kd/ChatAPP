package com.nsit.chatapp;

import android.content.Intent;
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
    private Button continueBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info_sign_up);

        userPhotoImageView = findViewById(R.id.userPhotoImageView);
        usernameEditText = findViewById(R.id.usernameEditText);
        continueBtn = findViewById(R.id.continueBtn);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                Intent intent = new Intent(ProfileInfoSignUp.this,MainActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

    }
}
