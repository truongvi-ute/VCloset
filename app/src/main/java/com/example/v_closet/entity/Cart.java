package com.example.v_closet.entity;

import java.io.Serializable;

/**
 * Lớp Cart đại diện cho giỏ hàng của người dùng.
 * Giúp quản lý tổng thể trạng thái giỏ hàng trước khi đặt hàng.
 */
public class Cart implements Serializable {
    private int id;
    private int userId;      // Liên kết với User.id

    // 1. Constructor mặc định
    public Cart() {}

    // 2. Constructor đầy đủ tham số
    public Cart(int id, int userId) {
        this.id = id;
        this.userId = userId;
    }

    // 3. Constructor cho tạo mới
    public Cart(int userId) {
        this.userId = userId;
    }

    // --- GETTERS AND SETTERS ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}