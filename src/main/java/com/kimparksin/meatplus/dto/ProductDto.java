package com.kimparksin.meatplus.dto;

import com.kimparksin.meatplus.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long productCode;
    private String productName;
    private BigDecimal productPrice; // 수정된 부분

    public ProductDto(Product product) {
        this.productCode = product.getProductCode();
        this.productName = product.getProductName();
        this.productPrice = product.getProductPrice();
    }

    public Product toEntity() {
        Product product = new Product();
        product.setProductCode(this.productCode);
        product.setProductName(this.productName);
        product.setProductPrice(this.productPrice);
        return product;
    }
}
