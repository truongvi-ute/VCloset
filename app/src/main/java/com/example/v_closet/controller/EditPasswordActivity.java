package com.example.v_closet.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.v_closet.R;
import com.example.v_closet.service.UserService;
import com.example.v_closet.util.InputValidation;

public class EditPasswordActivity extends AppCompatActivity {
    
    private UserService userService;
    private EditText edtUserPassword;
    private EditText edtUserConfirmPassword;
    private Button btnConfirm;
    private ImageButton btnBack;
    private TextView tvTitle;
    private TextView tvDescription;
    
    private String email;
    private int userId;
    private boolean fromProfile; // Kiểm tra xem có phải từ Profile không
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_password);
        
        // Khởi tạo UserService
        userService = new UserService(this);
        
        // Lấy thông tin từ Intent
        email = getIntent().getStringExtra("email");
        userId = getIntent().getIntExtra("user_id", 0);
        fromProfile = getIntent().getBooleanExtra("from_profile", false);
        
        if (email == null || email.isEmpty()) {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin email", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Ánh xạ views
        initViews();
        
        // Setup UI dựa trên nguồn gọi
        setupUI();
        
        // Xử lý nút xác nhận
        btnConfirm.setOnClickListener(v -> handlePasswordChange());
        
        // Xử lý nút back
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }
    
    private void initViews() {
        edtUserPassword = findViewById(R.id.edtUserPassword);
        edtUserConfirmPassword = findViewById(R.id.edtUserConfirmPassword);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnBack = findViewById(R.id.btn_back);
        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
    }
    
    /**
     * Setup UI dựa trên nguồn gọi (từ Profile hay Forgot Password)
     */
    private void setupUI() {
        if (fromProfile) {
            // Đổi mật khẩu từ Profile
            if (tvTitle != null) {
                tvTitle.setText("Đổi mật khẩu");
            }
            if (tvDescription != null) {
                tvDescription.setText("Nhập mật khẩu mới của bạn");
            }
        } else {
            // Reset mật khẩu từ Forgot Password
            if (tvTitle != null) {
                tvTitle.setText("Đặt mật khẩu mới");
            }
            if (tvDescription != null) {
                tvDescription.setText("Hãy ghi nhớ mật khẩu này!!\nMật khẩu này dùng để đăng nhập");
            }
        }
    }
    
    /**
     * Xử lý đổi/đặt lại mật khẩu
     */
    private void handlePasswordChange() {
        if (fromProfile) {
            handleChangePassword();
        } else {
            handleResetPassword();
        }
    }
    
    /**
     * Xử lý đổi mật khẩu (từ Profile)
     */
    private void handleChangePassword() {
        String newPassword = edtUserPassword.getText().toString().trim();
        String confirmPassword = edtUserConfirmPassword.getText().toString().trim();
        
        // Validate new password
        String passwordError = InputValidation.validatePassword(newPassword);
        if (passwordError != null) {
            edtUserPassword.setError(passwordError);
            edtUserPassword.requestFocus();
            return;
        }
        
        // Validate confirm password
        String confirmPasswordError = InputValidation.validateConfirmPassword(newPassword, confirmPassword);
        if (confirmPasswordError != null) {
            edtUserConfirmPassword.setError(confirmPasswordError);
            edtUserConfirmPassword.requestFocus();
            return;
        }
        
        // Disable button để tránh click nhiều lần
        btnConfirm.setEnabled(false);
        
        // Thực hiện đổi mật khẩu trực tiếp bằng userId
        UserService.ResetPasswordResult result = userService.resetPassword(email, newPassword);
        
        if (result.isSuccess()) {
            // Đổi mật khẩu thành công - đăng xuất và chuyển về màn hình đăng nhập
            Toast.makeText(this, "Đổi mật khẩu thành công. Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
            
            Intent intent = new Intent(EditPasswordActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("password_changed", true);
            intent.putExtra("email", email);
            startActivity(intent);
            finish();
        } else {
            // Đổi mật khẩu thất bại
            Toast.makeText(this, result.getMessage(), Toast.LENGTH_LONG).show();
            
            // Enable lại button
            btnConfirm.setEnabled(true);
        }
    }
    
    /**
     * Xử lý đặt lại mật khẩu (từ Forgot Password)
     */
    private void handleResetPassword() {
        String password = edtUserPassword.getText().toString().trim();
        String confirmPassword = edtUserConfirmPassword.getText().toString().trim();
        
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
        
        // Disable button để tránh click nhiều lần
        btnConfirm.setEnabled(false);
        
        // Thực hiện đặt lại mật khẩu
        UserService.ResetPasswordResult result = userService.resetPassword(email, password);
        
        if (result.isSuccess()) {
            // Đặt lại mật khẩu thành công
            Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();
            
            // Chuyển về màn hình đăng nhập
            Intent intent = new Intent(EditPasswordActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("reset_success", true);
            intent.putExtra("email", email);
            startActivity(intent);
            finish();
        } else {
            // Đặt lại mật khẩu thất bại
            Toast.makeText(this, result.getMessage(), Toast.LENGTH_LONG).show();
            
            // Enable lại button
            btnConfirm.setEnabled(true);
        }
    }
}
