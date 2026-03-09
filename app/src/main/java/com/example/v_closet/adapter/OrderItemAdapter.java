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
import com.example.v_closet.entity.OrderItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter hiển thị danh sách sản phẩm trong một đơn hàng
 */
public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {
    
    private Context context;
    private List<OrderItem> items;
    private NumberFormat currencyFormat;
    
    public OrderItemAdapter(Context context, List<OrderItem> items) {
        this.context = context;
        this.items = items;
        this.currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
    }
    
    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_product, parent, false);
        return new OrderItemViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderItem item = items.get(position);
        
        holder.tvProductName.setText(item.getProductName());
        
        // Variant info
        StringBuilder variantInfo = new StringBuilder();
        if (item.getColor() != null && !item.getColor().isEmpty()) {
            variantInfo.append("Màu: ").append(item.getColor());
        }
        if (item.getSize() != null && !item.getSize().isEmpty()) {
            if (variantInfo.length() > 0) {
                variantInfo.append(", ");
            }
            variantInfo.append("Size: ").append(item.getSize());
        }
        holder.tvVariantInfo.setText(variantInfo.toString());
        
        holder.tvQuantity.setText("x" + item.getQuantity());
        holder.tvPrice.setText(currencyFormat.format(item.getPriceAtOrder()) + "đ");
        
        // Load image
        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            try {
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
        }
    }
    
    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }
    
    static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName;
        TextView tvVariantInfo;
        TextView tvQuantity;
        TextView tvPrice;
        
        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvVariantInfo = itemView.findViewById(R.id.tvVariantInfo);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
