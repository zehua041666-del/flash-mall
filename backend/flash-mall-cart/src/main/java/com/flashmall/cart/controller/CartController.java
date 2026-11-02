package com.flashmall.cart.controller;

import com.flashmall.cart.dto.AddCartRequest;
import com.flashmall.cart.dto.CartItemResponse;
import com.flashmall.cart.service.CartService;
import com.flashmall.common.result.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * 加入购物车
     * POST /cart/add
     */
    @PostMapping("/add")
    public Result<Void> addCart(@Valid @RequestBody AddCartRequest request) {
        cartService.addCart(request);
        return Result.ok(null);
    }

    /**
     * 获取当前用户的购物车列表
     * GET /cart/list
     */
    @GetMapping("/list")
    public Result<List<CartItemResponse>> getCartList() {
        return Result.ok(cartService.getCartList());
    }

    /**
     * 修改购物车商品数量
     * PUT /cart/quantity?skuId=1&quantity=5
     */
    @PutMapping("/quantity")
    public Result<Void> updateQuantity(
            @RequestParam Long skuId,
            @RequestParam Integer quantity) {
        cartService.updateQuantity(skuId, quantity);
        return Result.ok(null);
    }

    /**
     * 删除购物车商品
     * DELETE /cart/1
     */
    @DeleteMapping("/{skuId}")
    public Result<Void> removeCartItem(@PathVariable Long skuId) {
        cartService.removeCartItem(skuId);
        return Result.ok(null);
    }
}