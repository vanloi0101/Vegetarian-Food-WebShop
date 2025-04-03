package com.devteria.identityservice.service;

import com.devteria.identityservice.dto.response.ReportResponse;
import com.devteria.identityservice.exception.AppException;
import com.devteria.identityservice.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JasperReportService {

    public byte[] generateRevenueReportPdf(ReportResponse reportData) {
        try {
            // Load template
            InputStream reportStream = getClass().getResourceAsStream("/reports/revenue_report.jrxml");
            if (reportStream == null) {
                throw new RuntimeException("Report template not found");
            }

            // Compile report
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Convert product sales to JRDataSource
            JRBeanCollectionDataSource productsDS = new JRBeanCollectionDataSource(
                    reportData.getTopProducts() != null ? reportData.getTopProducts() : List.of()
            );

            // Parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("REPORT_TITLE", "Revenue Report");
            parameters.put("totalRevenue", reportData.getTotalRevenue());
            parameters.put("productsDataSet", productsDS);

            // Fill report
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parameters,
                    new JREmptyDataSource()
            );

            // Export to PDF
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            log.error("Error generating PDF report", e);
            throw new AppException(ErrorCode.INVALID_REPORT_DATA);
        }
    }}