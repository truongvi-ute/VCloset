package com.example.v_closet.entity;

import java.io.Serializable;

/**
 * Lớp CartItem đại diện cho một dòng sản phẩm trong giỏ hàng.
 * Liên kết chặt chẽ với ProductVariant để biết chính xác màu sắc và kích cỡ.
 */
public class CartItem implements Serializable {
    private int id;
    private int cartId;        // Liên kết với Cart.id của người dùng
    private int variantId;     // ID của biến thể (Màu, Size) được chọn
    private int quantity;      // Số lượng khách muốn mua
    private double priceAtAdd; // Giá tại thời điểm cho vào giỏ (để theo dõi biến động giá)

    // 1. Constructor mặc định
    public CartItem() {}

    // 2. Constructor đầy đủ tham số
    public CartItem(int id, int cartId, int variantId, int quantity, double priceAtAdd) {
        this.id = id;
        this.cartId = cartId;
        this.variantId = variantId;
        this.quantity = quantity;
        this.priceAtAdd = priceAtAdd;
    }

    // 3. Constructor khi thêm mới vào giỏ
    public CartItem(int cartId, int variantId, int quantity, double priceAtAdd) {
        this.cartId = cartId;
        this.variantId = variantId;
        this.quantity = quantity;
        this.priceAtAdd = priceAtAdd;
    }

    // --- GETTERS AND SETTERS ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCartId() { return cartId; }
    public void setCartId(int cartId) { this.cartId = cartId; }

    public int getVariantId() { return variantId; }
    public void setVariantId(int variantId) { this.variantId = variantId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPriceAtAdd() { return priceAtAdd; }
    public void setPriceAtAdd(double priceAtAdd) { this.priceAtAdd = priceAtAdd; }
}