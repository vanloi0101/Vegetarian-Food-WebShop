package com.devteria.identityservice.service;

import com.devteria.identityservice.dto.request.OrderDetailRequest;
import com.devteria.identityservice.dto.response.OrderDetailResponse;
import com.devteria.identityservice.entity.Order;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetailResponse> createOrderDetails(List<OrderDetailRequest> requests, Order order);

    List<OrderDetailResponse> getOrderDetailsByOrderId(Long orderId);

}
