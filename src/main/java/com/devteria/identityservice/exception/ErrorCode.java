package com.devteria.identityservice.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // Lỗi chung
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid key", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST(1009, "Invalid request", HttpStatus.BAD_REQUEST),

    // Lỗi liên quan đến người dùng
    USER_EXISTED(1002, "User already exists", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME(1004, "Username is invalid", HttpStatus.BAD_REQUEST),  // Thêm lỗi này vào
    INVALID_PASSWORD(1005, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1006, "User does not exist", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1007, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1008, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1009, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),

    // Lỗi liên quan đến sản phẩm
    PRODUCT_NOT_FOUND(1010, "Product does not exist", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_AVAILABLE(1011, "Product is not available", HttpStatus.BAD_REQUEST),
    INVALID_PRODUCT_DATA(1012, "Invalid product data", HttpStatus.BAD_REQUEST),

    // Lỗi liên quan đến đơn hàng
    ORDER_NOT_FOUND(1013, "Order does not exist", HttpStatus.NOT_FOUND),
    INVALID_ORDER_DATA(1014, "Invalid order data", HttpStatus.BAD_REQUEST),

    // Lỗi liên quan đến thanh toán
    PAYMENT_FAILED(1015, "Payment failed", HttpStatus.BAD_REQUEST),
    INVALID_PAYMENT_REQUEST(1016, "Invalid payment request", HttpStatus.BAD_REQUEST),

    // Lỗi liên quan đến báo cáo
    INVALID_REPORT_DATA(1017, "Invalid report date range", HttpStatus.BAD_REQUEST);

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}