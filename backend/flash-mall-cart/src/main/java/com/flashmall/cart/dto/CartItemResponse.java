package com.flashmall.cart.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItemResponse {
    private Long skuId;
    private String skuName;
    private String skuImage;
    private BigDecimal price;
    private Integer quantity;
    
    // 总价 = 单价 * 数量。在服务端算好，前端直接展示，防止前端算错
    private BigDecimal totalPrice; 
}