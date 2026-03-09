package com.example.v_closet.controller;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.v_closet.R;
import com.example.v_closet.adapter.ImagePagerAdapter;
import com.example.v_closet.entity.Product;
import com.example.v_closet.entity.ProductImage;
import com.example.v_closet.entity.ProductVariant;
import com.example.v_closet.service.CartService;
import com.example.v_closet.service.ProductService;
import com.example.v_closet.service.WishlistService;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ProductDetailActivity extends AppCompatActivity {
    
    private ProductService productService;
    private CartService cartService;
    private WishlistService wishlistService;
    private int productId;
    private int userId;
    private Product product;
    private List<ProductVariant> variants;
    private List<ProductImage> images;
    private List<ProductImage> allImages; // Lưu tất cả ảnh
    
    private ViewPager2 viewPagerImages;
    private LinearLayout layoutIndicators;
    private TextView tvHeaderTitle;
    private TextView tvProductPrice;
    private TextView tvProductDescription;
    private TextView tvProductCategory;
    private LinearLayout layoutColors;
    private LinearLayout layoutSizes;
    private Button btnAddToCart;
    private Button btnBuyNow;
    private ImageButton btnBack;
    private ImageButton btnCart;
    private ImageButton btnWishlist;
    
    private boolean isInWishlist = false;
    
    private Map<String, String> colorMap = new HashMap<>();
    private String selectedColor = null;
    private String selectedSize = null;
    private ProductVariant selectedVariant = null;
    private ImagePagerAdapter imagePagerAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);
        
        initColorMap();
        productService = new ProductService(this);
        cartService = new CartService(this);
        wishlistService = new WishlistService(this);
        productId = getIntent().getIntExtra("product_id", 0);
        userId = getIntent().getIntExtra("user_id", 0);
        
        if (productId == 0) {
            Toast.makeText(this, "Lỗi: Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        initViews();
        loadProductDetail();
        setupListeners();
    }
    
    private void initColorMap() {
        colorMap.put("Black", "#000000");
        colorMap.put("White", "#FFFFFF");
        colorMap.put("Blue", "#2196F3");
        colorMap.put("Gray", "#9E9E9E");
        colorMap.put("Cream", "#FFFDD0");
        colorMap.put("Red", "#F44336");
        colorMap.put("Green", "#4CAF50");
        colorMap.put("Yellow", "#FFEB3B");
        colorMap.put("Pink", "#E91E63");
        colorMap.put("Brown", "#795548");
    }
    
    private void initViews() {
        viewPagerImages = findViewById(R.id.viewPagerImages);
        layoutIndicators = findViewById(R.id.layoutIndicators);
        tvHeaderTitle = findViewById(R.id.tvHeaderTitle);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        tvProductDescription = findViewById(R.id.tvProductDescription);
        tvProductCategory = findViewById(R.id.tvProductCategory);
        layoutColors = findViewById(R.id.layoutColors);
        layoutSizes = findViewById(R.id.layoutSizes);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);
        btnBack = findViewById(R.id.btn_back);
        btnCart = findViewById(R.id.btnCart);
        btnWishlist = findViewById(R.id.btnWishlist);
    }
    
    private void loadProductDetail() {
        List<Product> products = productService.getAllProducts();
        for (Product p : products) {
            if (p.getId() == productId) {
                product = p;
                break;
            }
        }
        
        if (product == null) {
            Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        variants = productService.getProductVariants(productId);
        allImages = productService.getProductImages(productId); // Lưu tất cả ảnh
        images = new java.util.ArrayList<>(allImages); // Copy để hiển thị ban đầu
        
        displayProductInfo();
        setupImagePager();
        displayColorOptions();
        displaySizeOptions();
        checkWishlistStatus();
    }
    
    /**
     * Kiểm tra trạng thái wishlist và cập nhật icon
     */
    private void checkWishlistStatus() {
        if (userId != 0) {
            isInWishlist = wishlistService.isInWishlist(userId, productId);
            updateWishlistIcon();
        }
    }
    
    /**
     * Cập nhật icon wishlist
     */
    private void updateWishlistIcon() {
        if (btnWishlist != null) {
            if (isInWishlist) {
                btnWishlist.setImageResource(R.drawable.ic_heart_filled);
                btnWishlist.setColorFilter(getResources().getColor(android.R.color.holo_red_dark));
            } else {
                btnWishlist.setImageResource(R.drawable.ic_heart_outline);
                btnWishlist.setColorFilter(getResources().getColor(android.R.color.darker_gray));
            }
        }
    }
    
    private void displayProductInfo() {
        tvHeaderTitle.setText(product.getName());
        
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvProductPrice.setText(formatter.format(product.getBasePrice()));
        
        tvProductDescription.setText(product.getDescription());
        tvProductCategory.setText(product.getCategory().name());
    }
    
    private void setupImagePager() {
        if (images == null || images.isEmpty()) {
            return;
        }
        
        imagePagerAdapter = new ImagePagerAdapter(this, images);
        viewPagerImages.setAdapter(imagePagerAdapter);
        
        setupIndicators(images.size());
        setCurrentIndicator(0);
        
        viewPagerImages.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });
    }
    
    private void setupIndicators(int count) {
        layoutIndicators.removeAllViews(); // Xóa các indicator cũ nếu có
        
        if (count <= 1) return; // Không cần indicator nếu chỉ có 1 ảnh
        
        int size = (int) (10 * getResources().getDisplayMetrics().density);
        int margin = (int) (4 * getResources().getDisplayMetrics().density);
        
        for (int i = 0; i < count; i++) {
            android.view.View indicator = new android.view.View(this);
            
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
            params.setMargins(margin, 0, margin, 0);
            indicator.setLayoutParams(params);
            
            // Set background mặc định là inactive
            android.graphics.drawable.GradientDrawable drawable = new android.graphics.drawable.GradientDrawable();
            drawable.setShape(android.graphics.drawable.GradientDrawable.OVAL);
            drawable.setColor(android.graphics.Color.parseColor("#CCCCCC"));
            indicator.setBackground(drawable);
            
            layoutIndicators.addView(indicator);
        }
    }
    
    private void setCurrentIndicator(int position) {
        int childCount = layoutIndicators.getChildCount();
        
        for (int i = 0; i < childCount; i++) {
            android.view.View indicator = layoutIndicators.getChildAt(i);
            
            android.graphics.drawable.GradientDrawable drawable = new android.graphics.drawable.GradientDrawable();
            drawable.setShape(android.graphics.drawable.GradientDrawable.OVAL);
            
            if (i == position) {
                // Active indicator - màu xanh
                drawable.setColor(getResources().getColor(R.color.blue_dark));
            } else {
                // Inactive indicator - màu xám
                drawable.setColor(android.graphics.Color.parseColor("#CCCCCC"));
            }
            
            indicator.setBackground(drawable);
        }
    }
    
    private void displayColorOptions() {
        if (variants == null || variants.isEmpty()) return;
        
        Set<String> uniqueColors = new HashSet<>();
        for (ProductVariant variant : variants) {
            if (variant.getColor() != null) {
                uniqueColors.add(variant.getColor());
            }
        }
        
        for (String colorName : uniqueColors) {
            android.view.View colorView = createColorCircle(colorName);
            layoutColors.addView(colorView);
        }
    }
    
    private android.view.View createColorCircle(String colorName) {
        // Tạo khung chứa cố định 40dp x 40dp
        LinearLayout container = new LinearLayout(this);
        int containerSize = (int) (40 * getResources().getDisplayMetrics().density);
        int margin = (int) (8 * getResources().getDisplayMetrics().density);
        
        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(containerSize, containerSize);
        containerParams.setMargins(margin, 0, margin, 0);
        container.setLayoutParams(containerParams);
        container.setGravity(android.view.Gravity.CENTER);
        
        // Đặt background transparent với border invisible ngay từ đầu để giữ kích thước
        android.graphics.drawable.GradientDrawable containerBg = new android.graphics.drawable.GradientDrawable();
        containerBg.setShape(android.graphics.drawable.GradientDrawable.OVAL);
        containerBg.setColor(Color.TRANSPARENT);
        containerBg.setStroke(6, Color.TRANSPARENT); // Border trong suốt, nhưng vẫn chiếm chỗ
        container.setBackground(containerBg);
        
        // Tạo cục màu bên trong (nhỏ hơn một chút để có chỗ cho border)
        android.view.View colorView = new android.view.View(this);
        int colorSize = (int) (30 * getResources().getDisplayMetrics().density);
        
        LinearLayout.LayoutParams colorParams = new LinearLayout.LayoutParams(colorSize, colorSize);
        colorView.setLayoutParams(colorParams);
        
        String hexColor = colorMap.get(colorName);
        
        android.graphics.drawable.GradientDrawable drawable = new android.graphics.drawable.GradientDrawable();
        drawable.setShape(android.graphics.drawable.GradientDrawable.OVAL);
        
        if (hexColor != null) {
            drawable.setColor(Color.parseColor(hexColor));
        } else {
            drawable.setColor(Color.GRAY);
        }
        
        if ("White".equals(colorName) || "Cream".equals(colorName)) {
            drawable.setStroke(2, Color.BLACK);
        }
        
        colorView.setBackground(drawable);
        container.addView(colorView);
        
        container.setOnClickListener(v -> {
            selectedColor = colorName;
            Toast.makeText(this, "Đã chọn màu: " + colorName, Toast.LENGTH_SHORT).show();
            updateImagesForColor(colorName);
            highlightSelectedColor(container);
        });
        
        return container;
    }
    
    /**
     * Cập nhật hình ảnh theo màu đã chọn
     */
    private void updateImagesForColor(String color) {
        // Tìm variantId của màu đã chọn
        List<Integer> variantIds = new java.util.ArrayList<>();
        for (ProductVariant variant : variants) {
            if (color.equals(variant.getColor())) {
                variantIds.add(variant.getId());
            }
        }
        
        // Lọc ảnh theo variantId
        List<ProductImage> filteredImages = new java.util.ArrayList<>();
        for (ProductImage img : allImages) {
            if (variantIds.contains(img.getVariantId())) {
                filteredImages.add(img);
            }
        }
        
        // Nếu không có ảnh cho màu này, hiển thị tất cả ảnh
        if (filteredImages.isEmpty()) {
            filteredImages = new java.util.ArrayList<>(allImages);
        }
        
        // Cập nhật adapter
        images = filteredImages;
        imagePagerAdapter = new ImagePagerAdapter(this, images);
        viewPagerImages.setAdapter(imagePagerAdapter);
        
        // Cập nhật indicators
        setupIndicators(images.size());
        setCurrentIndicator(0);
    }
    
    /**
     * Highlight màu đã chọn
     */
    private void highlightSelectedColor(android.view.View selectedContainer) {
        // Reset tất cả các color circles - đặt border trong suốt
        for (int i = 0; i < layoutColors.getChildCount(); i++) {
            android.view.View container = layoutColors.getChildAt(i);
            
            android.graphics.drawable.GradientDrawable containerBg = new android.graphics.drawable.GradientDrawable();
            containerBg.setShape(android.graphics.drawable.GradientDrawable.OVAL);
            containerBg.setColor(Color.TRANSPARENT);
            containerBg.setStroke(6, Color.TRANSPARENT); // Border trong suốt
            container.setBackground(containerBg);
        }
        
        // Thêm border xanh lá cho container đã chọn
        android.graphics.drawable.GradientDrawable containerDrawable = new android.graphics.drawable.GradientDrawable();
        containerDrawable.setShape(android.graphics.drawable.GradientDrawable.OVAL);
        containerDrawable.setStroke(6, getResources().getColor(R.color.green));
        containerDrawable.setColor(Color.TRANSPARENT);
        selectedContainer.setBackground(containerDrawable);
    }
    
    /**
     * Hiển thị các tùy chọn size
     */
    private void displaySizeOptions() {
        if (variants == null || variants.isEmpty()) return;
        
        Set<String> uniqueSizes = new HashSet<>();
        for (ProductVariant variant : variants) {
            if (variant.getSize() != null) {
                uniqueSizes.add(variant.getSize());
            }
        }
        
        for (String size : uniqueSizes) {
            Button btnSize = createSizeButton(size);
            layoutSizes.addView(btnSize);
        }
    }
    
    /**
     * Tạo button size
     */
    private Button createSizeButton(String size) {
        Button btn = new Button(this);
        btn.setText(size);
        btn.setTextColor(Color.BLACK);
        btn.setBackgroundColor(Color.WHITE);
        
        int padding = (int) (12 * getResources().getDisplayMetrics().density);
        int margin = (int) (8 * getResources().getDisplayMetrics().density);
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            (int) (60 * getResources().getDisplayMetrics().density),
            (int) (50 * getResources().getDisplayMetrics().density)
        );
        params.setMargins(margin, 0, margin, 0);
        btn.setLayoutParams(params);
        
        // Border mặc định
        android.graphics.drawable.GradientDrawable drawable = new android.graphics.drawable.GradientDrawable();
        drawable.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
        drawable.setColor(Color.WHITE);
        drawable.setStroke(4, Color.LTGRAY);
        drawable.setCornerRadius(8);
        btn.setBackground(drawable);
        
        btn.setOnClickListener(v -> {
            selectedSize = size;
            Toast.makeText(this, "Đã chọn size: " + size, Toast.LENGTH_SHORT).show();
            highlightSelectedSize(btn);
            updateSelectedVariant();
        });
        
        return btn;
    }
    
    /**
     * Highlight size đã chọn
     */
    private void highlightSelectedSize(Button selectedButton) {
        // Reset tất cả các size buttons
        for (int i = 0; i < layoutSizes.getChildCount(); i++) {
            Button child = (Button) layoutSizes.getChildAt(i);
            
            android.graphics.drawable.GradientDrawable drawable = new android.graphics.drawable.GradientDrawable();
            drawable.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
            drawable.setColor(Color.WHITE);
            drawable.setStroke(4, Color.LTGRAY);
            drawable.setCornerRadius(8);
            child.setBackground(drawable);
            child.setTextColor(Color.BLACK);
        }
        
        // Highlight size đã chọn với màu xanh lá
        android.graphics.drawable.GradientDrawable drawable = new android.graphics.drawable.GradientDrawable();
        drawable.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
        drawable.setColor(getResources().getColor(R.color.green));
        drawable.setStroke(4, getResources().getColor(R.color.green));
        drawable.setCornerRadius(8);
        selectedButton.setBackground(drawable);
        selectedButton.setTextColor(Color.WHITE);
    }
    
    /**
     * Cập nhật variant đã chọn dựa trên màu và size
     */
    private void updateSelectedVariant() {
        if (selectedColor != null && selectedSize != null) {
            for (ProductVariant v : variants) {
                if (v.getColor().equals(selectedColor) && v.getSize().equals(selectedSize)) {
                    selectedVariant = v;
                    Toast.makeText(this, "Đã chọn: " + selectedColor + " - " + selectedSize, Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    }
    
    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        
        btnCart.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(ProductDetailActivity.this, CartActivity.class);
            intent.putExtra("user_id", userId);
            startActivity(intent);
        });
        
        btnWishlist.setOnClickListener(v -> handleWishlistClick());
        
        btnAddToCart.setOnClickListener(v -> {
            if (selectedColor == null || selectedSize == null) {
                Toast.makeText(this, "Vui lòng chọn màu và size", Toast.LENGTH_SHORT).show();
            } else {
                addToCart();
            }
        });
        
        btnBuyNow.setOnClickListener(v -> {
            if (selectedColor == null || selectedSize == null) {
                Toast.makeText(this, "Vui lòng chọn màu và size", Toast.LENGTH_SHORT).show();
            } else {
                buyNow();
            }
        });
    }
    
    /**
     * Xử lý click vào nút wishlist
     */
    private void handleWishlistClick() {
        if (userId == 0) {
            Toast.makeText(this, "Vui lòng đăng nhập để sử dụng wishlist", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (isInWishlist) {
            // Xóa khỏi wishlist
            boolean success = wishlistService.removeFromWishlist(userId, productId);
            if (success) {
                isInWishlist = false;
                updateWishlistIcon();
                Toast.makeText(this, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Lỗi khi xóa khỏi wishlist", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Thêm vào wishlist
            boolean success = wishlistService.addToWishlist(userId, productId);
            if (success) {
                isInWishlist = true;
                updateWishlistIcon();
                Toast.makeText(this, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Sản phẩm đã có trong wishlist", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private void addToCart() {
        if (userId == 0) {
            Toast.makeText(this, "Vui lòng đăng nhập để thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (selectedVariant == null) {
            Toast.makeText(this, "Vui lòng chọn màu và size", Toast.LENGTH_SHORT).show();
            return;
        }
        
        double price = product.getBasePrice() + selectedVariant.getPriceAdjustment();
        boolean success = cartService.addToCart(userId, selectedVariant.getId(), 1, price);
        
        if (success) {
            Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Lỗi thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void buyNow() {
        if (userId == 0) {
            Toast.makeText(this, "Vui lòng đăng nhập để mua hàng", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (selectedVariant == null) {
            Toast.makeText(this, "Vui lòng chọn màu và size", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Tạo CartItemDetail cho sản phẩm hiện tại
        com.example.v_closet.entity.CartItem cartItem = new com.example.v_closet.entity.CartItem();
        cartItem.setVariantId(selectedVariant.getId());
        cartItem.setQuantity(1);
        cartItem.setPriceAtAdd(product.getBasePrice() + selectedVariant.getPriceAdjustment());
        
        com.example.v_closet.entity.CartItemDetail itemDetail = new com.example.v_closet.entity.CartItemDetail(
            cartItem,
            product,
            selectedVariant,
            images != null && !images.isEmpty() ? images.get(0).getImageUrl() : null
        );
        
        // Tạo danh sách chỉ có 1 sản phẩm
        java.util.ArrayList<com.example.v_closet.entity.CartItemDetail> orderItems = new java.util.ArrayList<>();
        orderItems.add(itemDetail);
        
        // Chuyển sang trang checkout
        android.content.Intent intent = new android.content.Intent(ProductDetailActivity.this, CheckoutActivity.class);
        intent.putExtra("orderItems", orderItems);
        intent.putExtra("user_id", userId);
        startActivity(intent);
    }
}
