package com.example.v_closet.entity;

import java.io.Serializable;

/**
 * Lớp OrderItem đại diện cho một dòng sản phẩm trong đơn hàng.
 * Lưu trữ snapshot thông tin sản phẩm tại thời điểm đặt hàng để đảm bảo tính nhất quán dữ liệu.
 */
public class OrderItem implements Serializable {
    private int id;
    private int orderId;           // Liên kết với Order.id
    private int productId;         // Liên kết với Product.id (Shirt, Pant...)
    private int variantId;         // Liên kết với ProductVariant.id (màu, size cụ thể)

    // Snapshot data - Không thay đổi sau khi đặt hàng
    private String productName;    // Tên sản phẩm tại thời điểm mua
    private String color;          // Màu đã chọn (có thể null nếu sản phẩm không có màu)
    private String size;           // Size đã chọn (có thể null nếu sản phẩm không có size)
    private double priceAtOrder;   // Giá tại thời điểm đặt hàng (bao gồm basePrice + priceAdjustment)
    private int quantity;          // Số lượng mua
    private String imageUrl;       // URL ảnh sản phẩm (optional)

    // 1. Constructor mặc định
    public OrderItem() {}

    // 2. Constructor đầy đủ tham số (Dùng khi lấy dữ liệu từ Database)
    public OrderItem(int id, int orderId, int productId, int variantId,
                     String productName, String color, String size,
                     double priceAtOrder, int quantity, String imageUrl) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.variantId = variantId;
        this.productName = productName;
        this.color = color;
        this.size = size;
        this.priceAtOrder = priceAtOrder;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    // 3. Constructor khi tạo OrderItem mới từ CartItem
    public OrderItem(int orderId, int productId, int variantId,
                     String productName, String color, String size,
                     double priceAtOrder, int quantity, String imageUrl) {
        this.orderId = orderId;
        this.productId = productId;
        this.variantId = variantId;
        this.productName = productName;
        this.color = color;
        this.size = size;
        this.priceAtOrder = priceAtOrder;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    // --- BUSINESS METHODS ---

    /**
     * Tính tổng giá cho dòng sản phẩm này
     * @return Tổng giá = priceAtOrder * quantity
     */
    public double getSubtotal() {
        return priceAtOrder * quantity;
    }

    // --- GETTERS AND SETTERS ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getPriceAtOrder() {
        return priceAtOrder;
    }

    public void setPriceAtOrder(double priceAtOrder) {
        this.priceAtOrder = priceAtOrder;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", color='" + color + '\'' +
                ", size='" + size + '\'' +
                ", quantity=" + quantity +
                ", priceAtOrder=" + priceAtOrder +
                '}';
    }
}
