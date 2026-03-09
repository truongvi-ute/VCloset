package com.example.v_closet.service;

import android.content.Context;

import com.example.v_closet.entity.Address;
import com.example.v_closet.entity.CartItemDetail;
import com.example.v_closet.entity.Order;
import com.example.v_closet.entity.OrderItem;
import com.example.v_closet.entity.enums.PaymentMethod;
import com.example.v_closet.repository.OrderRepository;

import java.util.List;

/**
 * Service xử lý logic nghiệp vụ cho Order
 */
public class OrderService {
    
    private OrderRepository orderRepository;
    
    public OrderService(Context context) {
        this.orderRepository = new OrderRepository(context);
    }
    
    /**
     * Tạo đơn hàng từ giỏ hàng
     * @return orderId nếu thành công, -1 nếu thất bại
     */
    public long createOrder(int userId, List<CartItemDetail> cartItems, Address shippingAddress, 
                           PaymentMethod paymentMethod, String note, double totalAmount) {
        
        // Validate
        if (cartItems == null || cartItems.isEmpty()) {
            return -1;
        }
        
        if (shippingAddress == null) {
            return -1;
        }
        
        // Tạo Order
        Order order = new Order(
            userId,
            totalAmount,
            shippingAddress.getReceiverName(),
            shippingAddress.getPhoneNumber(),
            shippingAddress.getFullAddress(),
            paymentMethod,
            note
        );
        
        long orderId = orderRepository.createOrder(order);
        
        if (orderId == -1) {
            return -1;
        }
        
        // Tạo OrderItems
        for (CartItemDetail cartItem : cartItems) {
            OrderItem orderItem = new OrderItem(
                (int) orderId,
                cartItem.getProduct().getId(),
                cartItem.getVariant().getId(),
                cartItem.getProduct().getName(),
                cartItem.getVariant().getColor(),
                cartItem.getVariant().getSize(),
                cartItem.getProduct().getBasePrice() + cartItem.getVariant().getPriceAdjustment(),
                cartItem.getCartItem().getQuantity(),
                cartItem.getImageUrl()
            );
            
            long itemId = orderRepository.addOrderItem(orderItem);
            
            if (itemId == -1) {
                // Nếu thêm item thất bại, có thể rollback hoặc log error
                // Tạm thời bỏ qua
            }
        }
        
        return orderId;
    }
    
    /**
     * Lấy danh sách đơn hàng của user
     */
    public List<Order> getUserOrders(int userId) {
        return orderRepository.getUserOrders(userId);
    }
    
    /**
     * Lấy chi tiết đơn hàng
     */
    public Order getOrderById(int orderId) {
        return orderRepository.getOrderById(orderId);
    }
    
    /**
     * Lấy danh sách sản phẩm trong đơn hàng
     */
    public List<OrderItem> getOrderItems(int orderId) {
        return orderRepository.getOrderItems(orderId);
    }
}
