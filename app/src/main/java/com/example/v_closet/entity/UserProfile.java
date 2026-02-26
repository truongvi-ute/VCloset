package com.example.v_closet.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Lớp UserProfile chứa thông tin cá nhân chi tiết của người dùng.
 * Tách biệt khỏi lớp User để đảm bảo tính đóng gói và dễ quản lý.
 */
public class UserProfile implements Serializable {
    private int id;
    private int userId;       // Liên kết ngược lại với User.id
    private String fullName;
    private String phoneNumber;
    private String avatarUrl; // Đường dẫn ảnh đại diện (Drawable hoặc URI)
    private String gender;    // Nam, Nữ, Khác
    private Date birthday;    // Ngày sinh
    private Date createdAt;
    private Date updatedAt;

    // 1. Constructor mặc định
    public UserProfile() {
        this.avatarUrl = "ic_default_avatar"; // Ảnh mặc định nếu chưa cập nhật
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // 2. Constructor đầy đủ tham số (Dùng khi lấy dữ liệu từ SQLite)
    public UserProfile(int id, int userId, String fullName, String phoneNumber, String avatarUrl, String gender, Date birthday, Date createdAt, Date updatedAt) {
        this.id = id;
        this.userId = userId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.avatarUrl = avatarUrl;
        this.gender = gender;
        this.birthday = birthday;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // 3. Constructor cho thêm mới Profile khi User vừa đăng ký xong
    public UserProfile(int userId, String fullName) {
        this.userId = userId;
        this.fullName = fullName;
        this.avatarUrl = "ic_default_avatar";
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // --- GETTERS AND SETTERS ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public Date getBirthday() { return birthday; }
    public void setBirthday(Date birthday) { this.birthday = birthday; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

}