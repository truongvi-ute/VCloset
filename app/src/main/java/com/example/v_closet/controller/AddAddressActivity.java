package com.example.v_closet.controller;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.example.v_closet.R;
import com.example.v_closet.entity.Address;
import com.example.v_closet.service.AddressService;

/**
 * AddAddressActivity - Màn hình thêm/sửa địa chỉ
 */
public class AddAddressActivity extends AppCompatActivity {
    
    private AddressService addressService;
    
    private TextView tvTitle;
    private EditText edtReceiverName;
    private EditText edtPhoneNumber;
    private EditText edtCity;
    private EditText edtDistrict;
    private EditText edtWard;
    private EditText edtStreet;
    private MaterialCheckBox cbSetDefault;
    private Button btnSave;
    private ImageButton btnBack;
    
    private int userId;
    private Address editingAddress; // Null nếu thêm mới, có giá trị nếu đang sửa
    private boolean isEditMode = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_address);
        
        // Khởi tạo service
        addressService = new AddressService(this);
        
        // Lấy thông tin từ Intent
        getInfoFromIntent();
        
        // Ánh xạ views
        initViews();
        
        // Setup UI
        setupUI();
        
        // Setup listeners
        setupListeners();
    }
    
    private void getInfoFromIntent() {
        userId = getIntent().getIntExtra("user_id", 0);
        editingAddress = (Address) getIntent().getSerializableExtra("address");
        isEditMode = editingAddress != null;
        
        if (userId == 0) {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    
    private void initViews() {
        tvTitle = findViewById(R.id.tvTitle);
        edtReceiverName = findViewById(R.id.edtReceiverName);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtCity = findViewById(R.id.edtCity);
        edtDistrict = findViewById(R.id.edtDistrict);
        edtWard = findViewById(R.id.edtWard);
        edtStreet = findViewById(R.id.edtStreet);
        cbSetDefault = findViewById(R.id.cbSetDefault);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btn_back);
    }
    
    private void setupUI() {
        if (isEditMode) {
            // Chế độ sửa
            tvTitle.setText("Sửa Địa Chỉ");
            btnSave.setText("Cập Nhật");
            
            // Fill dữ liệu
            edtReceiverName.setText(editingAddress.getReceiverName());
            edtPhoneNumber.setText(editingAddress.getPhoneNumber());
            edtCity.setText(editingAddress.getCity());
            edtDistrict.setText(editingAddress.getDistrict());
            edtWard.setText(editingAddress.getWard());
            edtStreet.setText(editingAddress.getStreet());
            cbSetDefault.setChecked(editingAddress.isDefault());
        } else {
            // Chế độ thêm mới
            tvTitle.setText("Thêm Địa Chỉ Mới");
            btnSave.setText("Lưu Địa Chỉ");
        }
    }
    
    private void setupListeners() {
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
        
        if (btnSave != null) {
            btnSave.setOnClickListener(v -> handleSave());
        }
    }
    
    /**
     * Xử lý lưu địa chỉ
     */
    private void handleSave() {
        String receiverName = edtReceiverName.getText().toString().trim();
        String phoneNumber = edtPhoneNumber.getText().toString().trim();
        String city = edtCity.getText().toString().trim();
        String district = edtDistrict.getText().toString().trim();
        String ward = edtWard.getText().toString().trim();
        String street = edtStreet.getText().toString().trim();
        boolean isDefault = cbSetDefault.isChecked();
        
        // Disable button để tránh click nhiều lần
        btnSave.setEnabled(false);
        
        if (isEditMode) {
            // Cập nhật địa chỉ
            editingAddress.setReceiverName(receiverName);
            editingAddress.setPhoneNumber(phoneNumber);
            editingAddress.setCity(city);
            editingAddress.setDistrict(district);
            editingAddress.setWard(ward);
            editingAddress.setStreet(street);
            editingAddress.setDefault(isDefault);
            
            AddressService.UpdateResult result = addressService.updateAddress(editingAddress);
            
            if (result.isSuccess()) {
                Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();
                btnSave.setEnabled(true);
            }
        } else {
            // Thêm địa chỉ mới
            Address newAddress = new Address(userId, receiverName, phoneNumber, 
                street, ward, district, city, isDefault);
            
            AddressService.AddResult result = addressService.addAddress(newAddress);
            
            if (result.isSuccess()) {
                Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();
                btnSave.setEnabled(true);
            }
        }
    }
}
