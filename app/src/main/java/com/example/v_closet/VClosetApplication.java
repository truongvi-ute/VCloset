package com.example.v_closet;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.example.v_closet.util.DataSeeder;
import com.example.v_closet.util.DbHelper;

/**
 * Application class cho V-Closet
 * Khởi tạo database khi app start
 */
public class VClosetApplication extends Application {
    
    private static VClosetApplication instance;
    private DbHelper dbHelper;
    
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        
        // Khởi tạo database ngay khi app start
        initDatabase();
    }
    
    /**
     * Khởi tạo database
     * Database sẽ được tạo nếu chưa tồn tại
     */
    private void initDatabase() {
        dbHelper = new DbHelper(this);
        
        // Gọi getWritableDatabase() để trigger onCreate() trong DbHelper
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        // Log để debug
        android.util.Log.d("VClosetApp", "Database initialized: " + db.getPath());
        
        // Seed dữ liệu mẫu nếu database trống
        DataSeeder seeder = new DataSeeder(this);
        seeder.seedAll();
        
        // Không cần close ở đây vì sẽ được sử dụng trong suốt lifecycle của app
    }
    
    /**
     * Lấy instance của Application
     */
    public static VClosetApplication getInstance() {
        return instance;
    }
    
    /**
     * Lấy DbHelper instance
     */
    public DbHelper getDbHelper() {
        return dbHelper;
    }
}
