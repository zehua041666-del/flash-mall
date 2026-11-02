package com.flashmall.cart.service;

import com.flashmall.cart.dto.AddCartRequest;
import com.flashmall.cart.dto.CartItemResponse;

import java.util.List;

public interface CartService {
    
    // 加入购物车
    void addCart(AddCartRequest request);

    // 查询当前用户的购物车列表
    List<CartItemResponse> getCartList();

    // 修改数量（传入 skuId 和 新数量）
    void updateQuantity(Long skuId, Integer quantity);

    // 删除商品
    void removeCartItem(Long skuId);
}