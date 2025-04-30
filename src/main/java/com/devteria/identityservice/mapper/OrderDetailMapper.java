package com.devteria.identityservice.mapper;

import com.devteria.identityservice.dto.response.OrderDetailResponse;
import com.devteria.identityservice.entity.OrderDetail;
import org.springframework.stereotype.Component;

@Component
public class OrderDetailMapper {

    public OrderDetailResponse toResponse(OrderDetail orderDetail) {
        return OrderDetailResponse.builder()
                .productId(orderDetail.getProduct().getId())
                .productName(orderDetail.getProductName())
                .quantity(orderDetail.getQuantity())
                .unitPrice(orderDetail.getUnitPrice())
                .subtotal(orderDetail.getSubtotal())
                .build();
    }
}
