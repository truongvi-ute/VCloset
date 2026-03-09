package com.example.v_closet.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.v_closet.entity.Order;
import com.example.v_closet.entity.OrderItem;
import com.example.v_closet.entity.enums.OrderStatus;
import com.example.v_closet.entity.enums.PaymentMethod;
import com.example.v_closet.util.DbHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Repository xử lý các thao tác database cho Order và OrderItem
 */
public class OrderRepository {
    
    private DbHelper dbHelper;
    
    public OrderRepository(Context context) {
        this.dbHelper = new DbHelper(context);
    }
    
    /**
     * Tạo đơn hàng mới và trả về orderId
     */
    public long createOrder(Order order) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put("userId", order.getUserId());
        values.put("orderDate", order.getOrderDate().getTime());
        values.put("totalAmount", order.getTotalAmount());
        values.put("status", order.getStatus().name());
        values.put("receiverName", order.getReceiverName());
        values.put("phoneNumber", order.getPhoneNumber());
        values.put("shippingAddress", order.getShippingAddress());
        values.put("paymentMethod", order.getPaymentMethod().name());
        values.put("note", order.getNote());
        
        long orderId = db.insert("Orders", null, values);
        db.close();
        
        return orderId;
    }
    
    /**
     * Thêm OrderItem vào đơn hàng
     */
    public long addOrderItem(OrderItem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put("orderId", item.getOrderId());
        values.put("productId", item.getProductId());
        values.put("variantId", item.getVariantId());
        values.put("productName", item.getProductName());
        values.put("color", item.getColor());
        values.put("size", item.getSize());
        values.put("priceAtOrder", item.getPriceAtOrder());
        values.put("quantity", item.getQuantity());
        values.put("imageUrl", item.getImageUrl());
        
        long itemId = db.insert("OrderItem", null, values);
        db.close();
        
        return itemId;
    }
    
    /**
     * Lấy danh sách đơn hàng của user
     */
    public List<Order> getUserOrders(int userId) {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        Cursor cursor = db.query("Orders", null, "userId = ?", 
            new String[]{String.valueOf(userId)}, null, null, "orderDate DESC");
        
        if (cursor.moveToFirst()) {
            do {
                Order order = new Order(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("userId")),
                    new Date(cursor.getLong(cursor.getColumnIndexOrThrow("orderDate"))),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("totalAmount")),
                    OrderStatus.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("status"))),
                    cursor.getString(cursor.getColumnIndexOrThrow("receiverName")),
                    cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber")),
                    cursor.getString(cursor.getColumnIndexOrThrow("shippingAddress")),
                    PaymentMethod.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("paymentMethod"))),
                    cursor.getString(cursor.getColumnIndexOrThrow("note"))
                );
                orders.add(order);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        
        return orders;
    }
    
    /**
     * Lấy chi tiết đơn hàng
     */
    public Order getOrderById(int orderId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Order order = null;
        
        Cursor cursor = db.query("Orders", null, "id = ?", 
            new String[]{String.valueOf(orderId)}, null, null, null);
        
        if (cursor.moveToFirst()) {
            order = new Order(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getInt(cursor.getColumnIndexOrThrow("userId")),
                new Date(cursor.getLong(cursor.getColumnIndexOrThrow("orderDate"))),
                cursor.getDouble(cursor.getColumnIndexOrThrow("totalAmount")),
                OrderStatus.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("status"))),
                cursor.getString(cursor.getColumnIndexOrThrow("receiverName")),
                cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber")),
                cursor.getString(cursor.getColumnIndexOrThrow("shippingAddress")),
                PaymentMethod.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("paymentMethod"))),
                cursor.getString(cursor.getColumnIndexOrThrow("note"))
            );
        }
        
        cursor.close();
        db.close();
        
        return order;
    }
    
    /**
     * Lấy danh sách OrderItem của đơn hàng
     */
    public List<OrderItem> getOrderItems(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        Cursor cursor = db.query("OrderItem", null, "orderId = ?", 
            new String[]{String.valueOf(orderId)}, null, null, null);
        
        if (cursor.moveToFirst()) {
            do {
                OrderItem item = new OrderItem(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("orderId")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("productId")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("variantId")),
                    cursor.getString(cursor.getColumnIndexOrThrow("productName")),
                    cursor.getString(cursor.getColumnIndexOrThrow("color")),
                    cursor.getString(cursor.getColumnIndexOrThrow("size")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("priceAtOrder")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                    cursor.getString(cursor.getColumnIndexOrThrow("imageUrl"))
                );
                items.add(item);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        
        return items;
    }
}
