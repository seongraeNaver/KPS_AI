package com.kimparksin.meatplus.dto;

import com.kimparksin.meatplus.entity.Order;
import com.kimparksin.meatplus.entity.Payment;
import com.kimparksin.meatplus.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    private Long id;
    private boolean isPay;
    private String paymentStatus;
    private LocalDateTime paymentDate;
    private String gatewayPaymentId;
    private String gatewayName;
    private BigDecimal amount; // 수정: double -> BigDecimal
    private Long orderId;

    public PaymentDto(Payment payment) {
        this.id = payment.getId();
        this.isPay = payment.isPay();
        this.paymentStatus = payment.getPaymentStatus().name();
        this.paymentDate = payment.getPaymentDate();
        this.gatewayPaymentId = payment.getGatewayPaymentId();
        this.gatewayName = payment.getGatewayName();
        this.amount = payment.getAmount(); // BigDecimal 타입으로 유지
        this.orderId = payment.getOrder().getId();
    }

    public Payment toEntity(Order order) {
        Payment payment = new Payment();
        payment.setId(this.id);
        payment.setPay(this.isPay);
        payment.setPaymentStatus(PaymentStatus.valueOf(this.paymentStatus));
        payment.setPaymentDate(this.paymentDate);
        payment.setGatewayPaymentId(this.gatewayPaymentId);
        payment.setGatewayName(this.gatewayName);
        payment.setAmount(this.amount);
        payment.setOrder(order); // Order 객체 설정
        return payment;
    }

}
