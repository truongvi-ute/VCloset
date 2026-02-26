package com.example.v_closet.entity;

import java.io.Serializable;

/**
 * Thực thể quản lý hình ảnh sản phẩm.
 * Hỗ trợ liên kết với sản phẩm chính hoặc từng biến thể (màu sắc) cụ thể.
 */
public class ProductImage implements Serializable {
    private int id;
    private int productId;   // Liên kết với sản phẩm chính
    private int variantId;   // (Optional) Liên kết với màu sắc cụ thể nếu cần
    private String imageUrl; // Đường dẫn file hoặc URL ảnh
    private boolean isPrimary; // Ảnh đại diện chính

    // 1. Constructor mặc định
    public ProductImage() {}

    // 2. Constructor đầy đủ tham số
    public ProductImage(int id, int productId, int variantId, String imageUrl, boolean isPrimary) {
        this.id = id;
        this.productId = productId;
        this.variantId = variantId;
        this.imageUrl = imageUrl;
        this.isPrimary = isPrimary;
    }

    // 3. Constructor cho thêm mới (ID thường tự tăng trong DB)
    public ProductImage(int productId, int variantId, String imageUrl, boolean isPrimary) {
        this.productId = productId;
        this.variantId = variantId;
        this.imageUrl = imageUrl;
        this.isPrimary = isPrimary;
    }

    // --- GETTERS AND SETTERS ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }
}