package com.devteria.identityservice.dto.request;

import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailRequest {


        Long productId;

        @Min(value = 1, message = "Quantity must be greater than 0")
        Integer quantity;

}
