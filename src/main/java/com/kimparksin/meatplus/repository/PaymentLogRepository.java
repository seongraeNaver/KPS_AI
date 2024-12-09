package com.kimparksin.meatplus.repository;

import com.kimparksin.meatplus.entity.PaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentLogRepository extends JpaRepository<PaymentLog, Long> {
    List<PaymentLog> findByPaymentId(Long paymentId); // 특정 결제와 관련된 로그 검색
}
