package com.example.v_closet.service;

import android.content.Context;

import com.example.v_closet.entity.User;
import com.example.v_closet.repository.UserRepository;
import com.example.v_closet.util.InputValidation;

/**
 * Service cho User - Xử lý logic nghiệp vụ
 */
public class UserService {
    private UserRepository userRepository;

    public UserService(Context context) {
        this.userRepository = new UserRepository(context);
    }

    /**
     * Đăng ký tài khoản mới
     */
    public RegisterResult register(String username, String password, String email) {
        String validationError = validateRegistrationInput(username, password, email);
        if (validationError != null) {
            return new RegisterResult(false, validationError, null);
        }

        if (userRepository.isUsernameExists(username)) {
            return new RegisterResult(false, "Tên đăng nhập đã tồn tại", null);
        }

        if (userRepository.isEmailExists(email)) {
            return new RegisterResult(false, "Email đã được sử dụng", null);
        }

        User newUser = new User(username, password, email);
        long userId = userRepository.insert(newUser);

        if (userId > 0) {
            newUser.setId((int) userId);
            return new RegisterResult(true, "Đăng ký thành công", newUser);
        } else {
            return new RegisterResult(false, "Đăng ký thất bại. Vui lòng thử lại", null);
        }
    }

    /**
     * Validate dữ liệu đăng ký
     */
    private String validateRegistrationInput(String username, String password, String email) {
        String usernameError = InputValidation.validateUsername(username);
        if (usernameError != null) {
            return usernameError;
        }

        String passwordError = InputValidation.validatePassword(password);
        if (passwordError != null) {
            return passwordError;
        }

        String emailError = InputValidation.validateEmail(email);
        if (emailError != null) {
            return emailError;
        }

        return null;
    }

    /**
     * Đăng nhập
     */
    public LoginResult login(String username, String password) {
        if (InputValidation.isEmpty(username)) {
            return new LoginResult(false, "Tên đăng nhập không được để trống", null);
        }
        if (InputValidation.isEmpty(password)) {
            return new LoginResult(false, "Mật khẩu không được để trống", null);
        }

        User user = userRepository.findByUsername(username);
        if (user == null) {
            user = userRepository.findByEmail(username);
        }

        if (user == null) {
            return new LoginResult(false, "Tên đăng nhập hoặc mật khẩu không đúng", null);
        }
        if (!user.getPassword().equals(password)) {
            return new LoginResult(false, "Tên đăng nhập hoặc mật khẩu không đúng", null);
        }

        return new LoginResult(true, "Đăng nhập thành công", user);
    }

    /**
     * Kiểm tra email có tồn tại trong hệ thống không (cho quên mật khẩu)
     */
    public VerifyEmailResult verifyEmailForReset(String email) {
        String emailError = InputValidation.validateEmail(email);
        if (emailError != null) {
            return new VerifyEmailResult(false, emailError, null);
        }

        User user = userRepository.findByEmail(email);
        if (user == null) {
            return new VerifyEmailResult(false, "Email không tồn tại trong hệ thống", null);
        }

        return new VerifyEmailResult(true, "Email hợp lệ", user);
    }

    /**
     * Đặt lại mật khẩu
     */
    public ResetPasswordResult resetPassword(String email, String newPassword) {
        String passwordError = InputValidation.validatePassword(newPassword);
        if (passwordError != null) {
            return new ResetPasswordResult(false, passwordError);
        }

        if (!userRepository.isEmailExists(email)) {
            return new ResetPasswordResult(false, "Email không tồn tại trong hệ thống");
        }

        int rows = userRepository.updatePasswordByEmail(email, newPassword);
        if (rows > 0) {
            return new ResetPasswordResult(true, "Đặt lại mật khẩu thành công");
        } else {
            return new ResetPasswordResult(false, "Đặt lại mật khẩu thất bại. Vui lòng thử lại");
        }
    }

    /**
     * Đổi mật khẩu (khi user đã đăng nhập)
     */
    public ChangePasswordResult changePassword(int userId, String oldPassword, String newPassword) {
        String passwordError = InputValidation.validatePassword(newPassword);
        if (passwordError != null) {
            return new ChangePasswordResult(false, passwordError);
        }

        User user = userRepository.findById(userId);
        if (user == null) {
            return new ChangePasswordResult(false, "User không tồn tại");
        }

        if (!user.getPassword().equals(oldPassword)) {
            return new ChangePasswordResult(false, "Mật khẩu cũ không đúng");
        }

        int rows = userRepository.updatePasswordById(userId, newPassword);
        if (rows > 0) {
            return new ChangePasswordResult(true, "Đổi mật khẩu thành công");
        } else {
            return new ChangePasswordResult(false, "Đổi mật khẩu thất bại. Vui lòng thử lại");
        }
    }

    public boolean isUsernameAvailable(String username) {
        return !userRepository.isUsernameExists(username);
    }

    public boolean isEmailAvailable(String email) {
        return !userRepository.isEmailExists(email);
    }

    // Inner classes cho kết quả trả về
    public static class RegisterResult {
        private boolean success;
        private String message;
        private User user;

        public RegisterResult(boolean success, String message, User user) {
            this.success = success;
            this.message = message;
            this.user = user;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public User getUser() { return user; }
    }

    public static class LoginResult {
        private boolean success;
        private String message;
        private User user;

        public LoginResult(boolean success, String message, User user) {
            this.success = success;
            this.message = message;
            this.user = user;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public User getUser() { return user; }
    }

    public static class VerifyEmailResult {
        private boolean success;
        private String message;
        private User user;

        public VerifyEmailResult(boolean success, String message, User user) {
            this.success = success;
            this.message = message;
            this.user = user;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public User getUser() { return user; }
    }

    public static class ResetPasswordResult {
        private boolean success;
        private String message;

        public ResetPasswordResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }

    public static class ChangePasswordResult {
        private boolean success;
        private String message;

        public ChangePasswordResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }
}
