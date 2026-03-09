package com.example.v_closet.controller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.v_closet.R;
import com.example.v_closet.entity.UserProfile;
import com.example.v_closet.repository.UserRepository;
import com.example.v_closet.service.UserProfileService;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * ProfileActivity - Màn hình thông tin cá nhân
 */
public class ProfileActivity extends AppCompatActivity {
    
    private int userId;
    private String username;
    private String email;
    private UserProfile currentProfile;
    
    private UserProfileService profileService;
    private UserRepository userRepository;
    
    private ImageView imgAvatar;
    private TextView tvUsername;
    private TextView tvEmail;
    private TextView tvFullName;
    private TextView tvPhoneNumber;
    private TextView tvGender;
    private TextView tvBirthday;
    private Button btnEditProfile;
    private Button btnChangePassword;
    private Button btnAddress;
    private Button btnOrderHistory;
    private Button btnWishList;
    private Button btnLogout;
    private ImageButton btnBack;
    
    // ActivityResultLauncher cho chọn ảnh
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        
        // Khởi tạo services
        profileService = new UserProfileService(this);
        userRepository = new UserRepository(this);
        
        // Setup ActivityResultLauncher
        setupActivityResultLaunchers();
        
        // Lấy thông tin user từ Intent
        getUserInfoFromIntent();
        
        // Ánh xạ views
        initViews();
        
        // Load và hiển thị thông tin user
        loadUserInfo();
        
        // Setup button listeners
        setupButtonListeners();
    }
    
    /**
     * Setup ActivityResultLaunchers
     */
    private void setupActivityResultLaunchers() {
        // Launcher cho chọn ảnh
        pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        // Hiển thị ảnh đã chọn
                        imgAvatar.setImageURI(imageUri);
                        
                        // Lưu URI vào profile
                        saveAvatarUri(imageUri.toString());
                    }
                }
            }
        );
        
        // Launcher cho request permission
        requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    openImagePicker();
                } else {
                    Toast.makeText(this, "Cần cấp quyền để chọn ảnh", Toast.LENGTH_SHORT).show();
                }
            }
        );
    }
    
    private void getUserInfoFromIntent() {
        userId = getIntent().getIntExtra("user_id", 0);
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        
        if (userId == 0 || username == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    
    private void initViews() {
        imgAvatar = findViewById(R.id.imgAvatar);
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvFullName = findViewById(R.id.tvFullName);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvGender = findViewById(R.id.tvGender);
        tvBirthday = findViewById(R.id.tvBirthday);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnAddress = findViewById(R.id.btnAddress);
        btnOrderHistory = findViewById(R.id.btnOrderHistory);
        btnWishList = findViewById(R.id.btnWishList);
        btnLogout = findViewById(R.id.btnLogout);
        btnBack = findViewById(R.id.btn_back);
    }
    
    private void loadUserInfo() {
        if (tvUsername != null) tvUsername.setText(username);
        if (tvEmail != null) tvEmail.setText(email);
        
        currentProfile = profileService.getProfileByUserId(userId);
        if (currentProfile != null) {
            displayProfileInfo(currentProfile);
        } else {
            displayDefaultInfo();
        }
    }
    
    private void displayProfileInfo(UserProfile profile) {
        // Hiển thị avatar
        if (imgAvatar != null && profile.getAvatarUrl() != null) {
            if (profile.getAvatarUrl().startsWith("content://") || 
                profile.getAvatarUrl().startsWith("file://")) {
                // URI từ storage
                imgAvatar.setImageURI(Uri.parse(profile.getAvatarUrl()));
            } else {
                // Resource từ drawable
                int resourceId = getResources().getIdentifier(
                    profile.getAvatarUrl(), "drawable", getPackageName());
                imgAvatar.setImageResource(resourceId != 0 ? resourceId : R.drawable.img_default_avatar);
            }
        }
        
        if (tvFullName != null) {
            tvFullName.setText(profile.getFullName() != null && !profile.getFullName().isEmpty() 
                ? profile.getFullName() : "Chưa cập nhật");
        }
        
        if (tvPhoneNumber != null) {
            tvPhoneNumber.setText(profile.getPhoneNumber() != null && !profile.getPhoneNumber().isEmpty() 
                ? profile.getPhoneNumber() : "Chưa cập nhật");
        }
        
        if (tvGender != null) {
            tvGender.setText(profile.getGender() != null && !profile.getGender().isEmpty() 
                ? profile.getGender() : "Chưa cập nhật");
        }
        
        if (tvBirthday != null) {
            if (profile.getBirthday() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("vi", "VN"));
                tvBirthday.setText(sdf.format(profile.getBirthday()));
            } else {
                tvBirthday.setText("Chưa cập nhật");
            }
        }
    }
    
    private void displayDefaultInfo() {
        if (imgAvatar != null) imgAvatar.setImageResource(R.drawable.img_default_avatar);
        if (tvFullName != null) tvFullName.setText("Chưa cập nhật");
        if (tvPhoneNumber != null) tvPhoneNumber.setText("Chưa cập nhật");
        if (tvGender != null) tvGender.setText("Chưa cập nhật");
        if (tvBirthday != null) tvBirthday.setText("Chưa cập nhật");
    }
    
    private void setupButtonListeners() {
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
        
        // Click vào avatar để chọn ảnh
        if (imgAvatar != null) {
            imgAvatar.setOnClickListener(v -> handleAvatarClick());
        }
        
        if (btnEditProfile != null) {
                    btnEditProfile.setOnClickListener(v -> handleEditProfile());
        }
        
        if (btnChangePassword != null) {
            btnChangePassword.setOnClickListener(v -> handleChangePassword());
        }
        
        if (btnAddress != null) {
            btnAddress.setOnClickListener(v -> handleAddress());
        }
        
        if (btnOrderHistory != null) {
            btnOrderHistory.setOnClickListener(v -> handleOrderHistory());
        }
        
        if (btnWishList != null) {
            btnWishList.setOnClickListener(v -> handleWishlist());
        }
        
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> handleLogout());
        }
    }
    
    /**
     * Xử lý khi click vào avatar
     */
    private void handleAvatarClick() {
        // Kiểm tra permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ sử dụng READ_MEDIA_IMAGES
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
            } else {
                openImagePicker();
            }
        } else {
            // Android 12 trở xuống sử dụng READ_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                openImagePicker();
            }
        }
    }
    
    /**
     * Mở image picker
     */
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
    }
    
    /**
     * Lưu avatar URI vào database
     */
    private void saveAvatarUri(String uri) {
        if (currentProfile == null) {
            // Tạo profile mới nếu chưa có
            UserProfileService.CreateProfileResult result = 
                profileService.createProfile(userId, username);
            if (result.isSuccess()) {
                currentProfile = result.getProfile();
            } else {
                Toast.makeText(this, "Không thể lưu avatar", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        
        // Cập nhật avatar URL
        currentProfile.setAvatarUrl(uri);
        UserProfileService.UpdateProfileResult result = profileService.updateProfile(currentProfile);
        
        if (result.isSuccess()) {
            Toast.makeText(this, "Đã cập nhật avatar", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Xử lý chuyển sang màn hình chỉnh sửa profile
     */
    private void handleEditProfile() {
        Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("username", username);
        startActivity(intent);
    }
    
    /**
     * Xử lý chuyển sang màn hình đổi mật khẩu
     */
    private void handleChangePassword() {
        Intent intent = new Intent(ProfileActivity.this, EditPasswordActivity.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("email", email);
        intent.putExtra("from_profile", true);
        startActivity(intent);
    }
    
    /**
     * Xử lý chuyển sang màn hình địa chỉ
     */
    private void handleAddress() {
        Intent intent = new Intent(ProfileActivity.this, AddressListActivity.class);
        intent.putExtra("user_id", userId);
        startActivity(intent);
    }
    
    /**
     * Xử lý chuyển sang màn hình lịch sử mua hàng
     */
    private void handleOrderHistory() {
        Intent intent = new Intent(ProfileActivity.this, OrderHistoryActivity.class);
        intent.putExtra("user_id", userId);
        startActivity(intent);
    }
    
    /**
     * Xử lý chuyển sang màn hình wishlist
     */
    private void handleWishlist() {
        Intent intent = new Intent(ProfileActivity.this, WishlistActivity.class);
        intent.putExtra("user_id", userId);
        startActivity(intent);
    }
    
    private void handleLogout() {
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
        finish();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadUserInfo();
    }
}
