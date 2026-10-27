-- ========================================
-- V2：插入商品测试数据
-- 只在开发环境用。生产环境不跑这个脚本。
-- ========================================

-- 3 个 SPU
INSERT INTO `product_spu` (`id`, `name`, `category_id`, `brand_id`, `main_image`, `description`, `status`) VALUES
(1, 'iPhone 15 Pro', 1, 1, 'https://picsum.photos/seed/iphone15pro/400/400', 'Apple 旗舰手机，A17 Pro 芯片', 1),
(2, 'AirPods Pro 2', 2, 1, 'https://picsum.photos/seed/airpodspro2/400/400', '主动降噪无线耳机', 1),
(3, 'MacBook Air M3', 3, 1, 'https://picsum.photos/seed/macbookairm3/400/400', '13 英寸轻薄本，M3 芯片', 1);

-- 6 个 SKU（每个 SPU 下 2 个规格）
INSERT INTO `product_sku` (`id`, `spu_id`, `sku_code`, `name`, `price`, `image`, `specs`, `status`) VALUES
(1, 1, 'IP15P-256-BLK', 'iPhone 15 Pro 256G 黑色', 7999.00, 'https://picsum.photos/seed/ip15p256blk/400/400', '{"颜色":"黑色","容量":"256G"}', 1),
(2, 1, 'IP15P-512-WHT', 'iPhone 15 Pro 512G 白色', 8999.00, 'https://picsum.photos/seed/ip15p512wht/400/400', '{"颜色":"白色","容量":"512G"}', 1),
(3, 2, 'APP2-STD', 'AirPods Pro 2 标准版', 1899.00, 'https://picsum.photos/seed/app2std/400/400', '{"版本":"标准版"}', 1),
(4, 2, 'APP2-USBC', 'AirPods Pro 2 USB-C 版', 1999.00, 'https://picsum.photos/seed/app2usbc/400/400', '{"版本":"USB-C版"}', 1),
(5, 3, 'MBA-M3-8-256', 'MacBook Air M3 8G+256G', 8999.00, 'https://picsum.photos/seed/mbam38256/400/400', '{"内存":"8G","硬盘":"256G"}', 1),
(6, 3, 'MBA-M3-16-512', 'MacBook Air M3 16G+512G', 11999.00, 'https://picsum.photos/seed/mbam316512/400/400', '{"内存":"16G","硬盘":"512G"}', 1);

-- 6 条库存（和 SKU 一一对应）
INSERT INTO `stock` (`sku_id`, `total_stock`, `available_stock`, `locked_stock`, `version`) VALUES
(1, 100, 100, 0, 0),
(2, 100, 100, 0, 0),
(3, 200, 200, 0, 0),
(4, 200, 200, 0, 0),
(5, 50, 50, 0, 0),
(6, 50, 50, 0, 0);