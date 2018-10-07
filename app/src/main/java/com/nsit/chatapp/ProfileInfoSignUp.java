package com.nsit.chatapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.URI;
import java.util.HashMap;
import java.util.Objects;

public class ProfileInfoSignUp extends AppCompatActivity {

    private ImageView userPhotoImageView;
    private EditText usernameEditText;
    private String completePhoneNumber;
    private String username;

    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;

    private void addUserToDatabase(String username, String completePhoneNumber){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users");
        databaseReference = databaseReference.push();
        databaseReference.child("uid").setValue(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());
        databaseReference.child("username").setValue(username);
        databaseReference.child("phoneNumber").setValue(completePhoneNumber);
    }

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

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        userPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(ProfileInfoSignUp.this);
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameEditText.getText().toString();
                addUserToDatabase(username,completePhoneNumber);
                saveUserDetails();
                Intent intent = new Intent(ProfileInfoSignUp.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                assert result != null;
                Uri imageURI = result.getUri();
                userPhotoImageView.setImageURI(imageURI);
                String userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                StorageReference imagePath = firebaseStorage.getReference().child("profile_images").child(userID + ".jpg");
                imagePath.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (!task.isSuccessful()){
                            Toast.makeText(ProfileInfoSignUp.this,Objects.requireNonNull(task.getException()).toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                assert result != null;
                Exception error = result.getError();
                Toast.makeText(ProfileInfoSignUp.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        }
    }

}
