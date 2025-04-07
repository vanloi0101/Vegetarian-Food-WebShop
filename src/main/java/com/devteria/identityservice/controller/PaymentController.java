package com.devteria.identityservice.controller;

import com.devteria.identityservice.dto.request.ApiResponse;
import com.devteria.identityservice.service.OrderService;
import com.devteria.identityservice.service.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PaymentController {
    PaymentService paymentService;
    OrderService orderService;

    @GetMapping("/create")
    public ApiResponse<String> createPaymentUrl(@RequestParam Long orderId) throws UnsupportedEncodingException {
        return ApiResponse.<String>builder()
                .result(paymentService.createPaymentUrl(orderId))
                .build();
    }

    @GetMapping("/return")
    public ApiResponse<String> handlePaymentReturn(
            @RequestParam("vnp_ResponseCode") String responseCode,
            @RequestParam("vnp_TxnRef") String orderId) {
        if ("00".equals(responseCode)) {
            orderService.updateOrderStatus(Long.parseLong(orderId), "PROCESSING");
            return ApiResponse.<String>builder()
                    .result("Payment successful for order: " + orderId)
                    .build();
        } else {
            orderService.updateOrderStatus(Long.parseLong(orderId), "CANCELLED");
            return ApiResponse.<String>builder()
                    .result("Payment failed for order: " + orderId)
                    .build();
        }
    }

    @GetMapping("/query")
    public ApiResponse<String> queryPayment(
            @RequestParam String orderId,
            @RequestParam String transDate) throws Exception {
        return ApiResponse.<String>builder()
                .result(paymentService.queryPayment(orderId, transDate))
                .build();
    }

    @PostMapping("/refund")
    public ApiResponse<String> refundPayment(
            @RequestParam String orderId,
            @RequestParam String transDate,
            @RequestParam String amount,
            @RequestParam String user) throws Exception {
        return ApiResponse.<String>builder()
                .result(paymentService.refundPayment(orderId, transDate, amount, user))
                .build();
    }
}