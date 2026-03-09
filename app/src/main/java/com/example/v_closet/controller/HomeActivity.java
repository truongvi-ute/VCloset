package com.example.v_closet.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.v_closet.R;
import com.example.v_closet.adapter.ProductAdapter;
import com.example.v_closet.entity.Product;
import com.example.v_closet.service.ProductService;

import java.util.List;

/**
 * HomeActivity - Màn hình chính hiển thị sản phẩm
 */
public class HomeActivity extends AppCompatActivity {
    
    private ProductService productService;
    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private ImageButton btnProfile;
    private ImageButton btnMyCart;
    
    private int userId;
    private String username;
    private String email;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        
        // Khởi tạo ProductService
        productService = new ProductService(this);
        
        // Lấy thông tin user từ Intent
        getUserInfoFromIntent();
        
        // Ánh xạ views
        initViews();
        
        // Setup button listeners
        setupButtonListeners();
        
        // Load và hiển thị sản phẩm
        loadProducts();
    }
    
    /**
     * Lấy thông tin user từ Intent
     */
    private void getUserInfoFromIntent() {
        userId = getIntent().getIntExtra("user_id", 0);
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        
        // Nếu không có username/email trong Intent, thử lấy từ SharedPreferences
        if (userId == 0) {
            android.content.SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            userId = prefs.getInt("user_id", 0);
            username = prefs.getString("username", null);
            email = prefs.getString("email", null);
        }
        
        if (userId == 0 || username == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    
    /**
     * Khởi tạo views
     */
    private void initViews() {
        btnProfile = findViewById(R.id.btnProfile);
        btnMyCart = findViewById(R.id.btnMyCart);
        recyclerViewProducts = findViewById(R.id.rv_products);
        
        // Setup RecyclerView với StaggeredGridLayoutManager (2 cột, hiệu ứng staggered)
        androidx.recyclerview.widget.StaggeredGridLayoutManager layoutManager = 
            new androidx.recyclerview.widget.StaggeredGridLayoutManager(2, 
                androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL);
        recyclerViewProducts.setLayoutManager(layoutManager);
        recyclerViewProducts.setHasFixedSize(true);
    }
    
    /**
     * Setup button listeners
     */
    private void setupButtonListeners() {
        // Xử lý nút Profile
        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            intent.putExtra("user_id", userId);
            intent.putExtra("username", username);
            intent.putExtra("email", email);
            startActivity(intent);
        });
        
        // Xử lý nút Cart
        btnMyCart.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            intent.putExtra("user_id", userId);
            startActivity(intent);
        });
    }
    
    /**
     * Load danh sách sản phẩm
     */
    private void loadProducts() {
        List<Product> products = productService.getAllProducts();
        
        if (products != null && !products.isEmpty()) {
            // Khởi tạo adapter và set cho RecyclerView
            productAdapter = new ProductAdapter(this, products, product -> {
                // Xử lý khi click vào sản phẩm
                onProductClick(product);
            });
            recyclerViewProducts.setAdapter(productAdapter);
            
            Toast.makeText(this, "Đã tải " + products.size() + " sản phẩm", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Không có sản phẩm nào", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Xử lý khi click vào sản phẩm
     */
    private void onProductClick(Product product) {
        Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
        intent.putExtra("product_id", product.getId());
        intent.putExtra("user_id", userId);
        startActivity(intent);
    }
    
    @Override
    public void onBackPressed() {
        // Không cho phép quay lại LoginActivity
        // Có thể hiển thị dialog xác nhận đăng xuất
        super.onBackPressed();
        finishAffinity(); // Đóng tất cả activity
    }
}
