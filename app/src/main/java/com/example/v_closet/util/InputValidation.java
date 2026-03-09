package com.example.v_closet.util;

import android.util.Patterns;

/**
 * Utility class cho việc validate input
 */
public class InputValidation {
    
    // Các hằng số cho validation
    private static final int USERNAME_MIN_LENGTH = 3;
    private static final int USERNAME_MAX_LENGTH = 20;
    private static final int PASSWORD_MIN_LENGTH = 6;
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9_]+$";

    /**
     * Kiểm tra chuỗi có rỗng không
     */
    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    /**
     * Validate email
     * @param email Email cần validate
     * @return null nếu hợp lệ, message lỗi nếu không hợp lệ
     */
    public static String validateEmail(String email) {
        if (isEmpty(email)) {
            return "Email không được để trống";
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Email không hợp lệ";
        }
        return null;
    }

    /**
     * Kiểm tra email có hợp lệ không (boolean)
     */
    public static boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Validate username
     * @param username Username cần validate
     * @return null nếu hợp lệ, message lỗi nếu không hợp lệ
     */
    public static String validateUsername(String username) {
        if (isEmpty(username)) {
            return "Tên đăng nhập không được để trống";
        }
        if (username.length() < USERNAME_MIN_LENGTH) {
            return "Tên đăng nhập phải có ít nhất " + USERNAME_MIN_LENGTH + " ký tự";
        }
        if (username.length() > USERNAME_MAX_LENGTH) {
            return "Tên đăng nhập không được quá " + USERNAME_MAX_LENGTH + " ký tự";
        }
        if (!username.matches(USERNAME_PATTERN)) {
            return "Tên đăng nhập chỉ được chứa chữ cái, số và dấu gạch dưới";
        }
        return null;
    }

    /**
     * Kiểm tra username có hợp lệ không (boolean)
     */
    public static boolean isValidUsername(String username) {
        return username != null 
            && username.length() >= USERNAME_MIN_LENGTH 
            && username.length() <= USERNAME_MAX_LENGTH
            && username.matches(USERNAME_PATTERN);
    }

    /**
     * Validate confirm password
     * @param password Password gốc
     * @param confirmPassword Password xác nhận
     * @return null nếu hợp lệ, message lỗi nếu không hợp lệ
     */
    public static String validateConfirmPassword(String password, String confirmPassword) {
        if (isEmpty(confirmPassword)) {
            return "Vui lòng xác nhận mật khẩu";
        }
        if (!password.equals(confirmPassword)) {
            return "Mật khẩu xác nhận không khớp";
        }
        return null;
    }

    /**
     * Validate phone number
     * @param phoneNumber Số điện thoại cần validate
     * @return null nếu hợp lệ, message lỗi nếu không hợp lệ
     */
    public static String validatePhoneNumber(String phoneNumber) {
        if (isEmpty(phoneNumber)) {
            return "Số điện thoại không được để trống";
        }
        // Regex cho số điện thoại Việt Nam (10-11 số, bắt đầu bằng 0)
        if (!phoneNumber.matches("^0[0-9]{9,10}$")) {
            return "Số điện thoại không hợp lệ";
        }
        return null;
    }

    /**
     * Kiểm tra phone number có hợp lệ không (boolean)
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("^0[0-9]{9,10}$");
    }

    /**
     * Validate full name
     * @param fullName Họ tên cần validate
     * @return null nếu hợp lệ, message lỗi nếu không hợp lệ
     */
    public static String validateFullName(String fullName) {
        if (isEmpty(fullName)) {
            return "Họ tên không được để trống";
        }
        if (fullName.trim().length() < 2) {
            return "Họ tên phải có ít nhất 2 ký tự";
        }
        return null;
    }

    /**
     * Validate OTP code
     * @param otp Mã OTP cần validate
     * @return null nếu hợp lệ, message lỗi nếu không hợp lệ
     */
    public static String validateOTP(String otp) {
        if (isEmpty(otp)) {
            return "Mã OTP không được để trống";
        }
        if (!otp.matches("^[0-9]{6}$")) {
            return "Mã OTP phải là 6 chữ số";
        }
        return null;
    }

    /**
     * Validate password
     * @param password Password cần validate
     * @return null nếu hợp lệ, message lỗi nếu không hợp lệ
     */
    public static String validatePassword(String password) {
        if (isEmpty(password)) {
            return "Mật khẩu không được để trống";
        }
        if (password.length() < PASSWORD_MIN_LENGTH) {
            return "Mật khẩu phải có ít nhất " + PASSWORD_MIN_LENGTH + " ký tự";
        }
        return null;
    }

    /**
     * Kiểm tra password có hợp lệ không (boolean)
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= PASSWORD_MIN_LENGTH;
    }
}
