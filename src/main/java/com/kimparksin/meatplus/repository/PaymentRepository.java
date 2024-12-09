package com.kimparksin.meatplus.repository;

import com.kimparksin.meatplus.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByOrderId(Long orderId); // 특정 주문과 관련된 결제 검색
}
