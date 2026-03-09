package com.example.v_closet.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.v_closet.R;
import com.example.v_closet.adapter.OrderAdapter;
import com.example.v_closet.entity.Order;
import com.example.v_closet.service.OrderService;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

/**
 * Activity hiển thị lịch sử mua hàng
 */
public class OrderHistoryActivity extends AppCompatActivity {
    
    private MaterialToolbar toolbar;
    private RecyclerView rvOrders;
    private LinearLayout layoutEmpty;
    
    private OrderService orderService;
    private OrderAdapter adapter;
    private int userId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_history);
        
        initViews();
        initServices();
        loadOrders();
    }
    
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        rvOrders = findViewById(R.id.rvOrders);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        
        // Setup toolbar
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        
        // Setup RecyclerView
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
    }
    
    private void initServices() {
        orderService = new OrderService(this);
    }
    
    private void loadOrders() {
        // Get userId from intent
        userId = getIntent().getIntExtra("user_id", -1);
        
        if (userId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Load orders
        List<Order> orders = orderService.getUserOrders(userId);
        
        if (orders == null || orders.isEmpty()) {
            showEmptyState();
        } else {
            showOrders(orders);
        }
    }
    
    private void showEmptyState() {
        rvOrders.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.VISIBLE);
    }
    
    private void showOrders(List<Order> orders) {
        rvOrders.setVisibility(View.VISIBLE);
        layoutEmpty.setVisibility(View.GONE);
        
        adapter = new OrderAdapter(this, orders);
        rvOrders.setAdapter(adapter);
    }
}
