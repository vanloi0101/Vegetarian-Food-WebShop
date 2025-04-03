package com.devteria.identityservice.service;

import com.devteria.identityservice.configuration.Config;
import com.devteria.identityservice.configuration.VNPayUtil;
import com.devteria.identityservice.entity.Order;
import com.devteria.identityservice.repository.OrderRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PaymentService {

    @Value("${vnpay.tmnCode:WQLVBKW6}")
    String tmnCode;

    @Value("${vnpay.hashSecret:013XTWHQVJC9R5JQY20U3C9V14CEVSRI}")
    String hashSecret;

    @Value("${vnpay.url:https://sandbox.vnpayment.vn/paymentv2/vpcpay.html}")
    String vnpayUrl;

    @Value("${vnpay.returnUrl:http://localhost:8080/payment/return}")
    String returnUrl;

    @Value("${vnpay.apiUrl:https://sandbox.vnpayment.vn/merchant_webapi/api/transaction}")
    String apiUrl;

    OrderRepository orderRepository;

    public String createPaymentUrl(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        String vnp_TxnRef = String.valueOf(orderId);
        String vnp_Amount = String.valueOf((long) (order.getTotalAmount() * 100));
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_OrderInfo = "Thanh toan don hang " + vnp_TxnRef;
        String vnp_Locale = "vn";
        String vnp_CurrCode = "VND";
        String vnp_IpAddr = "127.0.0.1"; // Thay bằng IP thực tế của client

        // Tạo chuỗi dữ liệu để tính checksum
        String data = vnp_Version + "|" + vnp_Command + "|" + tmnCode + "|" + vnp_TxnRef + "|" +
                vnp_Amount + "|" + vnp_CurrCode + "|" + vnp_OrderInfo + "|" + vnp_Locale + "|" +
                vnp_IpAddr + "|" + returnUrl;
        String secureHash;
        try {
            secureHash = VNPayUtil.hmacSHA512(data, hashSecret);
        } catch (Exception e) {
            throw new RuntimeException("Error creating payment URL", e);
        }

        // Tạo URL thanh toán
        return vnpayUrl + "?vnp_Version=" + vnp_Version + "&vnp_Command=" + vnp_Command +
                "&vnp_TmnCode=" + tmnCode + "&vnp_Amount=" + vnp_Amount + "&vnp_CurrCode=" + vnp_CurrCode +
                "&vnp_TxnRef=" + vnp_TxnRef + "&vnp_OrderInfo=" + vnp_OrderInfo + "&vnp_Locale=" + vnp_Locale +
                "&vnp_ReturnUrl=" + returnUrl + "&vnp_IpAddr=" + vnp_IpAddr + "&vnp_SecureHash=" + secureHash;
    }

    public String queryPayment(String orderId, String transDate) throws Exception {
        // Logic truy vấn thanh toán
        return "query-result";
    }

    public String refundPayment(String orderId, String transDate, String amount, String user) throws Exception {
        log.info("Processing refund for orderId: {}, transDate: {}, amount: {}, user: {}",
                orderId, transDate, amount, user);

        String vnp_RequestId = Config.getRandomNumber(8);
        String vnp_Version = "2.1.0";
        String vnp_Command = "refund";
        String vnp_TxnRef = orderId;
        String vnp_Amount = amount;
        String vnp_TransactionDate = transDate;
        String vnp_CreateBy = user;

        String data = vnp_RequestId + "|" + vnp_Version + "|" + vnp_Command + "|" + tmnCode + "|" +
                vnp_TxnRef + "|" + vnp_Amount + "|" + vnp_TransactionDate + "|" + vnp_CreateBy;
        String hash = VNPayUtil.hmacSHA512(data, hashSecret);

        String requestUrl = apiUrl + "?vnp_RequestId=" + vnp_RequestId + "&vnp_Version=" + vnp_Version +
                "&vnp_Command=" + vnp_Command + "&vnp_TmnCode=" + tmnCode +
                "&vnp_TxnRef=" + vnp_TxnRef + "&vnp_Amount=" + vnp_Amount +
                "&vnp_TransactionDate=" + vnp_TransactionDate + "&vnp_CreateBy=" + vnp_CreateBy +
                "&vnp_SecureHash=" + hash;

        return "Refund processed for order: " + orderId;
    }
}