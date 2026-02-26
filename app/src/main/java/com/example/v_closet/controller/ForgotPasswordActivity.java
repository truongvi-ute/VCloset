package com.example.v_closet.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.v_closet.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        EditText edtForgotEmail = findViewById(R.id.edtForgotEmail);
        Button btnEditPassword = findViewById(R.id.btnEditPassword);
        ImageButton btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        btnEditPassword.setOnClickListener(v -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, EditPasswordActivity.class);
            startActivity(intent);
        });
    }
}
