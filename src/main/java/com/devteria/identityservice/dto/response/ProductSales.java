package com.devteria.identityservice.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSales {
    private Long productId;
    private String productName;
    private Long totalQuantity;
    private Double totalRevenue;
}
