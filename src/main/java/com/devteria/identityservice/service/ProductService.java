package com.devteria.identityservice.service;


import com.devteria.identityservice.dto.request.ProductRequest;
import com.devteria.identityservice.dto.response.ProductResponse;
import com.devteria.identityservice.entity.Product;
import com.devteria.identityservice.mapper.ProductMapper;
import com.devteria.identityservice.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {
    ProductRepository productRepository;
    ProductMapper productMapper;

    public ProductResponse createProduct(ProductRequest request) {
        Product product = productMapper.toProduct(request);
        product = productRepository.save(product);
        return productMapper.toProductResponse(product);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.toProductResponse(product);
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productMapper.updateProduct(product, request);
        product = productRepository.save(product);
        return productMapper.toProductResponse(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}