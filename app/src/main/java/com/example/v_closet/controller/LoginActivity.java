package com.example.v_closet.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.v_closet.R;
import com.example.v_closet.entity.User;
import com.example.v_closet.service.UserService;
import com.example.v_closet.util.InputValidation;

public class LoginActivity extends AppCompatActivity {
    
    private UserService userService;
    private EditText edtEmail;
    private EditText edtPass;
    private Button btnLogin;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        // Khởi tạo UserService
        userService = new UserService(this);
        
        // Ánh xạ views
        edtEmail = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        TextView txtForgotPass = findViewById(R.id.txtForgotPassword);
        TextView txtRegister = findViewById(R.id.txtRegister);

        // Xử lý đăng nhập
        btnLogin.setOnClickListener(v -> handleLogin());
        
        // Xử lý quên mật khẩu
        txtForgotPass.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
        
        // Xử lý đăng ký
        txtRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        
        // Kiểm tra có username từ RegisterActivity không
        String registeredUsername = getIntent().getStringExtra("registered_username");
        if (registeredUsername != null) {
            edtEmail.setText(registeredUsername);
            Toast.makeText(this, "Đăng ký thành công! Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Xử lý logic đăng nhập
     */
    private void handleLogin() {
        String username = edtEmail.getText().toString().trim();
        String password = edtPass.getText().toString().trim();

        // Validate input
        if (InputValidation.isEmpty(username)) {
            edtEmail.setError("Tài khoản không được để trống");
            edtEmail.requestFocus();
            return;
        }
        
        if (InputValidation.isEmpty(password)) {
            edtPass.setError("Mật khẩu không được để trống");
            edtPass.requestFocus();
            return;
        }
        
        // Disable button để tránh click nhiều lần
        btnLogin.setEnabled(false);
        
        // Thực hiện đăng nhập
        UserService.LoginResult result = userService.login(username, password);
        
        if (result.isSuccess()) {
            // Đăng nhập thành công
            User user = result.getUser();
            Toast.makeText(this, "Chào mừng " + user.getUsername(), Toast.LENGTH_SHORT).show();
            
            // Lưu thông tin user vào SharedPreferences
            android.content.SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            android.content.SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("user_id", user.getId());
            editor.putString("username", user.getUsername());
            editor.putString("email", user.getEmail());
            editor.apply();
            
            // Chuyển sang HomeActivity và truyền thông tin user
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra("user_id", user.getId());
            intent.putExtra("username", user.getUsername());
            intent.putExtra("email", user.getEmail());
            startActivity(intent);
            finish();
        } else {
            // Đăng nhập thất bại
            Toast.makeText(this, result.getMessage(), Toast.LENGTH_LONG).show();
            edtPass.setError(result.getMessage());
            edtPass.requestFocus();
            
            // Enable lại button
            btnLogin.setEnabled(true);
        }
    }
}
