package com.devteria.identityservice.service;

import com.devteria.identityservice.dto.request.ReportRequest;
import com.devteria.identityservice.dto.response.ProductSales;
import com.devteria.identityservice.dto.response.ReportResponse;
import com.devteria.identityservice.repository.OrderRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportService {
    OrderRepository orderRepository;
    JasperReportService jasperReportService;

    public ReportResponse generateRevenueReport(ReportRequest request) {
        // Tính tổng doanh thu
        Double totalRevenue = orderRepository.calculateRevenueBetweenDates(
                request.getStartDate(),
                request.getEndDate()
        );

        // Lấy top sản phẩm với Pageable
        List<ProductSales> topProducts = orderRepository.findTopProductsBetweenDates(
                request.getStartDate(),
                request.getEndDate(),
                PageRequest.of(0, 5)); // Giới hạn 5 kết quả (tương đương LIMIT 5)

        return ReportResponse.builder()
                .totalRevenue(totalRevenue)
                .topProducts(topProducts)
                .build();
    }

    public ReportResponse generateReport(LocalDate start, LocalDate end) {
        Double revenue = orderRepository.calculateRevenueBetweenDates(start, end);
        List<ProductSales> topProducts = orderRepository.findTopProductsBetweenDates(start, end, PageRequest.of(0, 5));
        return ReportResponse.builder()
                .totalRevenue(revenue)
                .topProducts(topProducts)
                .build();
    }

    public byte[] exportReportToPDF(ReportRequest request) {
        ReportResponse reportData = generateRevenueReport(request);
        return jasperReportService.generateRevenueReportPdf(reportData);
    }
}