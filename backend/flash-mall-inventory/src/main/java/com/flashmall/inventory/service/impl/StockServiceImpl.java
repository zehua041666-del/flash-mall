package com.flashmall.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.flashmall.inventory.entity.Stock;
import com.flashmall.inventory.mapper.StockMapper;
import com.flashmall.inventory.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockMapper stockMapper;

    @Override
    public Stock getStock(Long skuId) {
        return stockMapper.selectOne(
                new LambdaQueryWrapper<Stock>().eq(Stock::getSkuId, skuId));
    }
}