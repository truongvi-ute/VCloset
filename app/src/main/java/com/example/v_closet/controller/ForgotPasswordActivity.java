package com.example.v_closet.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.v_closet.R;
import com.example.v_closet.service.UserService;
import com.example.v_closet.util.InputValidation;

public class ForgotPasswordActivity extends AppCompatActivity {
    
    private UserService userService;
    private EditText edtForgotEmail;
    private Button btnEditPassword;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        
        // Khởi tạo UserService
        userService = new UserService(this);
        
        // Ánh xạ views
        edtForgotEmail = findViewById(R.id.edtForgotEmail);
        btnEditPassword = findViewById(R.id.btnEditPassword);
        ImageButton btnBack = findViewById(R.id.btn_back);

        // Xử lý nút back
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Xử lý nút tiếp tục đổi mật khẩu
        btnEditPassword.setOnClickListener(v -> handleVerifyEmail());
    }
    
    /**
     * Xác thực email trước khi cho phép đổi mật khẩu
     */
    private void handleVerifyEmail() {
        String email = edtForgotEmail.getText().toString().trim();
        
        // Validate email
        String emailError = InputValidation.validateEmail(email);
        if (emailError != null) {
            edtForgotEmail.setError(emailError);
            edtForgotEmail.requestFocus();
            return;
        }
        
        // Disable button để tránh click nhiều lần
        btnEditPassword.setEnabled(false);
        
        // Kiểm tra email có tồn tại không
        UserService.VerifyEmailResult result = userService.verifyEmailForReset(email);
        
        if (result.isSuccess()) {
            // Email hợp lệ, chuyển sang màn hình đổi mật khẩu
            Intent intent = new Intent(ForgotPasswordActivity.this, EditPasswordActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
            finish();
        } else {
            // Email không tồn tại
            Toast.makeText(this, result.getMessage(), Toast.LENGTH_LONG).show();
            edtForgotEmail.setError(result.getMessage());
            edtForgotEmail.requestFocus();
            
            // Enable lại button
            btnEditPassword.setEnabled(true);
        }
    }
}
