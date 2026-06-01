package com.devteria.identityservice.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {
    private Double totalRevenue;
    private List<ProductSales> topProducts;

//    @Data
//    @Builder
//    @NoArgsConstructor
//// Thêm annotation này
//    public static class ProductSales {
//        private Long productId;
//        private String productName;
//        private Long totalQuantity;
//        private Double totalRevenue;
//
//        // Constructor cho JPQL result mapping
//        public ProductSales(Long productId, String productName, Long totalQuantity, Double totalRevenue) {
//            this.productId = productId;
//            this.productName = productName;
//            this.totalQuantity = totalQuantity;
//            this.totalRevenue = totalRevenue;
//        }
//    }
}
