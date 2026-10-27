package com.flashmall.product.dto.response;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品列表页的每一项。
 * 只返回列表页需要展示的字段——不返回 SPU 的全部字段（description 可能很长）。
 */
@Data
public class ProductListResponse {

    private Long spuId;

    private String name;

    private String mainImage;

    // 该 SPU 下所有在售 SKU 的最低价，列表页展示"¥xxxx 起"
    private BigDecimal minPrice;
}