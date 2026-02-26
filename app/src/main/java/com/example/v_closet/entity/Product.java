package com.example.v_closet.entity;

import com.example.v_closet.entity.enums.CategoryType;

import java.io.Serializable;
import java.util.Date;

public abstract class Product implements Serializable {
    private int id;
    private String name;
    private String description;
    private double basePrice;
    private CategoryType category; // Changed from String to Enum
    private boolean isActive;
    private Date createdAt;
    private Date updatedAt;

    public Product() {
        this.isActive = true;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public Product(int id, String name, String description, double basePrice, CategoryType category, boolean isActive) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
        this.category = category;
        this.isActive = isActive;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public Product(int id, String name, String description, double basePrice, CategoryType category, boolean isActive, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
        this.category = category;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // --- GETTERS AND SETTERS ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getBasePrice() { return basePrice; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }

    public CategoryType getCategory() { return category; }
    public void setCategory(CategoryType category) { this.category = category; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}