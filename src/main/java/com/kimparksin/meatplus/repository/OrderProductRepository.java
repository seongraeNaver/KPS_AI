package com.kimparksin.meatplus.repository;

import com.kimparksin.meatplus.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    List<OrderProduct> findByOrderId(Long orderId); // 특정 주문에 속한 제품 검색
}
