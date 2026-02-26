package com.example.v_closet.entity;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private int id;
    private String username;
    private String password;
    private String email;
    private Date createdAt;
    private Date updatedAt;

    // Liên kết với các thành phần khác qua ID (phục vụ database quan hệ)
    private int profileId;  // ID của thông tin cá nhân (UserProfile)
    private int cartId;     // ID của giỏ hàng (Cart)
    private int wishlistId; // ID của danh sách yêu thích (Wishlist)

    // 1. Constructor mặc định (Bắt buộc phải có)
    public User() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // 2. Constructor đầy đủ tham số (Dùng khi lấy dữ liệu từ Database)
    public User(int id, String username, String password, String email, Date createdAt, Date updatedAt, int profileId, int cartId, int wishlistId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.profileId = profileId;
        this.cartId = cartId;
        this.wishlistId = wishlistId;
    }

    // 3. Constructor cho đăng ký mới (Chưa có ID và các liên kết phụ)
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // --- GETTERS AND SETTERS ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getProfileId() { return profileId; }
    public void setProfileId(int profileId) { this.profileId = profileId; }

    public int getCartId() { return cartId; }
    public void setCartId(int cartId) { this.cartId = cartId; }

    public int getWishlistId() { return wishlistId; }
    public void setWishlistId(int wishlistId) { this.wishlistId = wishlistId; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}