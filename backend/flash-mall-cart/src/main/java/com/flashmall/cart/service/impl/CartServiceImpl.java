package com.flashmall.cart.service.impl;

import com.flashmall.cart.dto.AddCartRequest;
import com.flashmall.cart.dto.CartItemResponse;
import com.flashmall.cart.service.CartService;
import com.flashmall.common.context.UserContext;
import com.flashmall.product.entity.ProductSku;
import com.flashmall.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final StringRedisTemplate redisTemplate;
    private final ProductService productService;

    // Redis 购物车的 Key 前缀
    private static final String CART_KEY_PREFIX = "cart:";

    /**
     * 获取当前用户的 Redis Key，例如 cart:1001
     */
    private String getCartKey() {
        Long userId = UserContext.getUserId();
        // 如果拦截器没拦住，userId 为空，抛异常
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }
        return CART_KEY_PREFIX + userId;
    }

    @Override
    public void addCart(AddCartRequest request) {
        String key = getCartKey();
        String field = String.valueOf(request.getSkuId());
        
        // opsForHash().increment 是原子的。如果该 field 不存在，会初始化为 0 再累加。
        // 比如原本买了 2 个，再加 3 个，Redis 会直接变成 5。
        redisTemplate.opsForHash().increment(key, field, request.getQuantity());
    }

    @Override
    public List<CartItemResponse> getCartList() {
        String key = getCartKey();
        
        // 1. 从 Redis 获取所有购物车项（key 是 skuId，value 是数量）
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        if (entries.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. 提取所有的 skuId 并转为 Long 集合
        List<Long> skuIds = entries.keySet().stream()
                .map(obj -> Long.valueOf(obj.toString())) 
                .toList();

        // 3. 跨模块调用：通过商品模块的接口拿数据，绝对不查数据库
        List<ProductSku> skuList = productService.getSkuByIds(skuIds);
        
        // 将查出来的商品信息转为 Map，方便根据 skuId 快速匹配
        Map<Long, ProductSku> skuMap = skuList.stream()
                .collect(Collectors.toMap(ProductSku::getId, Function.identity()));

        // 4. 组装返回给前端的列表
        List<CartItemResponse> responseList = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            Long skuId = Long.valueOf(entry.getKey().toString());
            Integer quantity = Integer.valueOf(entry.getValue().toString());

            ProductSku sku = skuMap.get(skuId);
            if (sku == null) {
                // 如果商品被下架删除了（skuMap 里找不到），可以选择跳过或者当垃圾数据清理
                continue; 
            }

            CartItemResponse item = new CartItemResponse();
            item.setSkuId(skuId);
            item.setSkuName(sku.getName());
            item.setSkuImage(sku.getImage());
            item.setPrice(sku.getPrice());
            item.setQuantity(quantity);
            
            // 计算单行小计：单价 * 数量
            item.setTotalPrice(sku.getPrice().multiply(new BigDecimal(quantity)));

            responseList.add(item);
        }

        return responseList;
    }

    @Override
    public void updateQuantity(Long skuId, Integer quantity) {
        String key = getCartKey();
        String field = String.valueOf(skuId);
        
        if (quantity <= 0) {
            // 如果数量减到 0，直接删除该项
            redisTemplate.opsForHash().delete(key, field);
        } else {
            // 直接覆盖更新数量
            redisTemplate.opsForHash().put(key, field, String.valueOf(quantity));
        }
    }

    @Override
    public void removeCartItem(Long skuId) {
        String key = getCartKey();
        redisTemplate.opsForHash().delete(key, String.valueOf(skuId));
    }
}