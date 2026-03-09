package com.example.v_closet.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.v_closet.R;
import com.example.v_closet.adapter.AddressAdapter;
import com.example.v_closet.entity.Address;
import com.example.v_closet.service.AddressService;

import java.util.List;

/**
 * AddressListActivity - Màn hình danh sách địa chỉ
 */
public class AddressListActivity extends AppCompatActivity {
    
    private AddressService addressService;
    private RecyclerView rvAddresses;
    private LinearLayout layoutEmpty;
    private ImageButton btnBack;
    private ImageButton btnAdd;
    private AddressAdapter adapter;
    
    private int userId;
    private List<Address> addresses;
    private boolean selectMode = false; // Chế độ chọn địa chỉ
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_list);
        
        // Kiểm tra chế độ selection
        selectMode = getIntent().getBooleanExtra("selectMode", false);
        
        // Khởi tạo service
        addressService = new AddressService(this);
        
        // Lấy userId từ Intent
        getUserInfoFromIntent();
        
        // Ánh xạ views
        initViews();
        
        // Setup RecyclerView
        setupRecyclerView();
        
        // Load addresses
        loadAddresses();
        
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
        rvAddresses = findViewById(R.id.rv_addresses);
        layoutEmpty = findViewById(R.id.layout_empty);
        btnBack = findViewById(R.id.btn_back);
        btnAdd = findViewById(R.id.btnAdd);
    }
    
    private void setupRecyclerView() {
        rvAddresses.setLayoutManager(new LinearLayoutManager(this));
        rvAddresses.setHasFixedSize(true);
    }
    
    private void loadAddresses() {
        addresses = addressService.getUserAddresses(userId);
        
        if (addresses != null && !addresses.isEmpty()) {
            // Hiển thị danh sách
            rvAddresses.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
            
            // Setup adapter
            adapter = new AddressAdapter(this, addresses, new AddressAdapter.OnAddressItemClickListener() {
                @Override
                public void onSetDefaultClick(Address address) {
                    setDefaultAddress(address);
                }
                
                @Override
                public void onEditClick(Address address) {
                    // Chuyển sang màn hình edit address
                    android.content.Intent intent = new android.content.Intent(AddressListActivity.this, AddAddressActivity.class);
                    intent.putExtra("user_id", userId);
                    intent.putExtra("address", address);
                    startActivity(intent);
                }
                
                @Override
                public void onDeleteClick(Address address) {
                    showDeleteConfirmDialog(address);
                }
                
                @Override
                public void onItemClick(Address address) {
                    // Nếu ở chế độ selection, trả về địa chỉ đã chọn
                    if (selectMode) {
                        android.content.Intent resultIntent = new android.content.Intent();
                        resultIntent.putExtra("selectedAddress", address);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                }
            });
            
            rvAddresses.setAdapter(adapter);
        } else {
            // Hiển thị empty state
            rvAddresses.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        }
    }
    
    private void setupListeners() {
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
        
        if (btnAdd != null) {
            btnAdd.setOnClickListener(v -> {
                // Chuyển sang màn hình thêm địa chỉ mới
                android.content.Intent intent = new android.content.Intent(AddressListActivity.this, AddAddressActivity.class);
                intent.putExtra("user_id", userId);
                startActivity(intent);
            });
        }
    }
    
    /**
     * Set địa chỉ làm mặc định
     */
    private void setDefaultAddress(Address address) {
        boolean success = addressService.setDefaultAddress(address.getId(), userId);
        
        if (success) {
            Toast.makeText(this, "Đã đặt làm địa chỉ mặc định", Toast.LENGTH_SHORT).show();
            
            // Reload danh sách
            loadAddresses();
        } else {
            Toast.makeText(this, "Lỗi khi đặt địa chỉ mặc định", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Hiển thị dialog xác nhận xóa
     */
    private void showDeleteConfirmDialog(Address address) {
        new AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc muốn xóa địa chỉ này?")
            .setPositiveButton("Xóa", (dialog, which) -> {
                deleteAddress(address);
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
    
    /**
     * Xóa địa chỉ
     */
    private void deleteAddress(Address address) {
        boolean success = addressService.deleteAddress(address.getId());
        
        if (success) {
            Toast.makeText(this, "Đã xóa địa chỉ", Toast.LENGTH_SHORT).show();
            
            // Reload danh sách
            loadAddresses();
        } else {
            Toast.makeText(this, "Lỗi khi xóa địa chỉ", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Reload addresses khi quay lại màn hình
        loadAddresses();
    }
}
