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
import com.example.v_closet.entity.Product;
import com.example.v_closet.entity.ProductImage;
import com.example.v_closet.service.ProductService;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter để hiển thị danh sách sản phẩm trong RecyclerView
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    
    private Context context;
    private List<Product> productList;
    private ProductService productService;
    private OnProductClickListener listener;
    
    public interface OnProductClickListener {
        void onProductClick(Product product);
    }
    
    public ProductAdapter(Context context, List<Product> productList, OnProductClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
        this.productService = new ProductService(context);
    }
    
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        
        // Set tên sản phẩm
        holder.tvProductName.setText(product.getName());
        
        // Set giá sản phẩm (format tiền VND)
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.tvProductPrice.setText(formatter.format(product.getBasePrice()));
        
        // Set mô tả sản phẩm (nếu có trong layout)
        if (holder.tvProductDescription != null) {
            if (product.getDescription() != null && !product.getDescription().isEmpty()) {
                holder.tvProductDescription.setText(product.getDescription());
                holder.tvProductDescription.setVisibility(View.VISIBLE);
            } else {
                holder.tvProductDescription.setVisibility(View.GONE);
            }
        }
        
        // Load hình ảnh sản phẩm
        loadProductImage(holder.imgProduct, product.getId());
        
        // Xử lý click vào sản phẩm
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductClick(product);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }
    
    /**
     * Load hình ảnh sản phẩm
     */
    private void loadProductImage(ImageView imageView, int productId) {
        ProductImage primaryImage = productService.getPrimaryImage(productId);
        
        if (primaryImage != null && primaryImage.getImageUrl() != null) {
            // Lấy resource ID từ tên file
            String imageName = primaryImage.getImageUrl();
            int resourceId = context.getResources().getIdentifier(
                imageName, "drawable", context.getPackageName()
            );
            
            if (resourceId != 0) {
                imageView.setImageResource(resourceId);
            } else {
                // Nếu không tìm thấy ảnh, dùng ảnh mặc định
                imageView.setImageResource(R.drawable.img_unavailable);
            }
        } else {
            // Không có ảnh, dùng ảnh mặc định
            imageView.setImageResource(R.drawable.img_unavailable);
        }
    }
    
    /**
     * Cập nhật danh sách sản phẩm
     */
    public void updateProducts(List<Product> newProducts) {
        this.productList = newProducts;
        notifyDataSetChanged();
    }
    
    /**
     * ViewHolder cho Product item
     */
    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvProductDescription;
        
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_product);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            // Layout không có description, set null
            tvProductDescription = null;
        }
    }
}
