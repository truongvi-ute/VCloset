package com.example.v_closet.entity;

import com.example.v_closet.entity.enums.CategoryType;

public class Shirt extends Product {
    private String material;
    private String sleeveType;
    private String collarType;

    public Shirt() {
        super(); // Gọi constructor mặc định của Product (set isActive = true)
    }

    // Constructor dùng khi lấy dữ liệu từ SQLite (có ID và trạng thái)
    public Shirt(int id, String name, String description, double basePrice, CategoryType category,
                 boolean isActive, String material, String sleeveType, String collarType) {
        // Lưu ý: super phải nằm ở dòng đầu tiên
        super(id, name, description, basePrice, category, isActive);
        this.material = material;
        this.sleeveType = sleeveType;
        this.collarType = collarType;
    }

    // Constructor dùng khi tạo sản phẩm mới để lưu vào DB
    public Shirt(String name, String description, double basePrice, CategoryType category,
                 String material, String sleeveType, String collarType) {
        // Mặc định truyền true cho isActive
        super(0, name, description, basePrice, category, true);
        this.material = material;
        this.sleeveType = sleeveType;
        this.collarType = collarType;
    }

    // --- GETTERS AND SETTERS ---
    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    public String getSleeveType() { return sleeveType; }
    public void setSleeveType(String sleeveType) { this.sleeveType = sleeveType; }

    public String getCollarType() { return collarType; }
    public void setCollarType(String collarType) { this.collarType = collarType; }
}