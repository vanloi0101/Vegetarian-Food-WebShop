package com.devteria.identityservice.service;

import com.devteria.identityservice.dto.request.ReportRequest;
import com.devteria.identityservice.dto.response.DashboardStatsResponse;
import com.devteria.identityservice.dto.response.ProductSales;
import com.devteria.identityservice.dto.response.ReportResponse;
import com.devteria.identityservice.exception.AppException;
import com.devteria.identityservice.exception.ErrorCode;
import com.devteria.identityservice.repository.OrderRepository;
import com.devteria.identityservice.repository.ProductRepository;
import com.devteria.identityservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportService {
    OrderRepository orderRepository;
    UserRepository userRepository;
    ProductRepository productRepository;
    JasperReportService jasperReportService;
    public ReportResponse generateRevenueReport(@NotNull ReportRequest request) {
        if (request.getStartDate() == null || request.getEndDate() == null) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        Double totalRevenue = orderRepository.calculateRevenueBetweenDates(
                request.getStartDate(),
                request.getEndDate()
        );
        List<ProductSales> topProducts = orderRepository.findTopProductsBetweenDates(
                request.getStartDate(),
                request.getEndDate(),
                PageRequest.of(0, 5));

        return ReportResponse.builder()
                .totalRevenue(totalRevenue != null ? totalRevenue : 0.0)
                .topProducts(topProducts != null ? topProducts : List.of())
                .build();
    }

    public ReportResponse generateReport(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
        if (start.isAfter(end)) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        Double revenue = orderRepository.calculateRevenueBetweenDates(start, end);
        List<ProductSales> topProducts = orderRepository.findTopProductsBetweenDates(
                start, end, PageRequest.of(0, 5));
        return ReportResponse.builder()
                .totalRevenue(revenue != null ? revenue : 0.0)
                .topProducts(topProducts != null ? topProducts : List.of())
                .build();
    }

    public byte[] exportReportToPDF(ReportRequest request) {
        ReportResponse reportData = generateRevenueReport(request);
        return jasperReportService.generateRevenueReportPdf(reportData);
    }

    public DashboardStatsResponse getDashboardStats() {
        long totalUsers = userRepository.count();
        long totalProducts = productRepository.count();
        long totalOrders = orderRepository.count();

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30);
        Double totalRevenue = orderRepository.calculateRevenueBetweenDates(startDate, endDate);
        if (totalRevenue == null) totalRevenue = 0.0;

        List<Object[]> revenueDataRaw = orderRepository.findDailyRevenue(startDate, endDate);
        Map<LocalDate, Double> revenueMap = revenueDataRaw.stream()
                .collect(Collectors.toMap(
                        row -> ((java.sql.Date) row[0]).toLocalDate(),
                        row -> ((Number) row[1]).doubleValue(),
                        (v1, v2) -> v1));

        List<DashboardStatsResponse.DailyRevenue> revenueData = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            revenueData.add(DashboardStatsResponse.DailyRevenue.builder()
                    .date(date.format(formatter))
                    .revenue(revenueMap.getOrDefault(date, 0.0))
                    .build());
        }

        return DashboardStatsResponse.builder()
                .totalUsers(totalUsers)
                .totalProducts(totalProducts)
                .totalOrders(totalOrders)
                .totalRevenue(totalRevenue)
                .revenueData(revenueData)
                .build();
    }
}