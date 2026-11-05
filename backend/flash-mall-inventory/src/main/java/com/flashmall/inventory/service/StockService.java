package com.flashmall.inventory.service;

import com.flashmall.inventory.entity.Stock;

public interface StockService {

    Stock getStock(Long skuId);
}