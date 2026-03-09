package com.example.v_closet.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.v_closet.entity.Wishlist;
import com.example.v_closet.util.DbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository cho Wishlist - Xử lý các thao tác database
 */
public class WishlistRepository {
    private DbHelper dbHelper;

    public WishlistRepository(Context context) {
        this.dbHelper = new DbHelper(context);
    }

    /**
     * Thêm sản phẩm vào wishlist
     */
    public long insert(Wishlist wishlist) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put("userId", wishlist.getUserId());
        values.put("productId", wishlist.getProductId());
        
        long id = db.insert("Wishlist", null, values);
        db.close();
        return id;
    }

    /**
     * Xóa sản phẩm khỏi wishlist
     */
    public int delete(int userId, int productId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete("Wishlist", "userId = ? AND productId = ?", 
            new String[]{String.valueOf(userId), String.valueOf(productId)});
        db.close();
        return rows;
    }

    /**
     * Lấy tất cả wishlist của user
     */
    public List<Wishlist> findByUserId(int userId) {
        List<Wishlist> wishlists = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        Cursor cursor = db.query("Wishlist", null, "userId = ?", 
            new String[]{String.valueOf(userId)}, null, null, null);
        
        while (cursor.moveToNext()) {
            wishlists.add(mapCursorToWishlist(cursor));
        }
        
        cursor.close();
        db.close();
        return wishlists;
    }

    /**
     * Kiểm tra sản phẩm có trong wishlist không
     */
    public boolean isInWishlist(int userId, int productId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Wishlist", null, 
            "userId = ? AND productId = ?", 
            new String[]{String.valueOf(userId), String.valueOf(productId)}, 
            null, null, null);
        
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    /**
     * Map Cursor sang Wishlist
     */
    private Wishlist mapCursorToWishlist(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        int userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));
        int productId = cursor.getInt(cursor.getColumnIndexOrThrow("productId"));
        
        return new Wishlist(id, userId, productId);
    }
}
