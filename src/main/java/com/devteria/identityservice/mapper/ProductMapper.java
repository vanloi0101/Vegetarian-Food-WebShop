package com.devteria.identityservice.mapper;




import com.devteria.identityservice.dto.request.ProductRequest;
import com.devteria.identityservice.dto.response.ProductResponse;
import com.devteria.identityservice.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(ProductRequest request);

    ProductResponse toProductResponse(Product product);

    void updateProduct(@MappingTarget Product product, ProductRequest request);
}