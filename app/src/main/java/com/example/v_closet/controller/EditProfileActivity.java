package com.example.v_closet.controller;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.v_closet.R;
import com.example.v_closet.entity.UserProfile;
import com.example.v_closet.service.UserProfileService;
import com.example.v_closet.util.InputValidation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * EditProfileActivity - Màn hình chỉnh sửa thông tin cá nhân
 */
public class EditProfileActivity extends AppCompatActivity {
    
    private int userId;
    private String username;
    private UserProfile currentProfile;
    
    private UserProfileService profileService;
    
    private EditText edtFullName;
    private EditText edtPhoneNumber;
    private RadioGroup rgGender;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private RadioButton rbOther;
    private EditText edtBirthday;
    private Button btnSave;
    private ImageButton btnBack;
    
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("vi", "VN"));
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        
        // Khởi tạo service
        profileService = new UserProfileService(this);
        
        // Lấy thông tin user từ Intent
        getUserInfoFromIntent();
        
        // Ánh xạ views
        initViews();
        
        // Load thông tin hiện tại
        loadCurrentProfile();
        
        // Setup listeners
        setupListeners();
    }
    
    private void getUserInfoFromIntent() {
        userId = getIntent().getIntExtra("user_id", 0);
        username = getIntent().getStringExtra("username");
        
        if (userId == 0 || username == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    
    private void initViews() {
        edtFullName = findViewById(R.id.edtFullName);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        rgGender = findViewById(R.id.rgGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        rbOther = findViewById(R.id.rbOther);
        edtBirthday = findViewById(R.id.edtBirthday);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btn_back);
    }
    
    /**
     * Load thông tin profile hiện tại
     */
    private void loadCurrentProfile() {
        currentProfile = profileService.getProfileByUserId(userId);
        
        if (currentProfile != null) {
            // Hiển thị họ tên
            if (edtFullName != null && currentProfile.getFullName() != null) {
                edtFullName.setText(currentProfile.getFullName());
            }
            
            // Hiển thị số điện thoại
            if (edtPhoneNumber != null && currentProfile.getPhoneNumber() != null) {
                edtPhoneNumber.setText(currentProfile.getPhoneNumber());
            }
            
            // Hiển thị giới tính
            if (currentProfile.getGender() != null) {
                switch (currentProfile.getGender()) {
                    case "Nam":
                        if (rbMale != null) rbMale.setChecked(true);
                        break;
                    case "Nữ":
                        if (rbFemale != null) rbFemale.setChecked(true);
                        break;
                    case "Khác":
                        if (rbOther != null) rbOther.setChecked(true);
                        break;
                }
            }
            
            // Hiển thị ngày sinh
            if (edtBirthday != null && currentProfile.getBirthday() != null) {
                edtBirthday.setText(dateFormat.format(currentProfile.getBirthday()));
            }
        }
    }
    
    private void setupListeners() {
        // Nút back
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
        
        // Click vào trường ngày sinh để mở DatePicker
        if (edtBirthday != null) {
            edtBirthday.setOnClickListener(v -> showDatePicker());
        }
        
        // Nút lưu
        if (btnSave != null) {
            btnSave.setOnClickListener(v -> handleSaveProfile());
        }
    }
    
    /**
     * Hiển thị DatePicker để chọn ngày sinh
     */
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        
        // Nếu đã có ngày sinh, set làm ngày mặc định
        if (currentProfile != null && currentProfile.getBirthday() != null) {
            calendar.setTime(currentProfile.getBirthday());
        }
        
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            (view, selectedYear, selectedMonth, selectedDay) -> {
                calendar.set(selectedYear, selectedMonth, selectedDay);
                edtBirthday.setText(dateFormat.format(calendar.getTime()));
            },
            year, month, day
        );
        
        // Set ngày tối đa là hôm nay
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        
        datePickerDialog.show();
    }
    
    /**
     * Xử lý lưu thông tin profile
     */
    private void handleSaveProfile() {
        String fullName = edtFullName.getText().toString().trim();
        String phoneNumber = edtPhoneNumber.getText().toString().trim();
        String birthdayStr = edtBirthday.getText().toString().trim();
        
        // Validate họ tên (bắt buộc)
        if (InputValidation.isEmpty(fullName)) {
            edtFullName.setError("Họ tên không được để trống");
            edtFullName.requestFocus();
            return;
        }
        
        String nameError = InputValidation.validateFullName(fullName);
        if (nameError != null) {
            edtFullName.setError(nameError);
            edtFullName.requestFocus();
            return;
        }
        
        // Validate số điện thoại (nếu có nhập)
        if (!InputValidation.isEmpty(phoneNumber)) {
            String phoneError = InputValidation.validatePhoneNumber(phoneNumber);
            if (phoneError != null) {
                edtPhoneNumber.setError(phoneError);
                edtPhoneNumber.requestFocus();
                return;
            }
        }
        
        // Lấy giới tính
        String gender = null;
        int selectedGenderId = rgGender.getCheckedRadioButtonId();
        if (selectedGenderId == R.id.rbMale) {
            gender = "Nam";
        } else if (selectedGenderId == R.id.rbFemale) {
            gender = "Nữ";
        } else if (selectedGenderId == R.id.rbOther) {
            gender = "Khác";
        }
        
        // Parse ngày sinh
        Date birthday = null;
        if (!InputValidation.isEmpty(birthdayStr)) {
            try {
                birthday = dateFormat.parse(birthdayStr);
            } catch (ParseException e) {
                edtBirthday.setError("Định dạng ngày không hợp lệ");
                edtBirthday.requestFocus();
                return;
            }
        }
        
        // Disable button để tránh click nhiều lần
        btnSave.setEnabled(false);
        
        // Tạo hoặc cập nhật profile
        if (currentProfile == null) {
            // Tạo profile mới
            UserProfileService.CreateProfileResult createResult = 
                profileService.createProfile(userId, fullName);
            
            if (!createResult.isSuccess()) {
                Toast.makeText(this, createResult.getMessage(), Toast.LENGTH_SHORT).show();
                btnSave.setEnabled(true);
                return;
            }
            
            currentProfile = createResult.getProfile();
        }
        
        // Cập nhật thông tin
        currentProfile.setFullName(fullName);
        currentProfile.setPhoneNumber(InputValidation.isEmpty(phoneNumber) ? null : phoneNumber);
        currentProfile.setGender(gender);
        currentProfile.setBirthday(birthday);
        currentProfile.setUpdatedAt(new Date());
        
        // Lưu vào database
        UserProfileService.UpdateProfileResult result = profileService.updateProfile(currentProfile);
        
        if (result.isSuccess()) {
            Toast.makeText(this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
            
            // Quay lại màn hình Profile
            finish();
        } else {
            Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();
            btnSave.setEnabled(true);
        }
    }
}
