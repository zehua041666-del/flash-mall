package com.flashmall.order.service;

import com.flashmall.order.dto.CreateOrderRequest;

public interface OrderService {

    /** 创建订单，返回订单 ID */
    Long createOrder(CreateOrderRequest request);
}