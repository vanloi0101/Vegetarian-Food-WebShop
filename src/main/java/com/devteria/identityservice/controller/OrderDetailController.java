package com.devteria.identityservice.controller;

import com.devteria.identityservice.dto.request.OrderDetailRequest;
import com.devteria.identityservice.dto.response.OrderDetailResponse;
import com.devteria.identityservice.entity.Order;
import com.devteria.identityservice.repository.OrderRepository;
import com.devteria.identityservice.service.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-details")
@RequiredArgsConstructor
public class OrderDetailController {

    private final OrderDetailService orderDetailService;
    private final OrderRepository orderRepository;

    // Tạo các order detail gắn với 1 đơn hàng cụ thể
    @PostMapping("/order/{orderId}")
    public ResponseEntity<List<OrderDetailResponse>> createOrderDetails(
            @PathVariable Long orderId,
            @RequestBody @Valid List<OrderDetailRequest> requests
    ) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        List<OrderDetailResponse> response = orderDetailService.createOrderDetails(requests, order);
        return ResponseEntity.ok(response);
    }

    // Lấy các order detail theo orderId
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderDetailResponse>> getOrderDetailsByOrderId(
            @PathVariable Long orderId
    ) {
        List<OrderDetailResponse> responses = orderDetailService.getOrderDetailsByOrderId(orderId);
        return ResponseEntity.ok(responses);
    }

}
