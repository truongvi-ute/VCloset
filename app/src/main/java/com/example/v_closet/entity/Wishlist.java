package com.example.v_closet.entity;

import java.io.Serializable;

/**
 * Lớp Wishlist đại diện cho danh sách yêu thích của người dùng.
 * Lưu trữ các sản phẩm mà người dùng quan tâm.
 */
public class Wishlist implements Serializable {
    private int id;
    private int userId;    // Liên kết với User.id
    private int productId; // Liên kết với Product.id (Shirt, Pant...)

    // 1. Constructor mặc định
    public Wishlist() {}

    // 2. Constructor đầy đủ tham số (Dùng khi lấy dữ liệu từ Database)
    public Wishlist(int id, int userId, int productId) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
    }

    // 3. Constructor khi thêm mới sản phẩm vào danh sách yêu thích
    public Wishlist(int userId, int productId) {
        this.userId = userId;
        this.productId = productId;
    }

    // --- GETTERS AND SETTERS ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

}
