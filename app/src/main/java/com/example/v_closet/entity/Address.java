package com.example.v_closet.entity;

import java.io.Serializable;

/**
 * Lớp Address đại diện cho địa chỉ giao hàng của người dùng.
 * Một User có thể có nhiều Address (nhà riêng, cơ quan, v.v.)
 */
public class Address implements Serializable {
    private int id;
    private int userId;         // Liên kết với User.id
    private String receiverName; // Tên người nhận (có thể khác tên User)
    private String phoneNumber;  // Số điện thoại nhận hàng
    
    // Địa chỉ chi tiết
    private String street;      // Số nhà, tên đường
    private String ward;        // Phường/Xã
    private String district;    // Quận/Huyện
    private String city;        // Tỉnh/Thành phố
    
    private boolean isDefault;  // Đánh dấu địa chỉ mặc định khi đặt hàng

    // 1. Constructor mặc định
    public Address() {}

    // 2. Constructor đầy đủ tham số (Dùng khi lấy dữ liệu từ SQLite)
    public Address(int id, int userId, String receiverName, String phoneNumber, 
                   String street, String ward, String district, String city, boolean isDefault) {
        this.id = id;
        this.userId = userId;
        this.receiverName = receiverName;
        this.phoneNumber = phoneNumber;
        this.street = street;
        this.ward = ward;
        this.district = district;
        this.city = city;
        this.isDefault = isDefault;
    }

    // 3. Constructor cho thêm mới địa chỉ
    public Address(int userId, String receiverName, String phoneNumber, 
                   String street, String ward, String district, String city, boolean isDefault) {
        this.userId = userId;
        this.receiverName = receiverName;
        this.phoneNumber = phoneNumber;
        this.street = street;
        this.ward = ward;
        this.district = district;
        this.city = city;
        this.isDefault = isDefault;
    }

    // --- BUSINESS METHODS ---
    
    /**
     * Lấy địa chỉ đầy đủ dạng chuỗi
     * @return Địa chỉ đầy đủ
     */
    public String getFullAddress() {
        return street + ", " + ward + ", " + district + ", " + city;
    }

    // --- GETTERS AND SETTERS ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getWard() { return ward; }
    public void setWard(String ward) { this.ward = ward; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean aDefault) { isDefault = aDefault; }

}