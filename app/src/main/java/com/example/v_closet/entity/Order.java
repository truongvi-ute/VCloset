package com.example.v_closet.entity;

import com.example.v_closet.entity.enums.OrderStatus;
import com.example.v_closet.entity.enums.PaymentMethod;
import java.io.Serializable;
import java.util.Date;

/**
 * Represents a customer order in the V-Closet system.
 * Stores a snapshot of shipping information and the current order status.
 */
public class Order implements Serializable {
    private int id;
    private int userId;
    private Date orderDate;
    private double totalAmount;
    private OrderStatus status;

    // Snapshot of shipping info to preserve history
    private String receiverName;
    private String phoneNumber;
    private String shippingAddress;

    private PaymentMethod paymentMethod; // Phương thức thanh toán
    private String note;

    // 1. Default Constructor
    public Order() {
        this.orderDate = new Date();
        this.status = OrderStatus.PENDING;
    }

    // 2. Full Constructor (Used when retrieving from SQLite)
    public Order(int id, int userId, Date orderDate, double totalAmount, OrderStatus status,
                 String receiverName, String phoneNumber, String shippingAddress,
                 PaymentMethod paymentMethod, String note) {
        this.id = id;
        this.userId = userId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.receiverName = receiverName;
        this.phoneNumber = phoneNumber;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.note = note;
    }

    // 3. Constructor for creating new order
    public Order(int userId, double totalAmount, String receiverName, String phoneNumber,
                 String shippingAddress, PaymentMethod paymentMethod, String note) {
        this.userId = userId;
        this.orderDate = new Date();
        this.totalAmount = totalAmount;
        this.status = OrderStatus.PENDING;
        this.receiverName = receiverName;
        this.phoneNumber = phoneNumber;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.note = note;
    }

    // --- GETTERS AND SETTERS ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}