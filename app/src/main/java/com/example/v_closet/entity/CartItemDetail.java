package com.example.v_closet.entity;

import java.io.Serializable;

/**
 * Lớp CartItemDetail chứa thông tin đầy đủ của một item trong giỏ hàng
 * Kết hợp thông tin từ CartItem, Product, ProductVariant
 */
public class CartItemDetail implements Serializable {
    private CartItem cartItem;
    private Product product;
    private ProductVariant variant;
    private String imageUrl;

    public CartItemDetail(CartItem cartItem, Product product, ProductVariant variant, String imageUrl) {
        this.cartItem = cartItem;
        this.product = product;
        this.variant = variant;
        this.imageUrl = imageUrl;
    }

    public CartItem getCartItem() { return cartItem; }
    public Product getProduct() { return product; }
    public ProductVariant getVariant() { return variant; }
    public String getImageUrl() { return imageUrl; }
}
