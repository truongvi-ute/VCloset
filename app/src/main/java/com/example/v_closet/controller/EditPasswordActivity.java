package com.example.v_closet.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.v_closet.R;

public class EditPasswordActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_password);

        EditText edtUserPassword = findViewById(R.id.edtUserPassword);
        EditText edtUserConfirmPassword = findViewById(R.id.edtUserConfirmPassword);
        Button btnConfirm = findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(v -> {
            Intent intent = new Intent(EditPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
