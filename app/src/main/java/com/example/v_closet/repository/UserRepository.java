package com.example.v_closet.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.v_closet.entity.User;
import com.example.v_closet.util.DbHelper;

import java.util.Date;

/**
 * Repository cho User - Xử lý các thao tác database
 */
public class UserRepository {
    private DbHelper dbHelper;

    public UserRepository(Context context) {
        this.dbHelper = new DbHelper(context);
    }

    /**
     * Thêm user mới vào database
     * @param user User cần thêm
     * @return ID của user vừa tạo, hoặc -1 nếu thất bại
     */
    public long insert(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        values.put("email", user.getEmail());
        values.put("createdAt", user.getCreatedAt().getTime());
        values.put("updatedAt", user.getUpdatedAt().getTime());
        
        long id = db.insert("User", null, values);
        db.close();
        return id;
    }

    /**
     * Kiểm tra username đã tồn tại chưa
     * @param username Username cần kiểm tra
     * @return true nếu đã tồn tại, false nếu chưa
     */
    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("User", 
            new String[]{"id"}, 
            "username = ?", 
            new String[]{username}, 
            null, null, null);
        
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    /**
     * Kiểm tra email đã tồn tại chưa
     * @param email Email cần kiểm tra
     * @return true nếu đã tồn tại, false nếu chưa
     */
    public boolean isEmailExists(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("User", 
            new String[]{"id"}, 
            "email = ?", 
            new String[]{email}, 
            null, null, null);
        
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    /**
     * Tìm user theo username
     * @param username Username cần tìm
     * @return User object hoặc null nếu không tìm thấy
     */
    public User findByUsername(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("User", 
            null, 
            "username = ?", 
            new String[]{username}, 
            null, null, null);
        
        User user = null;
        if (cursor.moveToFirst()) {
            user = mapCursorToUser(cursor);
        }
        
        cursor.close();
        db.close();
        return user;
    }

    /**
     * Tìm user theo email
     * @param email Email cần tìm
     * @return User object hoặc null nếu không tìm thấy
     */
    public User findByEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("User", 
            null, 
            "email = ?", 
            new String[]{email}, 
            null, null, null);
        
        User user = null;
        if (cursor.moveToFirst()) {
            user = mapCursorToUser(cursor);
        }
        
        cursor.close();
        db.close();
        return user;
    }

    /**
     * Tìm user theo ID
     * @param id ID của user
     * @return User object hoặc null nếu không tìm thấy
     */
    public User findById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("User", 
            null, 
            "id = ?", 
            new String[]{String.valueOf(id)}, 
            null, null, null);
        
        User user = null;
        if (cursor.moveToFirst()) {
            user = mapCursorToUser(cursor);
        }
        
        cursor.close();
        db.close();
        return user;
    }

    /**
     * Cập nhật thông tin user
     * @param user User cần cập nhật
     * @return Số dòng bị ảnh hưởng
     */
    public int update(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        values.put("email", user.getEmail());
        values.put("updatedAt", new Date().getTime());
        values.put("profileId", user.getProfileId());
        values.put("cartId", user.getCartId());
        values.put("wishlistId", user.getWishlistId());
        
        int rows = db.update("User", values, "id = ?", 
            new String[]{String.valueOf(user.getId())});
        db.close();
        return rows;
    }

    /**
     * Cập nhật mật khẩu theo email
     * @param email Email của user
     * @param newPassword Mật khẩu mới
     * @return Số dòng bị ảnh hưởng
     */
    public int updatePasswordByEmail(String email, String newPassword) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put("password", newPassword);
        values.put("updatedAt", new Date().getTime());
        
        int rows = db.update("User", values, "email = ?", new String[]{email});
        db.close();
        return rows;
    }

    /**
     * Cập nhật mật khẩu theo user ID
     * @param userId ID của user
     * @param newPassword Mật khẩu mới
     * @return Số dòng bị ảnh hưởng
     */
    public int updatePasswordById(int userId, String newPassword) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put("password", newPassword);
        values.put("updatedAt", new Date().getTime());
        
        int rows = db.update("User", values, "id = ?", 
            new String[]{String.valueOf(userId)});
        db.close();
        return rows;
    }

    /**
     * Map Cursor sang User object
     */
    private User mapCursorToUser(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
        String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
        String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
        long createdAtMs = cursor.getLong(cursor.getColumnIndexOrThrow("createdAt"));
        long updatedAtMs = cursor.getLong(cursor.getColumnIndexOrThrow("updatedAt"));
        
        int profileId = cursor.getInt(cursor.getColumnIndexOrThrow("profileId"));
        int cartId = cursor.getInt(cursor.getColumnIndexOrThrow("cartId"));
        int wishlistId = cursor.getInt(cursor.getColumnIndexOrThrow("wishlistId"));
        
        return new User(id, username, password, email, 
            new Date(createdAtMs), new Date(updatedAtMs), 
            profileId, cartId, wishlistId);
    }
}
