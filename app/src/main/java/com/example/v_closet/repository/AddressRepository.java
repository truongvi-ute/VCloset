package com.example.v_closet.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.v_closet.entity.Address;
import com.example.v_closet.util.DbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository cho Address - Xử lý các thao tác database
 */
public class AddressRepository {
    private DbHelper dbHelper;

    public AddressRepository(Context context) {
        this.dbHelper = new DbHelper(context);
    }

    /**
     * Thêm địa chỉ mới
     */
    public long insert(Address address) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        // Nếu địa chỉ mới là default, bỏ default của các địa chỉ khác
        if (address.isDefault()) {
            clearDefaultAddress(db, address.getUserId());
        }
        
        ContentValues values = new ContentValues();
        values.put("userId", address.getUserId());
        values.put("receiverName", address.getReceiverName());
        values.put("phoneNumber", address.getPhoneNumber());
        values.put("street", address.getStreet());
        values.put("ward", address.getWard());
        values.put("district", address.getDistrict());
        values.put("city", address.getCity());
        values.put("isDefault", address.isDefault() ? 1 : 0);
        
        long id = db.insert("Address", null, values);
        db.close();
        return id;
    }

    /**
     * Cập nhật địa chỉ
     */
    public int update(Address address) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        // Nếu địa chỉ được set là default, bỏ default của các địa chỉ khác
        if (address.isDefault()) {
            clearDefaultAddress(db, address.getUserId());
        }
        
        ContentValues values = new ContentValues();
        values.put("receiverName", address.getReceiverName());
        values.put("phoneNumber", address.getPhoneNumber());
        values.put("street", address.getStreet());
        values.put("ward", address.getWard());
        values.put("district", address.getDistrict());
        values.put("city", address.getCity());
        values.put("isDefault", address.isDefault() ? 1 : 0);
        
        int rows = db.update("Address", values, "id = ?", 
            new String[]{String.valueOf(address.getId())});
        db.close();
        return rows;
    }

    /**
     * Xóa địa chỉ
     */
    public int delete(int addressId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete("Address", "id = ?", new String[]{String.valueOf(addressId)});
        db.close();
        return rows;
    }

    /**
     * Lấy tất cả địa chỉ của user
     */
    public List<Address> findByUserId(int userId) {
        List<Address> addresses = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        Cursor cursor = db.query("Address", null, "userId = ?", 
            new String[]{String.valueOf(userId)}, null, null, "isDefault DESC, id DESC");
        
        while (cursor.moveToNext()) {
            addresses.add(mapCursorToAddress(cursor));
        }
        
        cursor.close();
        db.close();
        return addresses;
    }

    /**
     * Lấy địa chỉ mặc định của user
     */
    public Address getDefaultAddress(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Address", null, "userId = ? AND isDefault = 1", 
            new String[]{String.valueOf(userId)}, null, null, null);
        
        Address address = null;
        if (cursor.moveToFirst()) {
            address = mapCursorToAddress(cursor);
        }
        
        cursor.close();
        db.close();
        return address;
    }

    /**
     * Set địa chỉ làm mặc định
     */
    public int setDefaultAddress(int addressId, int userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        // Bỏ default của tất cả địa chỉ
        clearDefaultAddress(db, userId);
        
        // Set địa chỉ này làm default
        ContentValues values = new ContentValues();
        values.put("isDefault", 1);
        
        int rows = db.update("Address", values, "id = ?", 
            new String[]{String.valueOf(addressId)});
        db.close();
        return rows;
    }

    /**
     * Bỏ default của tất cả địa chỉ
     */
    private void clearDefaultAddress(SQLiteDatabase db, int userId) {
        ContentValues values = new ContentValues();
        values.put("isDefault", 0);
        db.update("Address", values, "userId = ?", new String[]{String.valueOf(userId)});
    }

    /**
     * Map Cursor sang Address
     */
    private Address mapCursorToAddress(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        int userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));
        String receiverName = cursor.getString(cursor.getColumnIndexOrThrow("receiverName"));
        String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber"));
        String street = cursor.getString(cursor.getColumnIndexOrThrow("street"));
        String ward = cursor.getString(cursor.getColumnIndexOrThrow("ward"));
        String district = cursor.getString(cursor.getColumnIndexOrThrow("district"));
        String city = cursor.getString(cursor.getColumnIndexOrThrow("city"));
        boolean isDefault = cursor.getInt(cursor.getColumnIndexOrThrow("isDefault")) == 1;
        
        return new Address(id, userId, receiverName, phoneNumber, street, ward, district, city, isDefault);
    }
}
