package com.devteria.identityservice.mapper;



import com.devteria.identityservice.dto.request.OrderRequest;
import com.devteria.identityservice.dto.response.OrderResponse;
import com.devteria.identityservice.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface OrderMapper {
    Order toOrder(OrderRequest orderDetails);

    OrderResponse toOrderResponse(Order orderDetails);
}