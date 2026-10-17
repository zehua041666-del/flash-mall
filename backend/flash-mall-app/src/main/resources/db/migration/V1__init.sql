-- ========================================
-- flash-mall 初始化：核心七张表
-- ========================================

-- 1. 用户表
CREATE TABLE `user` (
    `id`         BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`   VARCHAR(50)  NOT NULL                COMMENT '用户名',
    `password`   VARCHAR(100) NOT NULL                COMMENT '密码(BCrypt)',
    `phone`      VARCHAR(20)  DEFAULT NULL            COMMENT '手机号',
    `email`      VARCHAR(100) DEFAULT NULL            COMMENT '邮箱',
    `nickname`   VARCHAR(50)  DEFAULT NULL            COMMENT '昵称',
    `avatar`     VARCHAR(255) DEFAULT NULL            COMMENT '头像URL',
    `status`     TINYINT      NOT NULL DEFAULT 1      COMMENT '1正常/0禁用',
    `deleted`    TINYINT      NOT NULL DEFAULT 0      COMMENT '0未删/1已删',
    `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_phone` (`phone`),
    UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 2. 商品SPU表
CREATE TABLE `product_spu` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`        VARCHAR(200) NOT NULL                COMMENT '商品名',
    `category_id` BIGINT       DEFAULT NULL            COMMENT '分类ID',
    `brand_id`    BIGINT       DEFAULT NULL            COMMENT '品牌ID',
    `main_image`  VARCHAR(255) DEFAULT NULL            COMMENT '主图URL',
    `description` TEXT                                 COMMENT '描述',
    `status`      TINYINT      NOT NULL DEFAULT 1      COMMENT '0下架/1上架',
    `deleted`     TINYINT      NOT NULL DEFAULT 0      COMMENT '0未删/1已删',
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品SPU表';

-- 3. 商品SKU表
CREATE TABLE `product_sku` (
    `id`         BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `spu_id`     BIGINT        NOT NULL                COMMENT '关联SPU',
    `sku_code`   VARCHAR(64)   NOT NULL                COMMENT '业务编码',
    `name`       VARCHAR(200)  NOT NULL                COMMENT '规格名',
    `price`      DECIMAL(10,2) NOT NULL                COMMENT '价格',
    `image`      VARCHAR(255)  DEFAULT NULL            COMMENT '规格图',
    `specs`      JSON          DEFAULT NULL            COMMENT '规格{"颜色":"黑"}',
    `status`     TINYINT       NOT NULL DEFAULT 1      COMMENT '1在售/0停售',
    `deleted`    TINYINT       NOT NULL DEFAULT 0      COMMENT '0未删/1已删',
    `created_at` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sku_code` (`sku_code`),
    KEY `idx_spu_id` (`spu_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品SKU表';

-- 4. 订单表
CREATE TABLE `order` (
    `id`               BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `order_no`         VARCHAR(32)   NOT NULL                COMMENT '订单号',
    `user_id`          BIGINT        NOT NULL                COMMENT '用户ID',
    `idempotent_key`   VARCHAR(64)   NOT NULL                COMMENT '幂等键',
    `total_amount`     DECIMAL(10,2) NOT NULL                COMMENT '商品总额',
    `discount_amount`  DECIMAL(10,2) NOT NULL DEFAULT 0.00   COMMENT '优惠',
    `pay_amount`       DECIMAL(10,2) NOT NULL                COMMENT '实付',
    `status`           TINYINT       NOT NULL DEFAULT 10     COMMENT '10待支付/20已支付/30已发货/40已完成/50已取消',
    `cancel_reason`    VARCHAR(255)  DEFAULT NULL            COMMENT '取消原因',
    `pay_type`         TINYINT       DEFAULT NULL            COMMENT '支付方式',
    `pay_time`         DATETIME      DEFAULT NULL            COMMENT '支付时间',
    `close_time`       DATETIME      DEFAULT NULL            COMMENT '关闭时间',
    `receiver_name`    VARCHAR(50)   NOT NULL                COMMENT '收货人',
    `receiver_phone`   VARCHAR(20)   NOT NULL                COMMENT '收货电话',
    `receiver_address` VARCHAR(255)  NOT NULL                COMMENT '收货地址',
    `remark`           VARCHAR(255)  DEFAULT NULL            COMMENT '备注',
    `deleted`          TINYINT       NOT NULL DEFAULT 0      COMMENT '0未删/1已删',
    `created_at`       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    UNIQUE KEY `uk_user_idempotent` (`user_id`, `idempotent_key`),
    KEY `idx_user_created` (`user_id`, `created_at`),
    KEY `idx_status_created` (`status`, `created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- 5. 订单明细表
CREATE TABLE `order_item` (
    `id`           BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `order_id`     BIGINT        NOT NULL                COMMENT '订单ID',
    `order_no`     VARCHAR(32)   NOT NULL                COMMENT '订单号(冗余)',
    `sku_id`       BIGINT        NOT NULL                COMMENT 'SKU_ID',
    `spu_id`       BIGINT        NOT NULL                COMMENT 'SPU_ID(冗余)',
    `sku_name`     VARCHAR(200)  NOT NULL                COMMENT 'SKU名(快照)',
    `sku_image`    VARCHAR(255)  DEFAULT NULL            COMMENT 'SKU图(快照)',
    `price`        DECIMAL(10,2) NOT NULL                COMMENT '单价(快照)',
    `quantity`     INT           NOT NULL                COMMENT '数量',
    `total_amount` DECIMAL(10,2) NOT NULL                COMMENT '小计',
    `deleted`      TINYINT       NOT NULL DEFAULT 0      COMMENT '0未删/1已删',
    `created_at`   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_sku_id` (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单明细表';

-- 6. 库存表
CREATE TABLE `stock` (
    `id`              BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键',
    `sku_id`          BIGINT   NOT NULL                COMMENT 'SKU_ID',
    `total_stock`     INT      NOT NULL DEFAULT 0      COMMENT '总库存',
    `available_stock` INT      NOT NULL DEFAULT 0      COMMENT '可售',
    `locked_stock`    INT      NOT NULL DEFAULT 0      COMMENT '锁定',
    `version`         INT      NOT NULL DEFAULT 0      COMMENT '乐观锁版本',
    `created_at`      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sku_id` (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存表';

-- 说明：cart 表不建，购物车数据存 Redis Hash（key: cart:{userId}, field: skuId, value: 数量）