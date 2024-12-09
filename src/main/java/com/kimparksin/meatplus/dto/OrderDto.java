package com.kimparksin.meatplus.dto;

import com.kimparksin.meatplus.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private LocalDateTime orderDate;
    private Long userId;

    public OrderDto(Order order) {
        this.id = order.getId();
        this.orderDate = order.getOrderDate();
        this.userId = order.getUser().getId();
    }

    public Order toEntity() {
        Order order = new Order();
        order.setId(this.id);
        order.setOrderDate(this.orderDate);

        // User 엔티티는 별도로 조회/설정 필요
        return order;
    }
}
