package com.flashmall.product.service;

import java.util.List;

import com.flashmall.common.result.PageResult;
import com.flashmall.product.dto.response.ProductListResponse;
import com.flashmall.product.entity.ProductSku;

public interface ProductService {
    PageResult<ProductListResponse> listProducts (int pageNum,int pageSize);

    List<ProductSku> getSkuByIds(List<Long> skuIds);
}