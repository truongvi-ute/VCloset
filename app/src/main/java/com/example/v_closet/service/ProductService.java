package com.example.v_closet.service;

import android.content.Context;

import com.example.v_closet.entity.Product;
import com.example.v_closet.entity.ProductImage;
import com.example.v_closet.entity.ProductVariant;
import com.example.v_closet.entity.enums.CategoryType;
import com.example.v_closet.repository.ProductRepository;

import java.util.List;

/**
 * Service cho Product - Xử lý logic nghiệp vụ
 */
public class ProductService {
    private ProductRepository productRepository;

    public ProductService(Context context) {
        this.productRepository = new ProductRepository(context);
    }

    /**
     * Lấy tất cả sản phẩm đang active
     */
    public List<Product> getAllProducts() {
        return productRepository.getAllActiveProducts();
    }

    /**
     * Lấy sản phẩm theo category
     */
    public List<Product> getProductsByCategory(CategoryType category) {
        return productRepository.getProductsByCategory(category);
    }

    /**
     * Lấy danh sách áo
     */
    public List<Product> getShirts() {
        return productRepository.getProductsByCategory(CategoryType.SHIRT);
    }

    /**
     * Lấy danh sách quần
     */
    public List<Product> getPants() {
        return productRepository.getProductsByCategory(CategoryType.PANT);
    }

    /**
     * Lấy variants của sản phẩm
     */
    public List<ProductVariant> getProductVariants(int productId) {
        return productRepository.getVariantsByProductId(productId);
    }

    /**
     * Lấy hình ảnh của sản phẩm
     */
    public List<ProductImage> getProductImages(int productId) {
        return productRepository.getImagesByProductId(productId);
    }

    /**
     * Lấy hình ảnh chính của sản phẩm
     */
    public ProductImage getPrimaryImage(int productId) {
        List<ProductImage> images = productRepository.getImagesByProductId(productId);
        if (images != null && !images.isEmpty()) {
            for (ProductImage image : images) {
                if (image.isPrimary()) {
                    return image;
                }
            }
            return images.get(0); // Trả về ảnh đầu tiên nếu không có ảnh primary
        }
        return null;
    }

    /**
     * Kiểm tra database đã có sản phẩm chưa
     */
    public boolean hasProducts() {
        return productRepository.hasProducts();
    }

    /**
     * Lấy sản phẩm theo ID
     */
    public Product getProductById(int productId) {
        List<Product> products = productRepository.getAllActiveProducts();
        for (Product product : products) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    /**
     * Lấy variant theo ID
     */
    public ProductVariant getVariantById(int variantId) {
        return productRepository.getVariantById(variantId);
    }
}
