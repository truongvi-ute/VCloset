package com.example.v_closet.service;

import android.content.Context;

import com.example.v_closet.entity.Cart;
import com.example.v_closet.entity.CartItem;
import com.example.v_closet.entity.CartItemDetail;
import com.example.v_closet.entity.Product;
import com.example.v_closet.entity.ProductImage;
import com.example.v_closet.entity.ProductVariant;
import com.example.v_closet.repository.CartRepository;

import java.util.ArrayList;
import java.util.List;

public class CartService {
    private CartRepository cartRepository;
    private ProductService productService;

    public CartService(Context context) {
        this.cartRepository = new CartRepository(context);
        this.productService = new ProductService(context);
    }

    public Cart getOrCreateCart(int userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            long cartId = cartRepository.createCart(userId);
            cart = new Cart((int) cartId, userId);
        }
        return cart;
    }

    
        public boolean addToCart(int userId, int variantId, int quantity, double price) {
            Cart cart = getOrCreateCart(userId);

            // Kiểm tra xem item với variantId này đã tồn tại chưa
            CartItem existingItem = cartRepository.findCartItemByVariant(cart.getId(), variantId);

            if (existingItem != null) {
                // Nếu đã tồn tại, tăng số lượng
                int newQuantity = existingItem.getQuantity() + quantity;
                return cartRepository.updateQuantity(existingItem.getId(), newQuantity) > 0;
            } else {
                // Nếu chưa tồn tại, tạo mới
                CartItem item = new CartItem(cart.getId(), variantId, quantity, price);
                long result = cartRepository.addCartItem(item);
                return result > 0;
            }
        }



    public List<CartItem> getCartItems(int userId) {
        Cart cart = getOrCreateCart(userId);
        return cartRepository.getCartItems(cart.getId());
    }

    public boolean updateQuantity(int cartItemId, int quantity) {
        if (quantity <= 0) {
            return cartRepository.deleteCartItem(cartItemId) > 0;
        }
        return cartRepository.updateQuantity(cartItemId, quantity) > 0;
    }

    public boolean removeItem(int cartItemId) {
        return cartRepository.deleteCartItem(cartItemId) > 0;
    }

    public boolean clearCart(int userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart != null) {
            return cartRepository.clearCart(cart.getId()) > 0;
        }
        return false;
    }

    public double calculateTotal(List<CartItem> items) {
        double total = 0;
        for (CartItem item : items) {
            total += item.getPriceAtAdd() * item.getQuantity();
        }
        return total;
    }

    public List<CartItemDetail> getCartItemsWithDetails(int userId) {
        List<CartItem> cartItems = getCartItems(userId);
        List<CartItemDetail> details = new ArrayList<>();
        
        for (CartItem item : cartItems) {
            ProductVariant variant = productService.getVariantById(item.getVariantId());
            if (variant != null) {
                Product product = productService.getProductById(variant.getProductId());
                if (product != null) {
                    // Lấy ảnh theo variantId (màu sắc) thay vì ảnh đầu tiên
                    String imageUrl = getImageForVariant(product.getId(), variant.getId());
                    details.add(new CartItemDetail(item, product, variant, imageUrl));
                }
            }
        }
        
        return details;
    }
    
    /**
     * Lấy ảnh tương ứng với variant (màu sắc)
     */
    private String getImageForVariant(int productId, int variantId) {
        // Lấy thông tin variant để biết màu
        ProductVariant variant = productService.getVariantById(variantId);
        if (variant == null) {
            return null;
        }
        
        String color = variant.getColor();
        List<ProductImage> allImages = productService.getProductImages(productId);
        
        // Tìm ảnh có màu tương ứng (tìm theo variantId của cùng màu)
        for (ProductImage img : allImages) {
            if (img.getVariantId() > 0) {
                ProductVariant imgVariant = productService.getVariantById(img.getVariantId());
                if (imgVariant != null && color.equals(imgVariant.getColor())) {
                    return img.getImageUrl();
                }
            }
        }
        
        // Nếu không tìm thấy, trả về ảnh đầu tiên
        return allImages.isEmpty() ? null : allImages.get(0).getImageUrl();
    }
}
