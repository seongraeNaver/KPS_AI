package com.kimparksin.meatplus.repository;

import com.kimparksin.meatplus.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // 제품 관련 추가 검색 메서드 작성 가능
}
