package com.flashmall.product.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flashmall.common.result.PageResult;
import com.flashmall.product.dto.response.ProductListResponse;
import com.flashmall.product.entity.ProductSku;
import com.flashmall.product.entity.ProductSpu;
import com.flashmall.product.mapper.ProductSkuMapper;
import com.flashmall.product.mapper.ProductSpuMapper;
import com.flashmall.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductSpuMapper productSpuMapper;
    private final ProductSkuMapper productSkuMapper;

    @Override
    public PageResult<ProductListResponse> listProducts(int pageNum, int pageSize) {
        Page<ProductSpu> page = productSpuMapper.selectPage(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<ProductSpu>().eq(ProductSpu::getStatus, 1).orderByDesc(ProductSpu::getId));
        List<ProductSpu> spuList = page.getRecords();
        if (spuList.isEmpty()) {
            return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), Collections.emptyList());
        }
        List<Long> spuIds = spuList.stream().map(ProductSpu::getId).toList();
        List<ProductSku> allSkus = productSkuMapper.selectList(
                new LambdaQueryWrapper<ProductSku>().in(ProductSku::getSpuId, spuIds).eq(ProductSku::getStatus, 1));
        Map<Long, List<ProductSku>> skusBySpu = allSkus.stream().collect(Collectors.groupingBy(ProductSku::getSpuId));
        List<ProductListResponse> list = spuList.stream().map(spu -> {
            ProductListResponse item = new ProductListResponse();
            item.setSpuId(spu.getId());
            item.setName(spu.getName());
            item.setMainImage(spu.getMainImage());

            List<ProductSku> skus = skusBySpu.getOrDefault(spu.getId(), Collections.emptyList());
            BigDecimal minPrice = skus.stream().map(ProductSku::getPrice).min(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);
            item.setMinPrice(minPrice);
            return item;
        }).toList();
        return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), list);
    }

    @Override
    public List<ProductSku> getSkuByIds(List<Long> skuIds) {
        if (skuIds == null || skuIds.isEmpty()) {
            return Collections.emptyList();
        }
        return productSkuMapper.selectList(
                new LambdaQueryWrapper<ProductSku>().in(ProductSku::getId, skuIds));
    }

}
