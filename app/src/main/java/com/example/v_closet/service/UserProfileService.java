package com.example.v_closet.service;

import android.content.Context;

import com.example.v_closet.entity.UserProfile;
import com.example.v_closet.repository.UserProfileRepository;
import com.example.v_closet.util.InputValidation;

/**
 * Service cho UserProfile - Xử lý logic nghiệp vụ
 */
public class UserProfileService {
    private UserProfileRepository profileRepository;

    public UserProfileService(Context context) {
        this.profileRepository = new UserProfileRepository(context);
    }

    /**
     * Lấy profile theo userId
     */
    public UserProfile getProfileByUserId(int userId) {
        return profileRepository.findByUserId(userId);
    }

    /**
     * Cập nhật profile
     */
    public UpdateProfileResult updateProfile(UserProfile profile) {
        // Validate full name
        if (profile.getFullName() != null && !profile.getFullName().isEmpty()) {
            String nameError = InputValidation.validateFullName(profile.getFullName());
            if (nameError != null) {
                return new UpdateProfileResult(false, nameError);
            }
        }

        // Validate phone number
        if (profile.getPhoneNumber() != null && !profile.getPhoneNumber().isEmpty()) {
            String phoneError = InputValidation.validatePhoneNumber(profile.getPhoneNumber());
            if (phoneError != null) {
                return new UpdateProfileResult(false, phoneError);
            }
        }

        int rows = profileRepository.update(profile);
        if (rows > 0) {
            return new UpdateProfileResult(true, "Cập nhật thông tin thành công");
        } else {
            return new UpdateProfileResult(false, "Cập nhật thất bại. Vui lòng thử lại");
        }
    }

    /**
     * Tạo profile mới cho user
     */
    public CreateProfileResult createProfile(int userId, String fullName) {
        UserProfile profile = new UserProfile(userId, fullName);
        long id = profileRepository.insert(profile);
        
        if (id > 0) {
            profile.setId((int) id);
            return new CreateProfileResult(true, "Tạo profile thành công", profile);
        } else {
            return new CreateProfileResult(false, "Tạo profile thất bại", null);
        }
    }

    // Inner classes
    public static class UpdateProfileResult {
        private boolean success;
        private String message;

        public UpdateProfileResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }

    public static class CreateProfileResult {
        private boolean success;
        private String message;
        private UserProfile profile;

        public CreateProfileResult(boolean success, String message, UserProfile profile) {
            this.success = success;
            this.message = message;
            this.profile = profile;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public UserProfile getProfile() { return profile; }
    }
}
