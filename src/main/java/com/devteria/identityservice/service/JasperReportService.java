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
        try (InputStream reportStream = getClass().getResourceAsStream("/reports/revenue_report.jrxml")) {
            if (reportStream == null) {
                throw new RuntimeException("Report template not found");
            }
            // Biên dịch tệp JRXML
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Chuẩn bị dữ liệu
            JRBeanCollectionDataSource productsDS = new JRBeanCollectionDataSource(
                    reportData.getTopProducts() != null ? reportData.getTopProducts() : List.of()
            );
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("REPORT_TITLE", "Revenue Report");
            parameters.put("totalRevenue", reportData.getTotalRevenue() != null ? reportData.getTotalRevenue() : 0.0);

            // Điền báo cáo với dữ liệu từ JRBeanCollectionDataSource
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, productsDS);

            // Xuất báo cáo thành PDF
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException e) {
            log.error("Error generating Jasper report: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.INVALID_REPORT_DATA);
        } catch (Exception e) {
            log.error("Unexpected error generating PDF report: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.INVALID_REPORT_DATA);
        }
    }
}