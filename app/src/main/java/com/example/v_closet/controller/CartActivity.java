package com.example.v_closet.controller;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.v_closet.R;
import com.example.v_closet.adapter.CartAdapter;
import com.example.v_closet.entity.CartItemDetail;
import com.example.v_closet.service.CartService;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartItemListener {
    
    private CartService cartService;
    private RecyclerView rvCartItems;
    private TextView tvEmptyCart;
    private TextView tvTotalPrice;
    private Button btnCheckout;
    private ImageButton btnBack;
    private CartAdapter adapter;
    
    private int userId;
    private List<CartItemDetail> cartItems;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);
        
        cartService = new CartService(this);
        userId = getIntent().getIntExtra("user_id", 0);
        
        if (userId == 0) {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        initViews();
        setupListeners();
        loadCartItems();
    }
    
    private void initViews() {
        rvCartItems = findViewById(R.id.rvCartItems);
        tvEmptyCart = findViewById(R.id.tvEmptyCart);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnBack = findViewById(R.id.btn_back);
        
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
    }
    
    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        
        btnCheckout.setOnClickListener(v -> {
            if (cartItems == null || cartItems.isEmpty()) {
                Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Lấy danh sách sản phẩm đã chọn
            List<CartItemDetail> selectedItems = adapter.getSelectedItems();
            
            if (selectedItems.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn sản phẩm để thanh toán", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Chuyển sang trang checkout với các sản phẩm đã chọn
            android.content.Intent intent = new android.content.Intent(CartActivity.this, CheckoutActivity.class);
            intent.putExtra("orderItems", new java.util.ArrayList<>(selectedItems));
            intent.putExtra("user_id", userId);
            startActivity(intent);
        });
    }
    
    private void loadCartItems() {
        cartItems = cartService.getCartItemsWithDetails(userId);
        
        if (cartItems == null || cartItems.isEmpty()) {
            showEmptyCart();
        } else {
            showCartItems();
        }
    }
    
    private void showEmptyCart() {
        rvCartItems.setVisibility(android.view.View.GONE);
        tvEmptyCart.setVisibility(android.view.View.VISIBLE);
        tvTotalPrice.setText("0₫");
    }
    
    private void showCartItems() {
        rvCartItems.setVisibility(android.view.View.VISIBLE);
        tvEmptyCart.setVisibility(android.view.View.GONE);
        
        adapter = new CartAdapter(this, cartItems, this);
        rvCartItems.setAdapter(adapter);
        
        // Thêm swipe-to-delete
        setupSwipeToDelete();
        
        updateTotalPrice();
    }
    
    /**
     * Thiết lập chức năng swipe sang phải để xóa
     */
    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            
            private final ColorDrawable background = new ColorDrawable(Color.parseColor("#E53935"));
            private Drawable deleteIcon;
            
            @Override
            public boolean onMove(@androidx.annotation.NonNull RecyclerView recyclerView, 
                                @androidx.annotation.NonNull RecyclerView.ViewHolder viewHolder, 
                                @androidx.annotation.NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            
            @Override
            public void onSwiped(@androidx.annotation.NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                CartItemDetail item = cartItems.get(position);
                
                // Hiển thị dialog xác nhận
                new AlertDialog.Builder(CartActivity.this)
                    .setTitle("Xác nhận")
                    .setMessage("Bạn có chắc muốn xóa sản phẩm này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        deleteItem(item, position);
                    })
                    .setNegativeButton("Hủy", (dialog, which) -> {
                        // Khôi phục item nếu hủy
                        adapter.notifyItemChanged(position);
                    })
                    .setOnCancelListener(dialog -> {
                        // Khôi phục item nếu cancel
                        adapter.notifyItemChanged(position);
                    })
                    .show();
            }
            
            @Override
            public void onChildDraw(@androidx.annotation.NonNull Canvas c, 
                                  @androidx.annotation.NonNull RecyclerView recyclerView, 
                                  @androidx.annotation.NonNull RecyclerView.ViewHolder viewHolder, 
                                  float dX, float dY, int actionState, boolean isCurrentlyActive) {
                
                android.view.View itemView = viewHolder.itemView;
                int backgroundCornerOffset = 20;
                
                if (deleteIcon == null) {
                    deleteIcon = ContextCompat.getDrawable(CartActivity.this, android.R.drawable.ic_menu_delete);
                    if (deleteIcon != null) {
                        deleteIcon.setTint(Color.WHITE);
                    }
                }
                
                int iconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                int iconTop = itemView.getTop() + (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                int iconBottom = iconTop + deleteIcon.getIntrinsicHeight();
                
                if (dX > 0) { // Swipe sang phải
                    int iconLeft = itemView.getLeft() + iconMargin;
                    int iconRight = itemView.getLeft() + iconMargin + deleteIcon.getIntrinsicWidth();
                    deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                    
                    background.setBounds(itemView.getLeft(), itemView.getTop(),
                            itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
                } else {
                    background.setBounds(0, 0, 0, 0);
                }
                
                background.draw(c);
                deleteIcon.draw(c);
                
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rvCartItems);
    }
    
    /**
     * Xóa item khỏi giỏ hàng
     */
    private void deleteItem(CartItemDetail item, int position) {
        boolean success = cartService.removeItem(item.getCartItem().getId());
        
        if (success) {
            cartItems.remove(position);
            
            if (cartItems.isEmpty()) {
                showEmptyCart();
            } else {
                adapter.notifyItemRemoved(position);
                updateTotalPrice();
            }
            
            Toast.makeText(this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Lỗi xóa sản phẩm", Toast.LENGTH_SHORT).show();
            adapter.notifyItemChanged(position);
        }
    }
    
    private void updateTotalPrice() {
        double total = 0;
        List<CartItemDetail> selectedItems = adapter.getSelectedItems();
        
        for (CartItemDetail item : selectedItems) {
            total += item.getCartItem().getPriceAtAdd() * item.getCartItem().getQuantity();
        }
        
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvTotalPrice.setText(formatter.format(total));
    }
    
    @Override
    public void onQuantityChanged(CartItemDetail item, int newQuantity) {
        boolean success = cartService.updateQuantity(item.getCartItem().getId(), newQuantity);
        
        if (success) {
            item.getCartItem().setQuantity(newQuantity);
            adapter.notifyDataSetChanged();
            updateTotalPrice();
            Toast.makeText(this, "Đã cập nhật số lượng", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Lỗi cập nhật số lượng", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public void onItemDeleted(CartItemDetail item) {
        // Không cần nữa vì đã dùng swipe-to-delete
    }
    
    @Override
    public void onSelectionChanged() {
        updateTotalPrice();
    }
}
