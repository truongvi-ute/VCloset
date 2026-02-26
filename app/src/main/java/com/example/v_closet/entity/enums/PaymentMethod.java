package com.example.v_closet.entity.enums;

/**
 * Enum đại diện cho các phương thức thanh toán được hỗ trợ
 */
public enum PaymentMethod {
    COD,            // Cash on Delivery - Thanh toán khi nhận hàng
    BANK_TRANSFER,  // Chuyển khoản ngân hàng
    E_WALLET,       // Ví điện tử (MoMo, ZaloPay, VNPay...)
    CREDIT_CARD,    // Thẻ tín dụng/ghi nợ
    PAYPAL          // PayPal (nếu có)
}
