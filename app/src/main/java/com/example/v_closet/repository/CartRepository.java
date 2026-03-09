package com.example.v_closet.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.v_closet.entity.Cart;
import com.example.v_closet.entity.CartItem;
import com.example.v_closet.util.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class CartRepository {
    private DbHelper dbHelper;

    public CartRepository(Context context) {
        this.dbHelper = new DbHelper(context);
    }

    public long createCart(int userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", userId);
        long id = db.insert("Cart", null, values);
        db.close();
        return id;
    }

    public Cart findByUserId(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Cart", null, "userId = ?", 
            new String[]{String.valueOf(userId)}, null, null, null);
        
        Cart cart = null;
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            cart = new Cart(id, userId);
        }
        cursor.close();
        db.close();
        return cart;
    }

    public long addCartItem(CartItem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("cartId", item.getCartId());
        values.put("variantId", item.getVariantId());
        values.put("quantity", item.getQuantity());
        values.put("priceAtAdd", item.getPriceAtAdd());
        long id = db.insert("CartItem", null, values);
        db.close();
        return id;
    }

    public List<CartItem> getCartItems(int cartId) {
        List<CartItem> items = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("CartItem", null, "cartId = ?", 
            new String[]{String.valueOf(cartId)}, null, null, null);
        
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            int variantId = cursor.getInt(cursor.getColumnIndexOrThrow("variantId"));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
            double priceAtAdd = cursor.getDouble(cursor.getColumnIndexOrThrow("priceAtAdd"));
            items.add(new CartItem(id, cartId, variantId, quantity, priceAtAdd));
        }
        cursor.close();
        db.close();
        return items;
    }

    public int updateQuantity(int cartItemId, int quantity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quantity", quantity);
        int rows = db.update("CartItem", values, "id = ?", 
            new String[]{String.valueOf(cartItemId)});
        db.close();
        return rows;
    }

    public int deleteCartItem(int cartItemId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete("CartItem", "id = ?", 
            new String[]{String.valueOf(cartItemId)});
        db.close();
        return rows;
    }

    public int clearCart(int cartId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete("CartItem", "cartId = ?", 
            new String[]{String.valueOf(cartId)});
        db.close();
        return rows;
    }

    public CartItem findCartItemByVariant(int cartId, int variantId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("CartItem", null, "cartId = ? AND variantId = ?",
            new String[]{String.valueOf(cartId), String.valueOf(variantId)}, null, null, null);

        CartItem item = null;
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
            double priceAtAdd = cursor.getDouble(cursor.getColumnIndexOrThrow("priceAtAdd"));
            item = new CartItem(id, cartId, variantId, quantity, priceAtAdd);
        }
        cursor.close();
        db.close();
        return item;
    }

}
