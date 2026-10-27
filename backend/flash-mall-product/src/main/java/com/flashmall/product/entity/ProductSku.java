package com.flashmall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("product_sku")
public class ProductSku {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long spuId;

    private String skuCode;

    private String name;

    // 金额必须用 BigDecimal，double 有精度问题（0.1+0.2 != 0.3）
    private BigDecimal price;

    private String image;

    // MySQL JSON 字段，存字符串形式：{"颜色":"黑色","容量":"256G"}
    // 读出来时原样返回给前端，不用在这里解析
    private String specs;

    private Integer status;

    @TableLogic
    private Integer deleted;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}