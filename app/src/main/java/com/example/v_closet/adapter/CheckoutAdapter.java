package com.example.v_closet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.v_closet.R;
import com.example.v_closet.entity.CartItemDetail;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter hiển thị danh sách sản phẩm trong trang checkout
 */
public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.CheckoutViewHolder> {
    
    private Context context;
    private List<CartItemDetail> items;
    private NumberFormat currencyFormat;
    
    public CheckoutAdapter(Context context, List<CartItemDetail> items) {
        this.context = context;
        this.items = items;
        this.currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
    }
    
    @NonNull
    @Override
    public CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_checkout_product, parent, false);
        return new CheckoutViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull CheckoutViewHolder holder, int position) {
        CartItemDetail item = items.get(position);
        
        // Set tên sản phẩm
        holder.tvProductName.setText(item.getProduct().getName());
        
        // Set thông tin variant (màu, size)
        StringBuilder variantInfo = new StringBuilder();
        if (item.getVariant().getColor() != null && !item.getVariant().getColor().isEmpty()) {
            variantInfo.append("Màu: ").append(item.getVariant().getColor());
        }
        if (item.getVariant().getSize() != null && !item.getVariant().getSize().isEmpty()) {
            if (variantInfo.length() > 0) {
                variantInfo.append(", ");
            }
            variantInfo.append("Size: ").append(item.getVariant().getSize());
        }
        holder.tvVariantInfo.setText(variantInfo.toString());
        
        // Set giá
        double price = item.getProduct().getBasePrice() + item.getVariant().getPriceAdjustment();
        holder.tvPrice.setText(currencyFormat.format(price) + "đ");
        
        // Set số lượng
        holder.tvQuantity.setText("x" + item.getCartItem().getQuantity());
        
        // Load hình ảnh
        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            try {
                // Load từ drawable resource
                int resId = context.getResources().getIdentifier(
                    item.getImageUrl().replace("drawable/", "").replace(".webp", ""),
                    "drawable", context.getPackageName());
                if (resId != 0) {
                    holder.ivProductImage.setImageResource(resId);
                } else {
                    holder.ivProductImage.setImageResource(R.drawable.ic_launcher_background);
                }
            } catch (Exception e) {
                holder.ivProductImage.setImageResource(R.drawable.ic_launcher_background);
            }
        } else {
            holder.ivProductImage.setImageResource(R.drawable.ic_launcher_background);
        }
    }
    
    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }
    
    static class CheckoutViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName;
        TextView tvVariantInfo;
        TextView tvPrice;
        TextView tvQuantity;
        
        public CheckoutViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvVariantInfo = itemView.findViewById(R.id.tvVariantInfo);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
        }
    }
}
