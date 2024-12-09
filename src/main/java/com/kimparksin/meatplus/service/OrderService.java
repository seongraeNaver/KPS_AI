package com.kimparksin.meatplus.service;

import com.kimparksin.meatplus.dto.OrderDto;
import com.kimparksin.meatplus.entity.Order;
import com.kimparksin.meatplus.entity.User;
import com.kimparksin.meatplus.repository.OrderRepository;
import com.kimparksin.meatplus.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    //클라이언트로부터 전달받은 주문 데이터를 데이터베이스에 저장.
    //주문은 반드시 사용자와 연관되어야 하므로 사용자 데이터(User 엔티티)를 먼저 조회
    public OrderDto createOrder(OrderDto orderDto) {
        log.info("Creating order for user ID: {}", orderDto.getUserId());
        User user = userRepository.findById(orderDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + orderDto.getUserId()));

        Order order = orderDto.toEntity();
        order.setUser(user);
        Order savedOrder = orderRepository.save(order);
        return new OrderDto(savedOrder);
    }

    //데이터베이스에서 모든 주문 데이터를 조회
    public List<OrderDto> getAllOrders() {
        log.info("Fetching all orders without pagination");
        return orderRepository.findAll() // 모든 데이터를 가져옵니다.
                .stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }


    //특정 주문 ID를 기반으로 데이터를 조회
    public OrderDto getOrderById(Long id) {
        log.info("Fetching order with id: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return new OrderDto(order);
    }
}
