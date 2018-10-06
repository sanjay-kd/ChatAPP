package com.nsit.chatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PhoneVerification extends AppCompatActivity {

    private TextView verificationSentTextView;
    private EditText verificationCodeEditText;
    private Button continueBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);

        Intent intent = getIntent();
        String countryCode = intent.getStringExtra("countryCode");
        String phoneNumber = intent.getStringExtra("phoneNumber");

        Button backBtn = findViewById(R.id.backBtn);
        verificationSentTextView = findViewById(R.id.verificationSentTextView);
        verificationCodeEditText = findViewById(R.id.verificationCodeEditText);
        continueBtn = findViewById(R.id.continueBtn);

        String completePhoneNumber = countryCode+"-"+phoneNumber;

        verificationSentTextView.setText("Please type the verification code sent to "+completePhoneNumber);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }
}
