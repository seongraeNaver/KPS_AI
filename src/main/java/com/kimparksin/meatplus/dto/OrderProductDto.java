package com.kimparksin.meatplus.dto;

import com.kimparksin.meatplus.entity.Order;
import com.kimparksin.meatplus.entity.OrderProduct;
import com.kimparksin.meatplus.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductDto {
    private Long id;
    private Long orderId;
    private Long productCode;
    private int quantity;

    public OrderProductDto(OrderProduct orderProduct) {
        this.id = orderProduct.getId();
        this.orderId = orderProduct.getOrder().getId();
        this.productCode = orderProduct.getProduct().getProductCode();
        this.quantity = orderProduct.getQuantity();
    }

    public OrderProduct toEntity(Order order, Product product) {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setId(this.id);
        orderProduct.setOrder(order);
        orderProduct.setProduct(product);
        orderProduct.setQuantity(this.quantity);
        return orderProduct;
    }
}
