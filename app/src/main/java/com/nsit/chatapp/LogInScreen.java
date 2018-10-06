package com.nsit.chatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class LogInScreen extends AppCompatActivity {

    private Spinner selectCountrySpinner;
    private EditText phoneNumberEditText;
    private ArrayAdapter<String> countryCodesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_screen);

        selectCountrySpinner = findViewById(R.id.selectCountrySpinner);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        Button nextBtn = findViewById(R.id.nextBtn);

        countryCodesAdapter = new ArrayAdapter<>(this,R.layout.spinner_item_layout,getResources().getStringArray(R.array.country_codes));
        countryCodesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectCountrySpinner.setAdapter(countryCodesAdapter);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String country = countryCodesAdapter.getItem(selectCountrySpinner.getSelectedItemPosition());

                assert country != null;
                int firstIndex = country.indexOf("(")+1;
                int lastIndex = country.indexOf(")");
                String countryCode = country.substring(firstIndex,lastIndex);
                String phoneNumber = phoneNumberEditText.getText().toString();

                Intent intent = new Intent(LogInScreen.this,PhoneVerification.class);
                intent.putExtra("countryCode",countryCode);
                intent.putExtra("phoneNumber",phoneNumber);
                startActivity(intent);
            }
        });


    }
}
