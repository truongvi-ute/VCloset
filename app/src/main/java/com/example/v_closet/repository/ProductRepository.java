package com.example.v_closet.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.v_closet.entity.Pant;
import com.example.v_closet.entity.Product;
import com.example.v_closet.entity.ProductImage;
import com.example.v_closet.entity.ProductVariant;
import com.example.v_closet.entity.Shirt;
import com.example.v_closet.entity.enums.CategoryType;
import com.example.v_closet.util.DbHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Repository cho Product - Xử lý các thao tác database
 */
public class ProductRepository {
    private DbHelper dbHelper;

    public ProductRepository(Context context) {
        this.dbHelper = new DbHelper(context);
    }

    /**
     * Thêm sản phẩm mới (Shirt hoặc Pant)
     */
    public long insertProduct(Product product) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put("name", product.getName());
        values.put("description", product.getDescription());
        values.put("basePrice", product.getBasePrice());
        values.put("category", product.getCategory().name());
        values.put("isActive", product.isActive() ? 1 : 0);
        values.put("createdAt", product.getCreatedAt().getTime());
        values.put("updatedAt", product.getUpdatedAt().getTime());
        
        long productId = db.insert("Product", null, values);
        
        // Insert vào bảng Shirt hoặc Pant
        if (productId > 0) {
            if (product instanceof Shirt) {
                insertShirt(db, (int) productId, (Shirt) product);
            } else if (product instanceof Pant) {
                insertPant(db, (int) productId, (Pant) product);
            }
        }
        
        db.close();
        return productId;
    }

    /**
     * Insert vào bảng Shirt
     */
    private void insertShirt(SQLiteDatabase db, int productId, Shirt shirt) {
        ContentValues values = new ContentValues();
        values.put("id", productId);
        values.put("material", shirt.getMaterial());
        values.put("sleeveType", shirt.getSleeveType());
        values.put("collarType", shirt.getCollarType());
        db.insert("Shirt", null, values);
    }

    /**
     * Insert vào bảng Pant
     */
    private void insertPant(SQLiteDatabase db, int productId, Pant pant) {
        ContentValues values = new ContentValues();
        values.put("id", productId);
        values.put("material", pant.getMaterial());
        values.put("pantType", pant.getPantType());
        values.put("fitType", pant.getFitType());
        db.insert("Pant", null, values);
    }

    /**
     * Thêm variant cho sản phẩm
     */
    public long insertVariant(ProductVariant variant) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put("productId", variant.getProductId());
        values.put("color", variant.getColor());
        values.put("size", variant.getSize());
        values.put("priceAdjustment", variant.getPriceAdjustment());
        
        long id = db.insert("ProductVariant", null, values);
        db.close();
        return id;
    }

    /**
     * Thêm hình ảnh cho sản phẩm
     */
    public long insertImage(ProductImage image) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put("productId", image.getProductId());
        values.put("variantId", image.getVariantId());
        values.put("imageUrl", image.getImageUrl());
        values.put("isPrimary", image.isPrimary() ? 1 : 0);
        
        long id = db.insert("ProductImage", null, values);
        db.close();
        return id;
    }

    /**
     * Lấy tất cả sản phẩm đang active
     */
    public List<Product> getAllActiveProducts() {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        Cursor cursor = db.query("Product", null, "isActive = ?", 
            new String[]{"1"}, null, null, "createdAt DESC");
        
        while (cursor.moveToNext()) {
            Product product = mapCursorToProduct(db, cursor);
            if (product != null) {
                products.add(product);
            }
        }
        
        cursor.close();
        db.close();
        return products;
    }

    /**
     * Lấy sản phẩm theo category
     */
    public List<Product> getProductsByCategory(CategoryType category) {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        Cursor cursor = db.query("Product", null, 
            "category = ? AND isActive = ?", 
            new String[]{category.name(), "1"}, 
            null, null, "createdAt DESC");
        
        while (cursor.moveToNext()) {
            Product product = mapCursorToProduct(db, cursor);
            if (product != null) {
                products.add(product);
            }
        }
        
        cursor.close();
        db.close();
        return products;
    }

    /**
     * Lấy variants của sản phẩm
     */
    public List<ProductVariant> getVariantsByProductId(int productId) {
        List<ProductVariant> variants = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        Cursor cursor = db.query("ProductVariant", null, 
            "productId = ?", new String[]{String.valueOf(productId)}, 
            null, null, null);
        
        while (cursor.moveToNext()) {
            variants.add(mapCursorToVariant(cursor));
        }
        
        cursor.close();
        db.close();
        return variants;
    }

    /**
     * Lấy hình ảnh của sản phẩm
     */
    public List<ProductImage> getImagesByProductId(int productId) {
        List<ProductImage> images = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        Cursor cursor = db.query("ProductImage", null, 
            "productId = ?", new String[]{String.valueOf(productId)}, 
            null, null, "isPrimary DESC");
        
        while (cursor.moveToNext()) {
            images.add(mapCursorToImage(cursor));
        }
        
        cursor.close();
        db.close();
        return images;
    }

    /**
     * Kiểm tra database đã có sản phẩm chưa
     */
    public boolean hasProducts() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM Product", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count > 0;
    }

    /**
     * Lấy variant theo ID
     */
    public ProductVariant getVariantById(int variantId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("ProductVariant", null, 
            "id = ?", new String[]{String.valueOf(variantId)}, 
            null, null, null);
        
        ProductVariant variant = null;
        if (cursor.moveToFirst()) {
            variant = mapCursorToVariant(cursor);
        }
        
        cursor.close();
        db.close();
        return variant;
    }

    /**
     * Map Cursor sang Product object
     */
    private Product mapCursorToProduct(SQLiteDatabase db, Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
        double basePrice = cursor.getDouble(cursor.getColumnIndexOrThrow("basePrice"));
        String categoryStr = cursor.getString(cursor.getColumnIndexOrThrow("category"));
        CategoryType category = CategoryType.valueOf(categoryStr);
        boolean isActive = cursor.getInt(cursor.getColumnIndexOrThrow("isActive")) == 1;
        long createdAtMs = cursor.getLong(cursor.getColumnIndexOrThrow("createdAt"));
        long updatedAtMs = cursor.getLong(cursor.getColumnIndexOrThrow("updatedAt"));
        
        Product product = null;
        
        if (category == CategoryType.SHIRT) {
            Cursor shirtCursor = db.query("Shirt", null, "id = ?", 
                new String[]{String.valueOf(id)}, null, null, null);
            if (shirtCursor.moveToFirst()) {
                String material = shirtCursor.getString(shirtCursor.getColumnIndexOrThrow("material"));
                String sleeveType = shirtCursor.getString(shirtCursor.getColumnIndexOrThrow("sleeveType"));
                String collarType = shirtCursor.getString(shirtCursor.getColumnIndexOrThrow("collarType"));
                product = new Shirt(id, name, description, basePrice, category, isActive, 
                    material, sleeveType, collarType);
            }
            shirtCursor.close();
        } else if (category == CategoryType.PANT) {
            Cursor pantCursor = db.query("Pant", null, "id = ?", 
                new String[]{String.valueOf(id)}, null, null, null);
            if (pantCursor.moveToFirst()) {
                String material = pantCursor.getString(pantCursor.getColumnIndexOrThrow("material"));
                String pantType = pantCursor.getString(pantCursor.getColumnIndexOrThrow("pantType"));
                String fitType = pantCursor.getString(pantCursor.getColumnIndexOrThrow("fitType"));
                product = new Pant(id, name, description, basePrice, category, isActive, 
                    material, pantType, fitType);
            }
            pantCursor.close();
        }
        
        if (product != null) {
            product.setCreatedAt(new Date(createdAtMs));
            product.setUpdatedAt(new Date(updatedAtMs));
        }
        
        return product;
    }

    /**
     * Map Cursor sang ProductVariant
     */
    private ProductVariant mapCursorToVariant(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        int productId = cursor.getInt(cursor.getColumnIndexOrThrow("productId"));
        String color = cursor.getString(cursor.getColumnIndexOrThrow("color"));
        String size = cursor.getString(cursor.getColumnIndexOrThrow("size"));
        double priceAdjustment = cursor.getDouble(cursor.getColumnIndexOrThrow("priceAdjustment"));
        
        return new ProductVariant(id, productId, color, size, priceAdjustment);
    }

    /**
     * Map Cursor sang ProductImage
     */
    private ProductImage mapCursorToImage(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        int productId = cursor.getInt(cursor.getColumnIndexOrThrow("productId"));
        int variantId = cursor.getInt(cursor.getColumnIndexOrThrow("variantId"));
        String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl"));
        boolean isPrimary = cursor.getInt(cursor.getColumnIndexOrThrow("isPrimary")) == 1;
        
        return new ProductImage(id, productId, variantId, imageUrl, isPrimary);
    }
}
