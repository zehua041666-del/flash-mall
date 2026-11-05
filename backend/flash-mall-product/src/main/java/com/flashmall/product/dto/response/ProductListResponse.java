package com.flashmall.product.dto.response;

import com.flashmall.product.entity.ProductSku;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品列表页的每一项。
 * W2 加购需求：带上该 SPU 下的 SKU 列表，前端选规格时用。
 */
@Data
public class ProductListResponse {

    private Long spuId;

    private String name;

    private String mainImage;

    // 该 SPU 下所有在售 SKU 的最低价，列表页展示"¥xxxx 起"
    private BigDecimal minPrice;

    // 该 SPU 下所有在售 SKU（已过滤 status=1），前端弹窗选规格用
    private List<ProductSku> skuList;
}