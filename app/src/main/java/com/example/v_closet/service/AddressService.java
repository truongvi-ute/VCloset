package com.example.v_closet.service;

import android.content.Context;

import com.example.v_closet.entity.Address;
import com.example.v_closet.repository.AddressRepository;
import com.example.v_closet.util.InputValidation;

import java.util.List;

/**
 * Service cho Address - Xử lý logic nghiệp vụ
 */
public class AddressService {
    private AddressRepository addressRepository;

    public AddressService(Context context) {
        this.addressRepository = new AddressRepository(context);
    }

    /**
     * Thêm địa chỉ mới
     */
    public AddResult addAddress(Address address) {
        // Validate
        String error = validateAddress(address);
        if (error != null) {
            return new AddResult(false, error, null);
        }
        
        long id = addressRepository.insert(address);
        if (id > 0) {
            address.setId((int) id);
            return new AddResult(true, "Thêm địa chỉ thành công", address);
        } else {
            return new AddResult(false, "Thêm địa chỉ thất bại", null);
        }
    }

    /**
     * Cập nhật địa chỉ
     */
    public UpdateResult updateAddress(Address address) {
        // Validate
        String error = validateAddress(address);
        if (error != null) {
            return new UpdateResult(false, error);
        }
        
        int rows = addressRepository.update(address);
        if (rows > 0) {
            return new UpdateResult(true, "Cập nhật địa chỉ thành công");
        } else {
            return new UpdateResult(false, "Cập nhật địa chỉ thất bại");
        }
    }

    /**
     * Xóa địa chỉ
     */
    public boolean deleteAddress(int addressId) {
        int rows = addressRepository.delete(addressId);
        return rows > 0;
    }

    /**
     * Lấy tất cả địa chỉ của user
     */
    public List<Address> getUserAddresses(int userId) {
        return addressRepository.findByUserId(userId);
    }

    /**
     * Lấy địa chỉ mặc định
     */
    public Address getDefaultAddress(int userId) {
        return addressRepository.getDefaultAddress(userId);
    }

    /**
     * Set địa chỉ làm mặc định
     */
    public boolean setDefaultAddress(int addressId, int userId) {
        int rows = addressRepository.setDefaultAddress(addressId, userId);
        return rows > 0;
    }

    /**
     * Validate địa chỉ
     */
    private String validateAddress(Address address) {
        if (InputValidation.isEmpty(address.getReceiverName())) {
            return "Tên người nhận không được để trống";
        }
        
        String phoneError = InputValidation.validatePhoneNumber(address.getPhoneNumber());
        if (phoneError != null) {
            return phoneError;
        }
        
        if (InputValidation.isEmpty(address.getStreet())) {
            return "Số nhà, tên đường không được để trống";
        }
        
        if (InputValidation.isEmpty(address.getWard())) {
            return "Phường/Xã không được để trống";
        }
        
        if (InputValidation.isEmpty(address.getDistrict())) {
            return "Quận/Huyện không được để trống";
        }
        
        if (InputValidation.isEmpty(address.getCity())) {
            return "Tỉnh/Thành phố không được để trống";
        }
        
        return null;
    }

    // Inner classes
    public static class AddResult {
        private boolean success;
        private String message;
        private Address address;

        public AddResult(boolean success, String message, Address address) {
            this.success = success;
            this.message = message;
            this.address = address;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Address getAddress() { return address; }
    }

    public static class UpdateResult {
        private boolean success;
        private String message;

        public UpdateResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }
}
