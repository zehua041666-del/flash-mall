# 周复盘

## W1（2026-10-13 ~ 2026-10-19）地基周

### 完成情况
- ✅ Docker Compose 起 MySQL + Redis，一键启动
- ✅ Flyway V1 建六张业务表（user/product_spu/product_sku/order/order_item/stock）
- ✅ Maven 多模块骨架（父 POM + common + user + app），启动类 + 配置 + Flyway 集成
- ✅ common 模块：Result / BizException / GlobalExceptionHandler
- ✅ 前端骨架：Vue3 + Vite + Element Plus + Pinia + Router + Axios
- ✅ GitHub Actions CI，编译后端绿勾
- ✅ 算法：两数之和 / 两数相加 / 无重复字符最长子串
- ✅ 决策日志：#000 / #001 / #002 / #003（待办）

### 未完成
- LeetCode 4（Hard）留 W2 补

### W2 准备
从 user 模块的注册 / 登录接口开始，走通 Controller → Service → Mapper → MySQL 全链路，作为后续模块的模板。