package com.devteria.identityservice.dto.response;


import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResponse {
    private long totalUsers;
    private long totalProducts;
    private long totalOrders;
    private double totalRevenue;
    private List<DailyRevenue> revenueData;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyRevenue {
        private String date; // Format: YYYY-MM-DD
        private double revenue;
    }}
