package com.nsit.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneVerification extends AppCompatActivity {

    private EditText verificationCodeEditText;
    private FirebaseAuth mAuth;
    private String verificationCode;
    private ProgressDialog progressDialog;

    private void createProgressDailog(){
        progressDialog = new ProgressDialog(PhoneVerification.this);
        progressDialog.setTitle("Verification in Progress");
        progressDialog.setMessage("Please wait while we verify your mobile number");
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);

        createProgressDailog();

        Intent intent = getIntent();
        String countryCode = intent.getStringExtra("countryCode");
        String phoneNumber = intent.getStringExtra("phoneNumber");

        Button backBtn = findViewById(R.id.backBtn);
        TextView verificationSentTextView = findViewById(R.id.verificationSentTextView);
        verificationCodeEditText = findViewById(R.id.verificationCodeEditText);
        Button continueBtn = findViewById(R.id.continueBtn);

        mAuth = FirebaseAuth.getInstance();

        String completePhoneNumber = countryCode+"-"+phoneNumber;

        verificationSentTextView.setText("Please type the verification code sent to "+completePhoneNumber);

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
            }

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                verificationCodeEditText.setText(phoneAuthCredential.getSmsCode());
                signInWithPhoneAuth(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(PhoneVerification.this,
                        "Some error occurred while verifying your mobile number. Please try again",
                        Toast.LENGTH_LONG)
                        .show();
            }
        };

        progressDialog.show();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                completePhoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks
        );

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode,verificationCodeEditText.getText().toString());
                signInWithPhoneAuth(credential);
            }
        });
    }

    private void signInWithPhoneAuth(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(PhoneVerification.this,ProfileInfoSignUp.class);
                    startActivity(intent);
                }
                else{
                    System.out.println("Some error occurred please try after some time");
                }
            }
        });
    }


}
