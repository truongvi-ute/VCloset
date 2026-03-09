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

public class RegisterActivity extends AppCompatActivity {
    
    private UserService userService;
    private EditText edtUsername;
    private EditText edtUserEmail;
    private EditText edtUserPassword;
    private EditText edtUserConfirmPassword;
    private Button btnRegister;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        
        // Khởi tạo UserService
        userService = new UserService(this);
        
        // Ánh xạ views
        edtUsername = findViewById(R.id.edtUsername);
        edtUserEmail = findViewById(R.id.edtUserEmail);
        edtUserPassword = findViewById(R.id.edtUserPassword);
        edtUserConfirmPassword = findViewById(R.id.edtUserConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        ImageButton btn_back = findViewById(R.id.btn_back);

        // Xử lý sự kiện đăng ký
        btnRegister.setOnClickListener(v -> handleRegister());

        // Xử lý nút back
        btn_back.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
    
    /**
     * Xử lý logic đăng ký tài khoản
     */
    private void handleRegister() {
        // Lấy dữ liệu từ form
        String username = edtUsername.getText().toString().trim();
        String email = edtUserEmail.getText().toString().trim();
        String password = edtUserPassword.getText().toString().trim();
        String confirmPassword = edtUserConfirmPassword.getText().toString().trim();
        
        // Validate username
        String usernameError = InputValidation.validateUsername(username);
        if (usernameError != null) {
            edtUsername.setError(usernameError);
            edtUsername.requestFocus();
            return;
        }
        
        // Validate email
        String emailError = InputValidation.validateEmail(email);
        if (emailError != null) {
            edtUserEmail.setError(emailError);
            edtUserEmail.requestFocus();
            return;
        }
        
        // Validate password
        String passwordError = InputValidation.validatePassword(password);
        if (passwordError != null) {
            edtUserPassword.setError(passwordError);
            edtUserPassword.requestFocus();
            return;
        }
        
        // Validate confirm password
        String confirmPasswordError = InputValidation.validateConfirmPassword(password, confirmPassword);
        if (confirmPasswordError != null) {
            edtUserConfirmPassword.setError(confirmPasswordError);
            edtUserConfirmPassword.requestFocus();
            return;
        }
        
        // Thực hiện đăng ký
        registerUser(username, password, email);
    }
    
    /**
     * Gọi UserService để đăng ký và lưu vào database
     */
    private void registerUser(String username, String password, String email) {
        // Disable button để tránh click nhiều lần
        btnRegister.setEnabled(false);
        
        // Gọi service để đăng ký
        UserService.RegisterResult result = userService.register(username, password, email);
        
        if (result.isSuccess()) {
            // Đăng ký thành công
            Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();
            
            // Chuyển về màn hình đăng nhập
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            intent.putExtra("registered_username", username);
            startActivity(intent);
            finish();
        } else {
            // Đăng ký thất bại
            Toast.makeText(this, result.getMessage(), Toast.LENGTH_LONG).show();
            
            // Enable lại button
            btnRegister.setEnabled(true);
            
            // Focus vào field tương ứng
            if (result.getMessage().contains("Tên đăng nhập")) {
                edtUsername.setError(result.getMessage());
                edtUsername.requestFocus();
            } else if (result.getMessage().contains("Email")) {
                edtUserEmail.setError(result.getMessage());
                edtUserEmail.requestFocus();
            }
        }
    }
}
