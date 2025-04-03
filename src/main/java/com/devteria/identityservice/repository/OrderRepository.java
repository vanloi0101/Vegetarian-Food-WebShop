package com.devteria.identityservice.repository;

import com.devteria.identityservice.entity.Order;
import com.devteria.identityservice.dto.response.ReportResponse.ProductSales;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(String userId);

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate AND o.status = 'COMPLETED'")
    Double calculateRevenueBetweenDates(LocalDate startDate, LocalDate endDate);

    @Query("SELECT new com.devteria.identityservice.dto.response.ReportResponse.ProductSales(od.product.id, od.product.name, SUM(od.quantity), SUM(od.subtotal)) " +
            "FROM OrderDetail od JOIN od.order o " +
            "WHERE o.createdAt BETWEEN :startDate AND :endDate AND o.status = 'COMPLETED' " +
            "GROUP BY od.product.id, od.product.name " +
            "ORDER BY SUM(od.subtotal) DESC")
    List<ProductSales> findTopProductsBetweenDates(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);
}