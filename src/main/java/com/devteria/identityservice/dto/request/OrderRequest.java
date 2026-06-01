package com.devteria.identityservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    @NotBlank(message = "Shipping address cannot be blank")
    String shippingAddress;

    @NotBlank(message = "Payment method cannot be blank")
    String paymentMethod;

    @NotEmpty(message = "Order details cannot be empty")
    List<OrderDetailRequest> orderDetails;
}

