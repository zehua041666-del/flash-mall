package com.flashmall.product.controller;

import com.flashmall.common.result.PageResult;
import com.flashmall.common.result.Result;
import com.flashmall.product.dto.response.ProductListResponse;
import com.flashmall.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * GET /product/list?pageNum=1&pageSize=10
     * @RequestParam(defaultValue=...) 让参数可不传，不传用默认值
     */
    @GetMapping("/list")
    public Result<PageResult<ProductListResponse>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(productService.listProducts(pageNum, pageSize));
    }
}