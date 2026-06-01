package com.devteria.identityservice.service;

import com.devteria.identityservice.dto.request.OrderRequest;
import com.devteria.identityservice.dto.request.OrderDetailRequest;
import com.devteria.identityservice.dto.response.OrderResponse;
import com.devteria.identityservice.entity.Order;
import com.devteria.identityservice.entity.OrderDetail;
import com.devteria.identityservice.entity.Product;
import com.devteria.identityservice.entity.User;
import com.devteria.identityservice.exception.AppException;
import com.devteria.identityservice.exception.ErrorCode;
import com.devteria.identityservice.mapper.OrderMapper;
import com.devteria.identityservice.repository.OrderDetailRepository;
import com.devteria.identityservice.repository.OrderRepository;
import com.devteria.identityservice.repository.ProductRepository;
import com.devteria.identityservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    OrderRepository orderRepository;
    OrderDetailRepository orderDetailRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    OrderMapper orderMapper;
    EmailService emailService;

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        // 1. Lấy User
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // 2. Validate products và tính total
        List<OrderDetail> orderDetails = new ArrayList<>();
        double totalAmount = 0;

        for (OrderDetailRequest detailRequest : request.getOrderDetails()) {
            Product product = productRepository.findById(detailRequest.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

            if (product.getIsAvailable() == null || !product.getIsAvailable()) {
                throw new AppException(ErrorCode.PRODUCT_NOT_AVAILABLE);
            }

            // Gán productName từ product
            OrderDetail orderDetail = OrderDetail.builder()
                    .product(product)
                    .productName(product.getName())  // Gán productName từ Product entity
                    .quantity(detailRequest.getQuantity())
                    .unitPrice(BigDecimal.valueOf(product.getPrice()))
                    .subtotal(BigDecimal.valueOf(product.getPrice() * detailRequest.getQuantity()))
                    .createdAt(new Date())
                    .build();

            orderDetails.add(orderDetail);
            totalAmount += product.getPrice() * detailRequest.getQuantity();
        }

        // 3. Tạo và save Order
        Order order = orderMapper.toOrder(request);
        order.setUser(user);
        order.setTotalAmount(totalAmount);
        order.setStatus("PENDING");
        order.setPaymentStatus("PENDING");
        order.setCreatedAt(new Date());

        // Gán order cho từng orderDetail
        for (OrderDetail detail : orderDetails) {
            detail.setOrder(order);
        }
        order.setOrderDetails(orderDetails);

        order = orderRepository.save(order); // Chỉ cần save order, Hibernate cascade tự save orderDetails

        // 4. Gửi email
        emailService.sendOrderConfirmationEmail(user.getUsername(), order);

        return orderMapper.toOrderResponse(order);
    }

    public OrderResponse updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        order.setStatus(status);
        order.setUpdatedAt(new Date());
        orderRepository.save(order);
        return orderMapper.toOrderResponse(order);
    }
    public List<OrderResponse> getUserOrders() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return orderRepository.findByUserId(user.getId()).stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }
}