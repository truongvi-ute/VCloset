package com.example.v_closet.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.v_closet.R;
import com.example.v_closet.adapter.CheckoutAdapter;
import com.example.v_closet.entity.Address;
import com.example.v_closet.entity.CartItemDetail;
import com.example.v_closet.entity.enums.PaymentMethod;
import com.example.v_closet.service.AddressService;
import com.example.v_closet.service.OrderService;
import com.google.android.material.appbar.MaterialToolbar;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Activity xác nhận đơn hàng trước khi thanh toán
 */
public class CheckoutActivity extends AppCompatActivity {
    
    private static final int REQUEST_SELECT_ADDRESS = 100;
    
    private MaterialToolbar toolbar;
    private TextView tvReceiverName, tvPhoneNumber, tvShippingAddress;
    private TextView tvChangeAddress;
    private RecyclerView rvOrderItems;
    private RadioGroup rgPaymentMethod;
    private RadioButton rbCOD, rbBankTransfer;
    private EditText etNote;
    private TextView tvSubtotal, tvShippingFee, tvTotalAmount;
    private Button btnPlaceOrder;
    
    private CheckoutAdapter adapter;
    private List<CartItemDetail> orderItems;
    private Address selectedAddress;
    private AddressService addressService;
    private OrderService orderService;
    private NumberFormat currencyFormat;
    
    private int userId;
    private double subtotal = 0;
    private double shippingFee = 30000; // Phí ship cố định 30k
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);
        
        initViews();
        initServices();
        loadData();
        setupListeners();
    }
    
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvReceiverName = findViewById(R.id.tvReceiverName);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvShippingAddress = findViewById(R.id.tvShippingAddress);
        tvChangeAddress = findViewById(R.id.tvChangeAddress);
        rvOrderItems = findViewById(R.id.rvOrderItems);
        rgPaymentMethod = findViewById(R.id.rgPaymentMethod);
        rbCOD = findViewById(R.id.rbCOD);
        rbBankTransfer = findViewById(R.id.rbBankTransfer);
        etNote = findViewById(R.id.etNote);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvShippingFee = findViewById(R.id.tvShippingFee);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        
        currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        
        // Setup toolbar
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }
    
    private void initServices() {
        addressService = new AddressService(this);
        orderService = new com.example.v_closet.service.OrderService(this);
    }
    
    private void loadData() {
        // Lấy userId từ intent và lưu vào biến instance
        userId = getIntent().getIntExtra("user_id", -1);
        if (userId == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Lấy danh sách sản phẩm từ intent
        orderItems = (List<CartItemDetail>) getIntent().getSerializableExtra("orderItems");
        if (orderItems == null) {
            orderItems = new ArrayList<>();
        }
        
        // Setup RecyclerView
        adapter = new CheckoutAdapter(this, orderItems);
        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
        rvOrderItems.setAdapter(adapter);
        
        // Tính tổng tiền
        calculateTotal();
        
        // Load địa chỉ mặc định
        loadDefaultAddress();
    }
    
    private void loadDefaultAddress() {
        selectedAddress = addressService.getDefaultAddress(userId);
        if (selectedAddress != null) {
            displayAddress(selectedAddress);
        } else {
            // Nếu chưa có địa chỉ mặc định, hiển thị thông báo
            tvReceiverName.setText("Chưa có địa chỉ");
            tvPhoneNumber.setText("");
            tvShippingAddress.setText("Vui lòng thêm địa chỉ giao hàng");
        }
    }
    
    private void displayAddress(Address address) {
        tvReceiverName.setText(address.getReceiverName());
        tvPhoneNumber.setText(address.getPhoneNumber());
        tvShippingAddress.setText(address.getFullAddress());
    }
    
    private void calculateTotal() {
        subtotal = 0;
        for (CartItemDetail item : orderItems) {
            double price = item.getProduct().getBasePrice() + item.getVariant().getPriceAdjustment();
            subtotal += price * item.getCartItem().getQuantity();
        }
        
        double total = subtotal + shippingFee;
        
        tvSubtotal.setText(currencyFormat.format(subtotal) + "đ");
        tvShippingFee.setText(currencyFormat.format(shippingFee) + "đ");
        tvTotalAmount.setText(currencyFormat.format(total) + "đ");
    }
    
    private void setupListeners() {
        // Thay đổi địa chỉ
        tvChangeAddress.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddressListActivity.class);
            intent.putExtra("selectMode", true);
            intent.putExtra("user_id", userId);
            startActivityForResult(intent, REQUEST_SELECT_ADDRESS);
        });
        
        // Đặt hàng
        btnPlaceOrder.setOnClickListener(v -> placeOrder());
    }
    
    private void placeOrder() {
        // Validate địa chỉ
        if (selectedAddress == null) {
            Toast.makeText(this, "Vui lòng chọn địa chỉ giao hàng", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Validate sản phẩm
        if (orderItems == null || orderItems.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Lấy phương thức thanh toán
        PaymentMethod paymentMethod = getSelectedPaymentMethod();
        
        // Lấy ghi chú
        String note = etNote.getText().toString().trim();
        
        // Hiển thị dialog xác nhận
        new AlertDialog.Builder(this)
            .setTitle("Xác nhận đặt hàng")
            .setMessage("Bạn có chắc chắn muốn đặt hàng?")
            .setPositiveButton("Đặt hàng", (dialog, which) -> {
                // Lưu đơn hàng vào database
                double totalAmount = subtotal + shippingFee;
                
                long orderId = orderService.createOrder(
                    userId,
                    orderItems,
                    selectedAddress,
                    paymentMethod,
                    note,
                    totalAmount
                );
                
                if (orderId != -1) {
                    // Đặt hàng thành công
                    Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                    
                    // Chuyển sang trang thành công
                    Intent intent = new Intent(this, OrderSuccessActivity.class);
                    intent.putExtra("order_id", (int) orderId);
                    intent.putExtra("total_amount", totalAmount);
                    intent.putExtra("payment_method", paymentMethod.name());
                    intent.putExtra("user_id", userId);
                    startActivity(intent);
                    finish();
                } else {
                    // Đặt hàng thất bại
                    Toast.makeText(this, "Lỗi khi đặt hàng. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
    
    private PaymentMethod getSelectedPaymentMethod() {
        int selectedId = rgPaymentMethod.getCheckedRadioButtonId();
        if (selectedId == R.id.rbCOD) {
            return PaymentMethod.COD;
        } else if (selectedId == R.id.rbBankTransfer) {
            return PaymentMethod.BANK_TRANSFER;
        }
        return PaymentMethod.COD; // Mặc định
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_ADDRESS && resultCode == RESULT_OK) {
            if (data != null) {
                selectedAddress = (Address) data.getSerializableExtra("selectedAddress");
                if (selectedAddress != null) {
                    displayAddress(selectedAddress);
                }
            }
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Reload địa chỉ mặc định mỗi khi quay lại activity
        // Điều này đảm bảo địa chỉ được cập nhật sau khi thêm mới hoặc thay đổi
        loadDefaultAddress();
    }
}
