package com.devteria.identityservice.controller;

import com.devteria.identityservice.dto.request.ApiResponse;
import com.devteria.identityservice.dto.request.ReportRequest;
import com.devteria.identityservice.dto.response.ReportResponse;
import com.devteria.identityservice.exception.AppException;
import com.devteria.identityservice.exception.ErrorCode;
import com.devteria.identityservice.service.ReportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/reports")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportController {
    ReportService reportService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<ReportResponse> generateReport(@RequestBody ReportRequest request) {
        return ApiResponse.<ReportResponse>builder()
                .result(reportService.generateRevenueReport(request))
                .build();
    }

    @PostMapping("/export")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> exportReport(@RequestBody ReportRequest request) {
        try {
            byte[] pdfBytes = reportService.exportReportToPDF(request);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "revenue_report.pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (AppException e) {
            return ResponseEntity.status(e.getErrorCode().getStatusCode())
                    .body(ApiResponse.builder()
                            .code(e.getErrorCode().getCode())
                            .message(e.getMessage())
                            .build());
        } catch (Exception e) {
            log.error("Error exporting report", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .code(ErrorCode.INVALID_REPORT_DATA.getCode())
                            .message("Error generating report")
                            .build());
        }
    }
}