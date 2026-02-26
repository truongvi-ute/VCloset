package com.example.v_closet.entity;

import java.io.Serializable;

/**
 * Lớp ProductVariant đại diện cho một biến thể cụ thể của sản phẩm (màu sắc, kích cỡ).
 * Giúp quản lý tồn kho và giá cả chi tiết cho từng tổ hợp sản phẩm.
 */
public class ProductVariant implements Serializable {
    private int id;
    private int productId;      // ID liên kết với lớp Product (Shirt, Pant...)
    private String color;       // Ví dụ: Đỏ, Xanh, Đen
    private String size;        // Ví dụ: S, M, L, XL
    private double priceAdjustment; // Giá cộng thêm (nếu có, ví dụ size XXL đắt hơn 20k)

    // 1. Constructor mặc định
    public ProductVariant() {}

    // 2. Constructor đầy đủ tham số (Dùng khi lấy dữ liệu từ Database)
    public ProductVariant(int id, int productId, String color, String size, double priceAdjustment) {
        this.id = id;
        this.productId = productId;
        this.color = color;
        this.size = size;
        this.priceAdjustment = priceAdjustment;
    }

    // 3. Constructor cho thêm mới biến thể
    public ProductVariant(int productId, String color, String size, double priceAdjustment) {
        this.productId = productId;
        this.color = color;
        this.size = size;
        this.priceAdjustment = priceAdjustment;
    }

    // --- GETTERS AND SETTERS ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public double getPriceAdjustment() { return priceAdjustment; }
    public void setPriceAdjustment(double priceAdjustment) { this.priceAdjustment = priceAdjustment; }
}