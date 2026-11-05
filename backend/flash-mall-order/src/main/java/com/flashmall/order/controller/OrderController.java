package com.flashmall.order.controller;

import com.flashmall.common.result.Result;
import com.flashmall.order.dto.CreateOrderRequest;
import com.flashmall.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public Result<Long> create(@Valid @RequestBody CreateOrderRequest request) {
        return Result.ok(orderService.createOrder(request));
    }
}