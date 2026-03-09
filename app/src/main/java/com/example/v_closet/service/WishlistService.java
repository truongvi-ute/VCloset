package com.example.v_closet.service;

import android.content.Context;

import com.example.v_closet.entity.Product;
import com.example.v_closet.entity.Wishlist;
import com.example.v_closet.repository.ProductRepository;
import com.example.v_closet.repository.WishlistRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Service cho Wishlist - Xử lý logic nghiệp vụ
 */
public class WishlistService {
    private WishlistRepository wishlistRepository;
    private ProductRepository productRepository;

    public WishlistService(Context context) {
        this.wishlistRepository = new WishlistRepository(context);
        this.productRepository = new ProductRepository(context);
    }

    /**
     * Thêm sản phẩm vào wishlist
     */
    public boolean addToWishlist(int userId, int productId) {
        // Kiểm tra đã tồn tại chưa
        if (wishlistRepository.isInWishlist(userId, productId)) {
            return false; // Đã có trong wishlist
        }
        
        Wishlist wishlist = new Wishlist(userId, productId);
        long id = wishlistRepository.insert(wishlist);
        return id > 0;
    }

    /**
     * Xóa sản phẩm khỏi wishlist
     */
    public boolean removeFromWishlist(int userId, int productId) {
        int rows = wishlistRepository.delete(userId, productId);
        return rows > 0;
    }

    /**
     * Lấy danh sách sản phẩm trong wishlist
     */
    public List<Product> getWishlistProducts(int userId) {
        List<Wishlist> wishlists = wishlistRepository.findByUserId(userId);
        List<Product> products = new ArrayList<>();
        
        for (Wishlist wishlist : wishlists) {
            // Lấy thông tin sản phẩm từ productId
            List<Product> allProducts = productRepository.getAllActiveProducts();
            for (Product product : allProducts) {
                if (product.getId() == wishlist.getProductId()) {
                    products.add(product);
                    break;
                }
            }
        }
        
        return products;
    }

    /**
     * Kiểm tra sản phẩm có trong wishlist không
     */
    public boolean isInWishlist(int userId, int productId) {
        return wishlistRepository.isInWishlist(userId, productId);
    }

    /**
     * Đếm số lượng sản phẩm trong wishlist
     */
    public int getWishlistCount(int userId) {
        return wishlistRepository.findByUserId(userId).size();
    }
}
