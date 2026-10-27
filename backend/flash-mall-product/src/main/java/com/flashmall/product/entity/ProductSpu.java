package com.flashmall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("product_spu")
public class ProductSpu {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Long categoryId;

    private Long brandId;

    private String mainImage;

    private String description;

    private Integer status;

    @TableLogic
    private Integer deleted;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}