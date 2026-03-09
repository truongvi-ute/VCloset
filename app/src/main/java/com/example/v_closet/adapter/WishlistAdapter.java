package com.example.v_closet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.v_closet.R;
import com.example.v_closet.entity.Product;
import com.example.v_closet.entity.ProductImage;
import com.example.v_closet.service.ProductService;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter cho RecyclerView hiển thị danh sách wishlist
 */
public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {
    
    private Context context;
    private List<Product> products;
    private ProductService productService;
    private OnWishlistItemClickListener listener;
    
    public interface OnWishlistItemClickListener {
        void onProductClick(Product product);
        void onRemoveClick(Product product);
    }
    
    public WishlistAdapter(Context context, List<Product> products, OnWishlistItemClickListener listener) {
        this.context = context;
        this.products = products;
        this.listener = listener;
        this.productService = new ProductService(context);
    }
    
    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_wishlist, parent, false);
        return new WishlistViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {
        Product product = products.get(position);
        
        // Set tên sản phẩm
        holder.tvProductName.setText(product.getName());
        
        // Set giá sản phẩm
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.tvProductPrice.setText(formatter.format(product.getBasePrice()));
        
        // Load hình ảnh sản phẩm
        loadProductImage(holder.imgProduct, product.getId());
        
        // Click vào sản phẩm
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductClick(product);
            }
        });

    }
    
    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }
    
    /**
     * Load hình ảnh sản phẩm
     */
    private void loadProductImage(ImageView imageView, int productId) {
        List<ProductImage> images = productService.getProductImages(productId);
        
        if (images != null && !images.isEmpty()) {
            // Lấy ảnh đầu tiên hoặc ảnh primary
            ProductImage primaryImage = null;
            for (ProductImage img : images) {
                if (img.isPrimary()) {
                    primaryImage = img;
                    break;
                }
            }
            
            if (primaryImage == null) {
                primaryImage = images.get(0);
            }
            
            // Load ảnh từ drawable
            String imageName = primaryImage.getImageUrl();
            int resourceId = context.getResources().getIdentifier(
                imageName, "drawable", context.getPackageName());
            
            if (resourceId != 0) {
                imageView.setImageResource(resourceId);
            } else {
                imageView.setImageResource(R.drawable.img_unavailable);
            }
        } else {
            imageView.setImageResource(R.drawable.img_unavailable);
        }
    }
    
    /**
     * Cập nhật danh sách sản phẩm
     */
    public void updateProducts(List<Product> newProducts) {
        this.products = newProducts;
        notifyDataSetChanged();
    }
    
    /**
     * ViewHolder cho wishlist item
     */
    static class WishlistViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName;
        TextView tvProductPrice;
        Button btnRemove;
        
        public WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
        }
    }
}
