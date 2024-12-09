package com.kimparksin.meatplus.repository;

import com.kimparksin.meatplus.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefundRepository extends JpaRepository<Refund, Long> {
    List<Refund> findByPaymentId(Long paymentId); // 특정 결제와 관련된 환불 검색
}
