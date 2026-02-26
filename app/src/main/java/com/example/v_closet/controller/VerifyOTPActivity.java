package com.example.v_closet.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.v_closet.R;

public class VerifyOTPActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_otp);

        EditText edtOTP = findViewById(R.id.edtOTP);
        Button btnVerifyOtp = findViewById(R.id.btnVerifyOtp);

        btnVerifyOtp.setOnClickListener(v -> {
            Intent intent = new Intent(VerifyOTPActivity.this, EditPasswordActivity.class);
            startActivity(intent);
        });
    }
}