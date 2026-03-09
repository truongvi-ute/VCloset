package com.example.v_closet.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.v_closet.entity.UserProfile;
import com.example.v_closet.util.DbHelper;

import java.util.Date;

/**
 * Repository cho UserProfile - Xử lý các thao tác database
 */
public class UserProfileRepository {
    private DbHelper dbHelper;

    public UserProfileRepository(Context context) {
        this.dbHelper = new DbHelper(context);
    }

    /**
     * Thêm profile mới
     */
    public long insert(UserProfile profile) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put("userId", profile.getUserId());
        values.put("fullName", profile.getFullName());
        values.put("phoneNumber", profile.getPhoneNumber());
        values.put("avatarUrl", profile.getAvatarUrl());
        values.put("gender", profile.getGender());
        if (profile.getBirthday() != null) {
            values.put("birthday", profile.getBirthday().getTime());
        }
        values.put("createdAt", profile.getCreatedAt().getTime());
        values.put("updatedAt", profile.getUpdatedAt().getTime());
        
        long id = db.insert("UserProfile", null, values);
        db.close();
        return id;
    }

    /**
     * Tìm profile theo userId
     */
    public UserProfile findByUserId(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("UserProfile", 
            null, 
            "userId = ?", 
            new String[]{String.valueOf(userId)}, 
            null, null, null);
        
        UserProfile profile = null;
        if (cursor.moveToFirst()) {
            profile = mapCursorToUserProfile(cursor);
        }
        
        cursor.close();
        db.close();
        return profile;
    }

    /**
     * Cập nhật profile
     */
    public int update(UserProfile profile) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put("fullName", profile.getFullName());
        values.put("phoneNumber", profile.getPhoneNumber());
        values.put("avatarUrl", profile.getAvatarUrl());
        values.put("gender", profile.getGender());
        if (profile.getBirthday() != null) {
            values.put("birthday", profile.getBirthday().getTime());
        }
        values.put("updatedAt", new Date().getTime());
        
        int rows = db.update("UserProfile", values, "id = ?", 
            new String[]{String.valueOf(profile.getId())});
        db.close();
        return rows;
    }

    /**
     * Map Cursor sang UserProfile
     */
    private UserProfile mapCursorToUserProfile(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        int userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));
        String fullName = cursor.getString(cursor.getColumnIndexOrThrow("fullName"));
        String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber"));
        String avatarUrl = cursor.getString(cursor.getColumnIndexOrThrow("avatarUrl"));
        String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
        
        long birthdayMs = cursor.getLong(cursor.getColumnIndexOrThrow("birthday"));
        Date birthday = birthdayMs > 0 ? new Date(birthdayMs) : null;
        
        long createdAtMs = cursor.getLong(cursor.getColumnIndexOrThrow("createdAt"));
        long updatedAtMs = cursor.getLong(cursor.getColumnIndexOrThrow("updatedAt"));
        
        return new UserProfile(id, userId, fullName, phoneNumber, avatarUrl, gender, 
            birthday, new Date(createdAtMs), new Date(updatedAtMs));
    }
}
