package com.example.v_closet.entity.enums;

public enum OrderStatus {
    PENDING,    // Just placed
    CONFIRMED,  // Verified by admin
    SHIPPING,   // Handed over to carrier
    DELIVERED,  // Received by customer
    CANCELLED,  // Cancelled by user or admin
    RETURNED    // Refunded
}