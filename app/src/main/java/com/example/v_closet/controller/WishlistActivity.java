package com.example.v_closet.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.v_closet.R;
import com.example.v_closet.adapter.WishlistAdapter;
import com.example.v_closet.entity.Product;
import com.example.v_closet.service.WishlistService;

import java.util.List;

/**
 * WishlistActivity - Màn hình danh sách yêu thích
 */
public class WishlistActivity extends AppCompatActivity {
    
    private WishlistService wishlistService;
    private RecyclerView rvWishlist;
    private LinearLayout layoutEmpty;
    private WishlistAdapter adapter;
    private ImageButton btnBack;
    
    private int userId;
    private List<Product> wishlistProducts;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishlist);
        
        // Khởi tạo service
        wishlistService = new WishlistService(this);
        
        // Lấy userId từ Intent
        getUserInfoFromIntent();
        
        // Ánh xạ views
        initViews();
        
        // Setup RecyclerView
        setupRecyclerView();
        
        // Load wishlist
        loadWishlist();
        
        // Setup listeners
        setupListeners();
    }
    
    private void getUserInfoFromIntent() {
        userId = getIntent().getIntExtra("user_id", 0);
        
        if (userId == 0) {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    
    private void initViews() {
        rvWishlist = findViewById(R.id.rv_wishlist);
        layoutEmpty = findViewById(R.id.layout_empty);
        btnBack = findViewById(R.id.btn_back);
    }
    
    private void setupRecyclerView() {
        // Setup GridLayoutManager với 2 cột
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvWishlist.setLayoutManager(layoutManager);
        rvWishlist.setHasFixedSize(true);
    }
    
    private void loadWishlist() {
        wishlistProducts = wishlistService.getWishlistProducts(userId);
        
        if (wishlistProducts != null && !wishlistProducts.isEmpty()) {
            // Hiển thị danh sách
            rvWishlist.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
            
            // Setup adapter
            adapter = new WishlistAdapter(this, wishlistProducts, new WishlistAdapter.OnWishlistItemClickListener() {
                @Override
                public void onProductClick(Product product) {
                    // Chuyển đến trang chi tiết sản phẩm
                    Intent intent = new Intent(WishlistActivity.this, ProductDetailActivity.class);
                    intent.putExtra("product_id", product.getId());
                    intent.putExtra("user_id", userId);
                    startActivity(intent);
                }
                
                @Override
                public void onRemoveClick(Product product) {
                    // Hiển thị dialog xác nhận xóa
                    showRemoveConfirmDialog(product);
                }
            });
            
            rvWishlist.setAdapter(adapter);
        } else {
            // Hiển thị empty state
            rvWishlist.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        }
    }
    
    private void setupListeners() {
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }
    
    /**
     * Hiển thị dialog xác nhận xóa sản phẩm
     */
    private void showRemoveConfirmDialog(Product product) {
        new AlertDialog.Builder(this)
            .setTitle("Xóa khỏi danh sách yêu thích")
            .setMessage("Bạn có chắc muốn xóa \"" + product.getName() + "\" khỏi danh sách yêu thích?")
            .setPositiveButton("Xóa", (dialog, which) -> {
                removeFromWishlist(product);
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
    
    /**
     * Xóa sản phẩm khỏi wishlist
     */
    private void removeFromWishlist(Product product) {
        boolean success = wishlistService.removeFromWishlist(userId, product.getId());
        
        if (success) {
            Toast.makeText(this, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
            
            // Reload wishlist
            loadWishlist();
        } else {
            Toast.makeText(this, "Lỗi khi xóa sản phẩm", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Reload wishlist khi quay lại màn hình
        loadWishlist();
    }
}
