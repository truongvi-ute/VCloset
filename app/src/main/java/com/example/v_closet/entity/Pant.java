package com.example.v_closet.entity;

import com.example.v_closet.entity.enums.CategoryType;

/**
 * Lớp Pant đại diện cho các sản phẩm là quần.
 * Kế thừa từ Product và bổ sung các đặc tính riêng như kiểu dáng và độ dài.
 */
public class Pant extends Product {
    private String material;  // Chất liệu (Ví dụ: Jean, Kaki, Tây)
    private String pantType;  // Kiểu quần (Ví dụ: Dài, Short, Jogger)
    private String fitType;   // Form dáng (Ví dụ: Slim-fit, Oversize, Straight)

    // 1. Constructor mặc định
    public Pant() {
        super();
    }

    // 2. Constructor đầy đủ tham số (Dùng khi lấy dữ liệu từ Database)
    public Pant(int id, String name, String description, double basePrice, CategoryType category,
                boolean isActive, String material, String pantType, String fitType) {
        // super phải nằm ở dòng đầu tiên để khởi tạo Product trước
        super(id, name, description, basePrice, category, isActive);
        this.material = material;
        this.pantType = pantType;
        this.fitType = fitType;
    }

    // 3. Constructor cho thêm mới sản phẩm
    public Pant(String name, String description, double basePrice, CategoryType category,
                String material, String pantType, String fitType) {
        super(0, name, description, basePrice, category, true);
        this.material = material;
        this.pantType = pantType;
        this.fitType = fitType;
    }

    // --- GETTERS AND SETTERS ---

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    public String getPantType() { return pantType; }
    public void setPantType(String pantType) { this.pantType = pantType; }

    public String getFitType() { return fitType; }
    public void setFitType(String fitType) { this.fitType = fitType; }

    @Override
    public String toString() {
        return "Pant{" +
                "name='" + getName() + '\'' +
                ", material='" + material + '\'' +
                ", type='" + pantType + '\'' +
                '}';
    }
}