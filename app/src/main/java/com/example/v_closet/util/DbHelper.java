package com.example.v_closet.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * Database Helper cho V-Closet App
 * Quản lý tạo và nâng cấp database SQLite
 * 
 * Lưu ý: Date được lưu dạng INTEGER (timestamp milliseconds)
 * Sử dụng date.getTime() để lưu và new Date(timestamp) để đọc
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "v_closet.db";
    private static final int DATABASE_VERSION = 2; // Tăng version do có thay đổi schema

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. User & Profile Tables
        db.execSQL("CREATE TABLE User (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL, " +
                "email TEXT UNIQUE NOT NULL, " +
                "createdAt INTEGER NOT NULL, " +
                "updatedAt INTEGER NOT NULL, " +
                "profileId INTEGER, " +
                "cartId INTEGER, " +
                "wishlistId INTEGER)");

        db.execSQL("CREATE TABLE UserProfile (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId INTEGER NOT NULL, " +
                "fullName TEXT, " +
                "phoneNumber TEXT, " +
                "avatarUrl TEXT, " +
                "gender TEXT, " +
                "birthday INTEGER, " +
                "createdAt INTEGER NOT NULL, " +
                "updatedAt INTEGER NOT NULL, " +
                "FOREIGN KEY(userId) REFERENCES User(id) ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE Address (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId INTEGER NOT NULL, " +
                "receiverName TEXT NOT NULL, " +
                "phoneNumber TEXT NOT NULL, " +
                "street TEXT NOT NULL, " +
                "ward TEXT NOT NULL, " +
                "district TEXT NOT NULL, " +
                "city TEXT NOT NULL, " +
                "isDefault INTEGER DEFAULT 0, " + // 0: false, 1: true
                "FOREIGN KEY(userId) REFERENCES User(id) ON DELETE CASCADE)");

        // 2. Product & Variant Tables
        db.execSQL("CREATE TABLE Product (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "description TEXT, " +
                "basePrice REAL NOT NULL, " +
                "category TEXT NOT NULL, " + // Store CategoryType enum name()
                "isActive INTEGER DEFAULT 1, " +
                "createdAt INTEGER NOT NULL, " +
                "updatedAt INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE Shirt (" +
                "id INTEGER PRIMARY KEY, " +
                "material TEXT, " +
                "sleeveType TEXT, " +
                "collarType TEXT, " +
                "FOREIGN KEY(id) REFERENCES Product(id) ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE Pant (" +
                "id INTEGER PRIMARY KEY, " +
                "material TEXT, " +
                "pantType TEXT, " +
                "fitType TEXT, " +
                "FOREIGN KEY(id) REFERENCES Product(id) ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE ProductVariant (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "productId INTEGER NOT NULL, " +
                "color TEXT, " +
                "size TEXT, " +
                "priceAdjustment REAL DEFAULT 0, " +
                "FOREIGN KEY(productId) REFERENCES Product(id) ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE ProductImage (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "productId INTEGER NOT NULL, " +
                "variantId INTEGER, " +
                "imageUrl TEXT NOT NULL, " +
                "isPrimary INTEGER DEFAULT 0, " +
                "FOREIGN KEY(productId) REFERENCES Product(id) ON DELETE CASCADE)");

        // 3. Cart & Wishlist Tables
        db.execSQL("CREATE TABLE Cart (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId INTEGER NOT NULL UNIQUE, " + // Mỗi user chỉ có 1 cart
                "FOREIGN KEY(userId) REFERENCES User(id) ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE CartItem (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "cartId INTEGER NOT NULL, " +
                "variantId INTEGER NOT NULL, " +
                "quantity INTEGER NOT NULL DEFAULT 1, " +
                "priceAtAdd REAL NOT NULL, " +
                "FOREIGN KEY(cartId) REFERENCES Cart(id) ON DELETE CASCADE, " +
                "FOREIGN KEY(variantId) REFERENCES ProductVariant(id))");

        db.execSQL("CREATE TABLE Wishlist (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId INTEGER NOT NULL, " +
                "productId INTEGER NOT NULL, " +
                "UNIQUE(userId, productId), " + // Không cho thêm trùng sản phẩm
                "FOREIGN KEY(userId) REFERENCES User(id) ON DELETE CASCADE, " +
                "FOREIGN KEY(productId) REFERENCES Product(id) ON DELETE CASCADE)");

        // 4. Order Tables
        db.execSQL("CREATE TABLE Orders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId INTEGER NOT NULL, " +
                "orderDate INTEGER NOT NULL, " +
                "totalAmount REAL NOT NULL, " +
                "status TEXT NOT NULL, " + // Store OrderStatus enum name()
                "receiverName TEXT NOT NULL, " +
                "phoneNumber TEXT NOT NULL, " +
                "shippingAddress TEXT NOT NULL, " +
                "paymentMethod TEXT NOT NULL, " + // Store PaymentMethod enum name()
                "note TEXT, " +
                "FOREIGN KEY(userId) REFERENCES User(id))");

        db.execSQL("CREATE TABLE OrderItem (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "orderId INTEGER NOT NULL, " +
                "productId INTEGER NOT NULL, " +
                "variantId INTEGER NOT NULL, " +
                "productName TEXT NOT NULL, " +
                "color TEXT, " +
                "size TEXT, " +
                "priceAtOrder REAL NOT NULL, " +
                "quantity INTEGER NOT NULL, " +
                "imageUrl TEXT, " +
                "FOREIGN KEY(orderId) REFERENCES Orders(id) ON DELETE CASCADE, " +
                "FOREIGN KEY(productId) REFERENCES Product(id), " +
                "FOREIGN KEY(variantId) REFERENCES ProductVariant(id))");

        // Create indexes for better query performance
        db.execSQL("CREATE INDEX idx_user_email ON User(email)");
        db.execSQL("CREATE INDEX idx_user_username ON User(username)");
        db.execSQL("CREATE INDEX idx_product_category ON Product(category)");
        db.execSQL("CREATE INDEX idx_product_active ON Product(isActive)");
        db.execSQL("CREATE INDEX idx_order_user ON Orders(userId)");
        db.execSQL("CREATE INDEX idx_order_status ON Orders(status)");
        db.execSQL("CREATE INDEX idx_wishlist_user ON Wishlist(userId)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Simple strategy: Drop and recreate (for development)
        // TODO: Implement proper migration strategy for production
        db.execSQL("DROP TABLE IF EXISTS OrderItem");
        db.execSQL("DROP TABLE IF EXISTS Orders");
        db.execSQL("DROP TABLE IF EXISTS Wishlist");
        db.execSQL("DROP TABLE IF EXISTS CartItem");
        db.execSQL("DROP TABLE IF EXISTS Cart");
        db.execSQL("DROP TABLE IF EXISTS ProductImage");
        db.execSQL("DROP TABLE IF EXISTS ProductVariant");
        db.execSQL("DROP TABLE IF EXISTS Pant");
        db.execSQL("DROP TABLE IF EXISTS Shirt");
        db.execSQL("DROP TABLE IF EXISTS Product");
        db.execSQL("DROP TABLE IF EXISTS Address");
        db.execSQL("DROP TABLE IF EXISTS UserProfile");
        db.execSQL("DROP TABLE IF EXISTS User");
        onCreate(db);
    }
}