package com.kimparksin.meatplus.repository;

import com.kimparksin.meatplus.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId); // 특정 사용자와 관련된 주문 찾기
}
