package com.devteria.identityservice.service.serviceImpl;

import com.devteria.identityservice.dto.request.OrderDetailRequest;
import com.devteria.identityservice.dto.response.OrderDetailResponse;
import com.devteria.identityservice.entity.Order;
import com.devteria.identityservice.entity.OrderDetail;
import com.devteria.identityservice.entity.Product;
import com.devteria.identityservice.mapper.OrderDetailMapper;
import com.devteria.identityservice.repository.OrderDetailRepository;
import com.devteria.identityservice.repository.ProductRepository;
import com.devteria.identityservice.service.OrderDetailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final OrderDetailMapper orderDetailMapper;

    @Override
    @Transactional
    public List<OrderDetailResponse> createOrderDetails(List<OrderDetailRequest> requests, Order order) {
        List<OrderDetailResponse> responses = new ArrayList<>();

        for (OrderDetailRequest request : requests) {
            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            BigDecimal unitPrice = BigDecimal.valueOf(product.getPrice());
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(request.getQuantity()));

            OrderDetail detail = OrderDetail.builder()
                    .order(order)
                    .product(product)
                    .productName(product.getName())
                    .quantity(request.getQuantity())
                    .unitPrice(unitPrice)
                    .subtotal(subtotal)
                    .build();

            orderDetailRepository.save(detail);
            responses.add(orderDetailMapper.toResponse(detail));
        }

        return responses;
    }

    @Override
    public List<OrderDetailResponse> getOrderDetailsByOrderId(Long orderId) {
        List<OrderDetail> details = orderDetailRepository.findByOrder_Id(orderId);
        return details.stream()
                .map(orderDetailMapper::toResponse)
                .toList();
    }

}
