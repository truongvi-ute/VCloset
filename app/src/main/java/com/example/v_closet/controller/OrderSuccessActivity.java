package com.example.v_closet.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.v_closet.R;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Activity hiển thị màn hình đặt hàng thành công
 */
public class OrderSuccessActivity extends AppCompatActivity {
    
    private TextView tvOrderId;
    private TextView tvTotalAmount;
    private TextView tvPaymentMethod;
    private Button btnBackToHome;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_success);
        
        initViews();
        loadOrderInfo();
        setupListeners();
    }
    
    private void initViews() {
        tvOrderId = findViewById(R.id.tvOrderId);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
        btnBackToHome = findViewById(R.id.btnBackToHome);
    }
    
    private void loadOrderInfo() {
        // Lấy thông tin đơn hàng từ Intent
        int orderId = getIntent().getIntExtra("order_id", 0);
        double totalAmount = getIntent().getDoubleExtra("total_amount", 0);
        String paymentMethod = getIntent().getStringExtra("payment_method");
        
        // Hiển thị thông tin
        tvOrderId.setText("#" + orderId);
        
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvTotalAmount.setText(formatter.format(totalAmount) + "đ");
        
        tvPaymentMethod.setText(getPaymentMethodText(paymentMethod));
    }
    
    private String getPaymentMethodText(String method) {
        if (method == null) return "Tiền mặt";
        
        switch (method) {
            case "COD":
                return "Tiền mặt";
            case "BANK_TRANSFER":
                return "Chuyển khoản";
            default:
                return "Tiền mặt";
        }
    }
    
    private void setupListeners() {
        btnBackToHome.setOnClickListener(v -> goBackToHome());
    }
    
    private void goBackToHome() {
        // Quay về trang chủ và xóa tất cả activity trước đó
        Intent intent = new Intent(OrderSuccessActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        // Không cần truyền user_id vì HomeActivity sẽ lấy từ SharedPreferences
        startActivity(intent);
        finish();
    }
    
    @Override
    public void onBackPressed() {
        // Chặn nút back, bắt buộc phải dùng nút "Quay lại trang chủ"
        goBackToHome();
    }
}
