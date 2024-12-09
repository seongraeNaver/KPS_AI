package com.kimparksin.meatplus.service;

import com.kimparksin.meatplus.dto.ProductDto;
import com.kimparksin.meatplus.entity.Product;
import com.kimparksin.meatplus.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //클라이언트로부터 전달받은 상품 데이터를 데이터베이스에 저장
    public ProductDto createProduct(ProductDto productDto) {
        log.info("Creating product: {}", productDto.getProductName());
        Product product = productDto.toEntity();
        Product savedProduct = productRepository.save(product);
        return new ProductDto(savedProduct);
    }

    //데이터베이스에서 모든 상품 데이터를 조회
    public List<ProductDto> getAllProducts() {
        log.info("Fetching all products without pagination");
        return productRepository.findAll() // 모든 데이터를 가져옵니다.
                .stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());
    }

    //특정 상품 ID를 기반으로 상품 데이터를 조회
    public ProductDto getProductById(Long id) {
        log.info("Fetching product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return new ProductDto(product);
    }
}
