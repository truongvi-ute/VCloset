package com.example.v_closet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.example.v_closet.R;
import com.example.v_closet.entity.CartItemDetail;

import java.text.NumberFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    
    private Context context;
    private List<CartItemDetail> items;
    private OnCartItemListener listener;
    private Set<Integer> selectedItems = new HashSet<>();
    
    public interface OnCartItemListener {
        void onQuantityChanged(CartItemDetail item, int newQuantity);
        void onItemDeleted(CartItemDetail item);
        void onSelectionChanged();
    }
    
    public CartAdapter(Context context, List<CartItemDetail> items, OnCartItemListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        android.view.View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItemDetail item = items.get(position);
        int itemId = item.getCartItem().getId();
        
        holder.tvProductName.setText(item.getProduct().getName());
        holder.tvVariantInfo.setText("Màu: " + item.getVariant().getColor() + ", Size: " + item.getVariant().getSize());
        
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.tvPrice.setText(formatter.format(item.getCartItem().getPriceAtAdd()));
        
        holder.tvQuantity.setText(String.valueOf(item.getCartItem().getQuantity()));
        
        // Set trạng thái checkbox
        holder.cbSelect.setChecked(selectedItems.contains(itemId));
        
        // Cập nhật màu giá tiền dựa trên trạng thái checkbox
        updatePriceColor(holder, selectedItems.contains(itemId));
        
        // Load image
        if (item.getImageUrl() != null) {
            int resId = context.getResources().getIdentifier(
                item.getImageUrl().replace("drawable/", "").replace(".webp", ""),
                "drawable", context.getPackageName());
            if (resId != 0) {
                holder.imgProduct.setImageResource(resId);
            }
        }
        
        // Xử lý checkbox
        holder.cbSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedItems.add(itemId);
            } else {
                selectedItems.remove(itemId);
            }
            updatePriceColor(holder, isChecked);
            
            if (listener != null) {
                listener.onSelectionChanged();
            }
        });
        
        // Xử lý tăng số lượng
        holder.btnIncrease.setOnClickListener(v -> {
            int newQuantity = item.getCartItem().getQuantity() + 1;
            if (listener != null) {
                listener.onQuantityChanged(item, newQuantity);
            }
        });
        
        // Xử lý giảm số lượng
        holder.btnDecrease.setOnClickListener(v -> {
            int newQuantity = item.getCartItem().getQuantity() - 1;
            if (newQuantity > 0 && listener != null) {
                listener.onQuantityChanged(item, newQuantity);
            }
        });
    }
    
    /**
     * Cập nhật màu giá tiền
     */
    private void updatePriceColor(CartViewHolder holder, boolean isSelected) {
        if (isSelected) {
            holder.tvPrice.setTextColor(context.getResources().getColor(R.color.blue_dark));
        } else {
            holder.tvPrice.setTextColor(android.graphics.Color.GRAY);
        }
    }
    
    /**
     * Lấy danh sách item đã chọn
     */
    public List<CartItemDetail> getSelectedItems() {
        List<CartItemDetail> selected = new java.util.ArrayList<>();
        for (CartItemDetail item : items) {
            if (selectedItems.contains(item.getCartItem().getId())) {
                selected.add(item);
            }
        }
        return selected;
    }
    
    /**
     * Chọn tất cả items
     */
    public void selectAll() {
        selectedItems.clear();
        for (CartItemDetail item : items) {
            selectedItems.add(item.getCartItem().getId());
        }
        notifyDataSetChanged();
        if (listener != null) {
            listener.onSelectionChanged();
        }
    }
    
    /**
     * Bỏ chọn tất cả items
     */
    public void deselectAll() {
        selectedItems.clear();
        notifyDataSetChanged();
        if (listener != null) {
            listener.onSelectionChanged();
        }
    }
    
    @Override
    public int getItemCount() {
        return items.size();
    }
    
    public void updateItems(List<CartItemDetail> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }
    
    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName;
        TextView tvVariantInfo;
        TextView tvPrice;
        TextView tvQuantity;
        ImageButton btnIncrease;
        ImageButton btnDecrease;
        MaterialCheckBox cbSelect;
        
        public CartViewHolder(@NonNull android.view.View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvVariantInfo = itemView.findViewById(R.id.tvVariantInfo);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            cbSelect = itemView.findViewById(R.id.cbSelect);
        }
    }
}
