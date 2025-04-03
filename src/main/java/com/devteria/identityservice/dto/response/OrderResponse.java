package com.devteria.identityservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    Long id;
    String shippingAddress;
    String paymentMethod;
    String status;
    Double totalAmount;
    Date createdAt;
    List<OrderDetailResponse> orderDetails;
}