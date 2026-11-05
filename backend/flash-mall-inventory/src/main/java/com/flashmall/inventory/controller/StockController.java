package com.flashmall.inventory.controller;

import com.flashmall.common.result.Result;
import com.flashmall.inventory.entity.Stock;
import com.flashmall.inventory.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping("/stock/{skuId}")
    public Result<Stock> getStock(@PathVariable Long skuId) {
        return Result.ok(stockService.getStock(skuId));
    }
}