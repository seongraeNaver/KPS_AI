package com.kimparksin.meatplus.service;

import com.kimparksin.meatplus.dto.PaymentDto;
import com.kimparksin.meatplus.entity.Order;
import com.kimparksin.meatplus.entity.Payment;
import com.kimparksin.meatplus.repository.OrderRepository;
import com.kimparksin.meatplus.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    //클라이언트로부터 전달받은 결제 데이터를 데이터베이스에 저장
    //결제는 주문(Order)과 연결되어야 하므로, 먼저 주문 데이터를 조회
    public PaymentDto createPayment(PaymentDto paymentDto) {
        Order order = orderRepository.findById(paymentDto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + paymentDto.getOrderId()));

        Payment payment = paymentDto.toEntity(order);
        Payment savedPayment = paymentRepository.save(payment);
        return new PaymentDto(savedPayment);
    }

    //데이터베이스에서 모든 결제 데이터를 조회
    public List<PaymentDto> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(PaymentDto::new) // Entity -> DTO 변환
                .collect(Collectors.toList());
    }

    //특정 결제 ID를 기반으로 결제 데이터를 조회
    public PaymentDto getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
        return new PaymentDto(payment); // Entity -> DTO 변환
    }
}
