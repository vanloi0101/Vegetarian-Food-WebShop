package com.devteria.identityservice.repository;

import com.devteria.identityservice.dto.response.ProductSales;
import com.devteria.identityservice.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(String userId);

    @Query(value = "SELECT SUM(total_amount) FROM `order` WHERE DATE(created_at) BETWEEN :startDate AND :endDate AND status = 'COMPLETED'", nativeQuery = true)
    Double calculateRevenueBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT od.product_id AS productId, p.name AS productName, SUM(od.quantity) AS totalQuantity, SUM(od.subtotal) AS totalRevenue " +
            "FROM order_detail od " +
            "JOIN `order` o ON od.order_id = o.id " +
            "JOIN product p ON od.product_id = p.id " +
            "WHERE DATE(o.created_at) BETWEEN :startDate AND :endDate AND o.status = 'COMPLETED' " +
            "GROUP BY od.product_id, p.name " +
            "ORDER BY SUM(od.subtotal) DESC " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}", nativeQuery = true)
    List<ProductSales> findTopProductsBetweenDates(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);

    @Query(value = "SELECT DATE(created_at) AS date, SUM(total_amount) AS revenue " +
            "FROM `order` WHERE DATE(created_at) BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(created_at)", nativeQuery = true)
    List<Object[]> findDailyRevenue(@Param("startDate") LocalDate startDate,
                                    @Param("endDate") LocalDate endDate);
}