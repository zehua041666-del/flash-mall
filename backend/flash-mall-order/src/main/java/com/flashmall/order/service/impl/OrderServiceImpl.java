package com.flashmall.order.service.impl;

import com.flashmall.common.context.UserContext;
import com.flashmall.common.exception.BizException;
import com.flashmall.common.util.SnowflakeIdGenerator;
import com.flashmall.order.dto.CreateOrderRequest;
import com.flashmall.order.entity.Order;
import com.flashmall.order.entity.OrderItem;
import com.flashmall.order.mapper.OrderItemMapper;
import com.flashmall.order.mapper.OrderMapper;
import com.flashmall.order.service.OrderService;
import com.flashmall.product.entity.ProductSku;
import com.flashmall.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ProductService productService;
    private final SnowflakeIdGenerator snowflakeIdGenerator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOrder(CreateOrderRequest request) {
        // 1. 取当前用户
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BizException(401, "用户未登录");
        }

        // 2. 跨模块查 SKU（走接口，不 JOIN）
        List<ProductSku> skus = productService.getSkuByIds(List.of(request.getSkuId()));
        if (skus.isEmpty()) {
            throw new BizException(400, "商品不存在");
        }
        ProductSku sku = skus.get(0);
        if (sku.getStatus() == null || sku.getStatus() != 1) {
            throw new BizException(400, "商品已下架");
        }

        // 3. 后端算金额
        BigDecimal totalAmount = sku.getPrice()
                .multiply(BigDecimal.valueOf(request.getQuantity()));
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal payAmount = totalAmount.subtract(discountAmount);

        // 4. 雪花生成订单号
        String orderNo = String.valueOf(snowflakeIdGenerator.nextId());

        // 5. 组装 Order
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setIdempotentKey(UUID.randomUUID().toString()); // Day1 占位
        order.setTotalAmount(totalAmount);
        order.setDiscountAmount(discountAmount);
        order.setPayAmount(payAmount);
        order.setStatus(10); // 待支付
        order.setReceiverName(request.getReceiverName());
        order.setReceiverPhone(request.getReceiverPhone());
        order.setReceiverAddress(request.getReceiverAddress());
        order.setRemark(request.getRemark());

        orderMapper.insert(order); // MP 回填自增 id

        // 6. 组装 OrderItem（快照从 SKU 抄）
        OrderItem item = new OrderItem();
        item.setOrderId(order.getId());
        item.setOrderNo(orderNo);
        item.setSkuId(sku.getId());
        item.setSpuId(sku.getSpuId());
        item.setSkuName(sku.getName());
        item.setSkuImage(sku.getImage());
        item.setPrice(sku.getPrice());
        item.setQuantity(request.getQuantity());
        item.setTotalAmount(totalAmount);

        orderItemMapper.insert(item);

        return order.getId();
    }
}